package de.npe.mcmods.nparcade.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.client.render.models.ModelArcadeCabinet;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeCabinet;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class RenderTileArcadeCabinet extends TileEntitySpecialRenderer {

	public void doRender(TileArcadeCabinet tile, double x, double y, double z, float tick) {
		glPushMatrix();
		glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		glRotatef(180F, 1F, 0F, 0F);

		switch (tile.facing()) {
			case NORTH:
				glRotatef(180F, 0F, 1F, 0F);
				break;
			case WEST:
				glRotatef(90F, 0F, 1F, 0F);
				break;
			case EAST:
				glRotatef(270F, 0F, 1F, 0F);
				break;
		}

		bindTexture(ModelArcadeCabinet.texture);

		ModelArcadeCabinet.instance.renderCabinet(tile, tick, 0.0625F);

		glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
		doRender((TileArcadeCabinet) tile, x, y, z, tick);
	}
}
