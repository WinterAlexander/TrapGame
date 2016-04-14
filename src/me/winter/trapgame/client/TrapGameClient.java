package me.winter.trapgame.client;

import me.winter.trapgame.client.board.TrapGameBoard;
import me.winter.trapgame.client.menu.TrapGameMenu;
import me.winter.trapgame.shared.Scheduler;
import me.winter.trapgame.shared.Task;
import me.winter.trapgame.util.FileUtil;
import me.winter.trapgame.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * Represents the client for TrapGame
 * Is a JFrame and a launcher for the app
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class TrapGameClient extends JFrame
{
	/**
	 * Launches the app, opening a frame
	 * @param args execution arguments are unused
	 */
	public static void main(String[] args)
	{
		try
		{
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			TrapGameClient client = new TrapGameClient();
			client.load();
			client.start();
		}
		catch(Throwable throwable)//catch copied from NewX
		{
			throwable.printStackTrace(System.err);
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, "We are sorry, an internal error occurred during your game session: \n\n" + StringUtil.getStackTrace(throwable) + "\nPlease report this error to me at a.w1nter@hotmail.com", "TrapGame has crashed :(", JOptionPane.ERROR_MESSAGE);
			frame.dispose();
			System.exit(-1);
		}

	}

	private Scheduler scheduler;
	private ClientConnection connection;
	private UserProperties userProperties;
	private ResourceManager resourceManager;

	private TrapGameMenu menu;
	private TrapGameBoard board;

	/**
	 * Default and unique constructor, builds a JFrame ready to display the game and puts it visible
	 *
	 */
	public TrapGameClient()
	{
		super("TrapGame");

		scheduler = new Scheduler();
		connection = new ClientConnection(this);
		userProperties = new UserProperties(new File(FileUtil.getAppData() + "/.TrapGame/user.properties"));
		resourceManager = new SimpleResourceManager();

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		int width = (int)(dimension.getWidth() * 3 / 4);

		setSize(width, width * 9 / 16);
		setResizable(false);
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

		JPanel loading = new JPanel();
		loading.setLayout(new BorderLayout());

		JLabel loadingText = new JLabel("Loading...", SwingConstants.CENTER);
		loadingText.setFont(new Font("Verdana", Font.BOLD, 18));
		loading.add(loadingText, BorderLayout.CENTER);
		setContentPane(loading);

		setVisible(true);
	}

	public void load() throws IOException
	{
		userProperties.loadIfPresent();
		resourceManager.scan("/index.properties");
		resourceManager.load();

		menu = new TrapGameMenu(this);
		board = new TrapGameBoard(this);

		setContentPane(menu);
		revalidate();
		repaint();
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
		if(getContentPane() == menu)
			return;

		setContentPane(menu);
		revalidate();
		repaint();
	}

	public void goToBoard()
	{
		if(getContentPane() == board)
			return;

		setContentPane(board);
		revalidate();
		repaint();
	}

	public boolean inMenu()
	{
		return getContentPane() == menu;
	}

	public boolean inBoard()
	{
		return getContentPane() == board;
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
}
