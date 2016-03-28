package me.winter.trapgame.shared;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Represents all the information of a player
 * Made to be passed from client to server and vice versa via packets
 *
 * Created by winter on 25/03/16.
 */
public class PlayerInfo implements Serializable
{
	private int playerId;
	private String name;
	private Color color;
	private PlayerStats stats;
	private Point2D.Double cursor;

	public PlayerInfo(int playerId, String name, Color color, PlayerStats stats, Point2D.Double cursor)
	{
		this.playerId = playerId;
		this.name = name;
		this.color = color;
		this.stats = stats;
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public PlayerStats getStats()
	{
		return stats;
	}

	public void setStats(PlayerStats stats)
	{
		this.stats = stats;
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
