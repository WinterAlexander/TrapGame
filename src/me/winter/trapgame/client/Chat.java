package me.winter.trapgame.client;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
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

		setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
		setMinimumSize(new Dimension(200, 0));
		setPreferredSize(new Dimension(350, Integer.MAX_VALUE));
		setEditable(false);
		setFont(new Font("Arial", Font.PLAIN, 18));
		((DefaultCaret)getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
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
