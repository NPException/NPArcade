package de.npe.mcmods.nparcade.client.arcade;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

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
public final class DummyGames {
	public static final ArcadeGameWrapper UNKNOWN_GAME_WRAPPER = initUnknownGameWrapper();
	public static final ArcadeGameWrapper EMPTY_GAME_WRAPPER = initEmptyGameWrapper();

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
		return new ArcadeGameWrapper("???", null, label, 0x686851, UnknownGame.class);
	}

	/**
	 * Initializes the {@link ArcadeGameWrapper} that will be used for empty cartridges.
	 */
	private static ArcadeGameWrapper initEmptyGameWrapper() {
		EmptyGame.init();
		return new ArcadeGameWrapper("___", null, null, -1, EmptyGame.class);
	}
}
