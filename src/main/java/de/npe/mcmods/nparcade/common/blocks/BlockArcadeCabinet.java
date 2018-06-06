package de.npe.mcmods.nparcade.common.blocks;

import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeCabinet;
import me.jezza.oc.common.blocks.BlockAbstractModel;
import me.jezza.oc.common.interfaces.IBlockNotifier;
import me.jezza.oc.common.interfaces.ITileProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

/**
 * Created by NPException (2015)
 */
public class BlockArcadeCabinet extends BlockAbstractModel implements ITileProvider {

	public BlockArcadeCabinet(Material material, String name) {
		super(material, name);

		setHardness(1.0F);
		setResistance(5.0F);
		setStepSound(soundTypeWood);

		setCreativeTab(NPArcade.creativeTab);

		// these 2 lines are here for proper break- and run- particles
		textureReg = true;
		setBlockTextureName("nparcade_blockParticles");
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		if (side == ForgeDirection.DOWN)
			return true;
		TileEntity te = world.getTileEntity(x, y, z);
		return (te instanceof TileArcadeCabinet) && side == ((TileArcadeCabinet) te).facing().getOpposite();
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
}
