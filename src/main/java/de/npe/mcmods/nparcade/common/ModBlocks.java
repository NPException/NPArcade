package de.npe.mcmods.nparcade.common;

import de.npe.mcmods.nparcade.common.blocks.BlockArcadeBase;
import de.npe.mcmods.nparcade.common.blocks.BlockArcadeCabinet;
import de.npe.mcmods.nparcade.common.blocks.BlockArcadeStool;
import de.npe.mcmods.nparcade.common.lib.Strings;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public final class ModBlocks {

	public static Block stool;
	public static Block arcadeBase;
	public static Block arcadeCabinet;

	public static void init() {
		stool = new BlockArcadeStool(Material.wood, Strings.BLOCK_STOOL);
		arcadeBase = new BlockArcadeBase(Material.wood, Strings.BLOCK_ARCADE_BASE);
		arcadeCabinet = new BlockArcadeCabinet(Material.wood, Strings.BLOCK_ARCADE_CABINET);
	}
}
