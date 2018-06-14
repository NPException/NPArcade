package de.npe.mcmods.nparcade.client.arcade.sound;

import de.npe.api.nparcade.util.IArcadeSound;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by NPException (2018)
 */
@SideOnly(Side.CLIENT)
public class EmptySound implements IArcadeSound {
	public static final EmptySound INSTANCE = new EmptySound();

	private EmptySound() {
	}

	@Override
	public void play(float volume, float pitch, boolean loop) {
	}

	@Override
	public boolean isPlaying() {
		return false;
	}

	@Override
	public void stop() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
