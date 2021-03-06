package de.npe.mcmods.nparcade.common.util;

import java.io.InputStream;

import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.client.arcade.ArcadeGameRegistry;
import de.npe.mcmods.nparcade.common.lib.Reference;
import de.npe.mcmods.nparcade.common.lib.Strings;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by NPException (2015)
 */
public final class Util {

	public static boolean isClientSide() {
		return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
	}

	public static InputStream getResourceStreamAbsolutePath(Class clazz, String resourcePath) {
		try {
			InputStream in = clazz.getResourceAsStream(resourcePath);
			if (in == null) {
				NPArcade.log.warn("Resource not found: " + resourcePath);
			}
			return in;
		} catch (Exception ex) {
			NPArcade.log.error("Could not load resource: " + resourcePath, ex);
			return null;
		}
	}

	public static InputStream getResourceStream(String resource) {
		return getResourceStreamAbsolutePath(Util.class, "/assets/" + Reference.MOD_ID + "/" + resource);
	}

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

	/**
	 * Spawns an itemStack in the world.
	 */
	public static void spawnItemStack(ItemStack itemStack, World world, double x, double y, double z, int delayBeforePickup) {
		EntityItem entityItem = new EntityItem(world, x, y, z, itemStack);
		entityItem.delayBeforeCanPickup = delayBeforePickup;

		world.spawnEntityInWorld(entityItem);
	}

	public static boolean isEmptyGame(String gameId) {
		return Strings.EMPTY_GAME_ID.equals(gameId);
	}

	public static boolean isUnknownGame(String gameId) {
		return !Strings.EMPTY_GAME_ID.equals(gameId) && !ArcadeGameRegistry.gameIDs().contains(gameId);
	}
}
