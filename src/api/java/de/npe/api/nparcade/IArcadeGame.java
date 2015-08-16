package de.npe.api.nparcade;

import java.awt.image.BufferedImage;

/**
 * Created by NPException (2015)
 */
public interface IArcadeGame {

	/**
	 * Returns the screen width this game requires.<br>
	 * This must return a positive value. The return value may change at any time.
	 * This method is called before each call to {@link #draw(BufferedImage, float)},
	 * to ensure that the game has enough space to draw on.
	 */
	public int screenWidth();

	/**
	 * Returns the screen height this game requires.<br>
	 * This must return a positive value. The return value may change at any time.
	 * This method is called before each call to {@link #draw(BufferedImage, float)},
	 * to ensure that the game has enough space to draw on.
	 */
	public int screenHeight();

	/**
	 * This method is called on each render tick of the arcade machine to check
	 * whether the {@link #draw(BufferedImage, float)} method should be called or not. This can (and should) be used
	 * to limit the games output to a certain framerate for example.<br>
	 * <i>(for simple arcade games it should be sufficient to only draw once after each game update.
	 * To do that this method should only return true once after each update tick.)</i>
	 *
	 * @return <i>true</i> if the {@link #draw(BufferedImage, float)} method should be called, <i>false</i> if not.
	 */
	public boolean needsDraw();

	/**
	 * When this is called, the game is supposed to draw to the supplied BufferedImage.
	 * The BufferedImage is of type {@link BufferedImage#TYPE_INT_ARGB}.
	 *
	 * @param partialTick the partial time that has passed since the last game tick.
	 *                    Usually between 0 and 1, but can grow larger than 1 if the game tick is lagging behind.
	 * @return the BufferedImage that this game rendered to.
	 */
	public void draw(BufferedImage screen, float partialTick);
}
