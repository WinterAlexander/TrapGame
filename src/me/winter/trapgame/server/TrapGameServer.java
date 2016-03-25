package me.winter.trapgame.server;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
			new TrapGameServer();
		}
		catch(Throwable ex)
		{
			System.err.println("A fatal error occured and stopped the server. Stack Trace:");
			ex.printStackTrace(System.err);
		}
	}

	private State state;
	private List<Player> players;
	private ServerConnection connection;

	public TrapGameServer()
	{

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
}
