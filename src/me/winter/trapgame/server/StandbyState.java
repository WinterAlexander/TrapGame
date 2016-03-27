package me.winter.trapgame.server;

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
		player.sendMessage("There's not enough players to start the game. ");
	}

	@Override
	public void leave(Player player)
	{

	}

	@Override
	public void start()
	{

	}
}
