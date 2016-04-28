package me.winter.trapgame.server;

import me.winter.trapgame.shared.Task;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;

/**
 *
 * Created by 1541869 on 2016-04-04.
 */
public class ServerConsole
{
	private TrapGameServer server;
	private ConsoleSender consoleSender;

	public ServerConsole(TrapGameServer server)
	{
		this.server = server;
		consoleSender = new ConsoleSender(this);

		new Thread(this::start).start();
	}

	public synchronized void start()
	{
		Scanner scanner = new Scanner(System.in);

		while(!server.isStopped())
		{
			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException ex)
			{
				ex.printStackTrace(System.err);
			}

			try
			{
				while(System.in.available() != 0)
				{
					String line = scanner.nextLine();

					server.getScheduler().addTask(new Task(0, false,
							() -> server.getCommandManager().execute(consoleSender, line)));
				}
			}
			catch(IOException ex)
			{
				server.getLogger().log(Level.SEVERE, "An internal exception occurred while listening to system input", ex);
			}

		}

		scanner.close();
	}

	public TrapGameServer getServer()
	{
		return server;
	}

	public ConsoleSender getConsoleSender()
	{
		return consoleSender;
	}
}
