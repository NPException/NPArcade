package de.npe.mcmods.nparcade.common.items;

import de.npe.mcmods.nparcade.arcade.ArcadeGameRegistry;
import de.npe.mcmods.nparcade.common.lib.Strings;
import de.npe.mcmods.nparcade.common.util.Util;
import me.jezza.oc.common.interfaces.IItemTooltip;
import me.jezza.oc.common.items.ItemAbstract;
import me.jezza.oc.common.utils.Localise;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by NPException (2015)
 */
public class ItemCartridge extends ItemAbstract {

	public ItemCartridge(String name) {
		super(name);
	}

	@Override
	protected void addInformation(ItemStack stack, EntityPlayer player, IItemTooltip tooltip) {
		StringBuilder text = new StringBuilder();
		text.append(Localise.translate(Strings.LANG_TOOLTIP_CARTRIDGE_GAME) + ": ");

		// TODO: only add info if item is not an empty game
		boolean hasGame = false;
		NBTTagCompound modTag = stack.hasTagCompound() ? Util.getModNBTTag(stack.getTagCompound(), false) : null;
		if (modTag != null) {
			String gameID = modTag.hasKey(Strings.NBT_GAME) ? modTag.getString(Strings.NBT_GAME) : null;
			ArcadeGameRegistry.GameInfo info = ArcadeGameRegistry.gameForID(gameID);
			if (info != null) {
				text.append("§6§o").append(info.name());
				hasGame = true;
			}
		}
		if (!hasGame) {
			text.append(Localise.translate(Strings.LANG_TOOLTIP_CARTRIDGE_GAME_NONE));
		}
		tooltip.addToInfoList(text.toString());
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {
		return super.getItemStackDisplayName(p_77653_1_); // TODO: use game name
	}
}
