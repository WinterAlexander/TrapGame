package me.winter.trapgame.client.board;

import java.awt.Point;

/**
 * Represents an animation for a fail while playing.
 * Keeps the needed informations
 *
 * Created by 1541869 on 2016-10-06.
 */
public class FailAnimation
{
	private Point location;
	private long start; //nanoseconds
	private int length; //milliseconds

	public FailAnimation(Point location, long start)
	{
		this.location = location;
		this.start = start;
		this.length = 500;
	}

	public int getOpacity()
	{
		if(finished())
			return 0;
		return 255;
	}

	public boolean finished()
	{
		return System.nanoTime() - start > length * 1_000_000;
	}

	public Point getLocation()
	{
		return location;
	}

	public int getLength()
	{
		return length;
	}
}
