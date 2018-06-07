package de.npe.mcmods.nparcade.common.tileentities;

import de.npe.mcmods.nparcade.common.entities.EntityArcadeStool;
import de.npe.mcmods.nparcade.common.util.CoordSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Created by NPException (2015)
 */
public class TileArcadeStool extends TileEntity {

	private float rotation = -1;

	public float rotationDeg() {
		if (rotation == -1 && !(xCoord == 0 && yCoord == 0 && zCoord == 0)) {
			int hash = 1;
			hash = 31 * hash + xCoord;
			hash = 31 * hash + yCoord;
			hash = 31 * hash + zCoord;
			int mod = hash % 360;
			rotation = mod < 0 ? mod + 360 : mod;
		}
		return rotation != -1
				? rotation
				: 0;
	}

	public CoordSet getCoordSet() {
		return new CoordSet(xCoord, yCoord, zCoord);
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	public boolean onActivated(World world, EntityPlayer player) {
		if (player.getCurrentEquippedItem() != null) {
			return false;
		}
		if (!worldObj.isRemote) {
			AxisAlignedBB stoolBoundingBox = getBlockType().getCollisionBoundingBoxFromPool(worldObj, xCoord, yCoord, zCoord).expand(0.0, 0.01, 0.0);
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

	//	@Override
	//	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
	//		readFromNBT(pkt.func_148857_g());
	//	}
	//
	//	@Override
	//	public Packet getDescriptionPacket() {
	//		NBTTagCompound tag = new NBTTagCompound();
	//		writeToNBT(tag);
	//		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
	//	}
}
