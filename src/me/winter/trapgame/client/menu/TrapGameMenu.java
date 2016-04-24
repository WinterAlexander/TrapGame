package me.winter.trapgame.client.menu;

import me.winter.trapgame.client.ImagePanel;
import me.winter.trapgame.client.SimpleLayout;
import me.winter.trapgame.client.TrapGameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
			public void paintComponent(Graphics graphics)
			{
				Graphics2D g2draw = (Graphics2D) graphics;

				g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

				g2draw.drawImage(getClient().getResourceManager().getImage("background"), 0, 0, TrapGameMenu.this.getWidth(), TrapGameMenu.this.getHeight(), null);

				super.paintComponent(graphics);
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
				setRightPane(new DemoPlayBoard(TrapGameMenu.this, 6));
			}
		});


		String line2 = getClient().getLang().getLine("client_copyright_line2");

		if(!line2.contains("Alexander Winter"))
			throw new Error("Piracy");

		JLabel copyright = new JLabel("<html><div style=\"text-align: right\">" +
				getClient().getLang().getLine("client_copyright_line1") +
				"<br>" +
				line2 +
				"</div></html>");

		copyright.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		copyright.setHorizontalAlignment(JLabel.RIGHT);
		copyright.setVerticalAlignment(JLabel.TOP);
		copyright.setFont(new Font("Verdana", Font.PLAIN, 16));
		/*copyright.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if(!Desktop.isDesktopSupported())
					return;

				Desktop desktop = Desktop.getDesktop();

				if(!desktop.isSupported(Desktop.Action.BROWSE))
					return;

				try
				{
					desktop.browse(new URL("https://github.com/WinterGuardian/TrapGame").toURI());
				}
				catch(Exception ex)
				{
					getClient().getLogger().log(Level.INFO, "Couldn't open github webpage", ex);
				}
			}
		});*/

		JButton howtoplay = new MenuButton(this, "client_howto_button");
		howtoplay.addActionListener(event -> setRightPane(new TutorialPane(this)));

		JButton joinGame = new MenuButton(this, "client_join_button");
		joinGame.addActionListener(event -> setRightPane(new OldJoinForm(getClient())));

		JButton hostGame = new MenuButton(this, "client_host_button");
		hostGame.addActionListener(event -> setRightPane(new HostForm()));

		JButton leave = new MenuButton(this, "client_leave_button");
		leave.addActionListener(event -> getClient().stop());

		JButton french = new CountryFlag(this, "french");
		JButton english = new CountryFlag(this, "english");

		buttonContainer.add(logo, SimpleLayout.constraints(8, 9,
				0, 1, 8, 1.6));

		buttonContainer.add(copyright, SimpleLayout.constraints(8, 9,
				4, 2.4, 4, 1.1));

		buttonContainer.add(howtoplay, SimpleLayout.constraints(8, 9,
				2, 3.5, 4, 0.75));

		buttonContainer.add(joinGame, SimpleLayout.constraints(8, 9,
				2, 4.5, 4, 0.75));

		buttonContainer.add(hostGame, SimpleLayout.constraints(8, 9,
				2, 5.5, 4, 0.75));

		buttonContainer.add(leave, SimpleLayout.constraints(8, 9,
				2, 7, 4, 0.75));

		buttonContainer.add(french, SimpleLayout.constraints(8, 9,
				0.1, 9 - 0.1 - 0.54, 0.9, 0.54));

		buttonContainer.add(english, SimpleLayout.constraints(8, 9,
				1.1, 9 - 0.1 - 0.54, 0.9, 0.54));

		add(buttonContainer);
		add(new DemoPlayBoard(this, 6));
	}

	public Component getRightPane()
	{
		return getComponent(1);
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
