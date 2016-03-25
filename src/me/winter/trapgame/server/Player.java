package me.winter.trapgame.server;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.packet.PacketOutChat;
import me.winter.trapgame.shared.packet.PacketOutKick;

import java.net.InetAddress;

/**
 * Represents a player connected to the server
 *
 * Created by winter on 25/03/16.
 */
public class Player
{
	private TrapGameServer server;

	private PlayerInfo info;
	private PlayerConnection connection;

	public Player(TrapGameServer server)
	{
		this.server = server;
	}

	public String getName()
	{
		return info.getName();
	}

	public void chat(String message)
	{
		getServer().broadcast(getName() + ": " + message);
	}

	public void sendMessage(String message)
	{
		getConnection().sendPacket(new PacketOutChat(message));
	}

	public void kick(String message)
	{
		getConnection().sendPacket(new PacketOutKick(message));
	}

	public InetAddress getIpAddress()
	{
		return null;
	}

	public TrapGameServer getServer()
	{
		return server;
	}

	public PlayerInfo getInfo()
	{
		return info;
	}

	public void setInfo(PlayerInfo info)
	{
		this.info = info;
	}

	public PlayerConnection getConnection()
	{
		return connection;
	}

	public void setConnection(PlayerConnection connection)
	{
		this.connection = connection;
	}
}
