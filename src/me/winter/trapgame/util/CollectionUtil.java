package me.winter.trapgame.util;

import java.util.Collection;

/**
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class CollectionUtil
{
	private CollectionUtil(){}

	public static int occurrences(Object object, Collection collection)
	{
		int count = 0;

		for(Object current : collection)
		{
			if(current.equals(object))
				count++;
		}

		return count;
	}
}
