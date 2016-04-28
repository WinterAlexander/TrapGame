package me.winter.trapgame.server.command;

import me.winter.trapgame.server.CommandSender;
import me.winter.trapgame.util.StringUtil;

import java.util.Arrays;
import java.util.List;

/**
 * <p>A command used to change the welcome message of the server</p>
 *
 * <p>Created by 1541869 on 2016-04-25.</p>
 */
public class WelcomeMessageCommand implements Command
{
	@Override
	public String getName()
	{
		return "welcomemessage";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("welcomemsg", "motd", "messageoftheday", "wmessage");
	}

	@Override
	public String getDescription()
	{
		return "A command used to change the welcome message of the server";
	}

	@Override
	public String getUsage()
	{
		return "/welcomemessage <message [...]>";
	}

	@Override
	public void execute(CommandSender sender, String label, String[] arguments)
	{
		String message = StringUtil.join(arguments, " ");

		sender.getServer().setWelcomeMessage(message);

		sender.sendMessage("The TrapGame server's welcome have been changed to: " + message);
	}

	@Override
	public boolean needSuper()
	{
		return true;
	}
}
