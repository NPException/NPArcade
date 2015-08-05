package de.npe.mcmods.nparcade.common.blocks;

import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.common.tiles.TileArcadeStool;
import me.jezza.oc.common.blocks.BlockAbstractModel;
import me.jezza.oc.common.interfaces.ITileProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileArcadeStool();
	}
}
