package me.winter.trapgame.server;

import me.winter.trapgame.server.command.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the commands of the server
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class CommandManager
{
	private TrapGameServer server;
	private List<Command> commands;

	public CommandManager(TrapGameServer server)
	{
		this.server = server;
		commands = new ArrayList<>();
		commands.add(new HelpCommand());
		commands.add(new SkipCommand());
		commands.add(new BoardSizeCommand());
		commands.add(new StopCommand());
	}

	public void execute(Player player, String input)
	{
		String label = null;
		List<String> argsList = new ArrayList<>();

		while(Character.isWhitespace(input.charAt(0)))
		{
			input = input.substring(1);
			if(input.length() == 0)
				return;
		}

		if(!input.startsWith("/"))
		{
			player.chat(input);
			return;
		}

		input = input.substring(1);

		for(String part : input.split(" "))
		{
			if(part.length() == 0)
				continue;

			if(label == null)
				label = part;
			else
				argsList.add(part);
		}

		Command command = findCommand(label);

		if(command == null)
		{
			player.sendMessage("Command \"" + label + "\" not found.");
			return;
		}

		command.execute(player, label, argsList.toArray(new String[argsList.size()]));
	}

	public Command findCommand(String label)
	{
		for(Command current : getCommands())
		{
			if(current.getName().equalsIgnoreCase(label))
			{
				return current;
			}
		}

		for(Command current : getCommands())
		{
			for(String alias : current.getAliases())
			{
				if(alias.equalsIgnoreCase(label))
				{
					return current;
				}
			}
		}

		return null;
	}

	public TrapGameServer getServer()
	{
		return server;
	}

	public List<Command> getCommands()
	{
		return commands;
	}
}
