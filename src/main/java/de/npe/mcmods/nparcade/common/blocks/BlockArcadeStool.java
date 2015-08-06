package de.npe.mcmods.nparcade.common.blocks;

import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeStool;
import me.jezza.oc.common.blocks.BlockAbstractModel;
import me.jezza.oc.common.interfaces.ITileProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by NPException (2015)
 */
public class BlockArcadeStool extends BlockAbstractModel implements ITileProvider {

	public BlockArcadeStool(Material material, String name) {
		super(material, name);

		setHardness(1.0F);
		setResistance(5.0F);
		setStepSound(soundTypeWood);

		setBlockBounds(0.2f, 0f, 0.2f, 0.8f, 0.625f, 0.8f);

		setCreativeTab(NPArcade.creativeTab);

		// these 2 lines are here for proper break- and run- particles
		textureReg = true;
		setBlockTextureName(name + "_icon");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
		return super.onBlockActivated(world, x, y, z, player, side, hitVecX, hitVecY, hitVecZ);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return side == ForgeDirection.UP;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileArcadeStool();
	}
}
