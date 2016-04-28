package me.winter.trapgame.client.menu;

import me.winter.trapgame.client.SimpleLayout;
import me.winter.trapgame.server.ServerProperties;
import me.winter.trapgame.server.TrapGameServer;
import me.winter.trapgame.shared.TrapGameLogFormatter;
import me.winter.trapgame.util.BetterRandom;
import me.winter.trapgame.util.ColorTransformer;
import me.winter.trapgame.util.LayoutUtil;
import me.winter.trapgame.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
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

	private JTextField password;
	private JTextField boardSize;
	private JTextField port;
	private JTextField maxPlayers;
	private JCheckBox publicServer;

	public HostForm(TrapGameMenu menu)
	{
		this.menu = menu;
		setBackground(ColorTransformer.TRANSPARENT);

		setLayout(new GridBagLayout());

		JLabel title = new JLabel("Host");
		JLabel warning = new JLabel("Warning, blablabla blablablabla blabla help help help help lalalalalala 123453131232 okaaaaaaaaaaaaay...... BOOM", JLabel.CENTER);
		JLabel passwordLabel = new JLabel("Passsword:", JLabel.RIGHT);
		JLabel boardSizeLabel = new JLabel("Board width:", JLabel.RIGHT);
		JLabel portLabel = new JLabel("Port (optional):", JLabel.RIGHT);
		JLabel publicLabel = new JLabel("Public:", JLabel.RIGHT);
		JLabel maxPlayersLabel = new JLabel("Max players:", JLabel.RIGHT);

		password = new JTextField();

		boardSize = new JTextField();

		port = new JTextField();

		publicServer = new JCheckBox();
		publicServer.setSelected(true);

		maxPlayers = new JTextField();

		JButton submit = new JButton("Host game");
		submit.addActionListener(event -> host());


		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = 4;
		constraints.gridheight = 2;
		add(title, constraints);

		LayoutUtil.newLine(constraints, 8, 1);
		add(warning, constraints);


		LayoutUtil.newLine(constraints, 2, 1);
		add(passwordLabel);

		LayoutUtil.nextTo(constraints, 2, 1);
		add(password);

		LayoutUtil.nextTo(constraints, 2, 1);
		add(boardSizeLabel);

		LayoutUtil.nextTo(constraints, 2, 1);
		add(boardSize);


		LayoutUtil.newLine(constraints, 2, 1);
		add(portLabel);

		LayoutUtil.nextTo(constraints, 2, 1);
		add(port);

		LayoutUtil.nextTo(constraints, 2, 1);
		add(publicLabel);

		LayoutUtil.nextTo(constraints, 2, 1);
		add(publicServer);


		LayoutUtil.newLine(constraints, 2, 1);
		add(maxPlayersLabel);

		LayoutUtil.nextTo(constraints, 2, 1);
		add(maxPlayers);

		//LayoutUtil.nextTo(constraints, 2, 1);
		//add(???);

		//LayoutUtil.nextTo(constraints, 2, 1);
		//add(???);


		LayoutUtil.newLine(constraints, 2, 1);
		constraints.gridx = 3;
		add(submit);

	}

	private void host()
	{
		final int port, maxPlayers, boardSize;
		final boolean publicServer = this.publicServer.isSelected();

		if(StringUtil.isInt(this.port.getText()))
			port = Integer.parseInt(this.port.getText());
		else
			port = -1;

		if(StringUtil.isInt(this.maxPlayers.getText()))
			maxPlayers = Integer.parseInt(this.port.getText());
		else
			maxPlayers = 16;

		if(StringUtil.isInt(this.boardSize.getText()))
			boardSize = Integer.parseInt(this.port.getText());
		else
			boardSize = 9;



		Thread serverThread = new Thread(() ->
		{
			Logger serverLogger = Logger.getLogger("Server");
			try
			{

				serverLogger.setUseParentHandlers(false);

				ConsoleHandler consoleHandler = new ConsoleHandler()
				{
					{
						setOutputStream(System.out);
					}
				};

				consoleHandler.setFormatter(new TrapGameLogFormatter());
				serverLogger.addHandler(consoleHandler);

				ServerProperties properties = new ServerProperties(serverLogger, null);
				properties.setPort(port);
				properties.setLogToDisk(false);
				properties.setDebugMode(false);
				properties.setMinPlayers(2);
				properties.setMaxPlayers(maxPlayers);
				properties.setBoardWidth(boardSize);
				properties.setBoardHeight(boardSize);
				properties.setPassword(password.getText());
				properties.setSuperPassword(null);
				properties.setEnableConsole(false);
				properties.setPublic(publicServer);

				new TrapGameServer(properties, serverLogger).start();
				serverLogger.info("Thanks for playing TrapGame ! :)");
			}
			catch(Throwable ex)
			{
				serverLogger.log(Level.SEVERE, "A fatal error occurred and stopped the server.", ex);
			}
			finally
			{
				for(Handler handler : serverLogger.getHandlers())
					handler.close();
				serverLogger = null;
			}
		});

		serverThread.start();
	}

	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D g2draw = (Graphics2D) graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2draw.drawImage(menu.getClient().getResourceManager().getImage("background"), -getX(), 0, menu.getWidth(), menu.getHeight(), null);

		super.paintComponent(graphics);
	}
}
