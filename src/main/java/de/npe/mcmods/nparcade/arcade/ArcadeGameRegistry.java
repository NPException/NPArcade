package de.npe.mcmods.nparcade.arcade;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import de.npe.api.nparcade.IArcadeGame;
import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.arcade.api.IItemGameCartridge;
import de.npe.mcmods.nparcade.arcade.games.SampleGame;
import net.minecraft.item.Item;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to register arcade games, so that there is an easy way to access
 * them from anywhere in the code where needed.
 * <p/>
 * Created by NPException (2015)
 */
public class ArcadeGameRegistry {

	private ArcadeGameRegistry() {
	}

	private static boolean isInitialized;
	private static final Map<String, ArcadeGameWrapper> games = new HashMap<>(512);
	private static final Set<String> gamesKeySetView = Collections.unmodifiableSet(games.keySet());

	/**
	 * Initializes the ArcadeGameRegistry
	 */
	public static synchronized void init() {
		if (isInitialized)
			return;
		isInitialized = true;

		// TODO: load games from jars

		try {
			register(SampleGame.class, SampleGame.ID, SampleGame.NAME, null, "4251AF");
		} catch (Exception ex) {
			NPArcade.log.warn("Could not register arcade game", ex);
		}
	}

	/**
	 * Registers a new game.
	 *
	 * @param gameClass       the Class of the game. (Must have a public no-args constructor)
	 * @param id              the ID of the game. (Must NOT be null)
	 * @param name            the human readable gameName of the game. (Must NOT be null)
	 * @param customCartridge a custom cartridge that the game should use. If null, the default cartridge will be used.
	 *                        If set, this must be an instance of {@link Item}.
	 * @throws IllegalArgumentException if one of the parameters does not meet the requirements,
	 *                                  or a game with the same ID was already registered.
	 */
	public static void register(Class<? extends IArcadeGame> gameClass, String id, String name, IItemGameCartridge customCartridge) throws IllegalArgumentException {
		register(gameClass, id, name, null, null, customCartridge);
	}

	/**
	 * Registers a new game.
	 *
	 * @param gameClass   the Class of the game. (Must have a public no-args constructor)
	 * @param id          the ID of the game. (Must NOT be null)
	 * @param name        the human readable gameName of the game. (Must NOT be null)
	 * @param label       a label for the game cartridge. (Can be null)
	 * @param colorString the custom color of the cartridge as a 3 byte hexadecimal String. (Can be null)
	 * @throws IllegalArgumentException if one of the parameters does not meet the requirements,
	 *                                  or a game with the same ID was already registered.
	 */
	public static void register(Class<? extends IArcadeGame> gameClass, String id, String name, BufferedImage label, String colorString) throws IllegalArgumentException {
		register(gameClass, id, name, label, colorString, null);
	}

	private static synchronized void register(Class<? extends IArcadeGame> gameClass, String id, String name, BufferedImage label, String colorString, IItemGameCartridge customCartridge) throws IllegalArgumentException {
		String gameToString = " Game -> ID:" + id + ", Name:" + name + ", Class:" + gameClass.getCanonicalName() + ", Label:" + (label != null) + ", Color:" + colorString + ", Custom_Cartridge:" + customCartridge;
		if (id == null) {
			throw new IllegalArgumentException("ID must not be null!" + gameToString);
		}
		if (name == null) {
			throw new IllegalArgumentException("Name must not be null!" + gameToString);
		}

		if (customCartridge != null && !(customCartridge instanceof Item)) {
			throw new IllegalArgumentException("Custom cartridge must be an instance of Item!" + gameToString);
		}

		int color = -1;
		if (colorString != null) {
			try {
				color = Integer.parseInt(colorString, 16);
			} catch (Exception ex) {
				throw new IllegalArgumentException("Color string given is not a valid hex value!" + gameToString, ex);
			}
		}

		try {
			gameClass.getConstructor();
		} catch (Exception ex) {
			throw new IllegalArgumentException("Could not grab public no-args constructor!" + gameToString, ex);
		}

		if (games.containsKey(id)) {
			throw new IllegalArgumentException("Game with ID already registered: " + id + gameToString);
		}

		boolean isClient = (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT);
		ArcadeGameWrapper wrapper = isClient ? new ArcadeGameWrapper(id, name, label, color, gameClass, customCartridge) : new ArcadeGameWrapper(id, name, null, -1, null, customCartridge);
		games.put(id, wrapper);
	}

	/**
	 * Returns the ArcadeGameWrapper object for the game with the given ID, or an empty
	 * wrapper (with gameID being <b>null</b>) if no game with the given ID was found.<br>
	 * <br>
	 * This method will therefor never return null.
	 */
	public static ArcadeGameWrapper gameForID(String id) {
		if (DummyGames.EMPTY_GAME_WRAPPER.gameID().equals(id)) {
			return DummyGames.EMPTY_GAME_WRAPPER;
		}
		ArcadeGameWrapper wrapper = games.get(id);
		return wrapper != null ? wrapper : DummyGames.UNKNOWN_GAME_WRAPPER;
	}

	/**
	 * Returns an unmodifiable view to all available game IDs.<br>
	 * The returned set is backed by the game IDs that are stored in the ArcadeGameRegistry.
	 */
	public static Set<String> gameIDs() {
		return gamesKeySetView;
	}
}
