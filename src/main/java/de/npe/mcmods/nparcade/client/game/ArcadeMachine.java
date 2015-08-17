package de.npe.mcmods.nparcade.client.game;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.api.nparcade.SampleGame;
import de.npe.api.nparcade.util.Size;
import net.minecraft.client.renderer.texture.TextureUtil;

import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class ArcadeMachine implements IArcadeMachine {
	private Size suggestedScreenSize;

	private IArcadeGame game;
	private int textureID = -1;
	private int[] screenData;
	private BufferedImage image;

	public ArcadeMachine(int suggestedScreenWidth, int suggestedScreenHeight) {
		suggestedScreenSize = new Size(suggestedScreenWidth, suggestedScreenHeight);
	}

	////////////////////////////
	// IArcadeMachine methods //
	////////////////////////////


	@Override
	public int ticksPerSecond() {
		return 20;
	}

	@Override
	public Size suggestedScreenSize() {
		return suggestedScreenSize;
	}

	/////////////////////////////////////////////////
	// external methods to be used by TileEntities //
	/////////////////////////////////////////////////

	public void update() {
		if (game != null) {
			game.update();
		}
	}

	/**
	 * loads the default game selection program into the arcade machine.
	 */
	public void load() {
		load(new SampleGame());
	}

	/**
	 * Loads a specific game into the arcade machine.
	 */
	public void load(IArcadeGame game) {
		if (game == null) {
			throw new InvalidParameterException("game must not be null!");
		}
		// unload previous game
		unload();
		// load new game
		this.game = game;
		this.game.load(this);
	}

	public void unload() {
		if (game != null) {
			game.unload();
			game = null;
		}
	}

	/////////////////////
	// rendering stuff //
	/////////////////////

	/**
	 * Renders the arcade machines screen via OpenGL using it's suggested screen size.
	 */
	public void doRender(float tick) {
		prepareRender(tick);

		if (textureID == -1) {
			return;
		}

		// texture variables
		float tx = 0F;
		float ty = 0F;

		float tw = suggestedScreenSize.width;
		float th = suggestedScreenSize.height;
		final float tRatio = tw / th;

		// screen variables
		final float sw = (float) image.getWidth();
		final float sh = (float) image.getHeight();
		final float sRatio = sw / sh;

		// these checks center the game's output on screen
		// if the game's aspect ratio does not match the arcade
		// machine's aspect ratio.
		if (sRatio > tRatio) {
			ty = th * 0.5F - (th * 0.5F) / sRatio * tRatio;
			th = th / sRatio * tRatio;
		} else if (sRatio < tRatio) {
			tx = tw * 0.5F - (tw * 0.5F) / tRatio * sRatio;
			tw = tw / tRatio * sRatio;
		}

		glBindTexture(GL_TEXTURE_2D, textureID);

		glBegin(GL_TRIANGLES);

		glTexCoord2f(1, 0); // top right
		glVertex2f(tx + tw, ty);
		glTexCoord2f(0, 0); // top left
		glVertex2f(tx, ty);
		glTexCoord2f(0, 1); // bottom left
		glVertex2f(tx, ty + th);

		glTexCoord2f(0, 1); // bottom left
		glVertex2f(tx, ty + th);
		glTexCoord2f(1, 1); // bottom right
		glVertex2f(tx + tw, ty + th);
		glTexCoord2f(1, 0); // top right
		glVertex2f(tx + tw, ty);

		glEnd();
	}

	/**
	 * Deletes the texture currently allocated for this arcade machine's screen
	 */
	public void deleteTexture() {
		if (textureID != -1) {
			TextureUtil.deleteTexture(textureID);
		}
	}

	/**
	 * Checks if the game is present and wants to update it's graphics.
	 */
	private boolean needsScreenRefresh() {
		return game != null && (textureID == -1 || game.needsDraw());
	}

	/**
	 * Checks if a graphics update is necessary first.<br>
	 * If so, it checks if the output size of the game has changed
	 * and allocates new texture memory if that is the case.<br>
	 * The games output is then drawn to the internal BufferedImage,
	 * and it's pixel data uploaded to the graphics card.
	 *
	 * @param tick the partial tick since the last update
	 */
	private void prepareRender(float tick) {
		if (needsScreenRefresh()) {
			Size size = game.screenSize();
			int width = size.width;
			int height = size.height;

			// allocate new texture if game output size changed or scren is not yet initialized
			if (textureID == -1 ||
					screenData == null ||
					image == null ||
					image.getWidth() != width || image.getHeight() != height) {

				screenData = new int[width * height];
				deleteTexture();
				textureID = TextureUtil.glGenTextures();
				TextureUtil.allocateTexture(textureID, width, height);

				image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			}

			// draw game
			game.draw(image, tick);
			// get pixel data
			image.getRGB(0, 0, width, height, screenData, 0, width);
			// upload pixels to texture
			TextureUtil.uploadTexture(textureID, screenData, width, height);
		}
	}
}
