package me.winter.trapgame.server;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.packet.PacketOutChat;
import me.winter.trapgame.shared.packet.PacketOutKick;

import java.net.InetAddress;
import java.net.Socket;

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

	public Player(TrapGameServer server, PlayerInfo info, InetAddress address, int port)
	{
		this.server = server;
		this.info = info;
		this.connection = new PlayerConnection(this, address, port);
	}

	public int getId()
	{
		return info.getPlayerId();
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
		leave();
	}

	public void leave()
	{
		if(getServer().getPlayers().contains(this))
			getServer().leave(this);
	}

	public void timeOut()
	{
		getServer().broadcast(getName() + " has timed out.");
		leave();
	}

	public InetAddress getIpAddress()
	{
		return connection.getAddress();
	}

	public TrapGameServer getServer()
	{
		return server;
	}

	public PlayerInfo getInfo()
	{
		return info;
	}

	public PlayerConnection getConnection()
	{
		return connection;
	}

}
