package de.npe.mcmods.nparcade.client.arcade;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.client.arcade.games.EmptyGame;
import de.npe.mcmods.nparcade.client.arcade.games.UnknownGame;
import de.npe.mcmods.nparcade.common.lib.Strings;
import de.npe.mcmods.nparcade.common.util.Util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
final class DummyGames {
	static final ArcadeGameWrapper UNKNOWN_GAME_WRAPPER = initUnknownGameWrapper();
	static final ArcadeGameWrapper EMPTY_GAME_WRAPPER = initEmptyGameWrapper();

	/**
	 * Initializes the {@link ArcadeGameWrapper} that will be used for all cartridges
	 * that have an unknown gameID.
	 */
	private static ArcadeGameWrapper initUnknownGameWrapper() {
		UnknownGame.init();
		BufferedImage label = null;
		try (InputStream in = Util.getResourceStream(Strings.TEXTURE_UNKNOWN_GAME_LABEL)) {
			label = in != null
					? ImageIO.read(in)
					: null;
		} catch (Exception ex) {
			NPArcade.log.warn("Failed to load label image for 'unknown' game: " + Strings.TEXTURE_UNKNOWN_GAME_LABEL);
		}
		return new ArcadeGameWrapper("???", null, label, -1, UnknownGame.class);
	}

	/**
	 * Initializes the {@link ArcadeGameWrapper} that will be used for empty cartridges.
	 */
	private static ArcadeGameWrapper initEmptyGameWrapper() {
		return new ArcadeGameWrapper("___", null, null, -1, EmptyGame.class);
	}
}
