package me.winter.trapgame.client.menu;

import me.winter.trapgame.client.TrapGameClient;
import me.winter.trapgame.client.UserProperties;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *  The menu used to connect to a server (and eventually to do more things)
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class TrapGameMenu extends JPanel
{
	private TrapGameClient container;

	private JTextField playerName;
	private JTextField serverAddress;
	private JPasswordField serverPassword;

	public TrapGameMenu(TrapGameClient container)
	{
		this.container = container;

		setLayout(new GridBagLayout());

		JLabel logoLabel = new JLabel(new ImageIcon(container.getResourceManager().getImage("logo")));

		playerName = new JTextField();
		playerName.setPreferredSize(new Dimension(200, 25));
		serverAddress = new JTextField();
		serverAddress.setPreferredSize(new Dimension(200, 25));
		serverPassword = new JPasswordField();
		serverPassword.setPreferredSize(new Dimension(200, 25));

		JLabel nameLabel = new JLabel("User name:");
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);

		JLabel serverLabel = new JLabel("Server address:");
		serverLabel.setHorizontalAlignment(JLabel.RIGHT);

		JLabel passwordLabel = new JLabel("Server password:");
		passwordLabel.setHorizontalAlignment(JLabel.RIGHT);

		final JCheckBox saveProprieties = new JCheckBox("Remember me");

		JButton button = new JButton("Connect");

		button.addActionListener(event -> {
			try
			{
				String name = playerName.getText(),
						password = new String(serverPassword.getPassword()),
						server = serverAddress.getText();

				if(saveProprieties.isSelected())
				{
					UserProperties properties = getContainer().getUserProperties();
					properties.setLastName(name);
					properties.setLastPassword(password);
					properties.setLastServer(server);
					properties.save();
				}

				getContainer().getConnection().connectTo(server, password, name);
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(getContainer(), "Sorry but the connection to this server failed: \n" + ex.getMessage(), "Connection failed", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace(System.err);
			}
		});

		GridBagConstraints bagConstraints = new GridBagConstraints();

		bagConstraints.fill = GridBagConstraints.BOTH;
		bagConstraints.anchor = GridBagConstraints.EAST;

		bagConstraints.insets = new Insets(5, 5, 5, 5);

		bagConstraints.gridx = 0;
		bagConstraints.gridy = 0;

		bagConstraints.gridwidth = 3;
		bagConstraints.gridheight = 1;

		add(logoLabel, bagConstraints);

		bagConstraints.gridy++;

		bagConstraints.gridwidth = 1;
		bagConstraints.gridheight = 1;

		add(nameLabel, bagConstraints);

		bagConstraints.gridx++;
		bagConstraints.gridwidth = 2;

		add(playerName, bagConstraints);

		bagConstraints.gridx = 0;
		bagConstraints.gridy++;
		bagConstraints.gridwidth = 1;

		add(serverLabel, bagConstraints);

		bagConstraints.gridx++;
		bagConstraints.gridwidth = 2;

		add(serverAddress, bagConstraints);

		bagConstraints.gridx = 0;
		bagConstraints.gridy++;
		bagConstraints.gridwidth = 1;

		add(passwordLabel, bagConstraints);

		bagConstraints.gridx++;
		bagConstraints.gridwidth = 2;

		add(serverPassword, bagConstraints);

		bagConstraints.gridx = 1;
		bagConstraints.gridy++;
		bagConstraints.gridwidth = 1;

		add(button, bagConstraints);

		bagConstraints.gridx++;

		add(saveProprieties, bagConstraints);

		UserProperties properties = getContainer().getUserProperties();

		if(properties.exists())
		{
			playerName.setText(properties.getLastName());
			serverPassword.setText(properties.getLastPassword());
			serverAddress.setText(properties.getLastServer());
		}

	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}

	public TrapGameClient getContainer()
	{
		return container;
	}

	public JTextField getServerAddress()
	{
		return serverAddress;
	}

	public JTextField getPlayerName()
	{
		return playerName;
	}
}
