package de.npe.mcmods.nparcade;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import de.npe.mcmods.nparcade.arcade.ArcadeGameRegistry;
import de.npe.mcmods.nparcade.common.CommonProxy;
import de.npe.mcmods.nparcade.common.ModBlocks;
import de.npe.mcmods.nparcade.common.ModItems;
import de.npe.mcmods.nparcade.common.lib.Reference;
import me.jezza.oc.client.CreativeTabSimple;
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
		log = event.getModLog();

		ArcadeGameRegistry.init();

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
}
