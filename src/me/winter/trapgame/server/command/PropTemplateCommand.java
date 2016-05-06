package me.winter.trapgame.server.command;

import me.winter.trapgame.server.CommandSender;
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
		return "Creates a template file at the jar location for a TrapGame server admin to configure it. (STANDALONE SERVERS ONLY)";
	}

	@Override
	public String getUsage()
	{
		return "/proptemplate";
	}

	@Override
	public void execute(CommandSender player, String label, String[] arguments)
	{
		ServerProperties properties = new ServerProperties(player.getServer().getLogger(), new File("server.properties"));
		properties.setServerName("Fun server 2");
		properties.setWelcomeMessage(properties.getWelcomeMessage());
		properties.setPort(1254);
		properties.setPassword("");
		properties.setMinPlayers(2);
		properties.setMaxPlayers(8);
		properties.setBoardWidth(11);
		properties.setBoardHeight(11);
		properties.setTimer(10);
		properties.setPublic(true);
		properties.setLogToDisk(true);
		properties.setEnableConsole(true);
		properties.setDebugMode(false);


		properties.save();
		player.sendMessage("server.properties file created.");
	}

	@Override
	public boolean needSuper()
	{
		return true;
	}
}
