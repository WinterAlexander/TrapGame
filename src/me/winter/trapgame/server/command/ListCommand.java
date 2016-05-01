package me.winter.trapgame.server.command;

import me.winter.trapgame.server.CommandManager;
import me.winter.trapgame.server.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Server command listing all the commands of a CommandManager to the sender
 *
 * Created by Alexander Winter on 2016-04-06.
 */
public class ListCommand implements Command
{
	private CommandManager commandManager;

	public ListCommand(CommandManager commandManager)
	{
		this.commandManager = commandManager;
	}

	@Override
	public String getName()
	{
		return "list";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("listcmd", "listcmds", "listcommands", "lscmd", "ls");
	}

	@Override
	public String getDescription()
	{
		return "Lists all the commands of the server.";
	}

	@Override
	public String getUsage()
	{
		return "/list";
	}

	@Override
	public void execute(CommandSender sender, String label, String[] arguments)
	{
		List<Command> cmds = commandManager.getCommands();

		if(cmds.size() <= 0)
		{
			sender.sendMessage("No commands could be found.");
			return;
		}

		sender.sendMessage("There's " + cmds.size() + " commands on this server:");
		for(Command cmd : cmds)
			sender.sendMessage(cmd.getName() + " : " + cmd.getDescription());
	}

	@Override
	public boolean needSuper()
	{
		return false;
	}
}
