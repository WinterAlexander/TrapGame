package me.winter.trapgame.util;

import java.util.HashMap;

/**
 *
 * Created by Alexander Winter on 2016-01-19.
 */
public class SortingUtil
{
	private SortingUtil(){}

	public static int[] quickSort(int[] array)
	{
		quickSort(null, array, 0, array.length - 1);
		return array;
	}

	public static Object[] quickSort(Object[] data, int[] indexes)
	{
		quickSort(data, indexes, 0, indexes.length - 1);
		return data;
	}

	public static double[] quickSort(double[] array)
	{
		quickSort(null, array, 0, array.length - 1);
		return array;
	}

	public static Object[] quickSort(Object[] data, double[] indexes)
	{
		quickSort(data, indexes, 0, indexes.length - 1);
		return data;
	}

	public static void quickSort(Object[] data, int[] array, int left, int right)
	{
		int i = left, j = right;
		int temp;
		Object dataTemp;
		int pivot = array[(left + right) / 2];

		/* partition */
		while(i <= j)
		{
			while (array[i] < pivot)
				i++;
			while (array[j] > pivot)
				j--;

			if (i <= j)
			{
				temp = array[i];
				array[i] = array[j];
				array[j] = temp;

				if(data != null)
				{
					dataTemp = data[i];
					data[i] = data[j];
					data[j] = dataTemp;
				}

				i++;
				j--;
			}
		}

		/* recursion */
		if(left < j)
			quickSort(data, array, left, j);

		if(i < right)
			quickSort(data, array, i, right);
	}

	public static void quickSort(Object[] data, double[] array, int left, int right)
	{
		int i = left, j = right;
		double temp;
		Object dataTemp;
		double pivot = array[(left + right) / 2];

		/* partition */
		while(i <= j)
		{
			while (array[i] < pivot)
				i++;
			while (array[j] > pivot)
				j--;

			if (i <= j)
			{
				temp = array[i];
				array[i] = array[j];
				array[j] = temp;

				if(data != null)
				{
					dataTemp = data[i];
					data[i] = data[j];
					data[j] = dataTemp;
				}

				i++;
				j--;
			}
		}

		/* recursion */
		if(left < j)
			quickSort(data, array, left, j);

		if(i < right)
			quickSort(data, array, i, right);
	}
}
