package me.winter.trapgame.shared.packet;

import me.winter.trapgame.server.Player;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a packet of data sent to spectator clients from server
 *
 * Used to show new players the current play board
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class PacketOutSpectator extends Packet
{
	private Map<Point, Integer> boardContent;

	public PacketOutSpectator()
	{

	}

	public PacketOutSpectator(Map<Point, Player> boardContent)
	{
		this.boardContent = new HashMap<>();

		if(boardContent != null)
			for(Map.Entry<Point, Player> entry : boardContent.entrySet())
				this.boardContent.put(entry.getKey(), entry.getValue().getId());

	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		try
		{
			ObjectInputStream objectStream = new ObjectInputStream(stream);
			setBoardContent((Map<Point, Integer>)objectStream.readObject());
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
		objectStream.writeObject(getBoardContent());
	}

	public Map<Point, Integer> getBoardContent()
	{
		return boardContent;
	}

	public void setBoardContent(Map<Point, Integer> boardContent)
	{
		this.boardContent = boardContent;
	}
}
