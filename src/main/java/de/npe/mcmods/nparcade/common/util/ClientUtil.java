package de.npe.mcmods.nparcade.common.util;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

/**
 * Credit goes to Jezza. I ripped this shamelessly out of OmnisCore.
 */
@SideOnly(Side.CLIENT)
public final class ClientUtil {

	public static boolean hasPressedShift() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}

	public static boolean hasPressedCtrl() {
		return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
	}

	public static boolean hasPressedAlt() {
		return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
	}

	public static boolean isPlayer(String name) {
		return Minecraft.getMinecraft().thePlayer.getCommandSenderName().endsWith(name);
	}

}
