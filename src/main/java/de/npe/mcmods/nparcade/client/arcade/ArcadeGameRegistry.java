package de.npe.mcmods.nparcade.client.arcade;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.common.lib.Strings;
import de.npe.mcmods.nparcade.common.util.Util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This class is used to register arcade games, so that there is an easy way to access
 * them from anywhere in the code where needed.
 * <p/>
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public final class ArcadeGameRegistry {

	private ArcadeGameRegistry() {
	}

	private static boolean isInitialized;

	private static final Map<String, ArcadeGameWrapper> games = new HashMap<>(512);
	private static final Set<String> gamesKeySetView = Collections.unmodifiableSet(games.keySet());

	/**
	 * Initializes the ArcadeGameRegistry
	 */
	public static synchronized void init() {
		if (!Util.isClientSide()) {
			throw new IllegalStateException("ArcadeGameRegistry may only be used on the client side!");
		}

		if (isInitialized) {
			return;
		}
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

	private static void loadGamesFromJarFiles() throws MalformedURLException {
		List<File> candidates = new ArrayList<>();
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
			if (content != null) {
				for (File f : content) {
					if (f.isDirectory()) {
						directories.addLast(f);
					} else {
						candidates.add(f);
					}
				}
			}
		}

		Map<File, List<Map<String, String>>> gameFilesData = new HashMap<>(candidates.size());

		Gson gson = new Gson();
		Type gameInfoType = new TypeToken<List<Map<String, String>>>() {
		}.getType();

		for (File candidate : candidates) {
			try (ZipFile zip = new ZipFile(candidate)) {
				ZipEntry gameInfoFile = zip.getEntry(Strings.JSON_GAME_INFO_FILE);
				if (gameInfoFile == null) {
					continue;
				}

				List<Map<String, String>> gameInfos = gson.fromJson(new InputStreamReader(zip.getInputStream(gameInfoFile)), gameInfoType);
				gameFilesData.put(candidate, gameInfos);
			} catch (Exception ex) {
				NPArcade.log.warn("Something went wrong while analyzing a game candidate file", ex);
			}
		}

		URL[] urls = new URL[gameFilesData.size()];
		int i = 0;
		for (File file : gameFilesData.keySet()) {
			urls[i++] = file.toURI().toURL();
		}
		URLClassLoader urlCl = new URLClassLoader(urls, ArcadeGameRegistry.class.getClassLoader());

		for (List<Map<String, String>> gameInfoDatas : gameFilesData.values()) {
			for (Map<String, String> gameInfoData : gameInfoDatas) {
				try {
					GameInfo info = new GameInfo(gameInfoData, urlCl);
					register(info.gameClass, info.id, info.title, info.description, info.label, info.cartridgeColor);
				} catch (Exception ex) {
					NPArcade.log.warn("Could not load arcade game -> " + ex.getMessage());
				}
			}
		}
	}

	/**
	 * Registers a new game.
	 *
	 * @param gameClass
	 * 		the Class of the game. (Must have a public no-args constructor)
	 * @param id
	 * 		the ID of the game. (Must NOT be null)
	 * @param title
	 * 		the human readable title for the game. (Must NOT be null)
	 * @param label
	 * 		a label for the game cartridge. (Can be null)
	 * @param colorString
	 * 		the custom color of the cartridge as a 3 byte hexadecimal String. (Can be null)
	 * @throws IllegalArgumentException
	 * 		if one of the parameters does not meet the requirements,
	 * 		or a game with the same ID was already registered.
	 */
	public static void register(Class<? extends IArcadeGame> gameClass, String id, String title, String description, BufferedImage label, String colorString) throws IllegalArgumentException {
		String gameToString = " Game -> ID:" + id
				+ ", Title:" + title
				+ ", Class:" + gameClass.getCanonicalName()
				+ ", Label:" + (label != null)
				+ ", Color:" + colorString;

		if (id == null) {
			throw new IllegalArgumentException("ID must not be null!" + gameToString);
		}
		if (title == null) {
			throw new IllegalArgumentException("Title must not be null!" + gameToString);
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
			gameClass.getConstructor(IArcadeMachine.class);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Could not grab public constructor with IArcadeMachine parameter!" + gameToString, ex);
		}

		if (games.containsKey(id)) {
			throw new IllegalArgumentException("Game with ID already registered: " + id + gameToString);
		}

		ArcadeGameWrapper wrapper = new ArcadeGameWrapper(title, description, label, color, gameClass);
		games.put(id, wrapper);
		NPArcade.log.info("Registered game with ID: " + id + ", title: " + title);
	}

	/**
	 * Returns the ArcadeGameWrapper object for the game with the given ID, or an "unknown game"
	 * wrapper if no game with the given ID was found.<br>
	 * <br>
	 * This method will therefor never return null.
	 */
	public static ArcadeGameWrapper gameForID(String id) {
		if (Util.isEmptyGame(id)) {
			return DummyGames.EMPTY_GAME_WRAPPER;
		}
		ArcadeGameWrapper wrapper = games.get(id);
		return wrapper != null
				? wrapper
				: DummyGames.UNKNOWN_GAME_WRAPPER;
	}

	/**
	 * Returns an unmodifiable view to all available game IDs.<br>
	 * The returned set is backed by the game IDs that are stored in the ArcadeGameRegistry.
	 */
	public static Set<String> gameIDs() {
		return gamesKeySetView;
	}
}
