package de.npe.mcmods.nparcade.common.tiles;

import de.npe.mcmods.nparcade.common.util.Util;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by NPException (2015)
 */
public class TileArcadeStool extends TileEntity {

	public float rotation;

	public TileArcadeStool() {
		rotation = (float) Math.round(Util.rand.nextFloat()*360.0F);
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		Util.getModNBTTag(tag,true).setShort("stoolRotation", (short) Math.round(rotation));
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		rotation = (float) Util.getModNBTTag(tag,false).getShort("stoolRotation");
	}
}
