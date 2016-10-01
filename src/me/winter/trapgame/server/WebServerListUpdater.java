package me.winter.trapgame.server;

import me.winter.trapgame.shared.Task;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * <p>Used to update the server's status on the webserver for users to see the server in the list</p>
 *
 * <p>Created by 1541869 on 2016-04-25.</p>
 */
public class WebServerListUpdater extends Task
{
	private List<String> addressList;
	private TrapGameServer server;

	public WebServerListUpdater(TrapGameServer server)
	{
		super(2000, true);
		this.server = server;
		this.addressList = new ArrayList<>(); //TODO load them from file
		addressList.add("http://trapgame.ml/");
		addressList.add("http://127.0.0.1/");

		server.getScheduler().addTask(this);
	}

	@Override
	public void run()
	{
		addressList.forEach(x -> new Thread(() -> update(x)).start());
	}

	private void update(String serverAddr)
	{
		try
		{
			URL url = new URL(serverAddr);

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
			addressList.remove(serverAddr);
		}
		catch(IOException ex)
		{
			server.getLogger().log(Level.INFO, "Server " + serverAddr + " couldn't be reached.");
		}
	}
}
