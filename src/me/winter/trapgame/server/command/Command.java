package me.winter.trapgame.server.command;

import me.winter.trapgame.server.CommandSender;
import me.winter.trapgame.server.Player;

import java.util.List;

/**
 * Picked up from NewX
 *
 * Represents a command
 *
 * Created by 1541869 on 2016-02-19.
 */
public interface Command
{
	/**
	 * The name of the command is the main way of identifying it
	 * @return name
	 */
	String getName();

	/**
	 * Aliases are alternates way of identifying a command
	 * @return aliases
	 */
	List<String> getAliases();

	/**
	 * Describe a command
	 * @return description
	 */
	String getDescription();

	/**
	 * Usage is how to use the command (syntax)
	 * @return usage
	 */
	String getUsage();

	/**
	 * Called to execute the command
	 * @param sender the executer
	 * @param label name used to identify the command
	 * @param arguments strings separated by spaces in the command line
	 */
	void execute(CommandSender sender, String label, String[] arguments);

	/**
	 * Tells is the command should only be used by super users
	 *
	 * @return if the command is reserved to super users
	 */
	boolean needSuper();
}
