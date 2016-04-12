package me.winter.trapgame.client.board;

import me.winter.trapgame.shared.packet.PacketInChat;

import javax.swing.*;
import java.io.*;
import java.util.List;
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

	private String style;

	public Chat(TrapGameBoard board)
	{
		this.board = board;

		setBackground(new Color(0, 0, 0, 0));

		messages = new ArrayList<>();

		textField = new JTextField();
		textArea = new JTextPane()
		{
			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
			}
		};

		textField.addKeyListener(this);

		textArea.setContentType("text/html");
		textArea.setEditable(false);
		textArea.setFont(new Font("Arial", Font.PLAIN, 18));
		textArea.setForeground(Color.WHITE);
		textArea.setBackground(new Color(0, 0, 0));

		textField.setFont(new Font("Arial", Font.PLAIN, 18));
		textField.setForeground(Color.WHITE);
		textField.setBackground(new Color(0, 0, 0));
		((DefaultCaret)textArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		setLayout(new BorderLayout());
		add(textArea, BorderLayout.CENTER);

		//JScrollPane scroll = new JScrollPane(textArea);
		//scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		//add(scroll, BorderLayout.EAST);
		add(textField, BorderLayout.SOUTH);

		try
		{
			StringBuilder styleBuilder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(Chat.class.getResourceAsStream("/chat.css")));

			String line;
			while((line = reader.readLine()) != null)
				styleBuilder.append(line);

			reader.close();
			style = styleBuilder.toString();
		}
		catch(IOException ex)
		{
			ex.printStackTrace(System.err);
			style = "";
		}
	}

	public void sendMessage(String message)
	{
		messages.add(message);
		StringBuilder builder = new StringBuilder("<!DOCTYPE html><html>");

		builder.append("<head><style>");

		builder.append(style);

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
		if(!textField.hasFocus())
			return;
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
