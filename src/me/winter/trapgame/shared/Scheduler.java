package me.winter.trapgame.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Scheduler 
{
	private List<Task> tasks;
	private long pauseTime;
	private long lastPause;
	private long lastPauseTime;
	private boolean pause;

	public Scheduler()
	{
		this.tasks = new ArrayList<>();
		this.pause = true;
		this.lastPause = System.nanoTime() / 1_000_000;
		this.lastPauseTime = 0;
		this.pauseTime = 0;
	}
	
	public void addTask(Task task)
	{
		this.tasks.add(task);
		task.register(this);
	}
	
	public void addTasks(Collection<Task> tasks)
	{
		for(Task task : tasks)
			this.addTask(task);
	}
	
	public void cancel(Task task)
	{
		this.tasks.remove(task);
	}
	
	public void cancelAll()
	{
		this.tasks.clear();
	}

	public void cancel(Runnable runnable)
	{
		for(Task task : new ArrayList<>(getTasks()))
			if(task.equals(runnable))
				cancel(task);
	}
	
	public void update()
	{
		if(this.pause)
			return;

		for(Task task : new ArrayList<>(this.tasks))
		{
			try
			{
				if(task.getDelay() == 0)
				{
					task.run();
					if(!task.isRepeating())
						cancel(task);
					continue;
				}

				int turns = (int) (((System.nanoTime() / 1_000_000 - this.pauseTime) - task.getLastWork()) / task.getDelay());
				if(!task.isRepeating() && turns >= 1)
				{
					task.run();
					cancel(task);
					continue;
				}

				for(int i = 0; i < turns; i++)
					task.run();

				task.setLastWork(task.getLastWork() + task.getDelay() * turns);
			}
			catch(Exception ex)
			{
				cancel(task);
				if(task != null)
					System.err.println("Error in scheduler with task " + task.toString());
				ex.printStackTrace(System.err);
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
	
	public long getGameTimeMillis()
	{
		return System.nanoTime() / 1_000_000 - this.pauseTime;
	}
}
