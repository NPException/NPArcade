package de.npe.mcmods.nparcade.arcade.games;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.api.nparcade.util.Size;
import de.npe.mcmods.nparcade.common.lib.Strings;
import de.npe.mcmods.nparcade.common.util.Util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by NPException (2015)
 */
public class EmptyGame implements IArcadeGame {
	private static boolean initialized;
	private static final Size screenSize = new Size(50, 65);
	private static BufferedImage[] screens;

	public static void init() {
		if (initialized) {
			return;
		}
		initialized = true;

		screens = new BufferedImage[2];
		try {
			screens[0] = ImageIO.read(Util.getResourceStream(Strings.TEXTURE_EMPTY_GAME_SCREEN));
			screens[1] = ImageIO.read(Util.getResourceStream(Strings.TEXTURE_EMPTY_GAME_SCREEN_GLITCH));
		} catch (Exception ex) {
			ex.printStackTrace();
			screens = null;
		}
	}

	private int screenToDraw = 0;
	private boolean needsDraw;

	@Override
	@SideOnly(Side.CLIENT)
	public void load(IArcadeMachine arcadeMachine) {
		screenToDraw = 1;
		needsDraw = true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void unload(IArcadeMachine arcadeMachine) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void update(IArcadeMachine arcadeMachine) {
		if (screenToDraw == 1) {
			screenToDraw = 0;
			needsDraw = true;
		} else if (ThreadLocalRandom.current().nextFloat() < 0.05F) {
			screenToDraw = 1;
			needsDraw = true;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Size screenSize() {
		return screenSize;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean needsDraw() {
		return needsDraw;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw(BufferedImage screen, float partialTick) {
		needsDraw = false;
		if (screens == null) {
			Graphics2D g = screen.createGraphics();
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, screenSize.width, screenSize.height);
		} else {
			screen.createGraphics().drawImage(screens[screenToDraw], 0, 0, null);
		}
	}
}
