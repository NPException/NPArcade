package de.npe.mcmods.nparcade.arcade;

import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.api.nparcade.util.Size;
import de.npe.mcmods.nparcade.common.util.Util;

import java.awt.image.BufferedImage;

/**
 * Created by NPException (2015)
 */
public class SampleGame implements IArcadeGame {

	public static final String ID = "nparcade_sampleGame";
	public static final String NAME = "Dat Sample Game";

	private Size screenSize;
	private long nextDraw = 0L;

	@Override
	public void load(IArcadeMachine arcadeMachine) {
		screenSize = new Size(20, 26);
	}

	@Override
	public void unload() {
		// nothing to do
	}

	@Override
	public void update() {
		// nothing to do (yet)
	}

	@Override
	public Size screenSize() {
		return screenSize;
	}

	@Override
	public boolean needsDraw() {
		long now = System.nanoTime();
		if (now > nextDraw) {
			long fps = 20L;
			nextDraw = now + 1000000000L / fps;
			return true;
		}
		return false;
	}

	@Override
	public void draw(BufferedImage screen, float partialTick) {
		for (int y = 0; y < screenSize.height; y++) {
			for (int x = 0; x < screenSize.width; x++) {
				int brightness = Util.rand.nextInt(256);
				int rgb = 255<<24 | brightness<<16 | brightness<<8 | brightness;
				screen.setRGB(x,y,rgb);
			}
		}
	}
}
