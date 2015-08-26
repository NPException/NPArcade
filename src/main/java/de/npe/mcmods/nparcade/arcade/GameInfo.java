package de.npe.mcmods.nparcade.arcade;

import de.npe.api.nparcade.IArcadeGame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Map;

import static de.npe.mcmods.nparcade.common.lib.Strings.*;

/**
 * Created by NPException (2015)
 */
class GameInfo {
	public final String id;
	public final String title;
	public final Class<? extends IArcadeGame> gameClass;
	public final BufferedImage label;
	public final String cartridgeColor;

	/**
	 * Tries to create a GameInfo object, that holds all the information necessary to register an arcade game.
	 * During the constructor call, the data map gets evaluated and an {@link IllegalArgumentException} is
	 * thrown, should the provided data be incomplete or not usable.
	 *
	 * @param data        the data to load the game. It was read from the game.info JSON file.
	 * @param classLoader the ClassLoader that should be used to load the game class
	 * @throws IllegalArgumentException
	 */
	GameInfo(Map<String, String> data, ClassLoader classLoader) throws Exception {
		// VALIDATE GAME ID
		id = data.get(JSON_GAME_INFO_ID);
		if (id == null)
			throw new IllegalArgumentException("game.info attribute '" + JSON_GAME_INFO_ID + "' is missing!");

		// VALIDATE GAME TITLE
		title = data.get(JSON_GAME_INFO_TITLE);
		if (title == null)
			throw new IllegalArgumentException("game.info attribute '" + JSON_GAME_INFO_TITLE + "' is missing!");

		// VALIDATE GAME CLASS
		String className = data.get(JSON_GAME_INFO_CLASS);
		if (className == null)
			throw new IllegalArgumentException("game.info attribute '" + JSON_GAME_INFO_CLASS + "' is missing!");

		try {
			gameClass = (Class<? extends IArcadeGame>) classLoader.loadClass(className);
		} catch (ClassNotFoundException cnfe) {
			throw new IllegalArgumentException("game.info attribute '" + JSON_GAME_INFO_CLASS + "' -> Class not found: " + className, cnfe);
		} catch (Exception ex) {
			throw new IllegalArgumentException("game.info attribute '" + JSON_GAME_INFO_CLASS + "' -> Class could not be loaded: " + className, ex);
		}

		boolean isArcadeGame = false;
		for (Class<?> iface : gameClass.getInterfaces()) {
			isArcadeGame |= iface == IArcadeGame.class;
		}
		if (!isArcadeGame)
			throw new IllegalArgumentException("game.info attribute '" + JSON_GAME_INFO_CLASS + "' -> Class not found!");

		// VALIDATE GAME LABEL
		String labelResourcePath = data.get(JSON_GAME_INFO_LABEL);
		if (labelResourcePath == null || !labelResourcePath.trim().isEmpty()) {
			label = null;
		} else {
			try {
				InputStream in = gameClass.getResourceAsStream(labelResourcePath.trim());
				label = in != null ? ImageIO.read(in) : null;
			} catch (Exception ex) {
				throw new IllegalArgumentException("game.info attribute '" + JSON_GAME_INFO_LABEL + "' -> could not load label image!" + labelResourcePath);
			}
			if (label == null)
				throw new IllegalArgumentException("game.info attribute '" + JSON_GAME_INFO_LABEL + "' -> could not find label image: " + labelResourcePath);
		}

		// VALIDATE CARTIDGE COLOR
		String colorHex = data.get(JSON_GAME_INFO_CARTRIDGE_COLOR);
		cartridgeColor = colorHex == null || colorHex.trim().isEmpty() ? null : colorHex;
		if (cartridgeColor != null) {
			try {
				Integer.parseInt(cartridgeColor, 16);
			} catch (Exception ex) {
				throw new IllegalArgumentException("game.info attribute '" + JSON_GAME_INFO_CARTRIDGE_COLOR + "' is not a valid hex value!", ex);
			}
		}
	}
}
