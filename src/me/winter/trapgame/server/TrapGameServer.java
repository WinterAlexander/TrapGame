package me.winter.trapgame.server;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a server of TrapGame
 * Has a ServerConnection, a list of players and the state of the game
 * This class also has a static main() method to launch the game
 *
 * Created by winter on 25/03/16.
 */
public class TrapGameServer
{
	/**
	 * Starts the server with a new instance of TrapGameServer
	 *
	 * @param args Exec string arguments
	 */
	public static void main(String[] args)
	{
		try
		{
			new TrapGameServer(2, 8);
		}
		catch(Throwable ex)
		{
			System.err.println("A fatal error occured and stopped the server. Stack Trace:");
			ex.printStackTrace(System.err);
		}
	}

	private static final Color[] COLORS = new Color[]{Color.RED, Color.CYAN, Color.YELLOW, Color.GREEN, Color.PINK, Color.ORANGE, Color.BLUE, Color.BLACK};

	private State state;
	private List<Player> players;
	private ServerConnection connection;
	private StatsManager statsManager;

	private int minPlayers, maxPlayers;

	public TrapGameServer(int minPlayers, int maxPlayers)
	{
		state = null;//new StandbyState();
		players = new ArrayList<>();
		connection = new ServerConnection(this, 1254);
		statsManager = new StatsManager(this, new File("stats"));
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
	}

	public void join(Player player)
	{
		getPlayers().add(player);
		broadcast(player + " has joined the game.");
		getState().join(player);
	}

	public void leave(Player player)
	{
		getPlayers().remove(player);
		broadcast(player + " has left the game.");
		getState().leave(player);
	}

	public boolean isAvailable(String playerName)
	{
		for(Player player : getPlayers())
			if(player.getName().equalsIgnoreCase(playerName))
				return false;
		return true;
	}

	public int generateNewPlayerId()
	{
		int id = 0;

		while(!isAvailable(id))
			id++;

		return id;
	}

	private boolean isAvailable(int playerId)
	{
		for(Player player : getPlayers())
			if(player.getId() == playerId)
				return false;
		return true;
	}

	public Color getColor(int id)
	{
		if(id < COLORS.length)
			return COLORS[id];

		return new Color(COLORS[id].getRed() + id, COLORS[id].getGreen() + id, COLORS[id].getBlue());
	}

	public void broadcast(String message)
	{
		getPlayers().forEach(player -> player.sendMessage(message));
	}

	public State getState()
	{
		return state;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public ServerConnection getConnection()
	{
		return connection;
	}

	public StatsManager getStatsManager()
	{
		return statsManager;
	}

	public int getMinPlayers()
	{
		return minPlayers;
	}

	public void setMinPlayers(int minPlayers)
	{
		if(minPlayers < 0 || minPlayers > maxPlayers)
			throw new IllegalArgumentException("min should be > 0 and < max");

		this.minPlayers = minPlayers;
	}

	public int getMaxPlayers()
	{
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers)
	{
		if(maxPlayers < 0 || maxPlayers < minPlayers)
			throw new IllegalArgumentException("max should be > 0 and > min");

		this.maxPlayers = maxPlayers;
	}
}
