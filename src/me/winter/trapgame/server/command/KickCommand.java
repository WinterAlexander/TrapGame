package me.winter.trapgame.server.command;

import me.winter.trapgame.server.CommandSender;
import me.winter.trapgame.server.Player;

import java.util.Arrays;
import java.util.List;

/**
 * A command to kick a user out of the server
 * Please avoid drama or abuse, this isn't Minecraft
 *
 * Created by Alexander Winter on 2016-04-06.
 */
public class KickCommand implements Command
{
	@Override
	public String getName()
	{
		return "kick";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("disconnect", "reject");
	}

	@Override
	public String getDescription()
	{
		return "Kick the specified player out of the server";
	}

	@Override
	public String getUsage()
	{
		return "/kick <player>";
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args)
	{
		Player player = sender.getServer().getPlayer(args[0]);

		if(player == null)
		{
			sender.sendMessage("That player couldn't be found.");
			sender.sendMessage("Usage: " + getUsage());
			return;
		}

		player.kick("Kicked out by " + sender.getName());
		sender.getServer().broadcast(sender.getName() + " kicked " + player.getName() + " out of the server.");
	}

	@Override
	public boolean needSuper()
	{
		return true;
	}
}
