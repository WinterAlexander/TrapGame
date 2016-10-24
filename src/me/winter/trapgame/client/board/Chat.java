package me.winter.trapgame.client.board;

import me.winter.trapgame.client.SimpleLayout;
import me.winter.trapgame.shared.packet.PacketInChat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

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

		messages = new ArrayList<>(100);

		textField = new JTextField()
		{
			@Override
			public void paintComponent(Graphics g)
			{
				Graphics2D g2draw = (Graphics2D)g;

				g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

				g2draw.drawImage(board.getContainer().getResourceManager().getImage("background"), -Chat.this.getX() - getX(), -Chat.this.getY() - getY(), board.getWidth(), board.getHeight(), null);
				g2draw.drawImage(board.getContainer().getResourceManager().getImage("chat-input"), 0, 0, getWidth(), getHeight(), null);

				super.paintComponent(g);
			}
		};

		textArea = new JTextPane()
		{
			@Override
			public void paintComponent(Graphics g)
			{
				Graphics2D g2draw = (Graphics2D)g;

				g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

				g2draw.drawImage(board.getContainer().getResourceManager().getImage("background"), -Chat.this.getX() - getX(), -Chat.this.getY() - getY(), board.getWidth(), board.getHeight(), null);

				super.paintComponent(g);
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
		if(messages.size() >= 20)
			messages.remove(0);

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
