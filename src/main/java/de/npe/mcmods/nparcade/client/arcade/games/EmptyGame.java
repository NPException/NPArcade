package de.npe.mcmods.nparcade.client.arcade.games;

import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.api.nparcade.util.Size;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Empty game implementation for empty game cartridges.
 * Cycles through a few prerendered white noise frames.
 *
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class EmptyGame implements IArcadeGame {
	private int[][] pixels;
	private int pixelsToDraw = 0;

	private final Size screenSize;
	private long nextDraw = 0L;

	public EmptyGame(IArcadeMachine arcadeMachine) {
		Size amSize = arcadeMachine.suggestedScreenSize();
		screenSize = new Size(amSize.width / 10, amSize.height / 10);

		// generate 10 frames of static noise
		ThreadLocalRandom random = ThreadLocalRandom.current();
		pixels = new int[10][];
		for (int pixelsIndex = 0; pixelsIndex < pixels.length; pixelsIndex++) {
			int[] pixelArray = pixels[pixelsIndex] = new int[screenSize.height * screenSize.width];
			int p = 0;
			for (int y = 0; y < screenSize.height; y++) {
				for (int x = 0; x < screenSize.width; x++) {
					int brightness = random.nextInt(256);
					int rgb = 0xFF000000     // alpha
							| brightness << 16 // red
							| brightness << 8  // green
							| brightness;      // blue
					pixelArray[p++] = rgb;
				}
			}
		}
	}

	@Override
	public void unload(IArcadeMachine arcadeMachine) {
		pixels = null;
	}

	@Override
	public void update(IArcadeMachine arcadeMachine) {
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
		pixelsToDraw = (pixelsToDraw + 1) % pixels.length;
		screen.setRGB(0, 0, screenSize.width, screenSize.height, pixels[pixelsToDraw], 0, screenSize.width);
	}
}
