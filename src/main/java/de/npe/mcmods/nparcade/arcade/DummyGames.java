package de.npe.mcmods.nparcade.arcade;

import de.npe.mcmods.nparcade.arcade.games.EmptyGame;
import de.npe.mcmods.nparcade.arcade.games.UnknownGame;
import de.npe.mcmods.nparcade.common.lib.Strings;
import de.npe.mcmods.nparcade.common.util.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Created by NPException (2015)
 */
public class DummyGames {
	public static final ArcadeGameWrapper UNKNOWN_GAME_WRAPPER = initUnknownGameWrapper();
	public static final ArcadeGameWrapper EMPTY_GAME_WRAPPER = initEmptyGameWrapper();

	public static boolean isDummyGame(String gameID) {
		return EMPTY_GAME_WRAPPER.gameID().equals(gameID) || UNKNOWN_GAME_WRAPPER.gameID().equals(gameID);
	}

	/**
	 * Initializes the {@link ArcadeGameWrapper} that will be used for all cartridges
	 * that have an unknown gameID.
	 */
	private static ArcadeGameWrapper initUnknownGameWrapper() {
		BufferedImage label = null;
		try {
			InputStream in = Util.getResourceStream(Strings.TEXTURE_UNKNOWN_GAME_LABEL);
			label = in != null ? ImageIO.read(in) : null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ArcadeGameWrapper("__nparcade_unknown", "???", null, label, 0x686851, UnknownGame.class, null);
	}

	/**
	 * Initializes the {@link ArcadeGameWrapper} that will be used for empty cartridges.
	 */
	private static ArcadeGameWrapper initEmptyGameWrapper() {
		EmptyGame.init();
		return new ArcadeGameWrapper("__nparcade_empty", "___", null, null, -1, EmptyGame.class, null);
	}
}
