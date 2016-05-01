package me.winter.trapgame.server.command;

import me.winter.trapgame.server.CommandSender;
import me.winter.trapgame.server.Player;

import java.util.Arrays;
import java.util.List;

/**
 * A command to change the board size
 *
 * Created by Alexander Winter on 2016-03-28.
 */
public class BoardSizeCommand implements Command
{
	@Override
	public String getName()
	{
		return "boardsize";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("setboardsize", "changeboardsize");
	}

	@Override
	public String getDescription()
	{
		return "Changes the board size to the specified(s) value(s)";
	}

	@Override
	public String getUsage()
	{
		return "/boardsize width [height]";
	}

	@Override
	public void execute(CommandSender player, String label, String[] arguments)
	{
		if(arguments.length == 0 || arguments.length > 2)
		{
			player.sendMessage("Invalid usage: " + getUsage());
			return;
		}

		try
		{
			int width = Integer.parseInt(arguments[0]);
			int height = arguments.length == 2 ? Integer.parseInt(arguments[1]) : width;

			player.getServer().setBoardSize(width, height);
		}
		catch(NumberFormatException ex)
		{
			player.sendMessage("Invalid argument(s).");
		}
	}

	@Override
	public boolean needSuper()
	{
		return true;
	}
}
