package me.winter.trapgame.client.menu;

import me.winter.trapgame.client.TrapGameClient;
import me.winter.trapgame.client.UserProperties;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;

/**
 *  The menu used to connect to a server (and eventually to do more things)
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class JoinForm extends JPanel
{
	private TrapGameClient container;

	private JTextField playerName;
	private JTextField serverAddress;
	private JPasswordField serverPassword;

	public JoinForm(TrapGameClient container)
	{
		this.container = container;

		setLayout(new GridBagLayout());
		setBackground(new Color(0, 0, 0, 0));

		playerName = new JTextField();
		playerName.setPreferredSize(new Dimension(200, 25));
		serverAddress = new JTextField();
		serverAddress.setPreferredSize(new Dimension(200, 25));
		serverPassword = new JPasswordField();
		serverPassword.setPreferredSize(new Dimension(200, 25));

		JLabel nameLabel = new JLabel(getContainer().getLang().getLine("client_username"));
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);

		JLabel serverLabel = new JLabel(getContainer().getLang().getLine("client_address"));
		serverLabel.setHorizontalAlignment(JLabel.RIGHT);

		JLabel passwordLabel = new JLabel(getContainer().getLang().getLine("client_password"));
		passwordLabel.setHorizontalAlignment(JLabel.RIGHT);

		final JCheckBox saveProprieties = new JCheckBox(getContainer().getLang().getLine("client_remember"));

		JButton button = new JButton(getContainer().getLang().getLine("client_connect_button"));

		button.addActionListener(event ->
		{
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
				JOptionPane.showMessageDialog(getContainer(), getContainer().getLang().getLine("client_connection_failed_message") + "\n" +
						ex.getMessage(),getContainer().getLang().getLine("client_connection_failed_title"), JOptionPane.ERROR_MESSAGE);

				if(getContainer().getUserProperties().isDebugMode())
					getContainer().getLogger().log(Level.INFO, "Couldn't connect to server", ex);
				else
					getContainer().getLogger().log(Level.INFO, "Couldn't connect to server");
			}
		});

		GridBagConstraints bagConstraints = new GridBagConstraints();

		bagConstraints.fill = GridBagConstraints.BOTH;
		bagConstraints.anchor = GridBagConstraints.EAST;

		bagConstraints.insets = new Insets(5, 5, 5, 5);

		bagConstraints.gridx = 0;
		bagConstraints.gridy = 0;

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

	public void paintComponent(Graphics graphics)
	{
		Graphics2D g2draw = (Graphics2D) graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2draw.drawImage(getContainer().getResourceManager().getImage("background"), -getX(), 0, getContainer().getWidth(), getContainer().getHeight(), null);
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
