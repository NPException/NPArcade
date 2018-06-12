package de.npe.mcmods.nparcade.client.arcade;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

import org.lwjgl.opengl.GL11;

import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.api.nparcade.util.Size;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureUtil;

/**
 * This class holds information about the game, and (if on the client side)
 * offers a method to create a new instance of that game.
 */
@SideOnly(Side.CLIENT)
public final class ArcadeGameWrapper {
	private final String title;
	private final String description;

	private boolean hasColor;
	private float colorRed = 1F;
	private float colorGreen = 1F;
	private float colorBlue = 1F;

	private final BufferedImage label;
	private final Size labelSize;
	private int textureID = -1;

	private final Class<? extends IArcadeGame> gameClass;
	private final Constructor<? extends IArcadeGame> constructor;

	ArcadeGameWrapper(String title, String description, BufferedImage label, int color, Class<? extends IArcadeGame> gameClass) {
		this.title = title;
		this.description = description;

		this.label = (label == null) ? null : new BufferedImage(label.getWidth(), label.getHeight(), BufferedImage.TYPE_INT_ARGB);
		labelSize = (label == null) ? null : new Size(label.getWidth(), label.getHeight());
		if (label != null) {
			this.label.createGraphics().drawImage(label, 0, 0, null);
		}

		this.gameClass = gameClass;
		try {
			constructor = gameClass != null
					? gameClass.getConstructor(IArcadeMachine.class)
					: null;
		} catch (Exception ex) {
			throw new IllegalArgumentException("Class " + gameClass.getCanonicalName() + " is missing a public constructor which takes an IArcadeMachine!", ex);
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

	public String gameTitle() {
		return title;
	}

	Class<? extends IArcadeGame> gameClass() {
		return gameClass;
	}

	public String gameDescription() {
		return description;
	}

	public boolean hasLabel() {
		return label != null;
	}

	public Size labelSize() {
		return labelSize;
	}

	public int prepareLabelTexture() {
		// allocate new texture if not yet initialized
		if (textureID == -1) {
			textureID = GL11.glGenTextures();
			TextureUtil.allocateTexture(textureID, labelSize.width, labelSize.height);

			// upload pixels to texture
			int[] pixels = new int[labelSize.width * labelSize.height];
			label.getRGB(0, 0, labelSize.width, labelSize.height, pixels, 0, labelSize.width);
			TextureUtil.uploadTexture(textureID, pixels, labelSize.width, labelSize.height);
		}
		return textureID;
	}

	public boolean hasColor() {
		return hasColor;
	}

	public float colorRed() {
		return colorRed;
	}

	public float colorGreen() {
		return colorGreen;
	}

	public float colorBlue() {
		return colorBlue;
	}

	IArcadeGame createGameInstance(IArcadeMachine arcadeMachine) {
		try {
			return constructor.newInstance(arcadeMachine);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
