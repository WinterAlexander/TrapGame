package me.winter.trapgame.shared;


public class Task implements Runnable
{
	private Scheduler scheduler;

	private long delay, lastWork;
	private boolean repeating;
	private Runnable runnable;

	public Task(long delay)
	{
		this(delay, false);
	}

	public Task(long delay, boolean repeating)
	{
		this(delay, repeating, null);
	}

	public Task(long delay, boolean repeating, Runnable runnable)
	{
		this.delay = delay;
		this.lastWork = -1;
		this.repeating = repeating;
		this.scheduler = null;
		this.runnable = runnable;
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

		if(scheduler != null)
			setLastWork(scheduler.getTimeMillis());
		else
			setLastWork(-1);
	}

	public boolean isRunning()
	{
		return scheduler != null && scheduler.getTasks().contains(this) && scheduler.isRunning();
	}

	public boolean isRegistered()
	{
		return scheduler != null;
	}

	@Override
	public void run()
	{
		if(runnable != null)
			runnable.run();
	}

	@Override
	public boolean equals(Object that)
	{
		return super.equals(that) || (runnable != null && runnable.equals(that));
	}

	public Scheduler getScheduler()
	{
		return scheduler;
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
}
