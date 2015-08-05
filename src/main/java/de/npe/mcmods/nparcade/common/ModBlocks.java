package de.npe.mcmods.nparcade.common;

import de.npe.mcmods.nparcade.common.blocks.BlockArcadeStool;
import de.npe.mcmods.nparcade.common.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {

	public static Block stool;

	public static void init() {
		stool = new BlockArcadeStool(Material.wood, Strings.BLOCK_STOOL);
	}
}
