package de.npe.api.nparcade;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by NPException (2015)
 */
public class SampleGame implements IArcadeGame {

	BufferedImage screen;

	@Override
	public boolean needsDraw() {
		return true;
	}

	@Override
	public BufferedImage draw(float partialTick) {
		if (screen == null) {
			screen = new BufferedImage(10,13, BufferedImage.TYPE_INT_ARGB);
		}
		Graphics2D g = (Graphics2D)screen.getGraphics();
		g.setBackground(Color.GREEN);
		g.clearRect(0, 0, 10, 13);
		g.setColor(Color.BLACK);
		g.drawRect(1, 1, 2, 2);

		return screen;
	}
}
