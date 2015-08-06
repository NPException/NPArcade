package de.npe.mcmods.nparcade.common.util;

import de.npe.mcmods.nparcade.common.lib.Reference;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

/**
 * Created by NPException (2015)
 */
public class Util {

	public static final Random rand = new Random();

	/**
	 * Tries to get a tag compound "{mod_id}" from the given compound.
	 * If the given compound does not contain the tag, it will be added to it.
	 */
	public static NBTTagCompound getModNBTTag(NBTTagCompound rootTag, boolean createIfMissing) {
		if (createIfMissing && !rootTag.hasKey(Reference.MOD_ID)) {
			rootTag.setTag(Reference.MOD_ID, new NBTTagCompound());
		}
		return rootTag.getCompoundTag(Reference.MOD_ID);
	}
}
