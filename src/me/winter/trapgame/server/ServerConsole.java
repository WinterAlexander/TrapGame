package me.winter.trapgame.server;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.PlayerStats;
import me.winter.trapgame.shared.Task;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

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
					server.getScheduler().addTask(new Task(0, false,
							() -> server.getCommandManager().execute(consoleSender, scanner.nextLine())));
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace(System.err);
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
