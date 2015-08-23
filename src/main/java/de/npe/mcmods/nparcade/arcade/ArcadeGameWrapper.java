package de.npe.mcmods.nparcade.arcade;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.api.nparcade.IArcadeGame;

import java.awt.*;
import java.lang.reflect.Constructor;

/**
 * This class holds information about the game, and (if on the client side)
 * offers a method to create a new instance of that game.
 */
public final class ArcadeGameWrapper {
	private final String id;
	private final String name;

	private final Image icon;
	private boolean hasColor;
	private float colorRed = 1F;
	private float colorGreen = 1F;
	private float colorBlue = 1F;

	private final Constructor<? extends IArcadeGame> constructor;

	ArcadeGameWrapper(String id, String name, Image icon, int color, Class<? extends IArcadeGame> gameClass) {
		this.id = id;
		this.name = name;
		this.icon = icon;

		try {
			constructor = gameClass != null ? gameClass.getConstructor() : null;
		} catch (Exception ex) {
			throw new IllegalArgumentException("Class " + gameClass.getCanonicalName() + " is missing public no-args constructor!", ex);
		}

		if (color != -1) {
			hasColor = true;
			int r = ((color >> 16) & 0xFF);
			colorRed = r / 255F;
			int g = ((color >> 8) & 0xFF);
			colorGreen = g / 255F;
			int b = (color & 0xFF);
			colorBlue = b / 255F;
		}
	}

	public String gameID() {
		return id;
	}

	public String gameName() {
		return name;
	}

	/////////////////////////
	// CLIENT ONLY METHODS //
	/////////////////////////

	@SideOnly(Side.CLIENT)
	public Image gameIcon() {
		return icon;
	}

	@SideOnly(Side.CLIENT)
	public boolean hasColor() {
		return hasColor;
	}

	@SideOnly(Side.CLIENT)
	public float colorRed() {
		return colorRed;
	}

	@SideOnly(Side.CLIENT)
	public float colorGreen() {
		return colorGreen;
	}

	@SideOnly(Side.CLIENT)
	public float colorBlue() {
		return colorBlue;
	}

	@SideOnly(Side.CLIENT)
	public IArcadeGame createGameInstance() {
		try {
			return constructor.newInstance();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
