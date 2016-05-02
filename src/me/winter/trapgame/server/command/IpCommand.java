package me.winter.trapgame.server.command;

import me.winter.trapgame.server.CommandSender;
import me.winter.trapgame.server.Player;

import java.util.Arrays;
import java.util.List;

/**
 * <p>A command used to get a player's ip</p>
 *
 * <p>Created by 1541869 on 2016-05-02.</p>
 */
public class IpCommand implements Command
{
	@Override
	public String getName()
	{
		return "ip";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("getip", "whois");
	}

	@Override
	public String getDescription()
	{
		return "Retrieves a player's ip address.";
	}

	@Override
	public String getUsage()
	{
		return "/ip <player>";
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
			args = new String[]{sender.getName()};

		Player pPlayer = sender.getServer().getPlayer(args[0]);

		if(pPlayer == null)
		{
			sender.sendMessage("That player couldn't be found.\n" +
					"Usage: " + getUsage());
			return;
		}

		sender.sendMessage(pPlayer.getConnection().getAddress().toString()  + ":" + pPlayer.getConnection().getPort());
	}

	@Override
	public boolean needSuper()
	{
		return true;
	}
}
