package me.winter.trapgame.client;

import javax.swing.*;
import java.awt.*;
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

	public TrapGameMenu(TrapGameClient container)
	{
		this.container = container;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		playerName = new JTextField();
		serverAddress = new JTextField();

		add(playerName);
		add(serverAddress);

		JButton button = new JButton("Connect");

		button.addActionListener(event -> {
			try
			{
				getContainer().getConnection().connectTo(serverAddress.getText(), playerName.getText());
			}
			catch(IOException ex)
			{
				JOptionPane.showMessageDialog(getContainer(), "Sorry but the connection to this server failed: \n" + ex.getMessage(), "Connection failed", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace(System.err);
			}
		});

		add(button);
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
