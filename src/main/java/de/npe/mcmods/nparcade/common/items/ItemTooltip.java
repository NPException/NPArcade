package de.npe.mcmods.nparcade.common.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.npe.mcmods.nparcade.common.util.ClientUtil;
import de.npe.mcmods.nparcade.common.util.Localize;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Credit goes to Jezza. I ripped this shamelessly out of OmnisCore.
 */
@SideOnly(Side.CLIENT)
class ItemTooltip {

	private final ArrayList<String> infoList = new ArrayList<>();
	private final ArrayList<String> shiftList = new ArrayList<>();

	public void populateList(List<String> list) {
		if (!shiftList.isEmpty() && ClientUtil.hasPressedShift()) {
			list.addAll(shiftList);
		} else {
			list.addAll(infoList);
		}
	}

	public void addDefaultShiftInfo() {
		infoList.add(Localize.translate("info.tooltip.default.shift"));
	}

	public void addToInfoList(String string) {
		infoList.add(string);
	}

	public void addAllToInfoList(String... strings) {
		infoList.addAll(Lists.newArrayList(strings));
	}

	public void addAllToInfoList(Collection<String> strings) {
		infoList.addAll(strings);
	}

	public void addToShiftList(String string) {
		shiftList.add(string);
	}

	public void addAllToShiftList(String... strings) {
		shiftList.addAll(Lists.newArrayList(strings));
	}

	public void addAllToShiftList(Collection<String> strings) {
		shiftList.addAll(strings);
	}
}