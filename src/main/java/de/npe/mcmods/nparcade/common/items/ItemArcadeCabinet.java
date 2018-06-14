package de.npe.mcmods.nparcade.common.items;

import java.util.List;

import de.npe.mcmods.nparcade.common.lib.Strings;
import de.npe.mcmods.nparcade.common.util.Localize;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemArcadeCabinet extends ItemBlock {
	public ItemArcadeCabinet(Block block) {
		super(block);
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

		tooltip.addAllToShiftList(Localize.wrapToSize(Localize.translate(Strings.LANG_TOOLTIP_ARCADE_CABINET), 42));
	}
}
