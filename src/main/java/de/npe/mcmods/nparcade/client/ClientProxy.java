package de.npe.mcmods.nparcade.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.client.render.*;
import de.npe.mcmods.nparcade.common.CommonProxy;
import de.npe.mcmods.nparcade.common.ModBlocks;
import de.npe.mcmods.nparcade.common.ModItems;
import de.npe.mcmods.nparcade.common.lib.Reference;
import de.npe.mcmods.nparcade.common.lib.Strings;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeCabinet;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeStool;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.input.Keyboard;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	public static KeyBinding keyBindingArcadeRed;
	public static KeyBinding keyBindingArcadeGreen;
	public static KeyBinding keyBindingArcadeBlue;
	public static KeyBinding keyBindingArcadeYellow;
	
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

		ClientRegistry.registerKeyBinding(keyBindingArcadeRed = new KeyBinding(Strings.KEY_ARCADE_BUTTON_RED, Keyboard.KEY_H, Reference.MOD_ID));
		ClientRegistry.registerKeyBinding(keyBindingArcadeGreen = new KeyBinding(Strings.KEY_ARCADE_BUTTON_GREEN, Keyboard.KEY_J, Reference.MOD_ID));
		ClientRegistry.registerKeyBinding(keyBindingArcadeBlue = new KeyBinding(Strings.KEY_ARCADE_BUTTON_BLUE, Keyboard.KEY_K, Reference.MOD_ID));
		ClientRegistry.registerKeyBinding(keyBindingArcadeYellow = new KeyBinding(Strings.KEY_ARCADE_BUTTON_YELLOW, Keyboard.KEY_L, Reference.MOD_ID));
	}

	@Override
	public void postInitClientSide() {
//		ClientCommandHandler.instance.registerCommand(new RenderTweakCommand());
	}
}
