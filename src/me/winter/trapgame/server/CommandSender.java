package me.winter.trapgame.server;

import java.net.InetAddress;

/**
 *
 * Created by 1541869 on 2016-04-05.
 */
public interface CommandSender
{
	String getName();
	void chat(String message);
	void sendMessage(String message);
	TrapGameServer getServer();
	InetAddress getIpAddress();
	boolean isSuperUser();
}
