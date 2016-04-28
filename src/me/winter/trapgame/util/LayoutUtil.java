package me.winter.trapgame.util;

import java.awt.*;

/**
 *
 *
 * Created by Alexander Winter on 2016-04-24.
 */
public class LayoutUtil
{
	public static void nextTo(GridBagConstraints constraints)
	{
		constraints.gridx += constraints.gridwidth;
	}

	public static void newLine(GridBagConstraints constraints)
	{
		constraints.gridx = 0;
		constraints.gridy += constraints.gridheight;
	}


	public static void nextTo(GridBagConstraints constraints, int width, int height)
	{
		constraints.gridx += constraints.gridwidth;
		constraints.gridwidth = width;
		constraints.gridheight = height;
	}

	public static void newLine(GridBagConstraints constraints, int width, int height)
	{
		constraints.gridx = 0;
		constraints.gridy += constraints.gridheight;
		constraints.gridwidth = width;
		constraints.gridheight = height;
	}

}
