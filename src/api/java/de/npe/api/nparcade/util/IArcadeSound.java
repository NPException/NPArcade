package de.npe.api.nparcade.util;

/**
 * Created by NPException (2015)
 */
public interface IArcadeSound {

	/**
	 * Starts playing the sound from the beginning if possible.
	 *
	 * @param volume
	 * 		the volume this sound should be played with. Value range: [0.0 - 1.0]
	 * @param pitch
	 * 		the pitch this sound should be played with. Value range: [0.5 - 2.0]
	 * @param loop
	 * 		whether this sound should loop endlessly or not.
	 */
	void play(float volume, float pitch, boolean loop);

	/**
	 * Checks if the sound is currently playing.
	 */
	boolean isPlaying();

	/**
	 * Stops playing the sound.
	 */
	void stop();

	/**
	 * Pauses playing the sound.
	 */
	void pause();

	/**
	 * Resumes playing the sound.
	 */
	void resume();
}
