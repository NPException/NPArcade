package de.npe.mcmods.nparcade.arcade.sound;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class ArcadeSoundManager {
	// Map of all sounds registered ever
	private static Map<String, ArcadeSound> globalSoundResources = new HashMap<>(512);

	private List<PositionalArcadeSound> localSounds = new LinkedList<>();

	public PositionalArcadeSound createPositionalSound(String soundName, URL soundURL, boolean streaming, float x, float y, float z) {
		ArcadeSound sndSource = globalSoundResources.get(soundName);
		if (sndSource == null) {
			sndSource = new ArcadeSound(soundName, soundURL, streaming);
			globalSoundResources.put(soundName, sndSource);
		}
		PositionalArcadeSound pSound = new PositionalArcadeSound(sndSource, x, y, z);
		localSounds.add(pSound);
		return pSound;
	}

	/**
	 * Stops all currently playing sounds of this instance,
	 * and removes them from the sound system if no other instance is using them
	 */
	public void unloadAllSounds() {
		for (PositionalArcadeSound sound : localSounds) {
			sound.stop();
			sound.invalidate();
			sound.remove();
		}
		localSounds.clear();
	}
}
