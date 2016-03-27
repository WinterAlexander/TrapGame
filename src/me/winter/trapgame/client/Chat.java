package me.winter.trapgame.client;

import javax.swing.*;
import java.awt.*;

/**
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class Chat extends JTextPane
{
	private TrapGameBoard board;

	public Chat(TrapGameBoard board)
	{
		this.board = board;

		setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
		setMinimumSize(new Dimension(100, 0));
		setPreferredSize(new Dimension(200, Integer.MAX_VALUE));
		setEditable(false);
	}

	public void sendMessage(String message)
	{
		setText(getText() + (getText().length() == 0 ? "" : "\n") + message);
	}

	public void reset()
	{
		setText("");
	}
}
