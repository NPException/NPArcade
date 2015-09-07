package de.npe.mcmods.nparcade.common;

import de.npe.mcmods.nparcade.common.items.ItemCartridge;
import de.npe.mcmods.nparcade.common.lib.Strings;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;


public class ModItems {

	public static ItemCartridge cartridge;

	public static void init() {
		cartridge = new ItemCartridge(Strings.ITEM_CARTRIDGE);
	}

	public static void initRecipes() {
		CraftingManager crafting = CraftingManager.getInstance();

		crafting.addRecipe(new ItemStack(ModBlocks.stool),
				"ss",
				"ll",
				"ll",

				's',
				new ItemStack(Blocks.wooden_slab, 1, OreDictionary.WILDCARD_VALUE),

				'l',
				new ItemStack(Items.stick, 1, OreDictionary.WILDCARD_VALUE)
		);

		// TODO: change test recipe to proper one
		ItemStack cartridgeStack = new ItemStack(cartridge);
		crafting.addShapelessRecipe(cartridgeStack, new ItemStack(Items.comparator, 1, OreDictionary.WILDCARD_VALUE));
	}
}
