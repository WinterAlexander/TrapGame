package me.winter.trapgame.client.menu;

import me.winter.trapgame.client.ImagePanel;
import me.winter.trapgame.client.TextAnimation;
import me.winter.trapgame.client.TrapGameClient;
import me.winter.trapgame.client.UserProperties;
import me.winter.trapgame.util.ColorTransformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.*;
import java.util.Random;
import java.util.logging.Level;

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

	private boolean useLan;
	private int pingLatency;

	private JLabel ping;
	private ImagePanel joinButton;

	public ServerPanel(ServerList serverList, String data) throws IllegalArgumentException
	{
		this.serverList = serverList;

		this.useLan = false;
		this.pingLatency = Integer.MAX_VALUE;

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

		this.pingLatency = Integer.MAX_VALUE;

		this.name = name;
		this.players = players;
		this.maxPlayers = maxPlayers;
		this.address = address;
		this.lanAddress = lanAddress;
		this.port = port;

		this.useLan = false;

		build();
	}

	private void build()
	{
		setLayout(new GridBagLayout());
		setBackground(ColorTransformer.TRANSPARENT);

		joinButton = new ImagePanel(serverList.getJoinForm().getMenu().getClient().getResourceManager().getImage("join-button"));

		joinButton.setPreferredSize(new Dimension(75, 75));
		joinButton.setBackground(new Color(230, 230, 230));

		joinButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				joinButton.setForeground(new Color(0, 0, 0, joinButton.getForeground().getAlpha() + 30));
				UserProperties properties = getServerList().getJoinForm().getMenu().getClient().getUserProperties();
				properties.setLastName(getServerList().getJoinForm().getPlayerName().getText());
				properties.save();

				new Thread(ServerPanel.this::connectTo).start();
			}
		});
		joinButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		joinButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				joinButton.setForeground(new Color(0, 0, 0, joinButton.getForeground().getAlpha() + 20));
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				joinButton.setForeground(new Color(0, 0, 0, Math.max(joinButton.getForeground().getAlpha() - 20, 0)));
			}
		});

		JLabel nameLabel = new JLabel(name);
		nameLabel.setFont(new Font("Arial", Font.BOLD, 20));

		JLabel ip = new JLabel(address.getHostAddress() + ":" + port);
		ip.setFont(new Font("Arial", Font.PLAIN, 20));
		ip.setPreferredSize(new Dimension(300, (int)ip.getPreferredSize().getHeight()));

		JLabel slots = new JLabel(players + " / " + maxPlayers, JLabel.RIGHT);
		slots.setFont(new Font("Arial", Font.PLAIN, 20));
		slots.setPreferredSize(new Dimension(100, (int)slots.getPreferredSize().getHeight()));

		ping = new JLabel("   ", JLabel.RIGHT);
		ping.setFont(new Font("Arial", Font.PLAIN, 20));

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;

		constraints.gridwidth = 2;
		constraints.gridheight = 2;

		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		add(joinButton, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;

		constraints.gridwidth = 5;
		constraints.gridheight = 1;

		constraints.anchor = GridBagConstraints.CENTER;

		add(nameLabel, constraints);

		constraints.gridy = 1;

		add(ip, constraints);

		constraints.gridx = 7;
		constraints.gridy = 0;

		constraints.gridwidth = 1;
		constraints.gridheight = 1;

		add(slots, constraints);

		constraints.gridx = 7;
		constraints.gridy = 1;

		constraints.gridwidth = 1;
		constraints.gridheight = 1;


		add(ping, constraints);


		Dimension size = getPreferredSize();

		size.width += 10;
		size.height += 10;

		setMinimumSize(size);

		setPreferredSize(size);
		setMaximumSize(size);

		getServerList().getJoinForm().getMenu().getClient().getScheduler().addTask(this::ping, 500);
	}

	public void connectTo()
	{
		TrapGameClient client = getServerList().getJoinForm().getMenu().getClient();

		String name = getServerList().getJoinForm().getPlayerName().getText();

		if(name.length() < 3)
			name = "Guest" + new Random().nextInt(10000);

		if(name.length() > 20)
			name = name.substring(0, 20);

		if(useLan)
			client.getConnection().connectTo(lanAddress, null, port, null, name);
		else
			client.getConnection().connectTo(address, lanAddress, port, null, name);

		joinButton.setForeground(new Color(0, 0, 0, joinButton.getForeground().getAlpha() - 30));
	}

	public void ping()
	{
		final TextAnimation pinging = new TextAnimation(ping, new String[]{".  ", ".. ", "..."});
		getServerList().getJoinForm().getMenu().getClient().getScheduler().addTask(pinging);

		new Thread(() -> {

			ping.setText("...");

			try
			{
				ping(lanAddress);
				useLan = true;
			}
			catch(Exception lanEx)
			{
				if(getServerList().getJoinForm().getMenu().getClient().getUserProperties().isDebugMode())
					getServerList().getJoinForm().getMenu().getClient().getLogger().log(Level.WARNING, "An exception occurred while pinging lan IP " + name, lanEx);

				try
				{
					ping(address);
				}
				catch(Exception publicEx)
				{
					if(getServerList().getJoinForm().getMenu().getClient().getUserProperties().isDebugMode())
						getServerList().getJoinForm().getMenu().getClient().getLogger().log(Level.WARNING, "An exception occurred while pinging public IP " + name, publicEx);

					ping.setText("X");
					ping.setForeground(Color.RED);
				}
			}
			finally
			{
				pinging.cancel();
				getServerList().placeServers();
			}
		}).start();
	}

	private void ping(InetAddress address) throws IOException
	{
		long timestamp = System.nanoTime();
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(5_000);

		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		new DataOutputStream(writer).writeUTF("KeepAlive");

		socket.send(new DatagramPacket(writer.toByteArray(), writer.size(), address, port));
		byte[] inputBuffer = new byte[256];

		DatagramPacket packet = new DatagramPacket(inputBuffer, inputBuffer.length);

		socket.receive(packet);

		ByteArrayInputStream reader = new ByteArrayInputStream(packet.getData());

		if(!new DataInputStream(reader).readUTF().equals("KeepAlive"))
			throw new IOException("Invalid response");

		pingLatency = (int)((System.nanoTime() - timestamp) / 1_000_000);
		ping.setText(pingLatency + "ms");

		if(pingLatency < 10)
			ping.setForeground(Color.GREEN.darker());
		else if(pingLatency < 50)
			ping.setForeground(Color.GREEN);
		else if(pingLatency < 100)
			ping.setForeground(Color.YELLOW.darker());
		else
			ping.setForeground(Color.ORANGE.darker());

	}

	@Override
	protected void paintComponent(Graphics graphics)
	{
		Graphics2D g2draw = (Graphics2D)graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);


		g2draw.setColor(new Color(110, 110, 110));
		g2draw.fillRect(0, 0, getWidth(), getHeight());

		g2draw.setColor(new Color(230, 230, 230));
		g2draw.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);

		super.paintComponent(graphics);
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

	public int getPing()
	{
		return pingLatency;
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
