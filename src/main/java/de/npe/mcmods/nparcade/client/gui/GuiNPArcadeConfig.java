package de.npe.mcmods.nparcade.client.gui;

import de.npe.mcmods.nparcade.NPArcadeConfig;
import de.npe.mcmods.nparcade.common.lib.Reference;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

@SideOnly(Side.CLIENT)
public class GuiNPArcadeConfig extends GuiConfig {
	public GuiNPArcadeConfig(GuiScreen parentScreen) {
		super(parentScreen, // Let Forge know the GUI we were at before
				new ConfigElement(NPArcadeConfig.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), // What category of config to show in the GUI, can be something else (HAS TO BE IN YOUR CONFIG ALREADY!!!)
				Reference.MOD_ID,
				false, // Whether changing config requires you to relog/restart world
				false, // Whether changing config requires you to relaunch Minecraft
				GuiConfig.getAbridgedConfigPath(NPArcadeConfig.config.toString())); // Config title; this will return the config path
	}
}
