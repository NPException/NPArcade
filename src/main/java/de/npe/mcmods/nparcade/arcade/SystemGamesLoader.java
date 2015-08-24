package de.npe.mcmods.nparcade.arcade;

import de.npe.mcmods.nparcade.arcade.games.UnknownGame;
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
			InputStream in = Util.getResourceStream("textures/misc/nparcade_label_torn.png");
			label = in != null ? ImageIO.read(in) : null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ArcadeGameWrapper("nparcade_unknown", "???", label, 0xFF7F00, UnknownGame.class);
	}

	/**
	 * Initializes the {@link ArcadeGameWrapper} that will be used for empty cartridges.
	 */
	public static ArcadeGameWrapper initEmptyGameWrapper() {
		// TODO: empty game class (with TV-test screen and proper label)
		// return new ArcadeGameWrapper("nparcade_empty", "___", null, -1, EmptyGame.class);
		return null;
	}
}
