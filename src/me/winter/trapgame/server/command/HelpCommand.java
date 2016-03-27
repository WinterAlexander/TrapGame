package me.winter.trapgame.server.command;

import me.winter.trapgame.server.Player;
import me.winter.trapgame.util.StringUtil;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by 1541869 on 2016-02-19.
 */
public class HelpCommand implements Command
{
	@Override
	public String getName()
	{
		return "help";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("aide", "helpme", "sos", "?", "lscmd", "listcommands");
	}

	@Override
	public String getDescription()
	{
		return "Gets the description and usage of the given command";
	}

	@Override
	public String getUsage()
	{
		return "/help [command]";
	}

	@Override
	public void execute(Player player, String label, String[] arguments)
	{
		if(arguments.length == 0)
			arguments = new String[]{"help"};

		Command command = player.getServer().getCommandManager().findCommand(arguments[0]);

		if(command == null)
		{
			player.sendMessage("Command \"" + arguments[0] + "\" not found.");
			return;
		}


		player.sendMessage(command.getName() + ":");
		player.sendMessage(command.getDescription());
		player.sendMessage("Usage: " + command.getUsage());
		player.sendMessage("Aliases: " + StringUtil.join(command.getAliases()));
	}
}
