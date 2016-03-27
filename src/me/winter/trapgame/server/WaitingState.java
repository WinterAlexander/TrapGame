package me.winter.trapgame.server;

/**
 * Represents the state of the game when there's enough players to start but we let a timer run to to let others join
 *
 * Created by Alexander Winter on 2016-03-26.
 */
public class WaitingState extends State
{
	public WaitingState(TrapGameServer server)
	{
		super(server);
	}

	@Override
	public void join(Player player)
	{

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
