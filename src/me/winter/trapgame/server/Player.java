package me.winter.trapgame.server;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.packet.PacketOutChat;
import me.winter.trapgame.shared.packet.PacketOutKick;
import me.winter.trapgame.util.StringUtil;

import java.awt.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Represents a player connected to the server
 *
 * Created by winter on 25/03/16.
 */
public class Player implements CommandSender
{
	private TrapGameServer server;

	private PlayerInfo info;
	private PlayerConnection connection;
	private boolean superUser;

	public Player(TrapGameServer server, PlayerInfo info, InetAddress address, int port)
	{
		this.server = server;
		this.info = info;
		this.connection = new PlayerConnection(this, address, port);
		this.superUser = false;
	}

	public int getId()
	{
		return info.getPlayerId();
	}

	@Override
	public String getName()
	{
		return info.getName();
	}

	public String getFormattedName()
	{
		return getFormattedName(getName());
	}

	private String getFormattedName(String name)
	{
		return "<span style=\"color: " + StringUtil.toCSS(getInfo().getColor()) + ";\">" + name + "</span>";
	}

	@Override
	public void chat(String message)
	{
		getServer().broadcast(getFormattedName(getName() + ":") + " " + message);
	}

	@Override
	public void sendMessage(String message)
	{
		getConnection().sendPacket(new PacketOutChat(message));
	}

	@Override
	public void sendMessage(Color color, String message)
	{
		sendMessage("<span style=\"color: " + StringUtil.toCSS(color) + ";\">" + message + "</span>");
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
		getServer().broadcast(Color.gray, getName() + " has timed out.");
		leave();
	}

	@Override
	public InetAddress getIpAddress()
	{
		return connection.getAddress();
	}

	@Override
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

	@Override
	public boolean isSuperUser()
	{
		return superUser;
	}

	public void setSuperUser(boolean superUser)
	{
		this.superUser = superUser;
	}
}
