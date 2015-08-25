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
public class SystemGamesLoader {
	/**
	 * Initializes the {@link ArcadeGameWrapper} that will be used for all cartridges
	 * that have an unknown gameID.
	 */
	public static ArcadeGameWrapper initUnknownGameWrapper() {
		BufferedImage label = null;
		try {
			InputStream in = Util.getResourceStream(Strings.TEXTURE_UNKNOWN_GAME_LABEL);
			label = in != null ? ImageIO.read(in) : null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ArcadeGameWrapper("__nparcade_unknown", "???", label, 0x686851, UnknownGame.class);
	}

	/**
	 * Initializes the {@link ArcadeGameWrapper} that will be used for empty cartridges.
	 */
	public static ArcadeGameWrapper initEmptyGameWrapper() {
		EmptyGame.init();
		return new ArcadeGameWrapper("__nparcade_empty", "___", null, -1, EmptyGame.class);
	}
}
