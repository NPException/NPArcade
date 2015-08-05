package de.npe.mcmods.nparcade.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.client.render.models.ModelArcadeStool;
import de.npe.mcmods.nparcade.common.tiles.TileArcadeStool;
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
		glTranslatef((float) x + 0.5f, (float) y + 0.625f, (float) z + 0.5f);
		glRotatef(180f, 1f, 0f, 0f);
		glRotatef(360f*tile.rotation, 0f, 1f, 0f);
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
