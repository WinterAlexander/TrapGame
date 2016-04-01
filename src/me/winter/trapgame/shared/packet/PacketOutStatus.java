package me.winter.trapgame.shared.packet;

import java.io.*;

/**
 * Packet sent from server to client
 * Used to inform the players the game is starting
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class PacketOutStatus extends Packet
{
	public static final int GAME_START = 0;
	public static final int GAME_STOP = 1;

	private int status;

	public PacketOutStatus()
	{

	}

	public PacketOutStatus(int status)
	{
		this.status = status;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		DataInputStream dataStream = new DataInputStream(stream);
		setStatus(dataStream.readShort());
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);
		dataStream.writeShort((short)getStatus());
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}
}
