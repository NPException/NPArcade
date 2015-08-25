package de.npe.mcmods.nparcade.common.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.arcade.ArcadeMachine;
import de.npe.mcmods.nparcade.arcade.api.IItemGameCartridge;
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

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		// do nothing
		// TODO: proper cartridge drop, keep in mind ArcadeGameRegistry.isDummyGame(gameID)
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
			if (player.isSneaking() && !worldObj.isRemote) {
				// create and spawn
				if (gameID != null) {
//						ItemStack oldCartridge = new ItemStack(ModItems.cartridge);
//						oldCartridge.setTagCompound(new NBTTagCompound());
					// TODO spawn item in world
				}
				gameID = null;
				markForUpdate();
			}
			return true;

		} else if (heldStack.getItem() instanceof IItemGameCartridge) {
			// change game with different cartridge
			IItemGameCartridge cartridge = (IItemGameCartridge) heldStack.getItem();
			String cartridgeGameID = cartridge.getGameID(heldStack);
			if (cartridgeGameID != null) {
				if (!worldObj.isRemote) {
					gameID = cartridgeGameID;
					markForUpdate();
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) {
			updateClientSide();
		}
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
