package de.npe.mcmods.nparcade.common.lib;

/**
 * Created by NPException (2015)
 */
public final class Strings {
	public static final String BLOCK_STOOL = Reference.MOD_ID + "_stool";
	public static final String BLOCK_ARCADE_BASE = Reference.MOD_ID + "_arcadeBase";
	public static final String BLOCK_ARCADE_CABINET = Reference.MOD_ID + "_arcadeCabinet";

	public static final String ITEM_CARTRIDGE = Reference.MOD_ID + "_gameCartridge";

	public static final String ENTITY_STOOL = BLOCK_STOOL + "Entity";


	private static final String KEY_PREFIX = "key." + Reference.MOD_ID;
	public static final String KEY_ARCADE_BUTTON_RED = KEY_PREFIX + ".button.red";
	public static final String KEY_ARCADE_BUTTON_GREEN = KEY_PREFIX + ".button.green";
	public static final String KEY_ARCADE_BUTTON_BLUE = KEY_PREFIX + ".button.blue";
	public static final String KEY_ARCADE_BUTTON_YELLOW = KEY_PREFIX + ".button.yellow";

	public static final String NBT_FACING = Reference.MOD_ID + "_facing";
	public static final String NBT_GAME = Reference.MOD_ID + "_game";

	public static final String TEXTURE_UNKNOWN_GAME_LABEL = "textures/misc/nparcade_label_torn.png";
	public static final String TEXTURE_EMPTY_GAME_SCREEN = "textures/misc/nparcade_empty_game_screen1.png";
	public static final String TEXTURE_EMPTY_GAME_SCREEN_GLITCH = "textures/misc/nparcade_empty_game_screen2.png";

	public static final String JSON_GAME_INFO_FILE = "game-info.json";
	public static final String JSON_GAME_INFO_ID = "id";
	public static final String JSON_GAME_INFO_TITLE = "title";
	public static final String JSON_GAME_INFO_DESCRIPTION = "description";
	public static final String JSON_GAME_INFO_CLASS = "class";
	public static final String JSON_GAME_INFO_LABEL = "label";
	public static final String JSON_GAME_INFO_CARTRIDGE_COLOR = "cartridge_color";

	public static final String PATH_FOLDER_NPARCADE_GAMES = "nparcade_games";

	public static final String EMPTY_GAME_ID = "__nparcade_empty";

	public static final String LANG_TOOLTIP_CARTRIDGE_UNKNOWN = "tooltip.item.cartridge.unknown";
	public static final String LANG_TOOLTIP_CARTRIDGE_UNKNOWN_EXPLANATION = "tooltip.item.cartridge.unknown.explanation";
	public static final String LANG_TOOLTIP_CARTRIDGE_CONTENT = "tooltip.item.cartridge.content";
	public static final String LANG_TOOLTIP_CARTRIDGE_CONTENT_NONE = "tooltip.item.cartridge.content.none";
}
