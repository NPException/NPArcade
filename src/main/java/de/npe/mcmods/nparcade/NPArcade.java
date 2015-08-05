package de.npe.mcmods.nparcade;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import de.npe.mcmods.nparcade.common.CommonProxy;
import de.npe.mcmods.nparcade.common.ModBlocks;
import de.npe.mcmods.nparcade.common.ModItems;
import de.npe.mcmods.nparcade.common.lib.Reference;
import me.jezza.oc.client.CreativeTabSimple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, dependencies = "required-after:Forge@[10.13.4.1448,);required-after:OmnisCore@[0.0.6,);")
public class NPArcade {

	public static Logger log;

	@Mod.Instance(Reference.MOD_ID)
	public static NPArcade instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static CreativeTabSimple creativeTab = new CreativeTabSimple(Reference.MOD_ID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		log = LogManager.getLogger(Reference.MOD_NAME.replaceAll(" ", ""));

		proxy.preInitServerSide();
		proxy.preInitClientSide();

		ModBlocks.init();
		ModItems.init();

		creativeTab.setIcon(ModBlocks.stool);

		ModItems.initRecipes();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.initServerSide();
		proxy.initClientSide();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInitServerSide();
		proxy.postInitClientSide();
	}

	@EventHandler
	public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
	}

	@EventHandler
	public void onServerStarted(FMLServerStartingEvent event) {
	}

	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent event) {
	}
}
