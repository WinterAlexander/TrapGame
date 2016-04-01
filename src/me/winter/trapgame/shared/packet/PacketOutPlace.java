package me.winter.trapgame.shared.packet;

import java.awt.*;
import java.io.*;

/**
 * Represents a packet sent from server to client
 * Used to inform player that a place has click on a spot on (x,y) location
 * The client should update his game board and finish the game if needed.
 *
 * Created by winter on 25/03/16.
 */
public class PacketOutPlace extends Packet
{
	private int playerId;
	private Point location;

	public PacketOutPlace()
	{

	}

	public PacketOutPlace(int playerId, Point location)
	{
		this.playerId = playerId;
		this.location = location;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		DataInputStream dataStream = new DataInputStream(stream);
		setPlayerId(dataStream.readShort());
		setLocation(new Point(dataStream.readShort(), dataStream.readShort()));
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);
		dataStream.writeShort((short)getPlayerId());
		dataStream.writeShort((short)getLocation().x);
		dataStream.writeShort((short)getLocation().y);
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
