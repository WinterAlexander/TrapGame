package me.winter.trapgame.shared.packet;

import java.awt.*;
import java.io.*;

/**
 * Represents a packet sent from client to server
 * Used when a player clicks on the board to try to capture a spot
 *
 * Created by winter on 25/03/16.
 */
public class PacketInClick extends Packet
{
	private Point location;

	public PacketInClick()
	{

	}

	public PacketInClick(Point location)
	{
		this.location = location;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		DataInputStream dataStream = new DataInputStream(stream);
		setLocation(new Point(dataStream.readShort(), dataStream.readShort()));
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);
		dataStream.writeShort((short)getLocation().x);
		dataStream.writeShort((short)getLocation().y);
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
