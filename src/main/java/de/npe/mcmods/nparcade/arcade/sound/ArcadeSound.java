package de.npe.mcmods.nparcade.arcade.sound;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.api.nparcade.util.IArcadeSound;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class ArcadeSound implements IArcadeSound { // TODO
	@Override
	public String play() {
		return null;
	}

	@Override
	public boolean isPlaying(String id) {
		return false;
	}

	@Override
	public void stop(String id) {

	}
}
