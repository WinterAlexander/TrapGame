package me.winter.trapgame.shared.packet;

import java.awt.*;
import java.io.*;

/**
 * A packet of data sent from server to client
 * Used to inform a client that a board fill has been
 * executed.
 *
 * Created by Alexander Winter on 2016-04-02.
 */
public class PacketOutFill extends Packet
{
	private int playerId;
	private Point location;

	public PacketOutFill()
	{

	}

	public PacketOutFill(int playerId, Point location)
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
