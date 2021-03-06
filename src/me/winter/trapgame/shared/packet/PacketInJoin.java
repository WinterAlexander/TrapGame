package me.winter.trapgame.shared.packet;

import java.io.*;

/**
 * Represents a packet sent from client to server
 * Used when a player wants to join a game
 * The player should give it's username
 * The player may be kicked (by receiving a PacketOutKick)
 * if the game has already started or if he is banned
 *
 * Created by winter on 25/03/16.
 */
public class PacketInJoin extends Packet
{
	private String playerName;

	public PacketInJoin()
	{

	}

	public PacketInJoin(String playerName)
	{
		this.playerName = playerName;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		DataInputStream dataStream = new DataInputStream(stream);
		setPlayerName(dataStream.readUTF());
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);
		dataStream.writeUTF(getPlayerName());
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}
}
