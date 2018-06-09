package de.npe.mcmods.nparcade.common.blocks;

import java.util.ArrayList;

import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.common.lib.Reference;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeCabinet;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by NPException (2015)
 */
public class BlockArcadeCabinet extends Block implements ITileEntityProvider {

	public BlockArcadeCabinet(Material material, String name) {
		super(material);
		setBlockName(name);
		setBlockTextureName("nparcade_blockParticles"); // only here for proper break- and run- particles
		GameRegistry.registerBlock(this, name);

		setHardness(1.0F);
		setResistance(5.0F);
		setStepSound(soundTypeWood);

		setCreativeTab(NPArcade.creativeTab);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		if (side == ForgeDirection.DOWN) {
			return true;
		}
		TileEntity te = world.getTileEntity(x, y, z);
		return (te instanceof TileArcadeCabinet) && side == ((TileArcadeCabinet) te).facing().getOpposite();
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		if (!world.isRemote) {
			ArrayList<ItemStack> items = new ArrayList<>(2);
			TileEntity te = world.getTileEntity(x, y, z);
			if (te instanceof TileArcadeCabinet) {
				items.addAll(((TileArcadeCabinet) te).generateItemStacksOnRemoval());
			}
			return items;
		}
		return new ArrayList<>(0);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileArcadeCabinet();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileArcadeCabinet) {
			return ((TileArcadeCabinet) te).onActivated(world, player);
		}
		return false;
	}

	@Override
	public boolean onBlockEventReceived(World world, int x, int y, int z, int id, int process) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		return tileEntity != null && tileEntity.receiveClientEvent(id, process);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileArcadeCabinet) {
			((TileArcadeCabinet) te).onBlockAdded(entityLiving);
		}
	}

	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileArcadeCabinet) {
			((TileArcadeCabinet) te).onBlockRemoval(null);
		}
		super.onBlockExploded(world, x, y, z, explosion);
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileArcadeCabinet) {
			((TileArcadeCabinet) te).onBlockRemoval(player);
		}
		return super.removedByPlayer(world, player, x, y, z, willHarvest);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon(Reference.MOD_IDENTIFIER + getTextureName());
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return -1;
	}
}
