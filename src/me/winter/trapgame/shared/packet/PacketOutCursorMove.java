package me.winter.trapgame.shared.packet;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * From server to client
 * Informs other players that this player has moved his cursor
 *
 * Created by Alexander Winter on 2016-03-28.
 */
public class PacketOutCursorMove extends Packet
{
	private int playerId;
	private Point2D.Double cursor;

	public PacketOutCursorMove(int playerId, Point2D.Double cursor)
	{
		this.playerId = playerId;
		this.cursor = cursor;
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
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
