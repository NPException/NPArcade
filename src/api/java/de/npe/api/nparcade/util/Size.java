package de.npe.api.nparcade.util;

/**
 * Created by NPException (2015)
 * <p/>
 * Small wrapper class used to define the size of an area.
 */
public final class Size {
	public final int width;
	public final int height;

	public Size(int width, int height) {
		if (width <= 0) {
			throw new IllegalArgumentException("Width must be larger than 0, but is " + width);
		}
		if (height <= 0) {
			throw new IllegalArgumentException("Height must be larger than 0, but is " + height);
		}
		this.width = width;
		this.height = height;
	}

	public Size scale(float factor) {
		return new Size((int) (width * factor), (int) (height * factor));
	}

	public Size scaleToWidth(int w) {
		float factor = (float) w / width;
		return new Size(w, (int) (height * factor));
	}

	public Size scaleToHeight(int h) {
		float factor = (float) h / height;
		return new Size((int) (width * factor), h);
	}
}
