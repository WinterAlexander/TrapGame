package me.winter.trapgame.client;

import me.winter.trapgame.shared.Scheduler;
import me.winter.trapgame.util.FileUtil;
import me.winter.trapgame.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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
			new TrapGameClient().start();
		}
		catch(Throwable throwable)//catch copied from NewX
		{
			throwable.printStackTrace(System.err);
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, "We are sorry, an internal error occurred during your game session: \n\n" + StringUtil.getStackTrace(throwable) + "\nPlease report this error to me at a.w1nter@hotmail.com", "TrapGame has crashed :(", JOptionPane.ERROR_MESSAGE);
			frame.dispose();
		}

	}

	private Scheduler scheduler;
	private ClientConnection connection;
	private UserProperties userProperties;

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
		userProperties = new UserProperties(new File(FileUtil.getAppData() + "/.TrapGame/user.proprieties"));
		userProperties.loadIfPresent();

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		setSize((int)dimension.getWidth() / 2, (int)dimension.getHeight() / 2);
		setLocation((int)dimension.getWidth() / 2 - getWidth() / 2, (int)dimension.getHeight() / 2 - getHeight() / 2);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		menu = new TrapGameMenu(this);
		board = new TrapGameBoard(this);

		setContentPane(menu);

		setVisible(true);
	}

	public void start()
	{
		getScheduler().start();

		while(isVisible())
		{
			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException ex)
			{
				ex.printStackTrace(System.err);
			}
			getScheduler().update();
		}
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
}
