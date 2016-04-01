package me.winter.trapgame.shared.packet;

import me.winter.trapgame.shared.PlayerInfo;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Packet sent from server to client
 * Used then a client join to confirm his connection,
 * and to give him the list of already connected
 * players
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class PacketOutWelcome extends Packet
{
	private int playerId;
	private List<PlayerInfo> players;

	public PacketOutWelcome()
	{

	}

	public PacketOutWelcome(int playerId, List<PlayerInfo> players)
	{
		this.playerId = playerId;
		this.players = players;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		try
		{
			ObjectInputStream objectStream = new ObjectInputStream(stream);
			setPlayerId(objectStream.readShort());
			setPlayers((List<PlayerInfo>)objectStream.readObject());
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
		objectStream.writeShort((short)getPlayerId());
		objectStream.writeObject(getPlayers());
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	public List<PlayerInfo> getPlayers()
	{
		return players;
	}

	public void setPlayers(List<PlayerInfo> players)
	{
		this.players = players;
	}
}
