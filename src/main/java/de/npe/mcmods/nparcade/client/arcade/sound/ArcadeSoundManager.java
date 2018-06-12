package de.npe.mcmods.nparcade.client.arcade.sound;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class ArcadeSoundManager {
	// Map of all sounds registered ever
	private static final Map<String, ArcadeSound> globalSoundResources = new ConcurrentHashMap<>(512);

	private final List<PositionalArcadeSound> localSounds = new ArrayList<>();

	public PositionalArcadeSound createPositionalSound(String soundFilePath, URL soundURL, boolean streaming, float x, float y, float z) {
		ArcadeSound sndSource = globalSoundResources.get(soundFilePath);
		if (sndSource == null) {
			sndSource = new ArcadeSound(soundFilePath, soundURL, streaming);
			globalSoundResources.put(soundFilePath, sndSource);
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
