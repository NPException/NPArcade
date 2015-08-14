package de.npe.api.nparcade.util;

import org.lwjgl.opengl.GL11;

import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.util.Hashtable;

/**
 * Created by NPException (2015)
 */
public class RenderUtil {
	public enum TextureFormat {
		/**
		 * RGB formate without alpha channel
		 */
		RGB(GL11.GL_RGB8, 3),
		/**
		 * RGB format with alpha channel
		 */
		RGBA(GL11.GL_RGBA8, 4);

		public final int glValue;
		public final int bytesPerPixel;

		TextureFormat(int glValue, int bytesPerPixel) {
			this.glValue = glValue;
			this.bytesPerPixel = bytesPerPixel;
		}
	}

	/**
	 * Creates a new BufferedImage with the given width, height, and TextureFormat.
	 */
	public static BufferedImage createBufferedImage(final int width, final int height, TextureFormat format) {
		if (format == TextureFormat.RGB) {
			WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 3, null);
			return new BufferedImage(glRGBColorModel, raster, false, new Hashtable<String, Object>());
		} else if (format == TextureFormat.RGBA) {
			WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 4, null);
			return new BufferedImage(glRGBAColorModel, raster, false, new Hashtable<String, Object>());
		} else {
			assert false;
			return null;
		}
	}

	protected static final ComponentColorModel glRGBAColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[]{8, 8, 8, 8},
			true,
			false,
			ComponentColorModel.TRANSLUCENT,
			DataBuffer.TYPE_BYTE);

	protected static final ComponentColorModel glRGBColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[]{8, 8, 8, 0},
			false,
			false,
			ComponentColorModel.OPAQUE,
			DataBuffer.TYPE_BYTE);
}
