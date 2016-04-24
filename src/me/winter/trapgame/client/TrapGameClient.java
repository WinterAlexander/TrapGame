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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
		}
		catch(Throwable throwable)//catch copied from NewX
		{
			trapGameLogger.log(Level.SEVERE, "Client crashed", throwable);
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, "We are sorry, an internal error occurred during your game session: \n\n" + StringUtil.getStackTrace(throwable) + "\nPlease report this error to me at a.w1nter@hotmail.com", "TrapGame has crashed :(", JOptionPane.ERROR_MESSAGE);
			frame.dispose();
			System.exit(-1);
		}
		finally
		{
			for(Handler handler : trapGameLogger.getHandlers())
				handler.close();
			trapGameLogger = null;
		}
	}

	private Scheduler scheduler;
	private ClientConnection connection;
	private UserProperties userProperties;
	private GameTranslation lang;
	private ResourceManager resourceManager;
	private Logger logger;

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

		addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_F11)
				{
					GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

					if(device.getFullScreenWindow() != null)
					{
						device.setFullScreenWindow(null);
						return;
					}

					device.setFullScreenWindow(TrapGameClient.this);
				}
			}
		});

		JPanel loading = new JPanel();
		loading.setLayout(new BorderLayout());

		loadingText = new JLabel("...", SwingConstants.CENTER);
		loadingText.setFont(new Font("Verdana", Font.BOLD, 18));
		loading.add(loadingText, BorderLayout.CENTER);
		getContentPane().add(loading);

		setVisible(true);
	}

	public void load() throws IOException
	{
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


		loadingText.setText(lang.getLine("client_loading"));

		resourceManager.scan("/index.properties");
		resourceManager.load();

		setIconImage(resourceManager.getImage("icon"));

		menu = new TrapGameMenu(this);
		board = new TrapGameBoard(this);

		goToMenu();
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
		synchronized(getScheduler())
		{
			getScheduler().start();

			while(isVisible())
			{
				long toWait = getScheduler().getWaitingDelay();
				if(toWait > 0)
				{
					try
					{
						if(toWait == Long.MAX_VALUE)
							getScheduler().wait(0);
						else
							getScheduler().wait(toWait);
					}
					catch(InterruptedException ex)
					{
						ex.printStackTrace(System.err);
					}
				}
				getScheduler().update();
			}
			getConnection().close();
			dispose();
		}
	}

	public void stop()
	{
		getScheduler().addTask(new Task(0, false, () -> setVisible(false)));
	}

	public void goToMenu()
	{
		getContentPane().removeAll();
		getContentPane().add(menu);
	}

	public void goToBoard()
	{
		getContentPane().removeAll();
		getContentPane().add(board);
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
