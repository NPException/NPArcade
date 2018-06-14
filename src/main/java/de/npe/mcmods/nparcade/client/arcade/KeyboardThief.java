package de.npe.mcmods.nparcade.client.arcade;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.client.arcade.KeyStatesMap.KeyState;

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
 * Created by NPException (2018)
 *
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

	private KeyboardThief() {
	}

	// holds the currently updated key state map
	private static WeakReference<KeyStatesMap> activeKeys;

	// holds previously active key states maps, which may need to be properly released first
	private static final List<KeyStatesMap> oldKeysList = new ArrayList<>(2);

	private static KeyStatesMap current() {
		return activeKeys != null
				? activeKeys.get()
				: null;
	}

	static void activate(KeyStatesMap keys) {
		NPArcade.log.info("activated KeyboardThief control");
		KeyBinding.unPressAllKeys();

		KeyStatesMap current = current();
		if (current != null && current != keys && !oldKeysList.contains(current)) {
			oldKeysList.add(current);
		}
		oldKeysList.remove(keys); // in case they were in the old keys before
		activeKeys = new WeakReference<>(keys);
		MC.gameSettings.hideGUI = true;
	}

	static void release(KeyStatesMap keys) {
		NPArcade.log.info("released KeyboardThief control");
		// safety check
		if (current() == keys) {
			activeKeys = null;
			MC.gameSettings.hideGUI = false;
		}
		if (oldKeysList.contains(keys)) {
			// should not happen, but you never know...
			NPArcade.log.error("release() was called with already released keys! If you see this, please tell the mod developer.", new Throwable());
		} else {
			oldKeysList.add(keys);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void tick(ClientTickEvent event) {
		if (event.phase != Phase.START || MC.isGamePaused()) {
			return;
		}
		if (!oldKeysList.isEmpty()) {
			Iterator<KeyStatesMap> setIt = oldKeysList.iterator();
			while (setIt.hasNext()) {
				KeyStatesMap oldKeys = setIt.next();
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

		while (Mouse.next()) {
			// eliminate mouse clicks as well
		}
	}
}
