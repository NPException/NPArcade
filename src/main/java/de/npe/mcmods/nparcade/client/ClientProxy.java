package de.npe.mcmods.nparcade.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.client.render.*;
import de.npe.mcmods.nparcade.common.CommonProxy;
import de.npe.mcmods.nparcade.common.ModBlocks;
import de.npe.mcmods.nparcade.common.ModItems;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeCabinet;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeStool;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	@Override
	public void preInitClientSide() {
		// do nothing
	}

	@Override
	public void initClientSide() {
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.stool), new RenderItemArcadeStool());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.arcadeCabinet), new RenderItemArcadeCabinet());
		MinecraftForgeClient.registerItemRenderer(ModItems.cartridge, new RenderItemCartridge());

		ClientRegistry.bindTileEntitySpecialRenderer(TileArcadeStool.class, new RenderTileArcadeStool());
		ClientRegistry.bindTileEntitySpecialRenderer(TileArcadeCabinet.class, new RenderTileArcadeCabinet());
	}

	@Override
	public void postInitClientSide() {
//		ClientCommandHandler.instance.registerCommand(new RenderTweakCommand());
	}
}
