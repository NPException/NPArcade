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
	private final Constructor<? extends IArcadeGame> constructor;

	ArcadeGameWrapper(String id, String name, Image icon, Constructor<? extends IArcadeGame> constructor) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.constructor = constructor;
	}

	public String gameID() {
		return id;
	}

	public String gameName() {
		return name;
	}

	@SideOnly(Side.CLIENT)
	public Image gameIcon() {
		return icon;
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
