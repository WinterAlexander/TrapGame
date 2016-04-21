package me.winter.trapgame.client.menu;

import me.winter.trapgame.client.SimpleLayout;
import me.winter.trapgame.client.TrapGameClient;
import me.winter.trapgame.shared.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;

/**
 * <p>A BorderLayout menu separated by 2 sections</p>
 *
 * <p>Created by 1541869 on 2016-04-19.</p>
 */
public class TrapGameMenu extends JPanel
{
	private TrapGameClient client;

	public TrapGameMenu(TrapGameClient client)
	{
		this.client = client;

		setLayout(new GridLayout(1, 2));

		JPanel buttonContainer = new JPanel()
		{
			@Override
			public void paint(Graphics graphics)
			{
				Graphics2D g2draw = (Graphics2D) graphics;

				g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

				g2draw.drawImage(getClient().getResourceManager().getImage("background"), 0, 0, getClient().getWidth(), getClient().getHeight(), null);

				super.paint(graphics);
			}
		};
		buttonContainer.setBackground(new Color(0, 0, 0, 0));
		buttonContainer.setLayout(new SimpleLayout());

		ImagePanel logo = new ImagePanel(client.getResourceManager().getImage("logo"));
		logo.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				setRightPane(new DemoPlayBoard(TrapGameMenu.this));
			}
		});

		JButton howtoplay = new JButton(getLangLine("client_howto_button"));
		howtoplay.addActionListener(event -> setRightPane(new TutorialPane(this)));

		JButton joinGame = new JButton(getLangLine("client_join_button"));
		joinGame.addActionListener(event -> setRightPane(new JoinForm(getClient())));

		JButton hostGame = new JButton(getLangLine("client_host_button"));
		hostGame.addActionListener(event -> setRightPane(new HostForm()));

		JButton leave = new JButton(getLangLine("client_leave_button"));
		leave.addActionListener(event -> getClient().stop());

		buttonContainer.add(logo, SimpleLayout.constraints(8d, 9,
				0, 1, 8, 8d / 5d));

		buttonContainer.add(howtoplay, SimpleLayout.constraints(8, 9,
				2, 4, 4, 0.75));

		buttonContainer.add(joinGame, SimpleLayout.constraints(8, 9,
				2, 5, 4, 0.75));

		buttonContainer.add(hostGame, SimpleLayout.constraints(8, 9,
				2, 6, 4, 0.75));

		buttonContainer.add(leave, SimpleLayout.constraints(8, 9,
				2, 7, 4, 0.75));


		add(buttonContainer);
		add(new DemoPlayBoard(this));
	}

	public void setRightPane(Component component)
	{
		remove(1);
		add(component);
		revalidate();
		repaint();
	}

	public String getLangLine(String line)
	{
		return client.getLang().getLine(line);
	}

	public TrapGameClient getClient()
	{
		return client;
	}

	public void setClient(TrapGameClient client)
	{
		this.client = client;
	}
}
