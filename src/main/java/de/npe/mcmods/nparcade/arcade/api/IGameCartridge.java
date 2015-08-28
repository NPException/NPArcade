package de.npe.mcmods.nparcade.arcade.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * If a mod wants to create its own cartridge, it just needs to implement this interface on a class,
 * and pass an instance of it as a parameter to
 * {@link de.npe.mcmods.nparcade.arcade.ArcadeGameRegistry#register(Class, String, String, IGameCartridge)}
 * <p/>
 * I suggest you just implent this on the actual {@link Item} you want to use as a cartridge, and then implement the
 * {@linkplain #getCartridgeItem()} method like that:
 * <pre>
 *    public void Item getCartridgeItem() {
 *       return this;
 *    }
 * </pre>
 *
 * Created by NPException (2015)
 */
public interface IGameCartridge {

	/**
	 * This method must return the Item instance that should be used when spawning the item in the world.
	 * <p/>
	 * It will be called only once when registering the game, to verify that this is a valid {@link IGameCartridge}
	 * and to get the cartridge item that should be spawned in the world.
	 */
	Item getCartridgeItem();

	/**
	 * This method returns a gameID for the given {@link ItemStack}.
	 * <p/>
	 * The ItemStack will hold the Item that was determined by a call to {@link #getCartridgeItem()}
	 * upon registration of the game.
	 *
	 * @param stack the ItemStack that the gameID should be read from.
	 */
	String getGameID(ItemStack stack);

	/**
	 * This method sets the gameID to the given {@link ItemStack}.
	 * <p/>
	 * The ItemStack will hold the Item that was determined by a call to {@link #getCartridgeItem()}
	 * upon registration of the game.
	 *
	 * @param stack  the ItemStack that the gameID should be set to.
	 * @param gameID the gameID that should be set. If this is NULL,
	 *               a possibly existing gameID should be removed from the ItemStack.
	 */
	void setGameID(ItemStack stack, String gameID);
}
