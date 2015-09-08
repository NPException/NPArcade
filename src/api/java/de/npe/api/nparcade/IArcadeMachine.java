package de.npe.api.nparcade;

import de.npe.api.nparcade.util.IArcadeSound;
import de.npe.api.nparcade.util.Size;

/**
 * Created by NPException (2015)
 */
public interface IArcadeMachine {

	/**
	 * Returns how often this arcade machine will call
	 * the {@link IArcadeGame#update()} method per second.
	 */
	int ticksPerSecond();

	/**
	 * Returns the screen size that this arcade machine suggests the game to use.<br>
	 * A game does not necessarily have to care about this size, but it is suggested to
	 * at least use the same aspect ratio.
	 */
	Size suggestedScreenSize();

	/**
	 * Returns if the key is currently pressed down.
	 * For a list of all available key codes see {@link de.npe.api.nparcade.util.Controls}.
	 * By calling {@link #hasKeyboard()}, a game can check if it can
	 * use a full keyboard as input, or only the standard arcade keys.
	 */
	boolean isKeyDown(int keyCode);

	/**
	 * Returns if this arcade machine has a keyboard or not.
	 * This basically tells you if the keyboard key codes
	 * from {@link de.npe.api.nparcade.util.Controls} can be used
	 * for the {@link #isKeyDown(int)} method, or only the basic
	 * arcade keys are available.
	 */
	boolean hasKeyboard();

	/**
	 * Registers a sound for the current game and returns an IArcadeSound instance
	 * for the game to work with.
	 *
	 * @param soundFilePath The path to the sound file within your game's jar file. Every subsequent call of this method with
	 *                      the same path may return the same {@link IArcadeSound} instance that was returned
	 *                      by the first call with that name, regardless of the <b>streaming</b> parameter.
	 * @param streaming     If this is set to true, the sound will be streamed directly from it's source.
	 *                      This is preferred for longer, less frequently played sounds, like music.
	 *                      If it is set to false, it will be loaded completely into RAM before playing.
	 *                      This is preferred for shorter, frequently used sounds, like sound effects.
	 */
	IArcadeSound registerSound(String soundFilePath, boolean streaming);
}
