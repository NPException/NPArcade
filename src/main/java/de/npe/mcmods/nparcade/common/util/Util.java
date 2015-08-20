package de.npe.mcmods.nparcade.common.util;

import de.npe.mcmods.nparcade.common.lib.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by NPException (2015)
 */
public class Util {

	public static final Random rand = new Random();

	/**
	 * Tries to get a tag compound "{mod_id}" from the given compound.
	 * If the given compound does not contain the tag and <i>createIfMissing</i> is true,
	 * it will be added to it.
	 */
	public static NBTTagCompound getModNBTTag(NBTTagCompound rootTag, boolean createIfMissing) {
		if (createIfMissing && !rootTag.hasKey(Reference.MOD_ID)) {
			rootTag.setTag(Reference.MOD_ID, new NBTTagCompound());
		}
		return rootTag.getCompoundTag(Reference.MOD_ID);
	}

	public static ForgeDirection getViewDirectionOfEntity(EntityLivingBase entityLivingBase) {
		int mcFacing = MathHelper.floor_double(entityLivingBase.rotationYaw * 4.0D / 360.0D + 2.5D) & 3;
		switch (mcFacing) {
			case 0:
				return ForgeDirection.NORTH;
			case 1:
				return ForgeDirection.EAST;
			case 2:
				return ForgeDirection.SOUTH;
			case 3:
				return ForgeDirection.WEST;
			default:
				return ForgeDirection.UNKNOWN;
		}
	}
}
