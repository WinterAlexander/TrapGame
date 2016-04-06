package me.winter.trapgame.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * Created by 1541869 on 2016-04-05.
 */
public class ConsoleSender implements CommandSender
{
	public ServerConsole console;

	public ConsoleSender(ServerConsole console)
	{
		this.console = console;
	}

	@Override
	public String getName()
	{
		return "Console";
	}

	@Override
	public void chat(String message)
	{
		console.getServer().broadcast(getName() + ": " + message);
	}

	@Override
	public void sendMessage(String message)
	{
		System.out.println(message);
	}

	@Override
	public TrapGameServer getServer()
	{
		return console.getServer();
	}

	@Override
	public InetAddress getIpAddress()
	{
		try
		{
			return InetAddress.getLocalHost();
		}
		catch(UnknownHostException ex)
		{
			ex.printStackTrace(System.err);
			return null;
		}
	}

	public ServerConsole getConsole()
	{
		return console;
	}

	@Override
	public boolean isSuperUser()
	{
		return true;
	}
}
