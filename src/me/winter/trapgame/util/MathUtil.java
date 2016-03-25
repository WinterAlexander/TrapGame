package me.winter.trapgame.util;

public class MathUtil
{
	private MathUtil(){}
	
	public static double distance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt(Math.pow(x2 - x1, 2) +  Math.pow(y2 - y1, 2));
	}
	
	public static double round(double number, int decimal)
	{
		return Math.round(number * Math.pow(10, decimal)) / Math.pow(10, decimal);
	}
	
	public static double max(double[] array)
	{
		double max = array[0];
		for(double i : array)
		{
			if(max < i)
				max = i;
		}
		return max;
	}
	
	public static double min(double[] array)
	{
		double min = array[0];
		for(double i : array)
		{
			if(min > i)
				min = i;
		}
		return min;
	}

	public static int negMod(int i, int j)
	{
		while(i < 0)
			i += j;

		return i % j;
	}
}
