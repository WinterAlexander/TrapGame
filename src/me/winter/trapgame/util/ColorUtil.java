package me.winter.trapgame.util;

import java.util.Random;

import org.newdawn.slick.Color;

public class ColorUtil
{
	private ColorUtil(){}
	
	public static Color pickRandomColor()
	{
		switch(new Random().nextInt(13))
		{
		case 0: return Color.black;
		case 1: return Color.blue;
		case 2: return Color.cyan;
		case 3: return Color.darkGray;
		case 4: return Color.gray;
		case 5: return Color.green;
		case 6: return Color.lightGray;
		case 7: return Color.magenta;
		case 8: return Color.orange;
		case 9: return Color.pink;
		case 10: return Color.red;
		case 11: return Color.white;
		case 12: return Color.yellow;
		default: return Color.transparent;
		}
	}
	
}
