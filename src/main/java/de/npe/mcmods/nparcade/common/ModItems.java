package de.npe.mcmods.nparcade.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;


public class ModItems {

//	public static ItemDP book;

	public static void init() {
//		book = new ItemBook(Strings.INFO_BOOK);
	}

	public static void initRecipes() {
		CraftingManager crafting = CraftingManager.getInstance();

		crafting.addRecipe(new ItemStack(ModBlocks.stool),
				"ss",
				"ll",
				"ll",

				's',
				new ItemStack(Blocks.wooden_slab,1, OreDictionary.WILDCARD_VALUE),

				'l',
				new ItemStack(Items.stick,1, OreDictionary.WILDCARD_VALUE)
		);
	}
}
