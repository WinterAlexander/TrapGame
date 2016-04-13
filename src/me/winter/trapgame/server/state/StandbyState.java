package me.winter.trapgame.server.state;

import me.winter.trapgame.server.Player;
import me.winter.trapgame.server.TrapGameServer;

import java.awt.*;

/**
 * Represents a state of the game when there's not enough players to play
 *
 * Created by Alexander Winter on 2016-03-26.
 */
public class StandbyState extends State
{
	public StandbyState(TrapGameServer server)
	{
		super(server);
	}

	@Override
	public void join(Player player)
	{
		if(getServer().getPlayers().size() >= getServer().getMinPlayers())
		{
			skip();
			return;
		}

		int amount = getServer().getMinPlayers() - getServer().getPlayers().size();

		getServer().broadcast(Color.gray, "Need " + amount + " more player" + (amount > 1 ? "1" : "") + " to start.");
	}

	@Override
	public void leave(Player player)
	{

	}

	@Override
	public void start()
	{
		getServer().broadcast(Color.gray, "Sorry, there's not enough players. Please wait.");
	}

	@Override
	public void skip()
	{
		getServer().setState(new WaitingState(getServer()));
		getServer().getState().start();
	}
}
