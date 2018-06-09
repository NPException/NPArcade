package de.npe.mcmods.nparcade.client.render;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public final class Helper {

	private static float lastBrightnessX;
	private static float lastBrightnessY;

	public static void pushMaxBrightness() {
		lastBrightnessX = OpenGlHelper.lastBrightnessX;
		lastBrightnessY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
	}

	public static void popMaxBrightness() {
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
	}

	public enum Alignment {
		UL,
		U,
		UR,
		L,
		M,
		R,
		DL,
		D,
		DR;

		private static Alignment[] horizontal = {L, M, R, L, M, R, L, M, R};
		private static Alignment[] vertical = {U, U, U, M, M, M, D, D, D};

		public Alignment horizontal() {
			return horizontal[ordinal()];
		}

		public Alignment vertical() {
			return vertical[ordinal()];
		}
	}

	public static void renderRectInBounds(float boundsWidth, float boundsHeight, float rectWidth, float rectHeight,
													  float u1, float v1, float u2, float v2,
													  Alignment alignment) {
		float x = 0F;
		float y = 0F;

		final float tRatio = boundsWidth / boundsHeight;

		final float sRatio = rectWidth / rectHeight;

		// these checks center the rectangle within the
		// given bounds
		if (sRatio > tRatio) {
			Alignment alignV = alignment.vertical();
			if (alignV != Alignment.U) {
				y = (boundsHeight - boundsHeight / sRatio * tRatio) * (alignV == Alignment.M ? 0.5F : 1F);
			}
			boundsHeight = boundsHeight / sRatio * tRatio;
		} else if (sRatio < tRatio) {
			Alignment alignH = alignment.horizontal();
			if (alignH != Alignment.L) {
				x = (boundsWidth - boundsWidth / tRatio * sRatio) * (alignH == Alignment.M ? 0.5F : 1F);
			}
			boundsWidth = boundsWidth / tRatio * sRatio;
		}

		glBegin(GL_TRIANGLES);

		glNormal3f(0, 0, -1);
		glTexCoord2f(u2, v1); // top right
		glVertex2f(x + boundsWidth, y);
		glTexCoord2f(u1, v1); // top left
		glVertex2f(x, y);
		glTexCoord2f(u1, v2); // bottom left
		glVertex2f(x, y + boundsHeight);

		glTexCoord2f(u1, v2); // bottom left
		glVertex2f(x, y + boundsHeight);
		glTexCoord2f(u2, v2); // bottom right
		glVertex2f(x + boundsWidth, y + boundsHeight);
		glTexCoord2f(u2, v1); // top right
		glVertex2f(x + boundsWidth, y);

		glEnd();
	}
}
