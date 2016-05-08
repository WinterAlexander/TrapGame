package me.winter.trapgame.server.command;

import me.winter.trapgame.server.CommandSender;
import me.winter.trapgame.server.Player;
import me.winter.trapgame.server.state.GameState;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * A command only used to debug. It shouldn't be included in the final version
 *
 * Created by Alexander Winter on 2016-05-01.
 */
public class DebugCommand implements Command
{
	@Override
	public String getName()
	{
		return "debug";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("dbg", "/");
	}

	@Override
	public String getDescription()
	{
		return "A command only used to debug. It shouldn't be included in the final version";
	}

	@Override
	public String getUsage()
	{
		return ":/";
	}

	@Override
	public void execute(CommandSender sender, String label, String[] arguments)
	{
		if(arguments.length == 0)
		{
			sender.sendMessage(getUsage());
			return;
		}

		if(arguments[0].equalsIgnoreCase("silentplace"))
		{
			if(!(sender.getServer().getState() instanceof GameState))
				return;

			((GameState)sender.getServer().getState()).place((Player)sender, new Point(Integer.parseInt(arguments[1]), Integer.parseInt(arguments[2])));
			sender.sendMessage("Success");
			return;

		}

		if(arguments[0].equalsIgnoreCase("breakpoint"))
		{
			System.out.println("breakpoint");
			//insert breakpoint above
			return;
		}
	}

	@Override
	public boolean needSuper()
	{
		return true;
	}
}
