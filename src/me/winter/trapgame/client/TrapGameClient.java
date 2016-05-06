package me.winter.trapgame.client;

import me.winter.trapgame.client.board.TrapGameBoard;
import me.winter.trapgame.client.menu.TrapGameMenu;
import me.winter.trapgame.shared.GameTranslation;
import me.winter.trapgame.shared.Scheduler;
import me.winter.trapgame.shared.Task;
import me.winter.trapgame.shared.TrapGameLogFormatter;
import me.winter.trapgame.util.FileUtil;
import me.winter.trapgame.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the client for TrapGame
 * Is a JFrame and a launcher for the app
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class TrapGameClient extends JFrame
{
	private static Logger trapGameLogger;

	/**
	 * Launches the app, opening a frame
	 * @param args execution arguments are unused
	 */
	public static void main(String[] args)
	{
		int exitCode = 2;

		try
		{
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			trapGameLogger = Logger.getLogger("Client");

			trapGameLogger.setUseParentHandlers(false);

			ConsoleHandler consoleHandler = new ConsoleHandler()
			{
				{
					setOutputStream(System.out);
				}
			};

			consoleHandler.setFormatter(new TrapGameLogFormatter());
			trapGameLogger.addHandler(consoleHandler);

			TrapGameClient client = new TrapGameClient(trapGameLogger);
			client.load();
			client.start();

			if(client.getUserProperties().isDebugMode())
				trapGameLogger.log(Level.FINE, "TrapGame stopped correctly.");
			exitCode = 0;
		}
		catch(Throwable throwable)//catch copied from NewX
		{
			trapGameLogger.log(Level.SEVERE, "Client crashed", throwable);
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, "We are sorry, an internal error occurred during your game session: \n\n" + StringUtil.getStackTrace(throwable) + "\nPlease report this error to me at a.w1nter@hotmail.com", "TrapGame has crashed :(", JOptionPane.ERROR_MESSAGE);
			frame.dispose();
			exitCode = 1;
		}

		for(Handler handler : trapGameLogger.getHandlers())
			handler.close();
		trapGameLogger = null;
		System.exit(exitCode);
	}

	private Scheduler scheduler;
	private ClientConnection connection;
	private UserProperties userProperties;
	private GameTranslation lang;
	private ResourceManager resourceManager;
	private Logger logger;
	private boolean loaded;

	private TrapGameMenu menu;
	private TrapGameBoard board;
	private JLabel loadingText;

	/**
	 * Default and unique constructor, builds a JFrame ready to display the game and puts it visible
	 *
	 */
	public TrapGameClient(Logger logger)
	{
		super("TrapGame");

		this.logger = logger;
		scheduler = new Scheduler(logger);
		connection = new ClientConnection(this);
		userProperties = new UserProperties(logger, new File(FileUtil.getAppData() + "/.TrapGame/user.properties"));
		resourceManager = new SimpleResourceManager(logger);

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		int width = (int)(dimension.getWidth() * 3 / 4);

		setContentPane(new JPanel(new ForceScaleLayout(16, 9)));
		setSize(width, width * 9 / 16);
		setMinimumSize(new Dimension(320, 200));
		setResizable(true);
		setLocation((int)dimension.getWidth() / 2 - getWidth() / 2, (int)dimension.getHeight() / 2 - getHeight() / 2);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				stop();
			}
		});

		this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F11"), "Fullscreen");
		this.getRootPane().getActionMap().put("Fullscreen", new AbstractAction()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

						if(device.getFullScreenWindow() != null)
						{
							device.setFullScreenWindow(null);
							return;
						}

						device.setFullScreenWindow(TrapGameClient.this);
					}
				});
		this.getRootPane().setFocusable(true);

		JPanel loading = new JPanel();
		loading.setLayout(new BorderLayout());

		loadingText = new JLabel("...", SwingConstants.CENTER);
		loadingText.setFont(new Font("Verdana", Font.BOLD, 18));
		loading.add(loadingText, BorderLayout.CENTER);
		getContentPane().add(loading);

		new Thread()
		{
			private int i = 0;
			private String[] values = {".  ", ".. ", "..."};

			@Override
			public void run()
			{
				while(!loaded)
				{
					try
					{
						Thread.sleep(250);
					}
					catch(InterruptedException ex)
					{
						ex.printStackTrace(System.err);
					}

					loadingText.setText(loadingText.getText().substring(0, loadingText.getText().length() - 3) + values[i]);

					i++;
					i %= 3;
				}
			}
		}.start();


		setVisible(true);
	}

	public void load() throws IOException
	{
		loaded = false;
		userProperties.loadIfPresent();

		//if(userProperties.isDebugMode())
		//  setResizable(true);

		lang = new GameTranslation(userProperties.getLanguage());

		try
		{
			lang.load();
		}
		catch(IOException ex)
		{
			logger.log(Level.WARNING, "Couldn't load lang file", ex);
		}


		loadingText.setText(lang.getLine("client_loading") + "...");

		resourceManager.scan("/index.properties");
		resourceManager.load();

		setIconImage(resourceManager.getImage("icon"));

		menu = new TrapGameMenu(this);
		board = new TrapGameBoard(this);

		goToMenu();
		loaded = true;
	}

	public void reloadLang()
	{
		userProperties.loadIfPresent();

		lang = new GameTranslation(userProperties.getLanguage());

		try
		{
			lang.load();
		}
		catch(IOException ex)
		{
			logger.log(Level.WARNING, "Couldn't load lang file", ex);
		}

		menu = new TrapGameMenu(this);
		board = new TrapGameBoard(this);

		goToMenu();
	}

	public void start()
	{
		getScheduler().loop(this::isVisible);
		getConnection().close();
		dispose();
	}

	public void stop()
	{
		getScheduler().addTask(new Task(0, false, () -> setVisible(false)));
	}

	public void goToMenu()
	{
		getContentPane().removeAll();
		getContentPane().add(menu);
		revalidate();
		repaint();
	}

	public void goToBoard()
	{
		getContentPane().removeAll();
		getContentPane().add(board);
		revalidate();
		repaint();
	}

	public boolean inMenu()
	{
		return getContentPane().getComponents()[0] == menu;
	}

	public boolean inBoard()
	{
		return getContentPane().getComponents()[0] == board;
	}

	public Scheduler getScheduler()
	{
		return scheduler;
	}

	public ClientConnection getConnection()
	{
		return connection;
	}

	public UserProperties getUserProperties()
	{
		return userProperties;
	}

	public void setUserProperties(UserProperties userProperties)
	{
		this.userProperties = userProperties;
	}

	public GameTranslation getLang()
	{
		return lang;
	}

	public TrapGameMenu getMenu()
	{
		return menu;
	}

	public TrapGameBoard getBoard()
	{
		return board;
	}

	public ResourceManager getResourceManager()
	{
		return resourceManager;
	}

	public Logger getLogger()
	{
		return logger;
	}
}
