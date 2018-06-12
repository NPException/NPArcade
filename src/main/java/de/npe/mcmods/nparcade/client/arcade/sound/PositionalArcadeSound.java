package de.npe.mcmods.nparcade.client.arcade.sound;

import java.util.UUID;

import de.npe.api.nparcade.util.IArcadeSound;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class PositionalArcadeSound implements IArcadeSound {

	private final ArcadeSound sndResource;
	private final float x, y, z;
	private final String id;

	private boolean invalid;

	PositionalArcadeSound(ArcadeSound sndResource, float x, float y, float z) {
		this.sndResource = sndResource;
		this.x = x;
		this.y = y;
		this.z = z;
		id = UUID.randomUUID().toString();
	}

	@Override
	public void play(float volume, float pitch, boolean loop) {
		if (!invalid) {
			sndResource.playAt(id, x, y, z, volume, pitch, loop);
		}
	}

	@Override
	public boolean isPlaying() {
		return !invalid && sndResource.isPlaying(id);
	}

	@Override
	public void stop() {
		if (!invalid) {
			sndResource.stop(id);
		}
	}

	@Override
	public void pause() {
		if (!invalid) {
			sndResource.pause(id);
		}
	}

	@Override
	public void resume() {
		if (!invalid) {
			sndResource.resume(id);
		}
	}

	public void remove() {
		sndResource.remove(id);
	}

	public void invalidate() {
		invalid = true;
	}
}
