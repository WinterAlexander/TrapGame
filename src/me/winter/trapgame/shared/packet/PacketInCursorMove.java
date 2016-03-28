package me.winter.trapgame.shared.packet;

import java.awt.geom.Point2D;

/**
 * From client to server
 * Tells the server the client moved it's cursor
 *
 * Created by Alexander Winter on 2016-03-28.
 */
public class PacketInCursorMove extends Packet
{
	private Point2D.Double cursor;

	public PacketInCursorMove(Point2D.Double cursor)
	{
		this.cursor = cursor;
	}

	public Point2D.Double getCursor()
	{
		return cursor;
	}

	public void setCursor(Point2D.Double cursor)
	{
		this.cursor = cursor;
	}
}
