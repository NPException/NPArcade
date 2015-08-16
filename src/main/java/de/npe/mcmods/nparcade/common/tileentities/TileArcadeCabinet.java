package de.npe.mcmods.nparcade.common.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.api.nparcade.IArcadeGame;
import de.npe.api.nparcade.IArcadeMachine;
import de.npe.api.nparcade.SampleGame;
import de.npe.mcmods.nparcade.common.util.Util;
import me.jezza.oc.common.interfaces.IBlockInteract;
import me.jezza.oc.common.interfaces.IBlockNotifier;
import me.jezza.oc.common.tile.TileAbstract;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.awt.image.BufferedImage;

/**
 * Created by NPException (2015)
 */
public class TileArcadeCabinet extends TileAbstract implements IBlockInteract, IBlockNotifier, IArcadeMachine {

	public ForgeDirection facing;

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
		// initiate gamesession
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		Util.getModNBTTag(tag, true).setByte("arcadeFacing", (byte) facing.ordinal());
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		facing = ForgeDirection.getOrientation(Util.getModNBTTag(tag, false).getByte("arcadeFacing"));
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	//          ARCADE GAME STUFF                                                             //
	////////////////////////////////////////////////////////////////////////////////////////////

	@SideOnly(Side.CLIENT)
	private IArcadeGame game;

	////////////
	// UPDATE //
	////////////

	@Override
	public void updateEntity() {
		if (game == null) {
			game = new SampleGame();
		}
	}


	///////////////
	// RENDERING //
	///////////////

	@SideOnly(Side.CLIENT)
	public static class RenderInfo {
		private int textureID = -1;
		private int[] screenData;
		private BufferedImage image;

		public int textureID() {
			return textureID;
		}

		public int width() {
			return image.getWidth();
		}

		public int height() {
			return image.getHeight();
		}
	}

	@SideOnly(Side.CLIENT)
	private RenderInfo renderInfo = new RenderInfo();

	@SideOnly(Side.CLIENT)
	private boolean needsScreenRefresh() {
		return game != null && (renderInfo.textureID == -1 || game.needsDraw());
	}

	/**
	 * prepares the arcade cabinet's screen texture for beeing rendered
	 */
	@SideOnly(Side.CLIENT)
	public RenderInfo prepareScreenTexture(float tick) {
		if (needsScreenRefresh()) {

			int width = game.screenWidth();
			int height = game.screenHeight();

			// allocate new texture if game output size changed or scren is not yet initialized
			if (renderInfo.textureID == -1 ||
					renderInfo.screenData == null ||
					renderInfo.image == null  ||
					renderInfo.width() != width || renderInfo.height() != height) {

				renderInfo.screenData = new int[width * height];
				if (renderInfo.textureID != -1) {
					TextureUtil.deleteTexture(renderInfo.textureID);
				}
				renderInfo.textureID = TextureUtil.glGenTextures();
				TextureUtil.allocateTexture(renderInfo.textureID, width, height);

				renderInfo.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			}

			// draw game
			game.draw(renderInfo.image, tick);
			// get pixel data
			renderInfo.image.getRGB(0, 0, width, height, renderInfo.screenData, 0, width);
			// upload pixels to texture
			TextureUtil.uploadTexture(renderInfo.textureID, renderInfo.screenData, width, height);
		}
		return renderInfo;
	}
}
