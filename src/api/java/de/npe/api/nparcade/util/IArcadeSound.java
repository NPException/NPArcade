package de.npe.api.nparcade.util;

/**
 * Created by NPException (2015)
 */
public interface IArcadeSound {

	/**
	 * Starts playing a new instance of the sound from the beginning,
	 * and returns an ID for that instance. Will return null if the
	 * sound could not be played.
	 */
	String play();

	/**
	 * Checks whether the instance of the sound with the given ID is currently playing.
	 */
	boolean isPlaying(String id);

	/**
	 * Stops playing the instance of the sound with the given ID.
	 */
	void stop(String id);
}
