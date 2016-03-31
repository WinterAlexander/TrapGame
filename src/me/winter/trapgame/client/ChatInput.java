package me.winter.trapgame.client;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.packet.PacketInChat;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class ChatInput extends JTextField
{
	private TrapGameBoard board;

	public ChatInput(TrapGameBoard board)
	{
		this.board = board;

		addKeyListener(new KeyListener()
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
				if(!ChatInput.this.hasFocus())
					return;

				if(e.getKeyCode() != KeyEvent.VK_ENTER)
					return;

				if(getText().length() > 0)
					board.getContainer().getConnection().sendPacketLater(new PacketInChat(ChatInput.this.getText()));
				ChatInput.this.setText("");
			}
		});
	}


}
