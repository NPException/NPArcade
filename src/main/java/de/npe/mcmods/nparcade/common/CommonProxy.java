package de.npe.mcmods.nparcade.common;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.common.entities.EntityArcadeStool;
import de.npe.mcmods.nparcade.common.lib.Strings;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeCabinet;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeStool;

/**
 * Created by NPException (2015)
 */
public class CommonProxy {
	public final void preInitServerSide() {
		// do nothing
	}

	public void preInitClientSide() {
		// do nothing
	}

	public final void initServerSide() {
		registerTileEntities();
		registerEntities();
	}

	public void initClientSide() {
		// do nothing
	}

	public final void postInitServerSide() {
		// do nothing
	}

	public void postInitClientSide() {
		// do nothing
	}

	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileArcadeStool.class, Strings.BLOCK_STOOL);
		GameRegistry.registerTileEntity(TileArcadeCabinet.class, Strings.BLOCK_ARCADE_CABINET);
	}

	private void registerEntities() {
		EntityRegistry.registerModEntity(EntityArcadeStool.class, Strings.ENTITY_STOOL, 0, NPArcade.instance, 80, 20, false);
	}
}
