package me.winter.trapgame.shared.packet;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.PlayerStats;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A packet of data sent from server to clients to update player stats
 *
 * Created by 1541869 on 2016-04-08.
 */
public class PacketOutUpdateStats extends Packet
{
	private Map<Integer, PlayerStats> stats;

	public PacketOutUpdateStats()
	{

	}

	public PacketOutUpdateStats(Map<Integer, PlayerStats> stats)
	{
		this.stats = stats;
	}

	public PacketOutUpdateStats(List<PlayerInfo> players)
	{
		this.stats = new HashMap<>();

		for(PlayerInfo player : players)
			stats.put(player.getPlayerId(), player.getStats());
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		try
		{
			stats = (Map<Integer, PlayerStats>)new ObjectInputStream(stream).readObject();
		}
		catch(ClassNotFoundException | ClassCastException | NoClassDefFoundError ex)
		{
			throw new IOException("Invalid packet content", ex);
		}
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		new ObjectOutputStream(stream).writeObject(stats);
	}

	public Map<Integer, PlayerStats> getStats()
	{
		return stats;
	}

	public void setStats(Map<Integer, PlayerStats> stats)
	{
		this.stats = stats;
	}
}
