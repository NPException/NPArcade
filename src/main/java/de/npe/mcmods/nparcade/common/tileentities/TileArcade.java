package de.npe.mcmods.nparcade.common.tileentities;

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
public class TileArcade extends TileAbstract implements IBlockInteract, IBlockNotifier {

	public ForgeDirection facing;

	public TileArcade() {
		facing = ForgeDirection.SOUTH;
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
		if (player.getCurrentEquippedItem() != null) {
			return false;
		}
		// initiate gamesession
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		Util.getModNBTTag(tag, true).setByte("arcadeFacing", (byte) facing.ordinal());
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		facing = ForgeDirection.getOrientation(Util.getModNBTTag(tag, false).getByte("arcadeFacing"));
	}
}
