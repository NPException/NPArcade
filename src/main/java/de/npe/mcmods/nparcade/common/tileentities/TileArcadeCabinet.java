package de.npe.mcmods.nparcade.common.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.arcade.ArcadeMachine;
import de.npe.mcmods.nparcade.arcade.SampleGame;
import de.npe.mcmods.nparcade.common.lib.Strings;
import de.npe.mcmods.nparcade.common.util.Util;
import me.jezza.oc.common.interfaces.IBlockInteract;
import me.jezza.oc.common.interfaces.IBlockNotifier;
import me.jezza.oc.common.tile.TileAbstract;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by NPException (2015)
 */
public class TileArcadeCabinet extends TileAbstract implements IBlockInteract, IBlockNotifier {

	public ForgeDirection facing;
	public String gameID;

	public TileArcadeCabinet() {
		facing = ForgeDirection.SOUTH;
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		// do nothing
	}

	@Override
	public void onBlockAdded(EntityLivingBase entityLivingBase, World world, int x, int y, int z, ItemStack itemStack) {
		facing = Util.getViewDirectionOfEntity(entityLivingBase).getOpposite();
	}

	@Override
	public void onNeighbourBlockChanged(World world, int x, int y, int z, Block block) {
		// do nothing
	}

	@Override
	public void onNeighbourTileChanged(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		// do nothing
	}

	@Override
	public boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
		if (player.getCurrentEquippedItem() != null) {
			return false;
		}

		if (!world.isRemote) {
			gameID = gameID != null ? null : SampleGame.ID;
			markForUpdate();
		}

		// initiate gamesession
		return true;
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) {
			updateClientSide();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		NBTTagCompound modTag = Util.getModNBTTag(tag, true);

		modTag.setByte(Strings.NBT_ARCADE_CABINET_FACING, (byte) facing.ordinal());

		if (gameID != null) {
			modTag.setString(Strings.NBT_ARCADE_CABINET_GAME, gameID);
		} else {
			modTag.removeTag(Strings.NBT_ARCADE_CABINET_GAME);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		NBTTagCompound modTag = Util.getModNBTTag(tag, false);

		facing = ForgeDirection.getOrientation(modTag.getByte(Strings.NBT_ARCADE_CABINET_FACING));

		gameID = modTag.hasKey(Strings.NBT_ARCADE_CABINET_GAME) ? modTag.getString(Strings.NBT_ARCADE_CABINET_GAME) : null;

		if (worldObj != null && worldObj.isRemote) {
			if (gameID != null) {
				loadGame(gameID, false);
			} else {
				unloadGame();
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	//          ARCADE GAME STUFF                                                             //
	////////////////////////////////////////////////////////////////////////////////////////////

	@SideOnly(Side.CLIENT)
	private ArcadeMachine arcadeMachine;

	@SideOnly(Side.CLIENT)
	public ArcadeMachine arcadeMachine() {
		if (arcadeMachine == null) {
			arcadeMachine = new ArcadeMachine(100,130);
		}
		return arcadeMachine;
	}

	////////////
	// UPDATE //
	////////////

	@SideOnly(Side.CLIENT)
	private void updateClientSide() {
		arcadeMachine().update();
	}

	@SideOnly(Side.CLIENT)
	private void loadGame(String gameID, boolean forceReload) {
		// TODO: switch out with game from cartridge
		arcadeMachine().load(gameID, forceReload);
	}

	@SideOnly(Side.CLIENT)
	private void unloadGame() {
		arcadeMachine().unload();
	}
}
