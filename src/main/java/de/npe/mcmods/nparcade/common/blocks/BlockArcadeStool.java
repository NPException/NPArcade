package de.npe.mcmods.nparcade.common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.common.lib.Reference;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeStool;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by NPException (2015)
 */
public class BlockArcadeStool extends Block implements ITileEntityProvider {

	public BlockArcadeStool(Material material, String name) {
		super(material);
		setBlockName(name);
		setBlockTextureName("nparcade_blockParticles"); // only here for proper break- and run- particles
		GameRegistry.registerBlock(this, name);

		setHardness(1.0F);
		setResistance(5.0F);
		setStepSound(soundTypeWood);

		setBlockBounds(0.2f, 0f, 0.2f, 0.8f, 0.625f, 0.8f);

		setCreativeTab(NPArcade.creativeTab);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return side == ForgeDirection.UP;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileArcadeStool();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileArcadeStool) {
			return ((TileArcadeStool) te).onActivated(world, player);
		}
		return false;
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

	@Override
	public int getRenderType() {
		return -1;
	}
}
