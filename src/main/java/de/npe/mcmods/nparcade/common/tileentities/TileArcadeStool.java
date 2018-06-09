package de.npe.mcmods.nparcade.common.tileentities;

import java.util.concurrent.ThreadLocalRandom;

import de.npe.mcmods.nparcade.common.entities.EntityArcadeStool;
import de.npe.mcmods.nparcade.common.util.CoordSet;
import de.npe.mcmods.nparcade.common.util.Util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Created by NPException (2015)
 */
public class TileArcadeStool extends TileEntity {

	public float rotation;

	public TileArcadeStool() {
		this.rotation = (float) Math.round(ThreadLocalRandom.current().nextFloat() * 360.0F);
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
}
