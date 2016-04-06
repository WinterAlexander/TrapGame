package me.winter.trapgame.server.command;

import me.winter.trapgame.server.CommandSender;
import me.winter.trapgame.server.Player;

import java.util.Arrays;
import java.util.List;

/**
 * This command has 2 different purposes.
 * <br />
 * <br />
 * First it permits super users to give power to other users.
 * <br />
 * Second it let non-powered users get super powers by entering
 * the super password specified in server.properties
 * <br />
 * <br />
 * Created by Alexander Winter on 2016-04-06.
 */
public class SuperUserCommand implements Command
{
	public SuperUserCommand()
	{

	}

	@Override
	public String getName()
	{
		return "superuser";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("su", "op");
	}

	@Override
	public String getDescription()
	{
		return "Permits both super users to share their powers and normal users to access super powered by entering a password.";
	}

	@Override
	public String getUsage()
	{
		return "/superuser [player|password]";
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args)
	{
		if(sender.isSuperUser())
		{
			if(args.length == 0)
			{
				sender.sendMessage("You need to specify the user you want to give powers.\n" +
						"Usage: " + getUsage());
				return;
			}

			Player player = sender.getServer().getPlayer(args[0]);

			if(player == null)
			{
				sender.sendMessage("That player couldn't be found.\n" +
						"Usage: " + getUsage());
				return;
			}

			player.setSuperUser(!player.isSuperUser());

			if(player.isSuperUser())
				sender.sendMessage(player.getName() + " is now super user.");
			else
				sender.sendMessage(player.getName() + " is no longer super user.");

			return;
		}

		String password = sender.getServer().getSuperPassword();

		if(password == null || password.length() == 0 || !(sender instanceof Player))
		{
			sender.sendMessage("Super user authentication has been disabled.");
			return;
		}

		if(args.length == 0)
		{
			sender.sendMessage("You need to specify the super password to get powers.\n" +
					"Usage: " + getUsage());
			return;
		}

		if(!args[0].equals(password))
		{
			sender.sendMessage("Invalid password.");
			return;
		}

		((Player)sender).setSuperUser(true);
		sender.sendMessage("Authentication successful, you are now a super user.");
	}

	@Override
	public boolean needSuper()
	{
		return false;
	}
}
