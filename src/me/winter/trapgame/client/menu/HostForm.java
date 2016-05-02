package me.winter.trapgame.client.menu;

import me.winter.trapgame.server.ServerProperties;
import me.winter.trapgame.server.TrapGameServer;
import me.winter.trapgame.shared.TrapGameLogFormatter;
import me.winter.trapgame.util.ColorTransformer;
import me.winter.trapgame.util.StringUtil;

import javax.swing.*;
import java.awt.*;
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

	private JTextField ownerName;
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

		JLabel title = new JLabel("Host", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 34));

		JTextPane warning = new JTextPane();
		warning.setFont(new Font("Arial", Font.BOLD, 16));
		warning.setEditable(false);
		warning.setText("Warning, plusieurs variations de Lorem Ipsum peuvent être trouvées ici ou là, mais la majeure partie d'entre elles a été altérée par l'addition d'humour ou de mots aléatoires qui ne ressemblent pas une seconde à du texte standard.");

		JLabel ownerNameLabel = new JLabel("Your name:", JLabel.RIGHT);
		ownerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));

		JLabel passwordLabel = new JLabel("Passsword:", JLabel.RIGHT);
		passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));

		JLabel boardSizeLabel = new JLabel("Board width:", JLabel.RIGHT);
		boardSizeLabel.setFont(new Font("Arial", Font.BOLD, 16));

		JLabel portLabel = new JLabel("Port (optional):", JLabel.RIGHT);
		portLabel.setFont(new Font("Arial", Font.BOLD, 16));

		JLabel maxPlayersLabel = new JLabel("Max players:", JLabel.RIGHT);
		maxPlayersLabel.setFont(new Font("Arial", Font.BOLD, 16));

		ownerName = new JTextField();
		ownerName.setPreferredSize(new Dimension(200, 25));
		ownerName.setMinimumSize(ownerName.getPreferredSize());

		password = new JTextField();
		password.setPreferredSize(new Dimension(200, 25));
		password.setMinimumSize(password.getPreferredSize());

		boardSize = new JTextField();
		boardSize.setPreferredSize(new Dimension(200, 25));
		boardSize.setMinimumSize(boardSize.getPreferredSize());
		boardSize.setText("11");

		port = new JTextField();
		port.setPreferredSize(new Dimension(200, 25));
		port.setMinimumSize(port.getPreferredSize());
		port.setText("1254");

		publicServer = new JCheckBox("Public");
		publicServer.setSelected(true);
		publicServer.setPreferredSize(new Dimension(200, 25));
		publicServer.setMinimumSize(publicServer.getPreferredSize());

		maxPlayers = new JTextField();
		maxPlayers.setPreferredSize(new Dimension(200, 25));
		maxPlayers.setMinimumSize(maxPlayers.getPreferredSize());
		maxPlayers.setText("12");

		JButton submit = new JButton("Host game");
		submit.addActionListener(event -> host());

		GridBagConstraints constraints = new GridBagConstraints();

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
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(warning, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(passwordLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(password, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(boardSizeLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(boardSize, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(portLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(port, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(ownerNameLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(ownerName, constraints);

		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(publicServer, constraints);

		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(maxPlayersLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(maxPlayers, constraints);

		//LayoutUtil.nextTo(constraints, 2, 1);
		//add(???);

		//LayoutUtil.nextTo(constraints, 2, 1);
		//add(???);


		constraints.gridx = 1;
		constraints.gridy = 8;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(submit, constraints);

	}

	private void host()
	{
		final int port, maxPlayers, boardSize;
		final boolean publicServer = this.publicServer.isSelected();

		if(StringUtil.isInt(this.port.getText()))
			port = Integer.parseInt(this.port.getText());
		else
			port = -1;

		if(StringUtil.isInt(this.port.getText()))
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
				properties.setServerName(ownerName.getText() + "'s game");
				properties.setPort(port);
				properties.setLogToDisk(false);
				properties.setDebugMode(false);
				properties.setMinPlayers(2);
				properties.setMaxPlayers(maxPlayers);
				properties.setBoardWidth(boardSize);
				properties.setBoardHeight(boardSize);
				properties.setPassword(password.getText());
				properties.setSuperPassword("");
				properties.setEnableConsole(false);
				properties.setPublic(publicServer);

				new TrapGameServer(properties, serverLogger).start();

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

		//menu.getClient().getConnection().connectTo();
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
