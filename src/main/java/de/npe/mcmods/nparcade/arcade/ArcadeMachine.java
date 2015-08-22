package de.npe.mcmods.nparcade.arcade;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.api.nparcade.util.Size;
import de.npe.mcmods.nparcade.client.render.Helper;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class ArcadeMachine implements IArcadeMachine {
	private Size suggestedScreenSize;

	private String gameID;
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

	public boolean hasGame() {
		return game != null;
	}

	public void update() {
		if (game != null) {
			game.update();
		}
	}

	/**
	 * Loads a specific game into the arcade machine.
	 * If another game is already loaded, it will be unloaded properly
	 * before the new game is loaded.
	 */
	public void load(String gameID, boolean forceReload) {
		if (gameID == null) {
			unload();
			// TODO: load error screen
			return;
		}

		// dont reload the game  if it is already running
		if (!forceReload && game != null && this.gameID != null && this.gameID.equals(gameID)) {
			return;
		}

		// unload previous game
		unload();
		// load new game
		ArcadeGameWrapper wrapper = ArcadeGameRegistry.gameForID(gameID);
		game = wrapper != null ? wrapper.createGameInstance() : null;

		// TODO: load placeholder screen or error screen if ArcadeGameWrapper is null
		if (game != null) {
			game.load(this);
		}
	}

	public void unload() {
		if (game != null) {
			game.unload();
			game = null;
			gameID = null;
		}
		deleteTexture();
	}

	/////////////////////
	// rendering stuff //
	/////////////////////

	/**
	 * Renders the arcade machines screen via OpenGL using it's suggested screen size.
	 */
	public void doRenderScreen(float tick) {
		if (game == null) {
			return;
		}

		prepareRender(tick);

		glBindTexture(GL_TEXTURE_2D, textureID);

		Helper.renderRectInBounds(suggestedScreenSize.width, suggestedScreenSize.height,
				image.getWidth(), image.getHeight(), 0, 0, 1, 1, Helper.Alignment.M);
	}

	/**
	 * Deletes the texture currently allocated for this arcade machine's screen
	 */
	public void deleteTexture() {
		if (textureID != -1) {
			GL11.glDeleteTextures(textureID);
		}
		screenData = null;
		image = null;
	}

	/**
	 * Checks if the game is present and wants to update it's graphics.
	 */
	private boolean needsScreenRefresh() {
		return textureID == -1 || game.needsDraw();
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

				deleteTexture();
				screenData = new int[width * height];
				textureID = GL11.glGenTextures();
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
