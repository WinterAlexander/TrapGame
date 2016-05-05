package me.winter.trapgame.server.command;

import me.winter.trapgame.server.CommandSender;

import java.util.Arrays;
import java.util.List;

/**
 * A server command used to close the
 *
 * Created by 1541869 on 2016-03-31.
 */
public class StopCommand implements Command
{
	@Override
	public String getName()
	{
		return "stop";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("close", "exit");
	}

	@Override
	public String getDescription()
	{
		return "Stops the server normally by kicking all the players and closing the connection.";
	}

	@Override
	public String getUsage()
	{
		return "/stop";
	}

	@Override
	public void execute(CommandSender player, String label, String[] arguments)
	{
		player.getServer().stop();
	}

	@Override
	public boolean needSuper()
	{
		return true;
	}
}
