package de.npe.mcmods.nparcade.arcade;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import de.npe.api.nparcade.IArcadeGame;
import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.arcade.api.IItemGameCartridge;
import de.npe.mcmods.nparcade.common.lib.Strings;
import net.minecraft.item.Item;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.*;

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

		// create arcade games subfolder if not already existing
		File arcadeGameFolder = new File("mods", Strings.PATH_FOLDER_NPARCADE_GAMES);
		if (!arcadeGameFolder.exists()) {
			arcadeGameFolder.mkdirs();
		}

		try {
			loadGamesFromJarFiles();
		} catch (Exception ex) {
			NPArcade.log.error("Caught Exception while loading game jar files", ex);
		}
	}

	private static void loadGamesFromJarFiles() {
		Deque<File> candidates = new LinkedList<>();
		Deque<File> directories = new LinkedList<>();
		directories.add(new File("mods"));

		// first, scan directories and subdirectories for candidate files
		while (!directories.isEmpty()) {
			File directory = directories.removeFirst();
			File[] content = directory.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.getName().endsWith(".zip") || file.getName().endsWith(".jar") || file.isDirectory();
				}
			});
			for (File f : content) {
				if (f.isDirectory()) {
					directories.addLast(f);
				} else {
					candidates.addLast(f);
				}
			}
		}

		for(Iterator<File> itr = candidates.iterator(); itr.hasNext();) {
			File candidate = itr.next();
			try {
				// TODO check for game.info in jar
			} catch (Exception ex) {
				NPArcade.log.warn("Something went wrong while analyzing a game candidate file", ex);
				itr.remove();
			}
		}

		URL[] urls = new URL[candidates.size()];

		// TODO: load games from jars
	}

	/**
	 * Registers a new game.
	 *
	 * @param gameClass       the Class of the game. (Must have a public no-args constructor)
	 * @param id              the ID of the game. (Must NOT be null)
	 * @param title           the human readable title for the game. (Must NOT be null)
	 * @param customCartridge a custom cartridge that the game should use. If null, the default cartridge will be used.
	 *                        If set, this must be an instance of {@link Item}.
	 * @throws IllegalArgumentException if one of the parameters does not meet the requirements,
	 *                                  or a game with the same ID was already registered.
	 */
	public static void register(Class<? extends IArcadeGame> gameClass, String id, String title, IItemGameCartridge customCartridge) throws IllegalArgumentException {
		register(gameClass, id, title, null, null, customCartridge);
	}

	/**
	 * Registers a new game.
	 *
	 * @param gameClass   the Class of the game. (Must have a public no-args constructor)
	 * @param id          the ID of the game. (Must NOT be null)
	 * @param title       the human readable title for the game. (Must NOT be null)
	 * @param label       a label for the game cartridge. (Can be null)
	 * @param colorString the custom color of the cartridge as a 3 byte hexadecimal String. (Can be null)
	 * @throws IllegalArgumentException if one of the parameters does not meet the requirements,
	 *                                  or a game with the same ID was already registered.
	 */
	public static void register(Class<? extends IArcadeGame> gameClass, String id, String title, BufferedImage label, String colorString) throws IllegalArgumentException {
		register(gameClass, id, title, label, colorString, null);
	}

	private static synchronized void register(Class<? extends IArcadeGame> gameClass, String id, String title, BufferedImage label, String colorString, IItemGameCartridge customCartridge) throws IllegalArgumentException {
		String gameToString = " Game -> ID:" + id + ", Title:" + title + ", Class:" + gameClass.getCanonicalName() + ", Label:" + (label != null) + ", Color:" + colorString + ", Custom_Cartridge:" + customCartridge;
		if (id == null) {
			throw new IllegalArgumentException("ID must not be null!" + gameToString);
		}
		if (title == null) {
			throw new IllegalArgumentException("Title must not be null!" + gameToString);
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
		ArcadeGameWrapper wrapper = isClient ? new ArcadeGameWrapper(id, title, label, color, gameClass, customCartridge) : new ArcadeGameWrapper(id, title, null, -1, null, customCartridge);
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
