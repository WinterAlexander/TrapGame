package me.winter.trapgame.shared.packet;

import java.io.*;

/**
 * Represents a packet sent from server to client
 * Informs a player that another player left the game
 *
 * Created by winter on 25/03/16.
 */
public class PacketOutLeave extends Packet
{
	private int playerId;

	public PacketOutLeave()
	{

	}

	public PacketOutLeave(int playerId)
	{
		this.playerId = playerId;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		DataInputStream dataStream = new DataInputStream(stream);
		setPlayerId(dataStream.readShort());
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);
		dataStream.writeShort((short)getPlayerId());
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}
}
