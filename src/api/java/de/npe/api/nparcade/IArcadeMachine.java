package de.npe.api.nparcade;

import de.npe.api.nparcade.util.IArcadeSound;
import de.npe.api.nparcade.util.Size;

import java.net.URL;

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
	 * Registers a sound for the current game and returns an IArcadeSound instance
	 * for the game to work with.
	 *
	 * @param soundName The name of the sound. Every subsequent call of this method with
	 *                  the same name will return the same {@link IArcadeSound} instance that
	 *                  was returned by the first call with that name, regardless of the other
	 *                  parameters.
	 * @param soundURL  a URL to the sound data. A game will usually get this by using
	 *                  {@link Class#getResource(String)}
	 * @param streaming If this is set to true, the sound will be streamed directly from it's source.
	 *                  This is preferred for longer, less frequently played sounds, like music.
	 *                  If it is set to false, it will be loaded completely into RAM before playing.
	 *                  This is preferred for shorter, frequently used sounds, like sound effects.
	 */
	IArcadeSound registerSound(String soundName, URL soundURL, boolean streaming);
}
