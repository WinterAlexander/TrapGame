package me.winter.trapgame.shared;


public class Task implements Runnable
{
	private long delay;
	private long lastWork;
	private boolean repeating;
	private Scheduler scheduler;
	private Runnable runnable;

	public Task(long delay, boolean repeating)
	{
		this.delay = delay;
		this.lastWork = System.nanoTime() / 1_000_000;
		this.repeating = repeating;
		this.scheduler = null;
		this.runnable = null;
	}

	public Task(long delay, boolean repeating, Runnable runnable)
	{
		this.delay = delay;
		this.lastWork = System.nanoTime() / 1_000_000;
		this.repeating = repeating;
		this.scheduler = null;
		this.runnable = runnable;
	}
	
	public long getDelay() 
	{
		return this.delay;
	}
	
	public long getLastWork() 
	{
		return lastWork;
	}
	
	public void setLastWork(long lastWork) 
	{
		this.lastWork = lastWork;
	}

	public boolean isRepeating()
	{
		return repeating;
	}

	public void cancel()
	{
		register(null);
	}

	public void register(Scheduler scheduler)
	{
		if(this.scheduler != null)
			this.scheduler.cancelTask(this);

		this.scheduler = scheduler;
	}

	public Scheduler getScheduler()
	{
		return scheduler;
	}

	public boolean isRunning()
	{
		return scheduler != null && scheduler.getTasks().contains(this) && !scheduler.isPause();
	}

	@Override
	public void run()
	{
		runnable.run();
	}

	@Override
	public boolean equals(Object that)
	{
		return super.equals(that) || (runnable != null && runnable.equals(that));
	}
}
