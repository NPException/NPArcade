package de.npe.mcmods.nparcade.arcade.games;

import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.api.nparcade.util.Size;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by NPException (2015)
 */
public class SampleGame implements IArcadeGame {

	public static final String ID = "nparcade_sampleGame";
	public static final String NAME = "Dat Sample Game";

	private Size screenSize;
	private boolean needsDraw;

	private int x, y;

	@Override
	public void load(IArcadeMachine arcadeMachine) {
		screenSize = new Size(100, 130);
		x = 10;
		y = 10;
	}

	@Override
	public void unload() {
		// nothing to do
	}

	@Override
	public void update() {
		if (y <= 10)
			x += 2;
		if (x >= screenSize.width-10)
			y += 2;
		if (y >= screenSize.height-10)
			x -= 2;
		if (x <= 10)
			y -= 2;
		needsDraw = true;
	}

	@Override
	public Size screenSize() {
		return screenSize;
	}

	@Override
	public boolean needsDraw() {
		return needsDraw;
	}

	@Override
	public void draw(BufferedImage screen, float partialTick) {
		needsDraw = false;
		Graphics2D g = (Graphics2D) screen.getGraphics();
		g.setBackground(Color.DARK_GRAY);
		g.clearRect(0, 0, screen.getWidth(), screen.getHeight());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawRoundRect(x-3,y-3,7,7,2,2);
	}
}
