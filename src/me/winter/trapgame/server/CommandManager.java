package me.winter.trapgame.server;

import me.winter.trapgame.server.command.*;
import me.winter.trapgame.util.StringUtil;

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
		commands.add(new PropTemplateCommand());
		commands.add(new StatsCommand());
		commands.add(new TimerCommand());
		commands.add(new ListCommand(this));
		commands.add(new SuperUserCommand());
		commands.add(new KickCommand());
		commands.add(new IpCommand());

		if(server.isDebugMode())
			commands.add(new DebugCommand());
	}

	public void execute(CommandSender sender, String input)
	{
		input = StringUtil.htmlSpecialChars(input);

		String originalInput = input;
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
			sender.chat(input);
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
			sender.sendMessage("Command \"" + label + "\" not found.");
			return;
		}

		if(command.needSuper() && !sender.isSuperUser())
		{
			sender.sendMessage("You need to be a super user in order to execute this command.");
			return;
		}

		server.getLogger().info(sender.getName() + " executed command: " + originalInput);
		command.execute(sender, label, argsList.toArray(new String[argsList.size()]));
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
