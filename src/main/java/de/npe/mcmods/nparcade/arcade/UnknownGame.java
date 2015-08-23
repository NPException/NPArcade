package de.npe.mcmods.nparcade.arcade;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.api.nparcade.util.Size;
import de.npe.mcmods.nparcade.common.util.Util;

import java.awt.image.BufferedImage;

/**
 * Created by NPException (2015)
 */
public class UnknownGame implements IArcadeGame {

	private int[][] pixels;
	private int pixelsToDraw = 0;

	private Size screenSize;
	private long nextDraw = 0L;

	@Override
	@SideOnly(Side.CLIENT)
	public void load(IArcadeMachine arcadeMachine) {
		Size amSize = arcadeMachine.suggestedScreenSize();
		screenSize = new Size(amSize.width/10, amSize.height/10);

		pixels = new int[10][];
		for (int pixelsIndex = 0; pixelsIndex< pixels.length; pixelsIndex++) {
			int[] pixelArray = pixels[pixelsIndex] = new int[screenSize.height*screenSize.width];
			int p = 0;
			for (int y = 0; y < screenSize.height; y++) {
				for (int x = 0; x < screenSize.width; x++) {
					int brightness = Util.rand.nextInt(256);
					int rgb = 255 << 24 | brightness << 16 | brightness << 8 | brightness;
					pixelArray[p++] = rgb;
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void unload() {
		pixels = null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void update() {
		// nothing to do (yet)
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Size screenSize() {
		return screenSize;
	}

	@Override
	@SideOnly(Side.CLIENT)
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
	@SideOnly(Side.CLIENT)
	public void draw(BufferedImage screen, float partialTick) {
		pixelsToDraw = (pixelsToDraw+ 1) % pixels.length;
		screen.setRGB(0, 0, screenSize.width, screenSize.height, pixels[pixelsToDraw], 0, screenSize.width);
	}
}
