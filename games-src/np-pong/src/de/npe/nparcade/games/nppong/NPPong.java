package de.npe.nparcade.games.nppong;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.api.nparcade.util.Controls;
import de.npe.api.nparcade.util.Size;

/**
 * Created by NPException (2015)
 */
public class NPPong implements IArcadeGame {
	private final Size screenSize;
	private boolean needsDraw;

	private static final float bgB = 0.6F;
	private final Color background = Color.getHSBColor(215F/255F, 50F/255F, 20F/255F);;
	private Color foreground;

	private final float speed;
	private float x, y, dx, dy;

	public NPPong(IArcadeMachine arcadeMachine) {
		int suggestedWidth = arcadeMachine.suggestedScreenSize().width;
		screenSize = new Size(4,3).scaleToWidth(suggestedWidth);
		Random rand = ThreadLocalRandom.current();

		speed = screenSize.width / 20f;

		x = 10F + rand.nextFloat() * 80F;
		y = 10F + rand.nextFloat() * 110F;

		dx = (1F - rand.nextFloat() * 2) * speed;
		dy = (1F - rand.nextFloat() * 2) * speed;

		foreground = Color.WHITE;
	}

	@Override
	public void update(IArcadeMachine arcadeMachine) {
		x += dx;
		y += dy;

		float edgeX = screenSize.width - 10;
		float edgeY = screenSize.height - 10;

		if (x <= 10F) {
			x = Math.abs(x - 10F) + 10F;
			dx = -dx;
		} else if (x >= edgeX) {
			x = -(x - edgeX) + edgeX;
			dx = -dx;
		}

		if (y <= 10F) {
			y = Math.abs(y - 10F) + 10F;
			dy = -dy;
		} else if (y >= edgeY) {
			y = -(y - edgeY) + edgeY;
			dy = -dy;
		}

		if (arcadeMachine.isKeyDown(Controls.ARCADE_KEY_LEFT)) {
			dx = -speed;
		} else if (arcadeMachine.isKeyDown(Controls.ARCADE_KEY_RIGHT)) {
			dx = speed;
		}
		if (arcadeMachine.isKeyDown(Controls.ARCADE_KEY_UP)) {
			dy = -speed;
		}
		if (arcadeMachine.isKeyDown(Controls.ARCADE_KEY_DOWN)) {
			dy = speed;
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
			foreground = Color.white;
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
