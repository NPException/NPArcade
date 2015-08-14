package de.npe.mcmods.nparcade.client.render;

/**
 * Created by NPException (2015)
 */

import de.npe.api.nparcade.util.RenderUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class TextureUtil extends RenderUtil {

	private static TextureFormat findFormat(BufferedImage img) {
		final int numBands = img.getRaster().getNumBands();
		return (numBands == 3) ? TextureFormat.RGB : TextureFormat.RGBA;
	}

	private static BufferedImage convertToGlFormat(BufferedImage inImage, final boolean flipY) {
		// If already in a suitable colour model then just return the input unchanged
		if ((inImage.getColorModel().equals(glRGBColorModel) || inImage.getColorModel().equals(glRGBAColorModel)) && !flipY) {
			return inImage;
		}

		TextureFormat format = findFormat(inImage);
		BufferedImage outImage = createBufferedImage(inImage.getWidth(), inImage.getHeight(), format);

		if (flipY) {
			outImage.getGraphics().drawImage(inImage,
					0, 0, inImage.getWidth(), inImage.getHeight(), // dest rect
					0, inImage.getHeight(), inImage.getWidth(), 0, // src rect
					null);
		} else {
			outImage.getGraphics().drawImage(inImage, 0, 0, null);
		}

		return outImage;
	}

	public static int createTexture(BufferedImage imageData, final boolean flipY) {
		imageData = convertToGlFormat(imageData, flipY);

		TextureFormat format = findFormat(imageData);

		IntBuffer buff = BufferUtils.createIntBuffer(16);
		buff.limit(1);
		GL11.glGenTextures(buff);

		final int textureId = buff.get();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

		ByteBuffer scratch = ByteBuffer.allocateDirect(format.bytesPerPixel * imageData.getWidth() * imageData.getHeight());

		Raster raster = imageData.getRaster();
		byte data[] = (byte[]) raster.getDataElements(0, 0, imageData.getWidth(), imageData.getHeight(), null);
		scratch.clear();
		scratch.put(data);
		scratch.rewind();

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format.glValue,            // Mip level & internal format
				imageData.getWidth(), imageData.getHeight(), 0,      // width, height, border
				format.glValue, GL11.GL_UNSIGNED_BYTE,         // pixel data format
				scratch);                                 // pixel data

		return textureId;
	}
}
