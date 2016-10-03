package me.winter.trapgame.client.menu.join;

import me.winter.trapgame.util.ColorTransformer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * <p>A simple panel containing the list of available servers</p>
 *
 * <p>Created by 1541869 on 2016-04-22.</p>
 */
public class ServerList extends JPanel
{
	private JoinForm joinForm;

	private JPanel content;
	private List<RemoteServer> servers;

	private boolean updating;

	public ServerList(JoinForm joinForm)
	{
		this.joinForm = joinForm;

		servers = new ArrayList<>();
		updating = false;

		setBorder(new EmptyBorder(0, 0, 0, 0));

		setLayout(new BorderLayout());

		JScrollPane scroller = new JScrollPane();

		content = new JPanel();
		content.setBackground(new Color(110, 110, 110));
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBorder(new EmptyBorder(0, 0, 0, 0));

		scroller.setViewportView(content);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroller.setBorder(new EmptyBorder(0, 0, 0, 0));
		scroller.setViewportBorder(new EmptyBorder(0, 0, 0, 0));

		add(scroller, BorderLayout.CENTER);


		servers.add(new RemoteServer(this, "127.0.0.1:1254"));

		//updateDisplay();
		//update();
	}

	public void update()
	{
		if(updating)
			return;

		new Thread(this::updateList).start();
	}

	private void updateList()
	{
		updating = true;

		servers.forEach(RemoteServer::ping);

		SwingUtilities.invokeLater(this::updateDisplay);

		updating = false;
	}

	/**
	 * Clears the ServerPanels and readd them
	 */
	public void updateDisplay()
	{
		content.removeAll();
		content.add(Box.createRigidArea(new Dimension(0, 5)));

		if(servers.size() == 0)
		{
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.setBackground(ColorTransformer.TRANSPARENT);
			panel.add(new JLabel(joinForm.getMenu().getLangLine("client_join_emptylist"), JLabel.CENTER), BorderLayout.CENTER);


			content.add(panel);
		}
		else
		{
			servers.sort(joinForm.getSortComparator());

			servers.forEach(server -> {
				content.add(server.getDisplay());
				content.add(Box.createRigidArea(new Dimension(0, 5)));
			});
		}


		content.setPreferredSize(content.getPreferredSize());

		content.revalidate();
		content.repaint();
	}

	public JPanel getContent()
	{
		return content;
	}

	public JoinForm getJoinForm()
	{
		return joinForm;
	}
}
