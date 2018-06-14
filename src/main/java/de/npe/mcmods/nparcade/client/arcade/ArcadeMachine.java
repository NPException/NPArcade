package de.npe.mcmods.nparcade.client.arcade;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import java.awt.image.BufferedImage;
import java.net.URL;

import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.api.nparcade.util.Controls;
import de.npe.api.nparcade.util.IArcadeSound;
import de.npe.api.nparcade.util.Size;
import de.npe.mcmods.nparcade.client.ClientProxy;
import de.npe.mcmods.nparcade.client.arcade.KeyStatesMap.KeyState;
import de.npe.mcmods.nparcade.client.arcade.sound.ArcadeSoundManager;
import de.npe.mcmods.nparcade.client.render.Helper;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeCabinet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class ArcadeMachine implements IArcadeMachine {

	private final Size suggestedScreenSize;
	private final ArcadeSoundManager soundManager;
	private final TileArcadeCabinet tile;

	private String gameID;
	private IArcadeGame game;
	private int textureID = -1;
	private int[] screenData;
	private BufferedImage image;

	public float stickOffsetX, stickOffsetY;
	public float buttonRedOffset, buttonGreenOffset, buttonBlueOffset, buttonYellowOffset;

	private final KeyStatesMap keyStates = new KeyStatesMap();

	public ArcadeMachine(int suggestedScreenWidth, int suggestedScreenHeight, TileArcadeCabinet tile) {
		suggestedScreenSize = new Size(suggestedScreenWidth, suggestedScreenHeight);
		soundManager = new ArcadeSoundManager();
		this.tile = tile;
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

	@Override
	public boolean isKeyPressed(int key) {
		return keyStates.get(mapKeyCode(key)) == KeyState.PRESS;
	}

	@Override
	public boolean isKeyDown(int key) {
		KeyState state = keyStates.get(mapKeyCode(key));
		return state == KeyState.PRESS
				|| state == KeyState.HOLD;
	}

	@Override
	public boolean isKeyReleased(int key) {
		return keyStates.get(mapKeyCode(key)) == KeyState.RELEASE;
	}

	@Override
	public boolean isKeyUp(int key) {
		KeyState state = keyStates.get(mapKeyCode(key));
		return state == KeyState.RELEASE
				|| state == null;
	}

	@Override
	public boolean hasKeyboard() {
		return true;
	}

	@Override
	public IArcadeSound registerSound(String soundFilePath, boolean streaming) {
		URL soundURL = ArcadeGameRegistry.gameForID(gameID).gameClass().getResource(soundFilePath);
		return soundManager.createPositionalSound(soundFilePath, soundURL, streaming, tile.xCoord + 0.5F, tile.yCoord + 0.5F, tile.zCoord + 0.5F);
	}

	////////////////////
	// Keyboard stuff //
	////////////////////

	public void activate() {
		KeyboardThief.activate(keyStates);
	}

	public void release() {
		KeyboardThief.release(keyStates);
	}

	/**
	 * Maps a keycode from the arcade basekey to the keyboard keycode
	 * if necessary
	 */
	private static int mapKeyCode(int keyCode) {
		if (keyCode > 0x0100 && keyCode < 0x0109) {
			switch (keyCode) {
				case Controls.ARCADE_KEY_UP:
					return Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode();
				case Controls.ARCADE_KEY_DOWN:
					return Minecraft.getMinecraft().gameSettings.keyBindBack.getKeyCode();
				case Controls.ARCADE_KEY_LEFT:
					return Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode();
				case Controls.ARCADE_KEY_RIGHT:
					return Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode();
				case Controls.ARCADE_KEY_RED:
					return ClientProxy.keyBindingArcadeRed.getKeyCode();
				case Controls.ARCADE_KEY_GREEN:
					return ClientProxy.keyBindingArcadeGreen.getKeyCode();
				case Controls.ARCADE_KEY_BLUE:
					return ClientProxy.keyBindingArcadeBlue.getKeyCode();
				case Controls.ARCADE_KEY_YELLOW:
					return ClientProxy.keyBindingArcadeYellow.getKeyCode();
			}
		}
		return keyCode;
	}

	private long lastEscapePress;

	private void checkReleaseHotkeys() {
		if (isKeyPressed(Controls.KEY_ESCAPE)) {
			long now = System.currentTimeMillis();
			// release control on escape double tap
			if (lastEscapePress + 500 > now) {
				release();
			} else {
				lastEscapePress = now;
			}
		}
	}

	/////////////////////////////////////////////////
	// external methods to be used by TileEntities //
	/////////////////////////////////////////////////

	public void update() {
		checkReleaseHotkeys();
		updateControlRenderOffsets();
		if (game != null) {
			game.update(this);
		}
	}

	private void updateControlRenderOffsets() {
		stickOffsetX = stickOffsetY = buttonRedOffset = buttonGreenOffset = buttonBlueOffset = buttonYellowOffset = 0;
		if (keyStates.isEmpty()) {
			return;
		}
		stickOffsetX -= offset(Controls.ARCADE_KEY_LEFT);
		stickOffsetX += offset(Controls.ARCADE_KEY_RIGHT);
		stickOffsetY -= offset(Controls.ARCADE_KEY_UP);
		stickOffsetY += offset(Controls.ARCADE_KEY_DOWN);

		buttonRedOffset = offset(Controls.ARCADE_KEY_RED);
		buttonGreenOffset = offset(Controls.ARCADE_KEY_GREEN);
		buttonBlueOffset = offset(Controls.ARCADE_KEY_BLUE);
		buttonYellowOffset = offset(Controls.ARCADE_KEY_YELLOW);
	}

	private float offset(int key) {
		// manual checks to avoid multiple method calls to isKeyDown etc.
		KeyState state = keyStates.get(mapKeyCode(key));
		if (state == null) {
			return 0F;
		}
		switch (state) {
			case PRESS:
				return 0.75F;
			case HOLD:
				return 1F;
			case RELEASE:
				return 0.25F;
			default:
				// should not happen
				throw new IllegalStateException("Uncheck KeyState: " + state);
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
		this.gameID = gameID;
		ArcadeGameWrapper wrapper = ArcadeGameRegistry.gameForID(this.gameID);

		game = wrapper.createGameInstance(this);
	}

	public void unload() {
		if (game != null) {
			game = null;
			gameID = null;
			gameInstance = null;
			gameWrapper = null;
		}
		deleteTexture();

		soundManager.unloadAllSounds();
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

		glBindTexture(GL_TEXTURE_2D, 0);
	}

	/**
	 * Deletes the texture currently allocated for this arcade machine's screen
	 */
	private void deleteTexture() {
		if (textureID != -1) {
			TextureUtil.deleteTexture(textureID);
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
	 * @param tick
	 * 		the partial tick since the last update
	 */
	private void prepareRender(float tick) {
		if (needsScreenRefresh()) {
			Size size = game.screenSize();
			int width = size.width;
			int height = size.height;

			// allocate new texture if game output size changed or screen is not yet initialized
			if (textureID == -1 ||
					screenData == null ||
					image == null ||
					image.getWidth() != width || image.getHeight() != height) {

				deleteTexture();
				screenData = new int[width * height];
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
