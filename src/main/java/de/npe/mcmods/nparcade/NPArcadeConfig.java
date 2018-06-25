package de.npe.mcmods.nparcade;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class NPArcadeConfig {
	public static Configuration config;
	private static Property showArcadeDebugWindows;

	static void initConfig(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		try {
			config.load();

			showArcadeDebugWindows = config.get(Configuration.CATEGORY_GENERAL, "show arcade debug windows", false,
					"When set to true, every arcade will spawn a separate window in which it's debug output will be shown.\n" +
							"The window will only be shown once some output is available.");

		} catch (Exception e) {
			NPArcade.log.error("Exception during config initialization. Any further errors may be a result of this.", e);
		} finally {
			if (config.hasChanged()) {
				config.save();
			}
		}
	}

	public static boolean showArcadeDebugWindows() {
		return showArcadeDebugWindows.getBoolean();
	}
}
