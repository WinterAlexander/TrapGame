package me.winter.trapgame.server;

import me.winter.trapgame.util.StringUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

/**
 * Represents the console sender of the server, the player having access to the command-line of the local machine
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
		console.getServer().getLogger().info("[CHAT] " + StringUtil.noHTML(message));
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
			console.getServer().getLogger().log(Level.SEVERE, "Unexpected exception while trying to get local ip", ex);
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
