package me.winter.trapgame.client.menu.host;

import me.winter.trapgame.client.menu.TrapGameMenu;
import me.winter.trapgame.server.ServerProperties;
import me.winter.trapgame.server.TrapGameServer;
import me.winter.trapgame.shared.TrapGameLogFormatter;
import me.winter.trapgame.util.ColorTransformer;
import me.winter.trapgame.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>A form listing the games to join</p>
 *
 * <p>Created by 1541869 on 2016-04-19.</p>
 */
public class HostForm extends JPanel
{
	private TrapGameMenu menu;

	private JTextField ownerName;
	private JTextField boardSize;
	private JTextField maxPlayers;

	public HostForm(TrapGameMenu menu)
	{
		this.menu = menu;
		setBackground(ColorTransformer.TRANSPARENT);

		setLayout(new GridBagLayout());

		JLabel title = new JLabel(menu.getLangLine("client_host_title"), JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 34));

		JTextPane warning = new JTextPane();
		warning.setFont(new Font("Arial", Font.BOLD, 14));
		warning.setEditable(false);
		warning.setText(menu.getLangLine("client_host_warning"));
		warning.setBackground(new Color(238, 238, 238));
		warning.setPreferredSize(new Dimension(400, 200));

		JLabel ownerNameLabel = new JLabel(menu.getLangLine("client_username"), JLabel.RIGHT);
		ownerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
		//ownerNameLabel.setPreferredSize(new Dimension(200, 25));

		JLabel boardSizeLabel = new JLabel(menu.getLangLine("client_host_boardsize"), JLabel.RIGHT);
		boardSizeLabel.setFont(new Font("Arial", Font.BOLD, 16));

		JLabel maxPlayersLabel = new JLabel(menu.getLangLine("client_host_maxplayers"), JLabel.RIGHT);
		maxPlayersLabel.setFont(new Font("Arial", Font.BOLD, 16));

		ownerName = new JTextField();
		ownerName.setPreferredSize(new Dimension(200, 25));
		ownerName.setMinimumSize(ownerName.getPreferredSize());
		ownerName.setText(menu.getClient().getUserProperties().getLastName());

		boardSize = new JTextField();
		boardSize.setPreferredSize(new Dimension(200, 25));
		boardSize.setMinimumSize(boardSize.getPreferredSize());
		boardSize.setText("11");

		maxPlayers = new JTextField();
		maxPlayers.setPreferredSize(new Dimension(200, 25));
		maxPlayers.setMinimumSize(maxPlayers.getPreferredSize());
		maxPlayers.setText("12");

		JButton submit = new JButton(menu.getLangLine("client_host_validate"));
		submit.addActionListener(event -> hostGame());

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.ipadx = 0;
		constraints.ipady = 0;
		//constraints.weightx = 0.02;
		//constraints.weighty = 0.02;
		constraints.insets = new Insets(5, 5, 5, 5);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(title, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		add(warning, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.LINE_END;
		add(ownerNameLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.LINE_START;
		add(ownerName, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.LINE_END;
		add(boardSizeLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.LINE_START;
		add(boardSize, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.LINE_END;
		add(maxPlayersLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.LINE_START;
		add(maxPlayers, constraints);


		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		add(submit, constraints);
	}

	public void hostGame()
	{
		String owner = ownerName.getText();

		menu.getClient().getUserProperties().setLastName(owner);
		menu.getClient().getUserProperties().save();

		if(owner.length() < 3)
			owner = "Host";

		if(owner.length() > 20)
			owner = owner.substring(0, 20);

		final String name = owner;

		Logger serverLogger = Logger.getLogger("Server");

		serverLogger.setUseParentHandlers(false);

		ConsoleHandler consoleHandler = new ConsoleHandler()
		{
			{
				setOutputStream(System.out);
			}
		};

		int boardSize = StringUtil.isInt(this.boardSize.getText()) ? Integer.parseInt(this.boardSize.getText()) : 11;
		int maxPlayers = StringUtil.isInt(this.maxPlayers.getText()) ? Integer.parseInt(this.maxPlayers.getText()) : 16;

		if(maxPlayers < 2)
			maxPlayers = 2;

		boardSize %= 100;

		consoleHandler.setFormatter(new TrapGameLogFormatter());
		serverLogger.addHandler(consoleHandler);

		ServerProperties properties = new ServerProperties(serverLogger, null);
		properties.setServerName(menu.getLangLine("client_host_gamename").replace("{$PLAYER}", owner));
		properties.setPort(1254);
		properties.setLogToDisk(false);
		properties.setDebugMode(menu.getClient().getUserProperties().isDebugMode());
		properties.setMinPlayers(2);
		properties.setTimer(10);
		properties.setMaxPlayers(maxPlayers);
		properties.setBoardWidth(boardSize);
		properties.setBoardHeight(boardSize);
		properties.setSuperPassword("");
		properties.setEnableConsole(false);
		properties.setSavingStats(false);

		TrapGameServer server;
		try
		{
			server = new TrapGameServer(properties, serverLogger);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(menu.getClient(),
					menu.getClient().getLang().getLine("client_host_failed"),
					menu.getClient().getLang().getLine("client_host_failed_title"),
					JOptionPane.ERROR_MESSAGE);

			menu.getClient().getLogger().log(Level.WARNING, "Couldn't host server", ex);
			return;
		}

		menu.getClient().getBoard().setHostedServer(server);

		new Thread(server::start).start();

		new Thread(() -> {
			try
			{
				menu.getClient().getConnection().connectTo("localhost:1254", name);
				server.getPlayer(name).setSuperUser(true);
			}
			catch(TimeoutException | IOException ex)
			{
				JOptionPane.showMessageDialog(menu.getClient(),
						menu.getClient().getLang().getLine("client_connection_failed_message"),
						menu.getClient().getLang().getLine("client_connection_failed_title"),
						JOptionPane.ERROR_MESSAGE);

				menu.getClient().getLogger().log(Level.INFO, "Couldn't connect to local server", ex);
			}
		}).start();
	}

	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D g2draw = (Graphics2D) graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2draw.drawImage(menu.getClient().getResourceManager().getImage("background"), -getX(), 0, menu.getWidth(), menu.getHeight(), null);

		int x = Integer.MAX_VALUE, y = Integer.MAX_VALUE, width = 0, height = 0; //width & height acts as x2, y2 in the loop

		for(Component component : getComponents())
		{
			if(component.getX() < x)
				x = component.getX();

			if(component.getY() < y)
				y = component.getY();

			if(component.getWidth() + component.getX() > width)
				width = component.getWidth() + component.getX();

			if(component.getHeight() + component.getY() > height)
				height = component.getHeight() + component.getY();
		}

		width -= x;
		height -= y;

		//padding
		x -= 5;
		y -= 5;
		width += 10;
		height += 10;

		//dark
		g2draw.setColor(new Color(0, 0, 0, 50));
		g2draw.fillRoundRect(x + 15, y + 15, width, height, 5, 5);

		//border
		g2draw.setColor(new Color(80, 80, 80));
		g2draw.fillRect(x - 1, y - 1, width + 2, height + 2);

		//background
		g2draw.setColor(new Color(238, 238, 238));
		g2draw.fillRect(x, y, width, height);

		super.paintComponent(graphics);
	}
}
