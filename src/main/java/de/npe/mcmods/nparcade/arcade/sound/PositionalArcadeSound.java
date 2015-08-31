package de.npe.mcmods.nparcade.arcade.sound;

import de.npe.api.nparcade.util.IArcadeSound;

import java.util.UUID;

/**
 * Created by NPException (2015)
 */
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
		if (!invalid)
			sndResource.playAt(id, x, y, z, volume, pitch, loop);
	}

	@Override
	public boolean isPlaying() {
		return !invalid ? sndResource.isPlaying(id) : false;
	}

	@Override
	public void stop() {
		if (!invalid)
			sndResource.stop(id);
	}

	@Override
	public void pause() {
		if (!invalid)
			sndResource.pause(id);
	}

	@Override
	public void resume() {
		if (!invalid)
			sndResource.resume(id);
	}

	public void remove() {
		sndResource.remove(id);
	}

	public void invalidate() {
		invalid = true;
	}
}