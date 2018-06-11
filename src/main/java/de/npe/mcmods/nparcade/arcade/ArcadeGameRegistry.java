package de.npe.mcmods.nparcade.arcade;

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
import de.npe.mcmods.nparcade.arcade.api.IGameCartridge;
import de.npe.mcmods.nparcade.common.ModItems;
import de.npe.mcmods.nparcade.common.lib.Strings;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * This class is used to register arcade games, so that there is an easy way to access
 * them from anywhere in the code where needed.
 * <p/>
 * Created by NPException (2015)
 */
public final class ArcadeGameRegistry {

	private ArcadeGameRegistry() {
	}

	private static boolean isInitialized;

	private static final Map<String, ArcadeGameWrapper> games = new HashMap<>(512);
	private static final Set<String> gamesKeySetView = Collections.unmodifiableSet(games.keySet());

	private static final Map<Item, IGameCartridge> customCartridges = new HashMap<>(64);

	/**
	 * Initializes the ArcadeGameRegistry
	 */
	public static synchronized void init() {
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
	 * @param customCartridge
	 * 		a custom cartridge that the game should use. If null, the default cartridge will be used.
	 * 		If set, this must be an instance of {@link Item}.
	 * @throws IllegalArgumentException
	 * 		if one of the parameters does not meet the requirements,
	 * 		or a game with the same ID was already registered.
	 */
	public static void register(Class<? extends IArcadeGame> gameClass, String id, String title, IGameCartridge customCartridge) throws IllegalArgumentException {
		register(gameClass, id, title, null, null, null, customCartridge);
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
		register(gameClass, id, title, description, label, colorString, null);
	}

	private static synchronized void register(Class<? extends IArcadeGame> gameClass, String id, String title, String description, BufferedImage label, String colorString, IGameCartridge customCartridge) throws IllegalArgumentException {
		String gameToString = " Game -> ID:" + id
				+ ", Title:" + title
				+ ", Class:" + gameClass.getCanonicalName()
				+ ", Label:" + (label != null)
				+ ", Color:" + colorString
				+ ", Custom_Cartridge:" + customCartridge;

		if (id == null) {
			throw new IllegalArgumentException("ID must not be null!" + gameToString);
		}
		if (title == null) {
			throw new IllegalArgumentException("Title must not be null!" + gameToString);
		}

		IGameCartridge wrapperCartridge = null;
		if (customCartridge != null) {
			Item cartridgeItem = customCartridge.getCartridgeItem();
			if (cartridgeItem == null) {
				throw new IllegalArgumentException("Custom cartridge must not return null for getCartridgeItem()!" + gameToString);
			}
			wrapperCartridge = new WrapperCartridge(cartridgeItem, customCartridge);
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

		boolean isClient = (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT);
		ArcadeGameWrapper wrapper = isClient
				? new ArcadeGameWrapper(id, title, description, label, color, gameClass, wrapperCartridge)
				: new ArcadeGameWrapper(id, title, null, null, -1, null, wrapperCartridge);
		games.put(id, wrapper);
		if (wrapperCartridge != null) {
			customCartridges.put(wrapperCartridge.getCartridgeItem(), wrapperCartridge);
		}
		NPArcade.log.info("Registered game with ID: " + id + ", title: " + title);
	}

	/**
	 * Returns the ArcadeGameWrapper object for the game with the given ID, or an empty
	 * wrapper (with gameID being <b>null</b>) if no game with the given ID was found.<br>
	 * <br>
	 * This method will therefor never return null.
	 */
	public static ArcadeGameWrapper gameForID(String id) {
		if (isEmptyGame(id)) {
			return DummyGames.EMPTY_GAME_WRAPPER;
		}
		ArcadeGameWrapper wrapper = games.get(id);
		return wrapper != null ? wrapper : DummyGames.UNKNOWN_GAME_WRAPPER;
	}

	public static boolean isEmptyGame(String gameId) {
		return DummyGames.EMPTY_GAME_WRAPPER.gameID().equals(gameId);
	}

	public static boolean isUnknownGame(String gameId) {
		return DummyGames.UNKNOWN_GAME_WRAPPER == gameForID(gameId);
	}

	/**
	 * Returns an unmodifiable view to all available game IDs.<br>
	 * The returned set is backed by the game IDs that are stored in the ArcadeGameRegistry.
	 */
	public static Set<String> gameIDs() {
		return gamesKeySetView;
	}

	/**
	 * Returns the registered {@link IGameCartridge} for the given Item.
	 * May return null if no cartridge was registerd for the given item.
	 */
	public static IGameCartridge cartridgeForItem(Item item) {
		return item == ModItems.cartridge ? ModItems.cartridge : customCartridges.get(item);
	}

	/**
	 * A wrapper class to prevent calling {@link IGameCartridge#getCartridgeItem()} more than once.
	 */
	private static class WrapperCartridge implements IGameCartridge {
		private final Item cartridgeItem;
		private final IGameCartridge delegate;

		WrapperCartridge(Item cartridgeItem, IGameCartridge delegate) {
			this.cartridgeItem = cartridgeItem;
			this.delegate = delegate;
		}

		@Override
		public Item getCartridgeItem() {
			return cartridgeItem;
		}

		@Override
		public String getGameID(ItemStack stack) {
			return delegate.getGameID(stack);
		}

		@Override
		public void setGameID(ItemStack stack, String gameID) {
			delegate.setGameID(stack, gameID);
		}
	}
}
