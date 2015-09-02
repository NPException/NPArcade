package com.example.arcadegame;

import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.api.nparcade.util.IArcadeSound;
import de.npe.api.nparcade.util.Size;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by NPException (2015)
 */
public class ExampleGame implements IArcadeGame {
	private Size screenSize;
	private boolean needsDraw;

	private Random rand;
	private float bgS = 0.5F, bgB = 0.6F, fgS = 0.5F, fgB = 0.8F;
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
		foreground = Color.getHSBColor(h + 0.5F, fgS, fgB);
	}

	@Override
	public void unload() {
		rand = null;
		background = null;
		foreground = null;
		bounceSound = null;
	}

	@Override
	public void update() {
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
			foreground = Color.getHSBColor(h + 0.5F, fgS, fgB);
			bounceSound.play(1F,1F,false);
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
		g.setColor(Color.BLACK);

		g.clearRect(0, 0, screen.getWidth(), screen.getHeight());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setStroke(new BasicStroke(2));
		g.fillOval((int) (x+dx*partialTick) - 10, (int) (y+dy*partialTick) - 10, 21, 21);
	}
}
