package me.winter.trapgame.util;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * <p>Just like the java.util.Random class but with additionnal methods</p>
 *
 * <p>Created by winter on 20/04/16.</p>
 */
public class BetterRandom extends Random
{
	public Color nextColor()
	{
		return nextColor(false);
	}

	public Color nextColor(boolean alpha)
	{
		return new Color(nextInt(256), nextInt(256), nextInt(256), alpha ? nextInt(256) : 255);
	}

	public <E> E nextObject(Collection<E> collection)
	{
		Iterator<E> iterator = collection.iterator();

		for(int i = 0; i < nextInt(collection.size()); i++)
			iterator.next();

		return iterator.next();
	}

	public <E> E nextObject(E[] array)
	{
		return array[nextInt(array.length)];
	}
}
