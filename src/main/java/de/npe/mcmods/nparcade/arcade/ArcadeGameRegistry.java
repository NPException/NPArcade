package de.npe.mcmods.nparcade.arcade;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.api.nparcade.IArcadeGame;
import de.npe.mcmods.nparcade.NPArcade;

import java.awt.*;
import java.lang.reflect.Constructor;
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
	private static final Map<String, GameInfo> games = new HashMap<>(512);
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
			register(SampleGame.class, SampleGame.ID, SampleGame.NAME, null);
		} catch (Exception ex) {
			NPArcade.log.warn("Could not register arcade game", ex);
		}
	}

	/**
	 * Registers a new game.
	 *
	 * @param gameClass the Class of the game. (Must have a public no-args constructor)
	 * @param id        the ID of the game. (Must NOT be null)
	 * @param name      the human readable name of the game. (Must NOT be null)
	 * @param icon      an icon for the game. (Can be null)
	 * @throws IllegalArgumentException if one of the parameters does not meet the requirements,
	 *                                  or a game with the same ID was already registered.
	 */
	public static synchronized void register(Class<? extends IArcadeGame> gameClass, String id, String name, Image icon) throws IllegalArgumentException {
		String gameToString = " Game -> ID:" + id + ", Name:" + name + ", Class:" + gameClass.getCanonicalName();
		if (id == null) {
			throw new IllegalArgumentException("ID must not be null!" + gameToString);
		}
		if (name == null) {
			throw new IllegalArgumentException("Name must not be null!" + gameToString);
		}

		Constructor<? extends IArcadeGame> constructor;
		try {
			constructor = gameClass.getConstructor();
		} catch (Exception ex) {
			throw new IllegalArgumentException("Could not grab public no-args constructor!" + gameToString, ex);
		}

		if (games.containsKey(id)) {
			throw new IllegalArgumentException("Game with ID already registered: " + id + gameToString);
		}

		boolean isClient = (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT);
		GameInfo info = isClient ? new GameInfo(id, name, icon, constructor) : new GameInfo(id, name, null, null);
		games.put(id, info);
	}

	/**
	 * Returns the GameInfo object for the game with the given ID
	 */
	public static GameInfo gameForID(String id) {
		return games.get(id);
	}

	/**
	 * Returns an unmodifiable view to all available game IDs.<br>
	 * The returned set is backed by the game IDs that are stored in the ArcadeGameRegistry.
	 */
	public static Set<String> gameIDs() {
		return gamesKeySetView;
	}

	/**
	 * This class holds information about the game, and (if on the client side)
	 * offers a method to create a new instance of that game.
	 */
	public static final class GameInfo {
		private final String id;
		private final String name;

		private final Image icon;
		private final Constructor<? extends IArcadeGame> constructor;

		private GameInfo(String id, String name, Image icon, Constructor<? extends IArcadeGame> constructor) {
			this.id = id;
			this.name = name;
			this.icon = icon;
			this.constructor = constructor;
		}

		public String id() {
			return id;
		}

		public String name() {
			return name;
		}

		@SideOnly(Side.CLIENT)
		public Image icon() {
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
}
