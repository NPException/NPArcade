package de.npe.mcmods.nparcade.common.lib;

import me.jezza.oc.api.configuration.Config;

public class Reference {
	public static final String MOD_ID = "nparcade";
	public static final String MOD_IDENTIFIER = MOD_ID + ":";
	public static final String MOD_VERSION = "0.1";
	public static final String MOD_NAME = "NPArcade";

	@Config.ConfigBoolean(category = "General", comment = "This is just an example config")
	public static boolean SOME_CONFIG_VALUE = false;
}
