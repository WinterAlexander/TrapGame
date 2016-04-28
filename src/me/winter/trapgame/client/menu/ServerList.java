package me.winter.trapgame.client.menu;

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

	private JScrollPane scroller;

	public ServerList(JoinForm joinForm)
	{
		this.joinForm = joinForm;

		setLayout(new BorderLayout());

		scroller = new JScrollPane();
		scroller.setViewport(new JViewport());
		scroller.getViewport().setLayout(new BoxLayout(scroller.getViewport(), BoxLayout.Y_AXIS));

		add(scroller, BorderLayout.CENTER);

		update();
	}

	public JoinForm getJoinForm()
	{
		return joinForm;
	}

	public void update()
	{
		scroller.getViewport().removeAll();

		List<ServerPanel> servers = new ArrayList<>();

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

		servers.sort((a, b) -> b.getPlayers() - a.getPlayers());

		servers.forEach(scroller.getViewport()::add);

		//this.getViewport().add(Box.createVerticalGlue());
	}
}
