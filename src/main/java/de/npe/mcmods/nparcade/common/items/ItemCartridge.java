package de.npe.mcmods.nparcade.common.items;

import de.npe.mcmods.nparcade.arcade.ArcadeGameRegistry;
import de.npe.mcmods.nparcade.arcade.ArcadeGameWrapper;
import de.npe.mcmods.nparcade.common.lib.Strings;
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
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			String gameID = tag.hasKey(Strings.NBT_GAME) ? tag.getString(Strings.NBT_GAME) : null;
			ArcadeGameWrapper wrapper = ArcadeGameRegistry.gameForID(gameID);
			if (wrapper != null) {
				text.append("§6§o").append(wrapper.gameName());
				hasGame = true;
			}
		}
		if (!hasGame) {
			text.append(Localise.translate(Strings.LANG_TOOLTIP_CARTRIDGE_GAME_NONE));
		}
		tooltip.addToInfoList(text.toString());
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return super.getItemStackDisplayName(stack); // TODO: use game gameName
	}
}
