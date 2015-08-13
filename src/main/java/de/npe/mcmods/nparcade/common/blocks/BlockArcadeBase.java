package de.npe.mcmods.nparcade.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.NPArcade;
import de.npe.mcmods.nparcade.common.lib.Reference;
import me.jezza.oc.common.blocks.BlockAbstract;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by NPException (2015)
 */
public class BlockArcadeBase extends BlockAbstract {

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public BlockArcadeBase(Material material, String name) {
		super(material, name);

		setHardness(1.0F);
		setResistance(5.0F);
		setStepSound(soundTypeWood);

		setCreativeTab(NPArcade.creativeTab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		icons = new IIcon[6];
		for (int i = 0; i < icons.length; i++) {
			icons[i] = iconRegister.registerIcon(Reference.MOD_IDENTIFIER + "nparcade_arcadeBase_" + i);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
		super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

		int mcFacing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0D / 360.0D + 2.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, mcFacing, 3);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {

		if (side > 1) {
			ForgeDirection fd = ForgeDirection.getOrientation(side);
			for (int i = 0; i < meta; i++) {
				fd = fd.getRotation(ForgeDirection.DOWN);
			}
			side = fd.ordinal();
		}

		return icons[side];
	}
}
