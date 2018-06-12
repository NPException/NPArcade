package de.npe.mcmods.nparcade.common.tileentities;

import java.util.List;

import de.npe.mcmods.nparcade.client.arcade.ArcadeMachine;
import de.npe.mcmods.nparcade.common.ModBlocks;
import de.npe.mcmods.nparcade.common.ModItems;
import de.npe.mcmods.nparcade.common.items.ItemCartridge;
import de.npe.mcmods.nparcade.common.lib.Strings;
import de.npe.mcmods.nparcade.common.util.Util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by NPException (2015)
 */
public class TileArcadeCabinet extends TileEntity {

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

	public void onBlockRemoval(EntityPlayer removingPlayer) {
		if (clientSide()) {
			release();
			unloadGame();
			return;
		}

		// don't spawn when removed by creative mode player
		if (!(removingPlayer != null && removingPlayer.capabilities.isCreativeMode)) {
			Util.spawnItemStack(new ItemStack(ModBlocks.arcadeCabinet), worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, 0);
		}
		ItemStack cartridge = generateCurrentGameCartridge();
		if (cartridge != null) {
			Util.spawnItemStack(cartridge, worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, 0);
		}

		gameID = null;
	}

	public void onBlockAdded(EntityLivingBase entityLivingBase) {
		facing = Util.getViewDirectionOfEntity(entityLivingBase).getOpposite();
	}

	public boolean onActivated(World world, EntityPlayer player) {
		ItemStack heldStack = player.getCurrentEquippedItem();

		if (heldStack == null) {
			// can remove game with empty hand and sneaking
			if (player.isSneaking() && !clientSide()) {
				// create and spawn
				if (gameID != null) {
					Util.spawnItemStack(generateCurrentGameCartridge(), world, player.posX, player.posY + 0.5, player.posZ, 0);
				}
				gameID = null;
				markDirty();

			} else if (!player.isSneaking() && clientSide()) {
				activate();
			}
			return true;

		} else {
			// TODO let the player place ANY item

			// player is holding a cartrige: change the played game
			if (!clientSide() && heldStack.getItem() == ModItems.cartridge) {
				heldStack.stackSize--;
				if (gameID != null) {
					Util.spawnItemStack(generateCurrentGameCartridge(), world, player.posX, player.posY + 0.5, player.posZ, 0);
				}
				gameID = ItemCartridge.getGameID(heldStack);
				markDirty();
				return true;
			}
		}

		return false;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if (clientSide()) {
			release();
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
			release();
			unloadGame();
		}
	}

	public ItemStack generateCurrentGameCartridge() {
		if (gameID == null) {
			return null;
		}
		ItemStack itemStack = new ItemStack(ModItems.cartridge);
		String cartrigeGameId = Util.isEmptyGame(gameID) ? null : gameID;
		ItemCartridge.setGameID(itemStack, cartrigeGameId);
		return itemStack;
	}

	public void generateItemStacksOnRemoval(List<ItemStack> items) {
		items.add(new ItemStack(ModBlocks.arcadeCabinet));
		ItemStack cartridgeItem = generateCurrentGameCartridge();
		if (cartridgeItem != null) {
			items.add(cartridgeItem);
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

		if (clientSide()) {
			if (gameID != null) {
				loadGame(false);
			} else {
				unloadGame();
			}
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	//          ARCADE GAME STUFF                                                             //
	////////////////////////////////////////////////////////////////////////////////////////////

	@SideOnly(Side.CLIENT)
	private ArcadeMachine arcadeMachine;

	@SideOnly(Side.CLIENT)
	public ArcadeMachine arcadeMachine() {
		if (arcadeMachine == null) {
			arcadeMachine = new ArcadeMachine(100, 130, this);
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

	@SideOnly(Side.CLIENT)
	private void activate() {
		arcadeMachine().activate();
	}

	@SideOnly(Side.CLIENT)
	private void release() {
		arcadeMachine().release();
	}
}
