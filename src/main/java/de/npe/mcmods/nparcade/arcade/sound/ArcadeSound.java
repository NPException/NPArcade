package de.npe.mcmods.nparcade.arcade.sound;

import java.lang.reflect.Field;
import java.net.URL;

import de.npe.mcmods.nparcade.NPArcade;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.MathHelper;
import paulscode.sound.SoundSystem;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class ArcadeSound {

	private static Field sndManagerField;
	private static Field sndSystemField;
	private static SoundSystem sndSystem;

	private final String soundFilePath;
	private final URL soundURL;
	private final boolean streaming;

	ArcadeSound(String soundFilePath, URL soundURL, boolean streaming) {
		this.soundFilePath = soundFilePath;
		this.soundURL = soundURL;
		this.streaming = streaming;
	}

	public void playAt(String id, float x, float y, float z, float volume, float pitch, boolean loop) {
		SoundSystem system = sndSystem();
		if (system == null || system.getMasterVolume() <= 0.0F) {
			return;
		}

		float initVolume = 16.0F;

		if (volume > 1.0F) {
			initVolume *= volume;
		}

		float normalizedVolume = MathHelper.clamp_float(volume * Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.BLOCKS), 0.0F, 1.0F);

		if (normalizedVolume > 0.0F) {
			if (streaming) {
				system.newStreamingSource(false, id, soundURL, soundFilePath, loop, x, y, z, ISound.AttenuationType.LINEAR.getTypeInt(), initVolume);
			} else {
				system.newSource(false, id, soundURL, soundFilePath, loop, x, y, z, ISound.AttenuationType.LINEAR.getTypeInt(), initVolume);
			}

			float normalizedPitch = MathHelper.clamp_float(pitch, 0.5F, 2.0F);
			system.setPitch(id, normalizedPitch);
			system.setVolume(id, normalizedVolume);
			system.play(id);
		}
	}

	public boolean isPlaying(String id) {
		SoundSystem system = sndSystem();
		if (system != null) {
			system.playing(id);
		}
		return false;
	}

	public void stop(String id) {
		SoundSystem system = sndSystem();
		if (system != null) {
			system.stop(id);
		}
	}

	public void pause(String id) {
		SoundSystem system = sndSystem();
		if (system != null) {
			system.pause(id);
		}
	}

	public void resume(String id) {
		SoundSystem system = sndSystem();
		if (system != null) {
			system.play(id);
		}
	}

	public void remove(String id) {
		SoundSystem system = sndSystem();
		if (system != null) {
			system.removeSource(id);
		}
	}

	private static SoundSystem sndSystem() {
		if (sndSystem == null) {
			try {
				Field field = sndManagerField();
				if (field != null) {
					SoundManager sndManager = (SoundManager) field.get(Minecraft.getMinecraft().getSoundHandler());
					field = sndSystemField();
					if (field != null) {
						sndSystem = (SoundSystem) field.get(sndManager);
					}
				}
			} catch (Exception ex) {
				NPArcade.log.error("Exception while trying to grab SoundSystem instance", ex);
			}
		}
		return sndSystem;
	}

	private static Field sndManagerField() {
		if (sndManagerField == null) {
			for (Field field : SoundHandler.class.getDeclaredFields()) {
				if (SoundManager.class.isAssignableFrom(field.getType())) {
					field.setAccessible(true);
					sndManagerField = field;
					break;
				}
			}
			if (sndManagerField == null) {
				NPArcade.log.error("Could not grab SoundManager field from MC's SoundHandler class!");
			}
		}
		return sndManagerField;
	}

	private static Field sndSystemField() {
		if (sndSystemField == null) {
			for (Field field : SoundManager.class.getDeclaredFields()) {
				if (SoundSystem.class.isAssignableFrom(field.getType())) {
					field.setAccessible(true);
					sndSystemField = field;
					break;
				}
			}
			if (sndSystemField == null) {
				NPArcade.log.error("Could not grab SoundSystem field from MC's SoundManager class!");
			}
		}
		return sndSystemField;
	}
}
