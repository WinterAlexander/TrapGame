package me.winter.trapgame.util;

import java.awt.*;
import java.awt.color.ColorSpace;

/**
 * <p>Class used to transform an existing java.awt.Color</p>
 *
 * <p>Created by 1541869 on 2016-04-14.</p>
 */
public class ColorTransformer extends Color
{
	public static final ColorTransformer TRANSPARENT = new ColorTransformer(0, 0, 0, 0);

	public ColorTransformer(int r, int g, int b)
	{
		super(r, g, b);
	}

	public ColorTransformer(int r, int g, int b, int a)
	{
		super(r, g, b, a);
	}

	public ColorTransformer(int rgb)
	{
		super(rgb);
	}

	public ColorTransformer(int rgba, boolean hasalpha)
	{
		super(rgba, hasalpha);
	}

	public ColorTransformer(float r, float g, float b)
	{
		super(r, g, b);
	}

	public ColorTransformer(float r, float g, float b, float a)
	{
		super(r, g, b, a);
	}

	public ColorTransformer(ColorSpace cspace, float[] components, float alpha)
	{
		super(cspace, components, alpha);
	}

	public ColorTransformer(Color color)
	{
		super(color.getRGB());
	}

	public ColorTransformer(Color color, int alpha)
	{
		super(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	public ColorTransformer setRed(int red)
	{
		return new ColorTransformer(red, getGreen(), getBlue(), getAlpha());
	}

	public ColorTransformer setGreen(int green)
	{
		return new ColorTransformer(getRed(), green, getBlue(), getAlpha());
	}

	public ColorTransformer setBlue(int blue)
	{
		return new ColorTransformer(getRed(), getGreen(), blue, getAlpha());
	}

	public ColorTransformer setAlpha(int alpha)
	{
		return new ColorTransformer(getRed(), getGreen(), getBlue(), alpha);
	}

	public ColorTransformer addRed(int red)
	{
		return new ColorTransformer(getRed() + red, getGreen(), getBlue(), getAlpha());
	}

	public ColorTransformer addGreen(int green)
	{
		return new ColorTransformer(getRed(), getGreen() + green, getBlue(), getAlpha());
	}

	public ColorTransformer addBlue(int blue)
	{
		return new ColorTransformer(getRed(), getGreen(), getBlue() + blue, getAlpha());
	}

	public ColorTransformer addAlpha(int alpha)
	{
		return new ColorTransformer(getRed(), getGreen(), getBlue(), getAlpha() + alpha);
	}
}
