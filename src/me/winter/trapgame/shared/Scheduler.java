package me.winter.trapgame.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scheduler 
{
	private Optional<Logger> logger;

	private List<Task> tasks;
	private long pauseTime;
	private long lastPause;
	private long lastPauseTime;
	private boolean pause;

	public Scheduler()
	{
		this(null);
	}

	public Scheduler(Logger logger)
	{
		this.logger = Optional.ofNullable(logger);
		this.tasks = new ArrayList<>();
		this.pause = true;
		this.pauseTime = 0;
		this.lastPause = getTimeMillis();
		this.lastPauseTime = 0;
	}

	public void addTask(Runnable runnable, int delay)
	{
		addTask(runnable, delay, false);
	}

	public void addTask(Runnable runnable, int delay, boolean repeat)
	{
		addTask(new Task(delay, repeat, runnable));
	}
	
	public synchronized void addTask(Task task)
	{
		this.tasks.add(task);
		task.register(this);
		notify();
	}
	
	public void addTasks(Collection<Task> tasks)
	{
		for(Task task : tasks)
			this.addTask(task);
	}
	
	public void cancelTask(Task task)
	{
		this.tasks.remove(task);
	}

	public void cancelTasks(Class<? extends Task> type)
	{
		cancelIf(type::isInstance);
	}

	public void cancelIf(Predicate<Task> filter)
	{
		this.tasks.removeIf(filter);
	}

	public void cancelAll()
	{
		this.tasks.clear();
	}
	
	public void update()
	{
		if(this.pause)
			return;

		for(Task task : new ArrayList<>(this.tasks))
		{
			try
			{
				if(task == null)
					continue;

				if(task.getScheduler() != this)
				{
					this.tasks.remove(task);
					continue;
				}

				if(task.getDelay() == 0)
				{
					task.run();
					if(!task.isRepeating())
						cancelTask(task);
					continue;
				}

				int turns = (int) ((getTimeMillis() - task.getLastWork()) / task.getDelay());
				if(!task.isRepeating() && turns >= 1)
				{
					task.run();
					cancelTask(task);
					continue;
				}

				for(int i = 0; i < turns; i++)
					task.run();

				task.setLastWork(task.getLastWork() + task.getDelay() * turns);
			}
			catch(Exception ex)
			{
				cancelTask(task);
				logger.ifPresent(logger -> logger.log(Level.SEVERE, "Error in scheduler with task " + task.toString(), ex));
			}
		}

	}

	public List<Task> getTasks()
	{
		return tasks;
	}
	
	public void pause()
	{
		if(!this.pause)
		{
			this.pause = true;
			this.lastPause = System.nanoTime() / 1_000_000;
		}
	}
	
	public void start()
	{
		this.pause = false;
		this.pauseTime += System.nanoTime() / 1_000_000 - this.lastPause;
		this.lastPauseTime = System.nanoTime() / 1_000_000 - this.lastPause;
	}

	public boolean isPause()
	{
		return this.pause;
	}

	public long getLastPauseTime()
	{
		return lastPauseTime;
	}
	
	public long getLastPause()
	{
		return lastPause;
	}
	
	public long getTimeMillis()
	{
		return System.nanoTime() / 1_000_000 - this.pauseTime;
	}

	public long getWaitingDelay()
	{
		long minDelay = Long.MAX_VALUE;

		for(Task task : new ArrayList<>(getTasks()))
		{
			long delay = task.getLastWork() + task.getDelay() - getTimeMillis();
			if(delay < minDelay)
				minDelay = delay;
		}

		return minDelay;
	}
}
