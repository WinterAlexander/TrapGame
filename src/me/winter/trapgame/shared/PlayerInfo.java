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
	private float cursorX, cursorY;

	public PlayerInfo(int playerId, String name, Color color, PlayerStats stats, float cursorX, float cursorY)
	{
		this.playerId = playerId;
		this.name = name;
		this.color = color;
		this.stats = stats;
		this.cursorX = cursorX;
		this.cursorY = cursorY;
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

	public float getCursorX()
	{
		return cursorX;
	}

	public void setCursorX(float cursorX)
	{
		this.cursorX = cursorX;
	}

	public float getCursorY()
	{
		return cursorY;
	}

	public void setCursorY(float cursorY)
	{
		this.cursorY = cursorY;
	}

	public void setCursor(float x, float y)
	{
		this.cursorX = x;
		this.cursorY = y;
	}
}
