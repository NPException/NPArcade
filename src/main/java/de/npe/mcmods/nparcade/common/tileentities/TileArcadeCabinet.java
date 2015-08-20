package de.npe.mcmods.nparcade.common.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.arcade.ArcadeMachine;
import de.npe.mcmods.nparcade.common.ModItems;
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

	public String gameID() {
		return gameID;
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		// do nothing
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
		ItemStack heldItem = player.getCurrentEquippedItem();
		if ((heldItem == null && player.isSneaking()) // can remove game with empty hand sneaking
				|| (heldItem != null && heldItem.getItem() == ModItems.cartridge)) { // can set game if no game is present
			if (!worldObj.isRemote) {
				if (heldItem == null) {
					// create and spawn
					if (gameID != null) {
//						ItemStack oldCartridge = new ItemStack(ModItems.cartridge);
//						oldCartridge.setTagCompound(new NBTTagCompound());
//						Util.getModNBTTag(oldCartridge.getTagCompound(), true).setString(Strings.NBT_GAME, gameID);
						// TODO spawn item in world
					}
					gameID = null;
				} else {
					NBTTagCompound modTag = heldItem.hasTagCompound() ? Util.getModNBTTag(heldItem.getTagCompound(), false) : null;
					if (modTag != null && modTag.hasKey(Strings.NBT_GAME)) {
						gameID = modTag.getString(Strings.NBT_GAME);
					} else {
						// TODO use one gameID for a old school TV test screen
						gameID = null;
					}
				}
				markForUpdate();
			}
			return true;
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
		NBTTagCompound modTag = Util.getModNBTTag(tag, true);

		modTag.setByte(Strings.NBT_FACING, (byte) facing.ordinal());

		if (gameID != null) {
			modTag.setString(Strings.NBT_GAME, gameID);
		} else {
			modTag.removeTag(Strings.NBT_GAME);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		NBTTagCompound modTag = Util.getModNBTTag(tag, false);

		facing = ForgeDirection.getOrientation(modTag.getByte(Strings.NBT_FACING));

		gameID = modTag.hasKey(Strings.NBT_GAME) ? modTag.getString(Strings.NBT_GAME) : null;

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
