package me.winter.trapgame.client;

import javax.swing.*;

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

		setEditable(false);
	}

	public void sendMessage(String message)
	{
		setText(getText() + "\n" + message);
	}
}
