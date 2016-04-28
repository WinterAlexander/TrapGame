package me.winter.trapgame.server.command;

import me.winter.trapgame.server.CommandSender;

import java.util.Arrays;
import java.util.List;

/**
 * Retrieve some information about the server such as the ips (public and lan)
 *
 * Created by Alexander Winter on 2016-04-24.
 */
public class ServerInfo implements Command
{
	@Override
	public String getName()
	{
		return "serverinfo";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("info", "ip");
	}

	@Override
	public String getDescription()
	{
		return "Retrieve some information about the server such as the ips (public and lan)";
	}

	@Override
	public String getUsage()
	{
		return "/serverinfo";
	}

	@Override
	public void execute(CommandSender sender, String label, String[] arguments)
	{

	}

	@Override
	public boolean needSuper()
	{
		return false;
	}
}
