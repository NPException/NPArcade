package de.npe.mcmods.nparcade.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.arcade.ArcadeMachine;
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

		glColor3f(1.0F, 1.0F, 1.0F);

		bindTexture(ModelArcadeCabinet.texture);
		ModelArcadeCabinet.instance.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		renderScreen(tile, tick);

		glPopMatrix();
	}

	private static float _16th = 1F / 16F;
	private static float screenShiftX = -(_16th * 5F) + 0.2025F * _16th;
	private static float screenShiftY = -(_16th * 6F) + 0.4025F * _16th;
	private static float screenShiftZ = _16th * 2.36F;

	private void renderScreen(TileArcadeCabinet tile, float tick) {
		ArcadeMachine arcade = tile.arcadeMachine();
		if (arcade == null) {
			return;
		}

		glPushMatrix();

		// set max brightness
		Helper.pushMaxBrightness();

		glTranslatef(0F, 0F, screenShiftZ);
		glRotatef(-0.5F * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
		glTranslatef(screenShiftX, screenShiftY, 0F);
		glScalef(0.006F, 0.006F, 0.006F);

		arcade.doRenderScreen(tick);

		// reset brightness
		Helper.popMaxBrightness();

		glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
		doRender((TileArcadeCabinet) tile, x, y, z, tick);
	}
}
