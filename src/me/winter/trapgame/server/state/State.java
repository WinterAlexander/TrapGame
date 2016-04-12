package me.winter.trapgame.server.state;

import me.winter.trapgame.server.Player;
import me.winter.trapgame.server.TrapGameServer;

/**
 * Represents a state of the game server
 *
 * Created by winter on 25/03/16.
 */
public abstract class State
{
	private TrapGameServer server;

	public State(TrapGameServer server)
	{
		this.server = server;
	}

	/**
	 * Called when a player joins the game
	 * @param player the player that joins
	 */
	public abstract void join(Player player);

	/**
	 * Called when a player leaves the game or gets kicked, or lost connection
	 * @param player the player that leaves
	 */
	public abstract void leave(Player player);

	/**
	 * Called just after the state has been changed
	 */
	public abstract void start();

	/**
	 * Force to go on the next state
	 */
	public abstract void skip();


	public TrapGameServer getServer()
	{
		return server;
	}
}
