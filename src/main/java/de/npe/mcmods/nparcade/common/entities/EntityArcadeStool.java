package de.npe.mcmods.nparcade.common.entities;

import de.npe.mcmods.nparcade.common.tileentities.TileArcadeStool;
import de.npe.mcmods.nparcade.common.util.CoordSet;
import de.npe.mcmods.nparcade.common.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by NPException (2015)
 */
public class EntityArcadeStool extends Entity {

	private int validityCheckTicks = 10;
	private CoordSet tileCoords;

	public EntityArcadeStool(World world, TileArcadeStool stoolTE) {
		super(world);

		tileCoords = stoolTE.getCoordSet();

		setPosition(stoolTE.xCoord + 0.5, stoolTE.yCoord, stoolTE.zCoord + 0.5);
	}

	@Override
	protected void entityInit() {
		setInvisible(true);
		setSize(0.0F, 0.0F);
	}

	@Override
	public void onEntityUpdate() {
		if (!worldObj.isRemote) {
			validityCheckTicks--;
			if (validityCheckTicks <= 0) {
				validityCheckTicks = 10;

				// check if stool tile is there. Set dead if not.
				if (riddenByEntity == null || tileCoords == null || !(tileCoords.getTileEntity(worldObj) instanceof TileArcadeStool)) {
					setDead();
				}
			}
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public double getMountedYOffset() {
		return 0.5;
	}

	@Override
	public void setPositionAndRotation2(double x, double y, double z, float par7, float par8, int par9) {
		// This fixes a bug where the player is shifted upwards sometimes, when sitting on the stool
		setPosition(x, y, z);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		tileCoords = CoordSet.readFromNBT(Util.getModNBTTag(tag, false), "tileCoords");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		if (tileCoords != null) {
			tileCoords.writeToNBT(Util.getModNBTTag(tag, true), "tileCoords");
		}
	}
}
