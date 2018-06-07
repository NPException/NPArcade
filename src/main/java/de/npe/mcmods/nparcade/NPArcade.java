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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID,
		name = Reference.MOD_NAME,
		version = Reference.MOD_VERSION,
		dependencies = "required-after:Forge@[10.13.4.1448,)")
public class NPArcade {

	public static Logger log;

	@Mod.Instance(Reference.MOD_ID)
	public static NPArcade instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	private static CommonProxy proxy;

	public static CreativeTabs creativeTab = new CreativeTabs(Reference.MOD_ID) {
		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(ModBlocks.arcadeCabinet);
		}
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		log = event.getModLog();

		ArcadeGameRegistry.init();

		proxy.preInitServerSide();
		proxy.preInitClientSide();

		ModBlocks.init();
		ModItems.init();

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
