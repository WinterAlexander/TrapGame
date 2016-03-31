package me.winter.trapgame.server.command;

import me.winter.trapgame.server.Player;

import java.util.Arrays;
import java.util.List;

/**
 *
 *
 * Created by Alexander Winter on 2016-03-31.
 */
public class StatsCommand implements Command
{
	@Override
	public String getName()
	{
		return "stats";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("statistics", "playerstats");
	}

	@Override
	public String getDescription()
	{
		return "Command used to get players stats in chat.";
	}

	@Override
	public String getUsage()
	{
		return "/stats <player>";
	}

	@Override
	public void execute(Player player, String label, String[] args)
	{
		if(args.length == 0)
			args = new String[]{player.getName()};

		Player pPlayer = player.getServer().getPlayer(args[0]);

		if(pPlayer == null)
		{
			player.sendMessage("That player couldn't be found.\n" +
					"Usage: " + getUsage());
			return;
		}

		player.sendMessage("---[ " + pPlayer.getName() + "'s Stats ]---\n" +
				"Wins:" + pPlayer.getName() + "\n" +
				"Loses:" + pPlayer.getName() + "\n" +
				"Draws:" + pPlayer.getName());
	}
}
