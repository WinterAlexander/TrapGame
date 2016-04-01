package me.winter.trapgame.shared.packet;

import me.winter.trapgame.shared.PlayerInfo;

import java.io.*;

/**
 * Represents a packet sent from server to client
 * Informs the user(s) that a client has join
 *
 * Created by winter on 25/03/16.
 */
public class PacketOutJoin extends Packet
{
	private PlayerInfo player;

	public PacketOutJoin()
	{

	}

	public PacketOutJoin(PlayerInfo player)
	{
		this.player = player;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		try
		{
			ObjectInputStream objectStream = new ObjectInputStream(stream);
			setPlayer((PlayerInfo)objectStream.readObject());
		}
		catch(ClassNotFoundException | ClassCastException | NoClassDefFoundError ex)
		{
			throw new IOException("Invalid packet content", ex);
		}
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		ObjectOutputStream objectStream = new ObjectOutputStream(stream);
		objectStream.writeObject(getPlayer());
	}

	public PlayerInfo getPlayer()
	{
		return player;
	}

	public void setPlayer(PlayerInfo player)
	{
		this.player = player;
	}
}
