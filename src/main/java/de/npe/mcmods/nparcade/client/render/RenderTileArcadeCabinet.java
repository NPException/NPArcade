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

		int textureID = tile.prepareScreenTexture(tick);
		if (textureID != -1) {
			glDisable(GL_LIGHTING);

			glScalef(0.06F, 0.06F, 0.06F);
			glTranslatef(-5F, -5.15F, 5.25F);
			glRotatef(-0.5F * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);

			// draw screen
			float tx = 0;
			float ty = 0;

			float w = 10;
			float h = 13;


			glBindTexture(GL_TEXTURE_2D, textureID);

			glBegin(GL_TRIANGLES);

			glTexCoord2f(1, 0); // top right
			glVertex2f(tx + w, ty);
			glTexCoord2f(0, 0); // top left
			glVertex2f(tx, ty);
			glTexCoord2f(0, 1); // bottom left
			glVertex2f(tx, ty + h);

			glTexCoord2f(0, 1); // bottom left
			glVertex2f(tx, ty + h);
			glTexCoord2f(1, 1); // bottom right
			glVertex2f(tx + w, ty + h);
			glTexCoord2f(1, 0); // top right
			glVertex2f(tx + w, ty);

			glEnd();
		}

		glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
		doRender((TileArcadeCabinet) tile, x, y, z, tick);
	}
}
