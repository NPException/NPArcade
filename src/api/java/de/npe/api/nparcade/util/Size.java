package de.npe.api.nparcade.util;

import java.security.InvalidParameterException;

/**
 * Created by NPException (2015)
 *
 * Small wrapper class used to define the size of an area.
 */
public final class Size {
	public final int width;
	public final int height;

	public Size(int width, int height) {
		if (width <= 0) {
			throw new InvalidParameterException("Width must be larger than 0, but is " + width);
		}
		if (height <= 0) {
			throw new InvalidParameterException("Height must be larger than 0, but is " + height);
		}
		this.width = width;
		this.height = height;
	}
}
