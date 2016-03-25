package me.winter.trapgame.shared.packet;

import java.awt.*;

/**
 * Represents a packet sent from client to server
 * Used when a player clicks on the board to try to capture a spot
 *
 * Created by winter on 25/03/16.
 */
public class PacketInClick extends Packet
{
	private Point location;

	public PacketInClick(Point location)
	{
		this.location = location;
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
