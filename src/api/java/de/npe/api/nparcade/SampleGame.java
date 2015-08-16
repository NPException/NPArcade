package de.npe.api.nparcade;

import de.npe.mcmods.nparcade.common.util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by NPException (2015)
 */
public class SampleGame implements IArcadeGame {

	private IArcadeMachine arcadeMachine;
	private long nextDraw = 0L;

	@Override
	public void init(IArcadeMachine arcadeMachine) {
		this.arcadeMachine = arcadeMachine;
	}

	@Override
	public int screenWidth() {
		return 50;
	}

	@Override
	public int screenHeight() {
		return 65;
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
		Graphics2D g = (Graphics2D) screen.getGraphics();
		for (int y = 0; y < screenHeight(); y++) {
			for (int x = 0; x < screenWidth(); x++) {
				int brightness = Util.rand.nextInt(256);
				int rgb = 255<<24 | brightness<<16 | brightness<<8 | brightness;
				screen.setRGB(x,y,rgb);
			}
		}
	}
}
