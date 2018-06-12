package de.npe.mcmods.nparcade.arcade;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import org.lwjgl.input.Keyboard;

import de.npe.mcmods.nparcade.NPArcade;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

/**
 * Main behaviour was grabbed from OmnisCore, many thanks to Jezza!
 * This class is used to hijack the keyboard input.
 */
@SideOnly(Side.CLIENT)
public final class KeyboardThief {
	private static final Minecraft MC = Minecraft.getMinecraft();
	private static final KeyboardThief INSTANCE = new KeyboardThief();
	private static boolean init = false;

	public static void init() {
		if (init) {
			return;
		}
		init = true;
		FMLCommonHandler.instance().bus().register(INSTANCE);
	}

	enum KeyState {
		// key has been pressed in this tick
		PRESS,
		// key is still down
		HOLD,
		// key has been released in this tick
		RELEASE
	}

	private KeyboardThief() {
	}

	// holds the currently updated key state map
	private static WeakReference<Map<Integer, KeyState>> activeKeys;

	// holds previously active key state maps, which may need to be properly released first
	private static final WeakHashMap<Map<Integer, KeyState>, Object> oldKeysSet = new WeakHashMap<>();

	private static Map<Integer, KeyState> current() {
		return activeKeys != null
				? activeKeys.get()
				: null;
	}

	static void activate(Map<Integer, KeyState> keys) {
		NPArcade.log.info("activated KeyboardThief control");
		KeyBinding.unPressAllKeys();

		Map<Integer, KeyState> current = current();
		if (current != null && current != keys) {
			oldKeysSet.put(current, null);
		}
		oldKeysSet.remove(keys); // in case they were in the old keys before
		activeKeys = new WeakReference<>(keys);
		MC.gameSettings.hideGUI = true;
	}

	static void release(Map<Integer, KeyState> keys) {
		NPArcade.log.info("released KeyboardThief control");
		// safety check
		if (current() == keys) {
			activeKeys = null;
			MC.gameSettings.hideGUI = false;
		}
		oldKeysSet.put(keys, null);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void tick(ClientTickEvent event) {
		if (event.phase != Phase.START) {
			return;
		}
		if (!oldKeysSet.isEmpty()) {
			Iterator<Map<Integer, KeyState>> setIt = oldKeysSet.keySet().iterator();
			while (setIt.hasNext()) {
				Map<Integer, KeyState> oldKeys = setIt.next();
				// two step deactivation process, to give the oportunity for keys to be properly released
				Iterator<Entry<Integer, KeyState>> it = oldKeys.entrySet().iterator();
				while (it.hasNext()) {
					Entry<Integer, KeyState> entry = it.next();
					if (entry.getValue() != KeyState.RELEASE) {
						// step 1, release key
						entry.setValue(KeyState.RELEASE);
					} else {
						// setp 2, remove key
						it.remove();
					}
				}
				if (oldKeys.isEmpty()) {
					setIt.remove();
				}
			}
		}

		// no processing in GUIs
		if (MC.currentScreen != null) {
			return;
		}

		// process last tick's key states if necessary
		Map<Integer, KeyState> keys = current();
		if (keys == null) {
			return;
		}

		Iterator<Entry<Integer, KeyState>> it = keys.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, KeyState> entry = it.next();
			switch (entry.getValue()) {
				case PRESS:
					entry.setValue(KeyState.HOLD);
					break;
				case RELEASE:
					it.remove();
					break;
			}
		}

		while (Keyboard.next()) {
			int key = Keyboard.getEventKey();
			KeyState state = Keyboard.getEventKeyState()
					? KeyState.PRESS
					: KeyState.RELEASE;
			keys.put(key, state);
		}
	}
}
