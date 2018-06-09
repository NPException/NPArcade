package de.npe.mcmods.nparcade.arcade;

import static de.npe.mcmods.nparcade.common.lib.Strings.JSON_GAME_INFO_CARTRIDGE_COLOR;
import static de.npe.mcmods.nparcade.common.lib.Strings.JSON_GAME_INFO_CLASS;
import static de.npe.mcmods.nparcade.common.lib.Strings.JSON_GAME_INFO_DESCRIPTION;
import static de.npe.mcmods.nparcade.common.lib.Strings.JSON_GAME_INFO_ID;
import static de.npe.mcmods.nparcade.common.lib.Strings.JSON_GAME_INFO_LABEL;
import static de.npe.mcmods.nparcade.common.lib.Strings.JSON_GAME_INFO_TITLE;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Map;

import javax.imageio.ImageIO;

import de.npe.api.nparcade.IArcadeGame;

/**
 * Created by NPException (2015)
 */
class GameInfo {
	public final String id;
	public final String title;
	public final String description;
	public final Class<? extends IArcadeGame> gameClass;
	public final BufferedImage label;
	public final String cartridgeColor;

	/**
	 * Tries to create a GameInfo object, that holds all the information necessary to register an arcade game.
	 * During the constructor call, the data map gets evaluated and an {@link IllegalArgumentException} is
	 * thrown, should the provided data be incomplete or not usable.
	 *
	 * @param data
	 * 		the data to load the game. It was read from the game.info JSON file.
	 * @param classLoader
	 * 		the ClassLoader that should be used to load the game class
	 * @throws IllegalStateException
	 * 		if the game could not be loaded properly
	 */
	GameInfo(Map<String, String> data, ClassLoader classLoader) throws IllegalStateException {
		// VALIDATE GAME ID
		id = data.get(JSON_GAME_INFO_ID);
		if (id == null) {
			throw new IllegalStateException("game.info attribute '" + JSON_GAME_INFO_ID + "' is missing!");
		}

		// VALIDATE GAME TITLE
		title = data.get(JSON_GAME_INFO_TITLE);
		if (title == null) {
			throw new IllegalStateException("game.info attribute '" + JSON_GAME_INFO_TITLE + "' is missing!");
		}

		String tmpDescription = data.get(JSON_GAME_INFO_DESCRIPTION);
		description = tmpDescription == null || tmpDescription.trim().isEmpty() ? null : tmpDescription.trim();

		// VALIDATE GAME CLASS
		String className = data.get(JSON_GAME_INFO_CLASS);
		if (className == null) {
			throw new IllegalStateException("game.info attribute '" + JSON_GAME_INFO_CLASS + "' is missing!");
		}

		Class<?> classCandidate;
		try {
			classCandidate = classLoader.loadClass(className);
		} catch (ClassNotFoundException cnfe) {
			throw new IllegalStateException("game.info attribute '" + JSON_GAME_INFO_CLASS + "' -> Class not found: " + className);
		} catch (Exception ex) {
			throw new IllegalStateException("game.info attribute '" + JSON_GAME_INFO_CLASS + "' -> Class could not be loaded: " + className);
		}

		if (!IArcadeGame.class.isAssignableFrom(classCandidate)) {
			throw new IllegalStateException("game.info attribute '" + JSON_GAME_INFO_CLASS + "' -> Class not found!");
		}

		// generics are fine now.
		//noinspection unchecked
		gameClass = (Class<? extends IArcadeGame>) classCandidate;

		// VALIDATE GAME LABEL
		String labelResourcePath = data.get(JSON_GAME_INFO_LABEL);
		if (labelResourcePath == null || labelResourcePath.trim().isEmpty()) {
			label = null;
		} else {
			try {
				InputStream in = gameClass.getResourceAsStream(labelResourcePath.trim());
				label = in != null ? ImageIO.read(in) : null;
			} catch (Exception ex) {
				throw new IllegalStateException("game.info attribute '" + JSON_GAME_INFO_LABEL + "' -> could not load label image!" + labelResourcePath);
			}
			if (label == null) {
				throw new IllegalStateException("game.info attribute '" + JSON_GAME_INFO_LABEL + "' -> could not find label image: " + labelResourcePath);
			}
		}

		// VALIDATE CARTIDGE COLOR
		String colorHex = data.get(JSON_GAME_INFO_CARTRIDGE_COLOR);
		cartridgeColor = colorHex == null || colorHex.trim().isEmpty() ? null : colorHex;
		if (cartridgeColor != null) {
			try {
				Integer.parseInt(cartridgeColor, 16);
			} catch (Exception ex) {
				throw new IllegalArgumentException("game.info attribute '" + JSON_GAME_INFO_CARTRIDGE_COLOR + "' -> '" + cartridgeColor + "' is not a valid hex value!");
			}
		}
	}
}
