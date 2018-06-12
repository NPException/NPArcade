package de.npe.mcmods.nparcade.client.arcade;

import java.util.HashMap;

import de.npe.mcmods.nparcade.client.arcade.KeyStatesMap.KeyState;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
class KeyStatesMap extends HashMap<Integer, KeyState> {
	@SideOnly(Side.CLIENT)
	enum KeyState {
		// key has been pressed in this tick
		PRESS,
		// key is still down
		HOLD,
		// key has been released in this tick
		RELEASE
	}

	KeyStatesMap() {
		super(16);
	}

	@Override
	public boolean equals(Object o) {
		// I realy don't need more than identity equality atm
		return this == o;
	}

	@Override
	public int hashCode() {
		// Enforce identity hashcode, to stop the hashcode from depending on the map's contents.
		return System.identityHashCode(this);
	}
}
