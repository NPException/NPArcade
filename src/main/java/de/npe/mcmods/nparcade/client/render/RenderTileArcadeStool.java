package de.npe.mcmods.nparcade.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.client.render.models.ModelArcadeStool;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeStool;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class RenderTileArcadeStool extends TileEntitySpecialRenderer {

	public void doRender(TileArcadeStool tile, double x, double y, double z, float tick) {
		glPushMatrix();
		glTranslatef((float) x + 0.5F, (float) y + 0.625F, (float) z + 0.5F);
		glRotatef(180F, 1F, 0F, 0F);
		glRotatef(tile.rotation, 0F, 1F, 0F);
		glColor3f(1.0F, 1.0F, 1.0F);
		bindTexture(ModelArcadeStool.texture);
		ModelArcadeStool.instance.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
		doRender((TileArcadeStool) tile, x, y, z, tick);
	}
}
