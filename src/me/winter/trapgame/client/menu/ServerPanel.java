package me.winter.trapgame.client.menu;

import com.sun.istack.internal.NotNull;
import me.winter.trapgame.client.ImagePanel;
import me.winter.trapgame.client.SimpleLayout;
import me.winter.trapgame.client.TrapGameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.IllegalFormatException;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * <p>Represents a server to be displayed in ServerList</p>
 *
 * <p>Created by 1541869 on 2016-04-22.</p>
 */
public class ServerPanel extends JPanel
{
	private ServerList serverList;

	private String name;
	private int players, maxPlayers;

	private InetAddress address, lanAddress;
	private int port;

	public ServerPanel(ServerList serverList, String data)
	{
		this.serverList = serverList;

		try
		{
			String[] entries = data.split("&");

			for(int i = 0; i < entries.length; i++)
				entries[i] = entries[i].split("=")[1];

			this.name = entries[2];
			this.players = Integer.parseInt(entries[5]);
			this.maxPlayers = Integer.parseInt(entries[6]);
			this.address = InetAddress.getByName(entries[1]);
			this.lanAddress = InetAddress.getByName(entries[4]);
			this.port = Integer.parseInt(entries[3]);

			if(this.name.length() < 3)
				throw new IllegalArgumentException("Server's name too short");

			if(this.maxPlayers < 0)
				throw new IllegalArgumentException("Illegal player limit");

			if(this.port < 0)
				throw new IllegalArgumentException("Bad port");

			build();
		}
		catch(NumberFormatException ex)
		{
			throw new IllegalArgumentException("A field supposed to be a number isn't", ex);
		}
		catch(UnknownHostException ex)
		{
			throw new IllegalArgumentException("Ip is invalid", ex);
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
			throw new IllegalArgumentException("Data supplied should have 7 entries (key value pair)", ex);
		}
	}

	public ServerPanel(ServerList serverList, String name, int players, int maxPlayers, InetAddress address, InetAddress lanAddress, int port)
	{
		this.serverList = serverList;

		this.name = name;
		this.players = players;
		this.maxPlayers = maxPlayers;
		this.address = address;
		this.lanAddress = lanAddress;
		this.port = port;

		build();
	}

	private void build()
	{
		setLayout(new GridBagLayout());

		ImagePanel joinButton = new ImagePanel(serverList.getJoinForm().getMenu().getClient().getResourceManager().getImage("join-button"));

		joinButton.setPreferredSize(new Dimension(75, 75));

		joinButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				connectTo();
			}
		});
		joinButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		joinButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				joinButton.setForeground(new Color(0, 0, 0, 50));
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				joinButton.setForeground(new Color(0, 0, 0, 0));
			}
		});

		JLabel nameLabel = new JLabel(name);
		nameLabel.setFont(new Font("Arial", Font.BOLD, 20));

		JLabel ip = new JLabel(address.toString() + ":" + port);
		ip.setFont(new Font("Arial", Font.PLAIN, 20));

		JLabel slots = new JLabel(players + " / " + maxPlayers, JLabel.RIGHT);
		slots.setFont(new Font("Arial", Font.PLAIN, 20));

		JLabel ping = new JLabel("23", JLabel.RIGHT);
		ping.setFont(new Font("Arial", Font.PLAIN, 20));

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;

		constraints.gridwidth = 2;
		constraints.gridheight = 2;

		add(joinButton, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;

		constraints.gridwidth = 4;
		constraints.gridheight = 1;

		add(nameLabel, constraints);

		constraints.gridy = 1;

		add(ip, constraints);

		constraints.gridx = 6;
		constraints.gridy = 0;

		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridheight = 1;

		add(slots, constraints);

		constraints.gridx = 6;
		constraints.gridy = 1;

		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridheight = 1;

		add(ping, constraints);
	}

	public void connectTo()
	{
		TrapGameClient client = getServerList().getJoinForm().getMenu().getClient();

		client.getConnection().connectTo(address, lanAddress, port, null, "Player");
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

	public InetAddress getLanAddress()
	{
		return lanAddress;
	}

	public void setLanAddress(InetAddress lanAddress)
	{
		this.lanAddress = lanAddress;
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
