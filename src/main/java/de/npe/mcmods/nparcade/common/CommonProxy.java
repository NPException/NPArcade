package de.npe.mcmods.nparcade.common;

import cpw.mods.fml.common.registry.GameRegistry;
import de.npe.mcmods.nparcade.common.lib.Strings;
import de.npe.mcmods.nparcade.common.tiles.TileArcadeStool;

/**
 * Created by NPException (2015)
 */
public class CommonProxy {
	public void preInitServerSide() {
		// do nothing
	}

	public void preInitClientSide() {
		// do nothing
	}

	public void initServerSide() {
		registerTileEntities();
	}

	public void initClientSide() {
		// do nothing
	}

	public void postInitServerSide() {
		// do nothing
	}

	public void postInitClientSide() {
		// do nothing
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileArcadeStool.class, Strings.BLOCK_STOOL);
	}
}
