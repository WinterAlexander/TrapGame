package me.winter.trapgame.client;

import me.winter.trapgame.shared.packet.PacketInChat;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class Chat extends JPanel
{
	private TrapGameBoard board;

	private JTextPane textArea;
	private JTextField textField;

	public Chat(TrapGameBoard board)
	{
		this.board = board;

		this.textField = new JTextField();
		this.textArea = new JTextPane();

		this.textField.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{

			}

			@Override
			public void keyPressed(KeyEvent e)
			{

			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				if(!textField.hasFocus())
					return;

				if(e.getKeyCode() != KeyEvent.VK_ENTER)
					return;

				if(textField.getText().length() > 0)
					board.getContainer().getConnection().sendPacketLater(new PacketInChat(textField.getText()));
				textField.setText("");
			}
		});

		textArea.setEditable(false);
		textArea.setFont(new Font("Arial", Font.PLAIN, 18));
		((DefaultCaret)textArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		setLayout(new BorderLayout());
		add(textArea, BorderLayout.CENTER);

		//JScrollPane scroll = new JScrollPane(textArea);
		//scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		//add(scroll, BorderLayout.EAST);
		add(textField, BorderLayout.SOUTH);
	}

	public void sendMessage(String message)
	{
		textArea.setText(textArea.getText() + (textArea.getText().length() == 0 ? "" : "\n") + message);
	}

	public void reset()
	{
		textArea.setText("");
	}
}
