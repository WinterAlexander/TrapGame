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
	 * @param args
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
}
