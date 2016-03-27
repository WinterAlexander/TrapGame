package me.winter.trapgame.server.command;

import me.winter.trapgame.server.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Command used to skip the waiting state timer or to finish a game
 * Temporary available to all players, a permission system will be
 * placed one day
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class SkipCommand implements Command
{
	@Override
	public String getName()
	{
		return "skip";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("pass", "start");
	}

	@Override
	public String getDescription()
	{
		return "Skips the current state of the game to the next one";
	}

	@Override
	public String getUsage()
	{
		return "/skip";
	}

	@Override
	public void execute(Player player, String label, String[] arguments)
	{
		player.getServer().getState().skip();
		player.sendMessage("The state has been skipped.");
	}
}
