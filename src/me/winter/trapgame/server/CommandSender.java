package me.winter.trapgame.server;

import java.awt.*;
import java.net.InetAddress;

/**
 * <p>Represents any media able to send a command. </p>
 *
 * <p>Created by 1541869 on 2016-04-05.</p>
 *
 * @see me.winter.trapgame.server.Player
 * @see me.winter.trapgame.server.ConsoleSender
 */
public interface CommandSender
{
	String getName();
	void chat(String message);
	void sendMessage(String message);
	default void sendMessage(Color color, String message)
	{
		sendMessage(message);
	}

	TrapGameServer getServer();
	InetAddress getIpAddress();
	boolean isSuperUser();
}
