package de.npe.api.nparcade;

import de.npe.api.nparcade.util.Size;

import java.awt.image.BufferedImage;

/**
 * This interface has to be implemented by every game that should work with NPArcade.<br>
 * The implementing class has to provide a no-args constructor.
 * <p/>
 * Created by NPException (2015)
 */
public interface IArcadeGame {

	/**
	 * This method is called whenever the game is loaded by an arcade machine.<br>
	 * It's suggested to keep <i>arcadeMachine</i> as an instance variable so you can
	 * use it's methods during calls to {@link #unload()} and {@link #update()}
	 *
	 * @param arcadeMachine the machine the game will be ran on.
	 */
	void load(IArcadeMachine arcadeMachine);

	/**
	 * This method is called when the game is unloaded by an arcade machine.
	 * That usually happens when the arcade machine is powering down or being broken.<br>
	 * It should be used to release all resources used by the game, and persist any data if necessary.
	 */
	void unload();

	/**
	 * This method is called to update the game's state.<br>
	 * How often this method is called per second can be checked by
	 * calling {@link IArcadeMachine#ticksPerSecond()} on the arcadeMachine
	 * you get during the call of the {@link #load(IArcadeMachine)} method.
	 */
	void update();

	/**
	 * Returns the screen size that the next call to {@link #draw(BufferedImage, float)} requires.<br>
	 * This must NOT return null. Each subsequent call to this method is allowed to return a different value.
	 * This method is called before each call to {@link #draw(BufferedImage, float)},
	 * to ensure that the game has enough space to draw on.
	 */
	Size screenSize();

	/**
	 * This method is called on each render tick of the arcade machine to check
	 * whether the {@link #draw(BufferedImage, float)} method should be called or not. This can (and should) be used
	 * to limit the games output to a certain framerate for example.<br>
	 * <i>(for simple arcade games it should be sufficient to only draw once after each game update.
	 * To do that this method should only return true once after each update tick.)</i>
	 *
	 * @return <i>true</i> if the {@link #draw(BufferedImage, float)} method should be called, <i>false</i> if not.
	 */
	boolean needsDraw();

	/**
	 * When this is called, the game is supposed to draw to the supplied BufferedImage.
	 * The BufferedImage is of type {@link BufferedImage#TYPE_INT_ARGB}.
	 *
	 * @param partialTick the partial time that has passed since the last game tick.
	 *                    Usually between 0 and 1, but can grow larger than 1 if the game tick is lagging behind.
	 */
	void draw(BufferedImage screen, float partialTick);
}
