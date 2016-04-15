package me.winter.trapgame.server.command;

import me.winter.trapgame.server.CommandSender;
import me.winter.trapgame.server.Player;
import me.winter.trapgame.server.ServerProperties;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Creates a template file at the jar location for a TrapGame server admin to configure it
 *
 * Created by Alexander Winter on 2016-03-31.
 */
public class PropTemplateCommand implements Command
{
	@Override
	public String getName()
	{
		return "proptemplate";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("propertiestemplate", "proptemp");
	}

	@Override
	public String getDescription()
	{
		return "Creates a template file at the jar location for a TrapGame server admin to configure it";
	}

	@Override
	public String getUsage()
	{
		return "/proptemplate";
	}

	@Override
	public void execute(CommandSender player, String label, String[] arguments)
	{
		ServerProperties properties = new ServerProperties(player.getServer(), new File("server.properties"));
		properties.setPort(1254);
		properties.setPassword("");
		properties.setMinPlayers(2);
		properties.setMaxPlayers(8);
		properties.setBoardWidth(10);
		properties.setBoardHeight(10);
		properties.setTimer(30);
		properties.setLogToDisk(false);

		properties.save();
		player.sendMessage("server.properties file created.");
	}

	@Override
	public boolean needSuper()
	{
		return true;
	}
}
