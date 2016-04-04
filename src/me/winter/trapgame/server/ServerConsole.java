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
	private Player consolePlayer;

	public ServerConsole(TrapGameServer server)
	{
		this.server = server;
		try
		{
			consolePlayer = new Player(server, new PlayerInfo(-1, "Console", Color.BLACK, new PlayerStats(99, 0, 0), 0, 0), InetAddress.getLocalHost(), -1)
			{
				public void sendMessage(String message)
				{
					System.out.println(message);
				}

				public void kick(String message){ }
			};
		}
		catch(UnknownHostException ex)
		{
			ex.printStackTrace(System.err);
		}

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
							() -> server.getCommandManager().execute(consolePlayer, scanner.nextLine())));
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace(System.err);
			}

		}

		scanner.close();
	}

	public Player getConsolePlayer()
	{
		return consolePlayer;
	}
}
