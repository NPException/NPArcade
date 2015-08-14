package de.npe.api.nparcade;

import de.npe.api.nparcade.util.RenderUtil;

import java.awt.image.BufferedImage;

/**
 * Created by NPException (2015)
 */
public interface IArcadeGame {

	/**
	 * This method is called on each render tick of the arcade machine to check
	 * whether the {@link #draw(float)} method should be called or not. This can (and should) be used
	 * to limit the games output to a certain framerate for example.<br>
	 * <i>(for simple arcade games it should be sufficient to only draw once after each game update.
	 * To do that this method should only return true once after each update tick.)</i>
	 *
	 * @return <i>true</i> if the {@link #draw(float)} method should be called, <i>false</i> if not.
	 */
	public boolean needsDraw();

	/**
	 * When this is called, the game is supposed to draw to a BufferedImage and return it.<br>
	 * <br>
	 * It is recommended to create one BufferedImage instance using
	 * {@link de.npe.api.nparcade.util.RenderUtil#createBufferedImage(int, int, RenderUtil.TextureFormat)},
	 * keep it in an instance variable, and always draw on that instance.
	 *
	 * @param partialTick the partial time that has passed since the last game tick.
	 *                    Usually between 0 and 1, but can grow larger than 1 if the game tick is lagging behind.
	 * @return the BufferedImage that this game rendered to.
	 */
	public BufferedImage draw(float partialTick);
}
