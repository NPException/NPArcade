package de.npe.mcmods.nparcade;

import de.npe.mcmods.nparcade.common.lib.Reference;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, dependencies = "required-after:Forge@[10.13.4.1448,);required-after:OmnisCore@[0.0.6,);")
public class ExampleMod {

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// some example code
		System.out.println("DIRT BLOCK >> " + Blocks.dirt.getUnlocalizedName());
	}
}
