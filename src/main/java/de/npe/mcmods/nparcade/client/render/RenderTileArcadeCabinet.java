package de.npe.mcmods.nparcade.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.client.render.models.ModelArcadeCabinet;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeCabinet;
import net.minecraft.client.renderer.OpenGlHelper;
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

		switch (tile.facing) {
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

	private void renderScreen(TileArcadeCabinet tile, float tick) {
		glPushMatrix();

		TileArcadeCabinet.RenderInfo renderInfo = tile.prepareScreenTexture(tick);
		if (renderInfo.textureID() != -1) {
			// set max brightness
			glDisable(GL_LIGHTING);
			float lastBrightnessX = OpenGlHelper.lastBrightnessX;
			float lastBrightnessY = OpenGlHelper.lastBrightnessY;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

			glScalef(0.06F, 0.06F, 0.06F);
			glTranslatef(-5F, -5.15F, 5.25F);
			glRotatef(-0.5F * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);

			// texture variables
			float tx = 0F;
			float ty = 0F;

			float tw = 10F;
			float th = 13F;
			final float tRatio = tw/th;

			// screen variables
			final float sw = (float) renderInfo.width();
			final float sh = (float) renderInfo.height();
			final float sRatio = sw/sh;

			if (sRatio > tRatio) {
				ty = th*0.5F - (th*0.5F)/sRatio*tRatio;
				th = th/sRatio*tRatio;
			}
			if (sRatio < tRatio) {
				tx = tw*0.5F - (tw*0.5F)/tRatio*sRatio;
				tw = tw/tRatio*sRatio;
			}

			glBindTexture(GL_TEXTURE_2D, renderInfo.textureID());

			glBegin(GL_TRIANGLES);

			glTexCoord2f(1, 0); // top right
			glVertex2f(tx + tw, ty);
			glTexCoord2f(0, 0); // top left
			glVertex2f(tx, ty);
			glTexCoord2f(0, 1); // bottom left
			glVertex2f(tx, ty + th);

			glTexCoord2f(0, 1); // bottom left
			glVertex2f(tx, ty + th);
			glTexCoord2f(1, 1); // bottom right
			glVertex2f(tx + tw, ty + th);
			glTexCoord2f(1, 0); // top right
			glVertex2f(tx + tw, ty);

			glEnd();

			// reset brightness
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
		}

		glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
		doRender((TileArcadeCabinet) tile, x, y, z, tick);
	}
}
