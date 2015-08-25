package de.npe.mcmods.nparcade.arcade.api;

import net.minecraft.item.ItemStack;

import java.awt.image.BufferedImage;

/**
 * If a mod wants to create its own cartridge, it just needs to implement this interface on an item
 * that should be used as the cartridge, and pass the singleton instance of that game as a parameter of
 * {@link de.npe.mcmods.nparcade.arcade.ArcadeGameRegistry#register(Class, String, String, BufferedImage, String)}
 * <p/>
 * Created by NPException (2015)
 */
public interface IItemGameCartridge {

	/**
	 * This method returns a gameID for the given {@link ItemStack}.
	 *
	 * @param stack the ItemStack that the gameID should be read from.
	 */
	String getGameID(ItemStack stack);

	/**
	 * This method sets the gameID to the given {@link ItemStack}
	 *
	 * @param stack  the ItemStack that the gameID should be set to.
	 * @param gameID the gameID that should be set. If this is NULL,
	 *               a possibly existing gameID should be removed from the ItemStack.
	 */
	void setGameID(ItemStack stack, String gameID);
}
