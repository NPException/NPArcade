package de.npe.mcmods.nparcade.common.items;

import de.npe.mcmods.nparcade.arcade.ArcadeGameRegistry;
import de.npe.mcmods.nparcade.arcade.ArcadeGameWrapper;
import de.npe.mcmods.nparcade.arcade.DummyGames;
import de.npe.mcmods.nparcade.arcade.api.IItemGameCartridge;
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
public class ItemCartridge extends ItemAbstract implements IItemGameCartridge {

	public ItemCartridge(String name) {
		super(name);
		setTextureless();
	}

	@Override
	protected void addInformation(ItemStack stack, EntityPlayer player, IItemTooltip tooltip) {
		StringBuilder text = new StringBuilder();
		text.append(Localise.translate(Strings.LANG_TOOLTIP_CARTRIDGE_CONTENT)).append(": ");

		String gameID = getGameID(stack);
		if (DummyGames.EMPTY_GAME_WRAPPER.gameID().equals(gameID)) {
			text.append(Localise.translate(Strings.LANG_TOOLTIP_CARTRIDGE_CONTENT_NONE));
			tooltip.addToInfoList(text.toString());
			return;
		}

		ArcadeGameWrapper wrapper = ArcadeGameRegistry.gameForID(gameID);

		text.append("§3").append(gameID);
		tooltip.addToInfoList(text.toString());

		if (wrapper == DummyGames.UNKNOWN_GAME_WRAPPER) {
			tooltip.defaultInfoList();
			tooltip.addAllToShiftList(Localise.wrapToSize(Localise.translate(Strings.LANG_TOOLTIP_CARTRIDGE_UNKNOWN_EXPLANATION), 60));
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String gameID = getGameID(stack);
		ArcadeGameWrapper wrapper = ArcadeGameRegistry.gameForID(gameID);
		if (!DummyGames.isDummyGame(wrapper.gameID())) {
			return wrapper.gameName();
		} else if (wrapper == DummyGames.UNKNOWN_GAME_WRAPPER) {
			return Localise.translate(Strings.LANG_TOOLTIP_CARTRIDGE_UNKNOWN);
		}
		return super.getItemStackDisplayName(stack);
	}

	@Override
	public String getGameID(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		return tag != null && tag.hasKey(Strings.NBT_GAME) ? tag.getString(Strings.NBT_GAME) : DummyGames.EMPTY_GAME_WRAPPER.gameID();
	}

	@Override
	public void setGameID(ItemStack stack, String gameID) {
		if (gameID == null) {
			stack.setTagCompound(null);
			return;
		}

		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		tag.setString(Strings.NBT_GAME, gameID);
	}
}
