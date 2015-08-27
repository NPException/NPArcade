package de.npe.mcmods.nparcade.common.lib;

import me.jezza.oc.api.configuration.Config;

public class Reference {
	public static final String MOD_ID = "nparcade";
	public static final String MOD_IDENTIFIER = MOD_ID + ":";
	public static final String MOD_VERSION = "0.1";
	public static final String MOD_NAME = "NP Arcade";

	public static final String CLIENT_PROXY_CLASS = "de.npe.mcmods.nparcade.client.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "de.npe.mcmods.nparcade.common.CommonProxy";

	public static final String MOD_DOWNLOAD_URL = "http://minecraft.curseforge.com/mc-mods/226990-dimensional-pockets";
	public static final String MOD_CHANGELOG_URL = "https://github.com/NPException42/Dimensional-Pockets/wiki/Changelog";
	public static final String REMOTE_VERSION_FILE = "https://raw.githubusercontent.com/NPException42/Dimensional-Pockets/master/latest_versions.json";

	@Config.ConfigBoolean(category = "General", comment = "This is just an example config")
	public static boolean SOME_CONFIG_VALUE = false;
}
