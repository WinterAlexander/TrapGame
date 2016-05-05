package me.winter.trapgame.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a scheduler executing tasks at a defined time
 *
 * @see Task
 */
public class Scheduler 
{
	private Optional<Logger> logger;

	private List<Task> tasks;
	private long pauseLength, lastPause, lastPauseLength;
	private boolean stop, updating;

	/**
	 * Creating a new scheduler stopped by default without logging
	 *
	 */
	public Scheduler()
	{
		this(null);
	}

	/**
	 * Creating a new scheduler stopped by default with a logger
	 *
	 */
	public Scheduler(Logger logger)
	{
		this.logger = Optional.ofNullable(logger);
		this.tasks = new ArrayList<>();
		this.stop = true;
		this.lastPauseLength = System.nanoTime() / 1_000_000;
		this.pauseLength = lastPauseLength;
		this.lastPause = getTimeMillis();
	}

	/**
	 * Mark the scheduler as started. You need to execute the update method
	 * in a loop or call the loop method to actually execute the tasks.
	 * Update won't work if the scheduler isn't started
	 */
	public void start()
	{
		stop = false;
		lastPauseLength = System.nanoTime() / 1_000_000 - lastPause;
		pauseLength += lastPauseLength;
	}

	/**
	 * Stops the scheduler
	 */
	public void stop()
	{
		if(!stop)
		{
			stop = true;
			lastPause = System.nanoTime() / 1_000_000;
		}
	}

	public void loop(BooleanSupplier condition)
	{
		if(!isRunning())
			start();

		while(condition.getAsBoolean())
		{
			long toWait = getWaitingDelay();
			if(toWait > 0)
			{
				try
				{
					if(toWait == Long.MAX_VALUE)
						wait(0);
					else
						wait(toWait);
				}
				catch(InterruptedException ex)
				{
					ex.printStackTrace(System.err);
				}
			}
			update();
		}
	}

	public synchronized void update()
	{
		if(stop)
			return;

		updating = true;

		for(Task task : new ArrayList<>(this.tasks))
		{
			try
			{
				if(task == null)
					continue;

				if(task.getScheduler() != this)
				{
					tasks.remove(task);
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

		updating = false;
	}

	public long getWaitingDelay()
	{
		long minDelay = Long.MAX_VALUE;

		for(Task task : new ArrayList<>(getTasks()))
		{
			if(task == null)
				continue;

			long delay = task.getLastWork() + task.getDelay() - getTimeMillis();
			if(delay < minDelay)
				minDelay = delay;
		}

		return minDelay;
	}

	public long getTimeMillis()
	{
		return System.nanoTime() / 1_000_000 - this.pauseLength;
	}

	public void addTask(Runnable runnable, int delay)
	{
		addTask(runnable, delay, false);
	}

	public void addTask(Runnable runnable, int delay, boolean repeat)
	{
		addTask(new Task(delay, repeat, runnable));
	}

	public void addTask(Task task)
	{
		task.register(this);
		tasks.add(task);

		if(!updating)
			synchronized(this)
			{
				notify();
			}
	}

	public void addTasks(Collection<Task> tasks)
	{
		for(Task task : tasks)
			addTask(task);
	}

	public void cancelTask(Task task)
	{
		tasks.remove(task);
	}

	public void cancelTasks(Class<? extends Task> type)
	{
		cancelIf(type::isInstance);
	}

	public void cancelIf(Predicate<Task> filter)
	{
		tasks.removeIf(filter);
	}

	public void cancelAll()
	{
		tasks.clear();
	}

	public List<Task> getTasks()
	{
		return tasks;
	}

	public boolean isRunning()
	{
		return !stop;
	}

	public boolean isUpdating()
	{
		return updating;
	}

	public long getLastPauseLength()
	{
		return lastPauseLength;
	}
	
	public long getLastPause()
	{
		return lastPause;
	}
}
