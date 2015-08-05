package de.npe.mcmods.nparcade.common.tiles;

import net.minecraft.tileentity.TileEntity;

import java.util.Random;

/**
 * Created by NPException (2015)
 */
public class TileArcadeStool extends TileEntity {

	public final float rotation = new Random().nextFloat();

	@Override
	public boolean canUpdate() {
		return false;
	}
}
