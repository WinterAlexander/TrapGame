package me.winter.trapgame.client.menu;

import me.winter.trapgame.client.SimpleLayout;
import me.winter.trapgame.server.WebServerListUpdater;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
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
	private List<ServerPanel> servers;

	public ServerList(JoinForm joinForm)
	{
		this.joinForm = joinForm;

		servers = new ArrayList<>();

		setLayout(new BorderLayout());

		JScrollPane scroller = new JScrollPane();

		content = new JPanel();
		content.setBackground(getBackground().darker());
		/*content.setLayout(new SimpleLayout()
		{
			@Override
			public Dimension minimumLayoutSize(Container parent)
			{
				return preferredLayoutSize(parent);
			}

			@Override
			public Dimension preferredLayoutSize(Container parent)
			{
				double height = 0;

				for(ComponentGuide guide : guides)
				{
					if(guide.getY() + getHeight() > height)
						height = guide.getY() + getHeight();
				}

				return new Dimension(ServerList.this.getWidth(), (int)(height * ServerList.this.getHeight()));
			}
		});*/
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		scroller.setViewportView(content);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		add(scroller, BorderLayout.CENTER);

		//update();
	}

	public JoinForm getJoinForm()
	{
		return joinForm;
	}

	public void update()
	{
		content.removeAll();
		servers.clear();

		content.add(Box.createRigidArea(new Dimension(0, 5)));

		try
		{
			URL url = new URL(WebServerListUpdater.WEB_SERVER_ADDR);

			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);

			OutputStreamWriter writer = new OutputStreamWriter(new BufferedOutputStream(connection.getOutputStream()));

			writer.write("action=query");
			writer.flush();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line;

			while((line = reader.readLine()) != null)
				servers.add(new ServerPanel(this, line));

			writer.close();
			reader.close();

		}
		catch(MalformedURLException ex)
		{
			joinForm.getMenu().getClient().getLogger().log(Level.SEVERE, "It seem format of urls changed since 2016 !", ex);
		}
		catch(IOException ex)
		{
			joinForm.getMenu().getClient().getLogger().log(Level.WARNING, "An exception occurred while receiving server's data from webserver", ex);
		}
		catch(IllegalArgumentException ex)
		{
			joinForm.getMenu().getClient().getLogger().log(Level.WARNING, "Webserver's data seem corrupted", ex);
		}

		//to test only
//		try
//		{
//			servers.add(new ServerPanel(this, "Example1", 9, 16, InetAddress.getByName("google.com"), InetAddress.getLocalHost(), 1111));
//			servers.add(new ServerPanel(this, "Example2", 7, 32, InetAddress.getByName("facebook.com"), InetAddress.getLocalHost(), 1112));
//			servers.add(new ServerPanel(this, "Example45", 6, 1, InetAddress.getByName("messenger.com"), InetAddress.getLocalHost(), 1234));
//			servers.add(new ServerPanel(this, "Example11", 0, 3, InetAddress.getByName("trapgame.ml"), InetAddress.getLocalHost(), 7777));
//
//		}
//		catch(Exception ex)
//		{
//			ex.printStackTrace();
//		}

		servers.sort((a, b) -> b.getPlayers() - a.getPlayers());

		servers.forEach(server -> {
			content.add(server);
			content.add(Box.createRigidArea(new Dimension(0, 5)));
		});

		content.setPreferredSize(content.getPreferredSize());
		//this.getViewport().add(Box.createVerticalGlue());
	}

	public JPanel getContent()
	{
		return content;
	}
}
