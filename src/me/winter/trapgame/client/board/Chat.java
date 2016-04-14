package me.winter.trapgame.client.board;

import me.winter.trapgame.client.SimpleLayout;
import me.winter.trapgame.shared.packet.PacketInChat;
import me.winter.trapgame.util.FileUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.util.List;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * A client side chat used to display received messages and to send new ones
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class Chat extends JPanel implements KeyListener
{
	private TrapGameBoard board;

	private List<String> messages;

	private JTextPane textArea;
	private JTextField textField;

	public Chat(TrapGameBoard board)
	{
		this.board = board;

		setBackground(new Color(0, 0, 0, 0));

		messages = new ArrayList<>();

		textField = new JTextField()
		{
			@Override
			public void paint(Graphics g)
			{
				g.drawImage(board.getContainer().getResourceManager().getImage("chat-input"), 0, 0, getWidth(), getHeight(), null);

				super.paint(g);
			}
		};
		textArea = new JTextPane()
		{
			@Override
			public void paint(Graphics g)
			{
				g.drawImage(board.getContainer().getResourceManager().getImage("chat-background"), 0, 0, getWidth(), getHeight(), null);

				super.paint(g);
			}
		};

		textField.addKeyListener(this);

		textArea.setContentType("text/html");
		textArea.setEditable(false);
		textArea.setFont(new Font("Arial", Font.PLAIN, 18));
		textArea.setForeground(Color.BLACK);
		textArea.setBackground(new Color(0, 0, 0, 0));

		textField.setFont(new Font("Arial", Font.PLAIN, 18));
		textField.setForeground(Color.BLACK);
		textField.setBackground(new Color(0, 0, 0, 0));
		textField.setBorder(new EmptyBorder(5, 10, 5, 10));
		((DefaultCaret)textArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		setLayout(new SimpleLayout());
		add(textField, SimpleLayout.constraints(0d, 0d, 1d, 1d / 12d));

		//JScrollPane scroll = new JScrollPane(textArea);
		//scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		//add(scroll, BorderLayout.EAST);
		add(textArea, SimpleLayout.constraints(0d, 1d / 12d, 1d, 11d / 12d));
	}

	public void sendMessage(String message)
	{
		messages.add(message);
		StringBuilder builder = new StringBuilder("<!DOCTYPE html><html>");

		builder.append("<head><style>");

		builder.append(board.getContainer().getResourceManager().getText("chat-style"));

		builder.append("</style></head><body><ul>");

		for(int i = messages.size(); i --> 0 ;) //;)
		{
			builder.append("<li>")
					.append(messages.get(i))
					.append("</li>");
		}

		builder.append("</ul></body></html>");
		textArea.setText(builder.toString());
	}

	public void reset()
	{
		textArea.setText("");
		messages.clear();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		//if(!textField.hasFocus())
		//	return;
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


}