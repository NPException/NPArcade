package de.npe.api.nparcade;

import de.npe.api.nparcade.util.Size;

import java.awt.image.BufferedImage;

/**
 * <p>This interface has to be implemented by every game that should work with NPArcade.</p>
 * <p>An implementing class must have a public no-args constructor available.
 * (For example by not declaring an explicit constructor at all.)</p>
 * <p>It is highly recommended to not do anything in the constructor,
 * and do all preparations and setup operations when {@link #load(IArcadeMachine)} is called on the instance.</p>
 * <p>For a detailed specification of how an arcade machine interacts with the game, and how {@link IArcadeGame}'s
 * methods have to be used, visit the
 * <a href="https://github.com/NPException42/NPArcade/wiki/Technical-Specification">specification page of the wiki</a>.</p>
 * <p/>
 * Created by NPException (2015)
 */
public interface IArcadeGame {

	////////////////////////////////
	// GAME STATE RELATED METHODS //
	////////////////////////////////

	/**
	 * This method is called whenever the game is loaded by an arcade machine.
	 * The arcade machine instance the game will run on is provided as a parameter.
	 */
	void load(IArcadeMachine arcadeMachine);

	/**
	 * This method is called when the game is unloaded by an arcade machine.
	 * That usually happens when the arcade machine is powering down or being broken.<br>
	 * It should be used to release all resources used by the game, and persist any data if necessary.
	 */
	void unload(IArcadeMachine arcadeMachine);

	/**
	 * This method is called to update the game's state.<br>
	 * How often this method is called per second can be checked by
	 * calling {@link IArcadeMachine#ticksPerSecond()} on the arcade machine
	 * that is provided as a parameter.
	 */
	void update(IArcadeMachine arcadeMachine);

	///////////////////////////////
	// RENDERING RELATED METHODS //
	///////////////////////////////

	/**
	 * Returns the screen size that the next call to {@link #draw(BufferedImage, float)} requires.<br>
	 * Each subsequent call to this method is allowed to return a different value.
	 * This method is called before each call to {@link #draw(BufferedImage, float)},
	 * to ensure that the game has enough space to draw on.<br>
	 * <br>
	 * <i>Must NOT return null!</i>
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
	 * The BufferedImage is of type {@link BufferedImage#TYPE_INT_ARGB} and guaranteed to have the size
	 * that was returned by the last call to {@link #screenSize()}.
	 *
	 * @param partialTick the partial time that has passed since the last game tick.
	 *                    Usually between 0 and 1, but can grow larger than 1 if the game tick is lagging behind.
	 */
	void draw(BufferedImage screen, float partialTick);
}
