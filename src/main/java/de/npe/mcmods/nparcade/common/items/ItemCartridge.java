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
		setTextureless();
	}

	@Override
	protected void addInformation(ItemStack stack, EntityPlayer player, IItemTooltip tooltip) {
		StringBuilder text = new StringBuilder();
		text.append(Localise.translate(Strings.LANG_TOOLTIP_CARTRIDGE_CONTENT) + ": ");

		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null && tag.hasKey(Strings.NBT_GAME)) {
			String gameID = tag.getString(Strings.NBT_GAME);
			ArcadeGameWrapper wrapper = ArcadeGameRegistry.gameForID(gameID);

			text.append("§3").append(gameID);
			tooltip.addToInfoList(text.toString());
			if (wrapper == null) {
				tooltip.defaultInfoList();
				tooltip.addAllToShiftList(Localise.wrapToSize(Localise.translate(Strings.LANG_TOOLTIP_CARTRIDGE_UNKNOWN_EXPLANATION), 60));
			}
			return;
		}
		text.append(Localise.translate(Strings.LANG_TOOLTIP_CARTRIDGE_CONTENT_NONE));
		tooltip.addToInfoList(text.toString());
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			String gameID = tag.hasKey(Strings.NBT_GAME) ? tag.getString(Strings.NBT_GAME) : null;
			ArcadeGameWrapper wrapper = ArcadeGameRegistry.gameForID(gameID);
			if (wrapper != null) {
				return wrapper.gameName();
			} else {
				return Localise.translate(Strings.LANG_TOOLTIP_CARTRIDGE_UNKNOWN);
			}
		}
		return super.getItemStackDisplayName(stack);
	}
}
