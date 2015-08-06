package de.npe.mcmods.nparcade.common.tileentities;

import de.npe.mcmods.nparcade.common.entities.EntityArcadeStool;
import de.npe.mcmods.nparcade.common.util.Util;
import me.jezza.oc.common.interfaces.IBlockInteract;
import me.jezza.oc.common.tile.TileAbstract;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Created by NPException (2015)
 */
public class TileArcadeStool extends TileAbstract implements IBlockInteract {

	public float rotation;

	public TileArcadeStool() {
		rotation = (float) Math.round(Util.rand.nextFloat() * 360.0F);
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
		if (player.getCurrentEquippedItem() != null) {
			return false;
		}
		if (!worldObj.isRemote) {
			AxisAlignedBB stoolBoundingBox = getBlockType().getCollisionBoundingBoxFromPool(worldObj,xCoord,yCoord,zCoord).expand(0.0,0.01,0.0);
			Entity stool = world.findNearestEntityWithinAABB(EntityArcadeStool.class, stoolBoundingBox, player);
			if (stool == null || stool.isDead) {
				stool = new EntityArcadeStool(worldObj, this);
				worldObj.spawnEntityInWorld(stool);
			}
			if (stool.riddenByEntity == null) {
				int above = yCoord + 1;
				if (worldObj.getBlock(xCoord, above, zCoord).isAir(worldObj, xCoord, above, zCoord)) {
					player.mountEntity(stool);
				} else {
					stool.setDead();
				}
			}
		}
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		Util.getModNBTTag(tag, true).setShort("stoolRotation", (short) Math.round(rotation));
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		rotation = (float) Util.getModNBTTag(tag, false).getShort("stoolRotation");
	}
}
