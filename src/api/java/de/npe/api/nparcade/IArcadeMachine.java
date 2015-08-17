package de.npe.api.nparcade;

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
}
