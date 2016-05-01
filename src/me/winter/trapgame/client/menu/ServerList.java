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
import java.util.Random;
import java.util.function.Predicate;
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

	private boolean updating;

	public ServerList(JoinForm joinForm)
	{
		this.joinForm = joinForm;

		servers = new ArrayList<>();
		updating = false;

		setLayout(new BorderLayout());

		JScrollPane scroller = new JScrollPane();

		content = new JPanel();
		content.setBackground(new Color(110, 110, 110));
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
		if(updating)
			return;

		new Thread(this::updateList).start();
	}

	private void updateList()
	{
		updating = true;
		servers.clear();

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
		try
		{
				servers.add(new ServerPanel(this, "abc", 9, 16, InetAddress.getByName("google.com"), InetAddress.getLocalHost(), 1111));
				servers.add(new ServerPanel(this, "zxw", 7, 32, InetAddress.getByName("facebook.com"), InetAddress.getLocalHost(), 1112));
				servers.add(new ServerPanel(this, "hmmmm", 6, 1, InetAddress.getByName("messenger.com"), InetAddress.getLocalHost(), 1234));
				servers.add(new ServerPanel(this, "Fun Server 2", 0, 3, InetAddress.getByName("trapgame.ml"), InetAddress.getLocalHost(), 7777));

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		SwingUtilities.invokeLater(this::placeServers);

		updating = false;
	}

	public void placeServers()
	{
		content.removeAll();
		content.add(Box.createRigidArea(new Dimension(0, 5)));

		servers.sort(joinForm.getSortComparator());

		servers.forEach(server -> {
			content.add(server);
			content.add(Box.createRigidArea(new Dimension(0, 5)));
		});

		content.setPreferredSize(content.getPreferredSize());

		content.revalidate();
		content.repaint();
	}

	public JPanel getContent()
	{
		return content;
	}
}
