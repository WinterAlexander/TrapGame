package me.winter.trapgame.client.menu.join;

import me.winter.trapgame.client.ImagePanel;
import me.winter.trapgame.client.TextAnimation;
import me.winter.trapgame.client.TrapGameClient;
import me.winter.trapgame.client.UserProperties;
import me.winter.trapgame.shared.packet.PacketOutPong;
import me.winter.trapgame.util.ColorTransformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;

/**
 * <p>Represents a server to be displayed in ServerList</p>
 *
 * <p>Created by 1541869 on 2016-04-22.</p>
 */
public class ServerPanel extends JPanel
{
	private RemoteServer server;

	private JLabel nameLabel, ipLabel, slotsLabel, pingLabel;
	private ImagePanel joinButton;

	public ServerPanel(RemoteServer server, String connectionString)
	{
		this.server = server;

		setLayout(new GridBagLayout());
		setBackground(ColorTransformer.TRANSPARENT);

		joinButton = new ImagePanel(server.getServerList().getJoinForm().getMenu().getClient().getResourceManager().getImage("join-button"));

		joinButton.setPreferredSize(new Dimension(75, 75));
		joinButton.setBackground(new Color(230, 230, 230));

		joinButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserProperties properties = getServerList().getJoinForm().getMenu().getClient().getUserProperties();
				properties.setLastName(getServerList().getJoinForm().getPlayerName().getText());
				properties.save();

				new Thread(server::connectTo).start();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				joinButton.setForeground(new Color(0, 0, 0, joinButton.getForeground().getAlpha() + 30));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				joinButton.setForeground(new Color(0, 0, 0, Math.max(joinButton.getForeground().getAlpha() - 30, 0)));
			}
		});
		joinButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		joinButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				joinButton.setForeground(new Color(0, 0, 0, joinButton.getForeground().getAlpha() + 20));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				joinButton.setForeground(new Color(0, 0, 0, Math.max(joinButton.getForeground().getAlpha() - 20, 0)));
			}
		});

		nameLabel = new JLabel("");
		nameLabel.setFont(new Font("Arial", Font.BOLD, 20));

		ipLabel = new JLabel("");
		ipLabel.setFont(new Font("Arial", Font.PLAIN, 20));

		slotsLabel = new JLabel("", JLabel.RIGHT);
		slotsLabel.setFont(new Font("Arial", Font.PLAIN, 20));

		pingLabel = new JLabel("", JLabel.RIGHT);
		pingLabel.setFont(new Font("Arial", Font.PLAIN, 20));

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

		add(ipLabel, constraints);

		constraints.gridx = 7;
		constraints.gridy = 0;

		constraints.gridwidth = 1;
		constraints.gridheight = 1;

		add(slotsLabel, constraints);

		constraints.gridx = 7;
		constraints.gridy = 1;

		constraints.gridwidth = 1;
		constraints.gridheight = 1;


		add(pingLabel, constraints);

		setName(connectionString);
		setPing(-1);
		slotsLabel.setText("?");
		ipLabel.setText(connectionString);
		scale();
	}

	public void scale()
	{
		ipLabel.setPreferredSize(null);
		ipLabel.setPreferredSize(new Dimension(300, (int)ipLabel.getPreferredSize().getHeight()));

		slotsLabel.setPreferredSize(null);
		slotsLabel.setPreferredSize(new Dimension(100, (int)slotsLabel.getPreferredSize().getHeight()));

		setPreferredSize(null);
		Dimension size = getPreferredSize();

		size.width += 10;
		size.height += 10;

		setMinimumSize(size);

		setPreferredSize(size);
		setMaximumSize(size);
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
		return server.getServerList();
	}

	public ImagePanel getJoinButton()
	{
		return joinButton;
	}

	public void setName(String name)
	{
		nameLabel.setText(name);
		scale();
	}

	public void setIp(InetAddress address, int port)
	{
		ipLabel.setText(address.getHostAddress() + ":" + port);
		scale();
	}

	public void setPing(String ping)
	{
		pingLabel.setText(ping);
		pingLabel.setForeground(new JLabel().getForeground());
		scale();
	}

	public void setPing(int ping)
	{
		if(ping != -1 && ping != Integer.MAX_VALUE)
		{
			pingLabel.setText(ping + "ms");

			if(ping < 10)
				pingLabel.setForeground(Color.GREEN.darker());
			else if(ping < 50)
				pingLabel.setForeground(Color.GREEN);
			else if(ping < 100)
				pingLabel.setForeground(Color.YELLOW.darker());
			else
				pingLabel.setForeground(Color.ORANGE.darker());
		}
		else
		{
			setPing("");
		}
		scale();
	}

	public JLabel getPingLabel()
	{
		return pingLabel;
	}

	public void setPlayers(int players, int maxPlayers)
	{
		slotsLabel.setText(players + " / " + maxPlayers);
		scale();
	}
}

