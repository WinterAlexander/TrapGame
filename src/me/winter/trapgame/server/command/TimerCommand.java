package me.winter.trapgame.server.command;

import me.winter.trapgame.server.CommandSender;

import java.util.Arrays;
import java.util.List;

/**
 * Server command used to change the timer of the Waiting State
 *
 * Created by 1541869 on 2016-04-05.
 */
public class TimerCommand implements Command
{
	@Override
	public String getName()
	{
		return "timer";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("settimer", "countdown");
	}

	@Override
	public String getDescription()
	{
		return "Sets the timer of the current waiting state.";
	}

	@Override
	public String getUsage()
	{
		return "/timer <seconds>";
	}

	@Override
	public void execute(CommandSender sender, String label, String[] arguments)
	{
		int timer;

		try
		{
			timer = Integer.parseInt(arguments[0]);
		}
		catch(NumberFormatException ex)
		{
			return;
		}

		sender.getServer().setWaitingTimer(timer);
		sender.sendMessage("The timer for the next waiting states has been set.");
	}

	@Override
	public boolean needSuper()
	{
		return true;
	}
}
