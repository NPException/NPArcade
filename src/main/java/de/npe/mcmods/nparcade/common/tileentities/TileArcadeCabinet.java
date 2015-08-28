package de.npe.mcmods.nparcade.common.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.arcade.ArcadeGameRegistry;
import de.npe.mcmods.nparcade.arcade.ArcadeMachine;
import de.npe.mcmods.nparcade.arcade.DummyGames;
import de.npe.mcmods.nparcade.arcade.api.IGameCartridge;
import de.npe.mcmods.nparcade.common.ModBlocks;
import de.npe.mcmods.nparcade.common.lib.Strings;
import de.npe.mcmods.nparcade.common.util.Util;
import me.jezza.oc.common.interfaces.IBlockInteract;
import me.jezza.oc.common.interfaces.IBlockNotifier;
import me.jezza.oc.common.tile.TileAbstract;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by NPException (2015)
 */
public class TileArcadeCabinet extends TileAbstract implements IBlockInteract, IBlockNotifier {

	private ForgeDirection facing;
	private String gameID;

	public TileArcadeCabinet() {
		facing = ForgeDirection.SOUTH;
	}

	public ForgeDirection facing() {
		return facing;
	}

	private boolean clientSide() {
		return worldObj != null && worldObj.isRemote;
	}

	private EntityPlayer removingPlayer;

	public void startRemoveByPlayer(EntityPlayer player) {
		if (removingPlayer != null) {
			throw new IllegalStateException("startRemoveByPlayer was already invoked!");
		}
		removingPlayer = player;
	}

	public void endRemoveByPlayer() {
		if (removingPlayer == null) {
			throw new IllegalStateException("endRemoveByPlayer was already invoked!");
		}
		removingPlayer = null;
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		if (clientSide()) {
			unloadGame();
			return;
		}

		if (removingPlayer == null || !removingPlayer.capabilities.isCreativeMode) {
			Util.spawnItemStack(new ItemStack(ModBlocks.arcadeCabinet), worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, 0);
		}
		ItemStack cartridge = generateCurrentGameCartridge();
		if (cartridge != null) {
			Util.spawnItemStack(cartridge, worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, 0);
		}

		gameID = null;
	}

	@Override
	public void onBlockAdded(EntityLivingBase entityLivingBase, World world, int x, int y, int z, ItemStack itemStack) {
		facing = Util.getViewDirectionOfEntity(entityLivingBase).getOpposite();
	}

	@Override
	public void onNeighbourBlockChanged(World world, int x, int y, int z, Block block) {
		// do nothing
	}

	@Override
	public void onNeighbourTileChanged(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		// do nothing
	}

	@Override
	public boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
		ItemStack heldStack = player.getCurrentEquippedItem();

		if (heldStack == null) {
			// can remove game with empty hand and sneaking
			if (player.isSneaking() && !clientSide()) {
				// create and spawn
				if (gameID != null) {
					Util.spawnItemStack(generateCurrentGameCartridge(), world, player.posX, player.posY + 0.5, player.posZ, 0);
				}
				gameID = null;
				markForUpdate();
			}
			return true;

		} else {
			IGameCartridge cartridge = ArcadeGameRegistry.cartridgeForItem(heldStack.getItem());
			if (cartridge != null) {
				// change game with different cartridge
				String cartridgeGameID = cartridge.getGameID(heldStack);
				if (cartridgeGameID != null) {
					if (!clientSide()) {
						player.destroyCurrentEquippedItem();
						if (gameID != null) {
							Util.spawnItemStack(generateCurrentGameCartridge(), world, player.posX, player.posY + 0.5, player.posZ, 0);
						}
						gameID = cartridgeGameID;
						markForUpdate();
					}
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if (clientSide()) {
			unloadGame();
		}
	}

	@Override
	public void updateEntity() {
		if (clientSide()) {
			updateClientSide();
		}
	}

	@Override
	public void onChunkUnload() {
		if (clientSide()) {
			unloadGame();
		}
		gameID = null;
	}

	private ItemStack generateCurrentGameCartridge() {
		if (gameID == null)
			return null;

		IGameCartridge cartridge = ArcadeGameRegistry.gameForID(gameID).cartridgeItem();
		ItemStack cartridgeItem = new ItemStack(cartridge.getCartridgeItem());
		if (!DummyGames.EMPTY_GAME_WRAPPER.gameID().equals(gameID)) {
			cartridge.setGameID(cartridgeItem, gameID);
		}
		return cartridgeItem;
	}

	public List<ItemStack> generateItemStacksOnRemoval() {
		ItemStack cartridgeItem = generateCurrentGameCartridge();

		ItemStack cabinetItem = new ItemStack(ModBlocks.arcadeCabinet);

		return cartridgeItem == null ? Collections.singletonList(cabinetItem) : Arrays.asList(cabinetItem, cartridgeItem);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);

		tag.setByte(Strings.NBT_FACING, (byte) facing.ordinal());

		if (gameID != null) {
			tag.setString(Strings.NBT_GAME, gameID);
		} else {
			tag.removeTag(Strings.NBT_GAME);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);

		facing = ForgeDirection.getOrientation(tag.getByte(Strings.NBT_FACING));

		gameID = tag.hasKey(Strings.NBT_GAME) ? tag.getString(Strings.NBT_GAME) : null;

		if (worldObj != null && worldObj.isRemote) {
			if (gameID != null) {
				loadGame(false);
			} else {
				unloadGame();
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	//          ARCADE GAME STUFF                                                             //
	////////////////////////////////////////////////////////////////////////////////////////////

	@SideOnly(Side.CLIENT)
	private ArcadeMachine arcadeMachine;

	@SideOnly(Side.CLIENT)
	public ArcadeMachine arcadeMachine() {
		if (arcadeMachine == null) {
			arcadeMachine = new ArcadeMachine(100, 130);
		}
		return arcadeMachine;
	}

	////////////
	// UPDATE //
	////////////

	@SideOnly(Side.CLIENT)
	private void updateClientSide() {
		arcadeMachine().update();
	}

	@SideOnly(Side.CLIENT)
	private void loadGame(boolean forceReload) {
		arcadeMachine().load(gameID, forceReload);
	}

	@SideOnly(Side.CLIENT)
	private void unloadGame() {
		arcadeMachine().unload();
	}
}
