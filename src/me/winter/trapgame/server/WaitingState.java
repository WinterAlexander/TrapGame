package me.winter.trapgame.server;

import me.winter.trapgame.shared.Task;

/**
 * Represents the state of the game when there's enough players to start but we let a timer run to to let others join
 *
 * Created by Alexander Winter on 2016-03-26.
 */
public class WaitingState extends State
{
	private Task task;
	private int timer;

	public WaitingState(TrapGameServer server)
	{
		super(server);
		timer = server.getWaitingTimer();
		task = new Task(1000, true, this::tick);
	}

	@Override
	public void join(Player player)
	{

	}

	@Override
	public void leave(Player player)
	{
		if(getServer().getPlayers().size() < getServer().getMinPlayers())
		{
			getServer().getScheduler().cancel(task);
			getServer().setState(new StandbyState(getServer()));
			getServer().getState().start();
		}
	}

	@Override
	public void start()
	{
		getServer().getScheduler().addTask(task);
		getServer().broadcast("There's now enough players, the game will start in " + timer + " second" + (timer > 1 ? "s" : "") + ".");
	}

	@Override
	public void skip()
	{
		getServer().getScheduler().cancel(task);
		getServer().setState(new GameState(getServer()));
		getServer().getState().start();
	}

	private void tick()
	{
		switch(--timer)
		{
			case 25:
			case 20:
			case 15:
			case 10:
			case 5:
			case 4:
			case 3:
			case 2:
			case 1:
				getServer().broadcast("The game is starting in " + timer + " second" + (timer > 1 ? "s" : "") + ".");
				return;

			case 0:
				skip();
		}
	}

	public int getTimer()
	{
		return timer;
	}

	public void setTimer(int timer)
	{
		this.timer = timer;
	}
}
