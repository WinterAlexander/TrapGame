package me.winter.trapgame.client;

import me.winter.trapgame.shared.Scheduler;
import me.winter.trapgame.util.StringUtil;

import javax.swing.*;
import java.awt.*;

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

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		setSize((int)(dimension.getWidth() / 2), (int)(dimension.getHeight() / 2));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		menu = new TrapGameMenu();
		board = new TrapGameBoard();

		setContentPane(menu);

		setVisible(true);
	}

	public void start()
	{
		while(isValid())
			getScheduler().update();
	}

	public Scheduler getScheduler()
	{
		return scheduler;
	}
}
