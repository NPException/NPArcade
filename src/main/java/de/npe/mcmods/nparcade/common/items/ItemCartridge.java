package de.npe.mcmods.nparcade.common.items;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.client.arcade.ArcadeGameRegistry;
import de.npe.mcmods.nparcade.client.arcade.ArcadeGameWrapper;
import de.npe.mcmods.nparcade.common.lib.Strings;
import de.npe.mcmods.nparcade.common.util.Localize;
import de.npe.mcmods.nparcade.common.util.Util;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by NPException (2015)
 */
public class ItemCartridge extends Item {

	public ItemCartridge(String name) {
		setUnlocalizedName(name);
		GameRegistry.registerItem(this, name);
		setCreativeTab(NPArcade.creativeTab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		// no standard icon
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote) {
			player.swingItem();
			if (ThreadLocalRandom.current().nextFloat() <= 0.05F && Util.isEmptyGame(getGameID(stack))) {
				String[] gameIdArray = ArcadeGameRegistry.gameIDs().toArray(new String[0]);
				String chosenId = gameIdArray[ThreadLocalRandom.current().nextInt(gameIdArray.length)];
				// TODO: send chosenId to server, put the game cartridge into the players inventory, and decrement the stacksize of the item he held
			}
		}
		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		ItemTooltip tooltip = new ItemTooltip();
		addInformation(stack, tooltip);
		//noinspection unchecked
		tooltip.populateList(list);
	}

	@SideOnly(Side.CLIENT)
	private static void addInformation(ItemStack stack, ItemTooltip tooltip) {
		tooltip.addDefaultShiftInfo();

		StringBuilder idInfo = new StringBuilder();
		idInfo.append(Localize.translate(Strings.LANG_TOOLTIP_CARTRIDGE_CONTENT)).append(": ");

		String gameID = getGameID(stack);

		if (Util.isEmptyGame(gameID)) {
			idInfo.append(Localize.translate(Strings.LANG_TOOLTIP_CARTRIDGE_CONTENT_NONE));
			tooltip.addToShiftList(idInfo.toString());
			return;
		}

		if (Util.isUnknownGame(gameID)) {
			tooltip.addAllToShiftList(Localize.wrapToSize(Localize.translate(Strings.LANG_TOOLTIP_CARTRIDGE_UNKNOWN_EXPLANATION), 42));
			tooltip.addToShiftList("");
			idInfo.append("§3").append(gameID);
			tooltip.addToShiftList(idInfo.toString());
			return;
		}

		ArcadeGameWrapper wrapper = ArcadeGameRegistry.gameForID(gameID);
		if (wrapper.gameDescription() != null) {
			// add description
			for (String line : Localize.wrapToSize(wrapper.gameDescription(), 40)) {
				tooltip.addToShiftList("  §6" + line);
			}
			tooltip.addToShiftList("");
		}

		idInfo.append("§3").append(gameID);
		tooltip.addToShiftList(idInfo.toString());
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String gameID = getGameID(stack);
		if (Util.isUnknownGame(gameID)) {
			return Localize.translate(Strings.LANG_TOOLTIP_CARTRIDGE_UNKNOWN);
		}
		if (Util.isEmptyGame(gameID)) {
			return super.getItemStackDisplayName(stack);
		}

		return ArcadeGameRegistry.gameForID(gameID).gameTitle();
	}

	public static String getGameID(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		return tag != null && tag.hasKey(Strings.NBT_GAME)
				? tag.getString(Strings.NBT_GAME)
				: Strings.EMPTY_GAME_ID;
	}

	public static void setGameID(ItemStack stack, String gameID) {
		NBTTagCompound tag = stack.getTagCompound();
		if (gameID == null || Util.isEmptyGame(gameID)) {
			if (tag != null) {
				tag.removeTag(Strings.NBT_GAME);
				if (tag.hasNoTags()) {
					stack.setTagCompound(null);
				}
			}
			return;
		}
		if (tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		tag.setString(Strings.NBT_GAME, gameID);
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item thisItem, CreativeTabs tab, List subItems) {
		// empty cartridge
		subItems.add(createItemStack(null, 1));

		// broken cartridge (no game with id "" should be able to ever exist)
		subItems.add(createItemStack("", 1));

		// all available games
		for (String gameId : ArcadeGameRegistry.gameIDs()) {
			subItems.add(createItemStack(gameId, 1));
		}
	}

	private ItemStack createItemStack(String gameId, int num) {
		num = num < 1 ? 1 : num > 64 ? 64 : num;
		ItemStack stack = new ItemStack(this, num);
		setGameID(stack, gameId);
		return stack;
	}
}
