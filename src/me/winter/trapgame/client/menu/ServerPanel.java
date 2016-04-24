package me.winter.trapgame.client.menu;

import me.winter.trapgame.client.TrapGameClient;

import javax.swing.*;
import java.net.InetAddress;
import java.util.logging.Level;

/**
 * <p>Represents a server to be displayed in ServerList</p>
 *
 * <p>Created by 1541869 on 2016-04-22.</p>
 */
public class ServerPanel
{
	private ServerList serverList;

	private String name;
	private int players, maxPlayers;

	private InetAddress address;
	private int port;

	public ServerPanel(ServerList serverList, String name, int players, int maxPlayers, InetAddress address, int port)
	{
		this.serverList = serverList;

		this.name = name;
		this.players = players;
		this.maxPlayers = maxPlayers;
		this.address = address;
		this.port = port;
	}

	public void connectTo()
	{
		TrapGameClient client = getServerList().getJoinForm().getMenu().getClient();

		try
		{
			client.getConnection().connectTo(address, port, null, "Player");
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(client, getServerList().getJoinForm().getMenu().getClient().getLang().getLine("client_connection_failed_message") + "\n" +
					ex.getMessage(), client.getLang().getLine("client_connection_failed_title"), JOptionPane.ERROR_MESSAGE);

			if(client.getUserProperties().isDebugMode())
				getServerList().getJoinForm().getMenu().getClient().getLogger().log(Level.INFO, "Couldn't connect to server", ex);
			else
				client.getLogger().log(Level.INFO, "Couldn't connect to server");
		}
	}

	public ServerList getServerList()
	{
		return serverList;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getPlayers()
	{
		return players;
	}

	public void setPlayers(int players)
	{
		this.players = players;
	}

	public int getMaxPlayers()
	{
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers)
	{
		this.maxPlayers = maxPlayers;
	}

	public InetAddress getAddress()
	{
		return address;
	}

	public void setAddress(InetAddress address)
	{
		this.address = address;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}
}
