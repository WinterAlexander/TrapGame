package me.winter.trapgame.server;

import me.winter.trapgame.shared.Task;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

/**
 * <p>Used to update the server's status on the webserver for users to see the server in the list</p>
 *
 * <p>Created by 1541869 on 2016-04-25.</p>
 */
public class WebServerListUpdater extends Task
{
	public static final String WEB_SERVER_ADDR = "http://trapgame.ml/";

	private TrapGameServer server;

	public WebServerListUpdater(TrapGameServer server)
	{
		super(2000, true);
		this.server = server;
		server.getScheduler().addTask(this);
	}

	@Override
	public void run()
	{
		new Thread(this::update).start();
	}

	private void update()
	{
		try
		{
			URL url = new URL(WEB_SERVER_ADDR);

			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			//connection.setDoInput(false);

			OutputStreamWriter writer = new OutputStreamWriter(new BufferedOutputStream(connection.getOutputStream()));

			String string = "action=update" +
					"&name=" + server.getName() +
					"&port=" + server.getConnection().getUdpSocket().getLocalPort() +
					"&lanip=" + InetAddress.getLocalHost().getHostAddress() +
					"&players=" + server.getPlayers().size() +
					"&slots=" + server.getMaxPlayers();

			writer.write(string);

			writer.flush();
			writer.close();
			connection.getInputStream().close();
		}
		catch(MalformedURLException ex)
		{
			server.getLogger().log(Level.SEVERE, "It seem format of urls changed since 2016 !", ex);
			cancel();
		}
		catch(IOException ex)
		{
			server.getLogger().log(Level.WARNING, "An exception occurred while sending server's data to webserver", ex);
		}
	}
}
