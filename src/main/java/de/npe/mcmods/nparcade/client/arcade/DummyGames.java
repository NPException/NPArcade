package de.npe.mcmods.nparcade.client.arcade;

import de.npe.mcmods.nparcade.client.arcade.games.EmptyGame;
import de.npe.mcmods.nparcade.client.arcade.games.UnknownGame;

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
		UnknownGame.init();
		return new ArcadeGameWrapper("???", null, null, 0x686851, UnknownGame.class);
	}

	/**
	 * Initializes the {@link ArcadeGameWrapper} that will be used for empty cartridges.
	 */
	private static ArcadeGameWrapper initEmptyGameWrapper() {
		return new ArcadeGameWrapper("___", null, null, -1, EmptyGame.class);
	}
}
