package me.winter.trapgame.shared.packet;

import java.awt.*;

/**
 * Represents a packet sent from server to client
 * Used to inform player that a place has click on a spot on (x,y) location
 * The client should update his game board and finish the game if needed.
 *
 * Created by winter on 25/03/16.
 */
public class PacketOutPlace
{
	private int playerId;
	private Point location;

	public PacketOutPlace(int playerId, Point location)
	{
		this.playerId = playerId;
		this.location = location;
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	public Point getLocation()
	{
		return location;
	}

	public void setLocation(Point location)
	{
		this.location = location;
	}
}
