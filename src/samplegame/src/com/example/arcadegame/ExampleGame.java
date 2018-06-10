package com.example.arcadegame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;

import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.api.nparcade.util.Controls;
import de.npe.api.nparcade.util.IArcadeSound;
import de.npe.api.nparcade.util.Size;

/**
 * Created by NPException (2015)
 */
public class ExampleGame implements IArcadeGame {
	private Size screenSize;
	private boolean needsDraw;

	private Random rand;
	private final float bgS = 0.5F;
	private final float bgB = 0.6F;
	private Color background;
	private Color foreground;

	private IArcadeSound bounceSound;

	private float x, y, dx, dy;

	@Override
	public void load(IArcadeMachine arcadeMachine) {
		screenSize = new Size(100, 130);
		rand = new Random(0L);

		x = 10F + rand.nextFloat() * 80F;
		y = 10F + rand.nextFloat() * 110F;

		dx = (1F - rand.nextFloat() * 2) * 5;
		dy = (1F - rand.nextFloat() * 2) * 5;

		bounceSound = arcadeMachine.registerSound("/com/example/arcadegame/assets/bounce.ogg", false);

		float h = rand.nextFloat();
		background = Color.getHSBColor(h, bgS, bgB);
		foreground = Color.BLACK;
	}

	@Override
	public void unload(IArcadeMachine arcadeMachine) {
		rand = null;
		background = null;
		foreground = null;
		bounceSound = null;
	}

	@Override
	public void update(IArcadeMachine arcadeMachine) {
		x += dx;
		y += dy;

		boolean change = false;

		if (x <= 10F) {
			x = Math.abs(x - 10F) + 10F;
			dx = -dx;
			change = true;
		} else if (x >= 90F) {
			x = -(x - 90F) + 90F;
			dx = -dx;
			change = true;
		}

		if (y <= 10F) {
			y = Math.abs(y - 10F) + 10F;
			dy = -dy;
			change = true;
		} else if (y >= 120F) {
			y = -(y - 120F) + 120F;
			dy = -dy;
			change = true;
		}

		if (change) {
			float h = rand.nextFloat();
			background = Color.getHSBColor(h, bgS, bgB);
			bounceSound.play(1F, 1F, false);
		}

		int keyColor = 0;
		if (arcadeMachine.isKeyDown(Controls.ARCADE_KEY_BLUE)) {
			keyColor = keyColor | 0xFF0000FF;
		}
		if (arcadeMachine.isKeyDown(Controls.ARCADE_KEY_GREEN)) {
			keyColor = keyColor | 0xFF00FF00;
		}
		if (arcadeMachine.isKeyDown(Controls.ARCADE_KEY_RED)) {
			keyColor = keyColor | 0xFFFF0000;
		}
		if (arcadeMachine.isKeyDown(Controls.ARCADE_KEY_YELLOW)) {
			keyColor = 0xFF000000;
		}

		if (keyColor != 0) {
			foreground = new Color(keyColor);
		}

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
		Graphics2D g = screen.createGraphics();
		g.setBackground(background);
		g.setColor(foreground);

		g.clearRect(0, 0, screen.getWidth(), screen.getHeight());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setStroke(new BasicStroke(2));
		g.fillOval((int) (x + dx * partialTick) - 10, (int) (y + dy * partialTick) - 10, 21, 21);
	}
}
