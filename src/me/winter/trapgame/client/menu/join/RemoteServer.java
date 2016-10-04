package me.winter.trapgame.client.menu.join;

import me.winter.trapgame.client.BroadcastResponse;
import me.winter.trapgame.client.TextAnimation;
import me.winter.trapgame.client.TrapGameClient;
import me.winter.trapgame.shared.packet.PacketOutPong;
import me.winter.trapgame.util.Callback;

import java.awt.Color;
import java.net.InetAddress;
import java.util.Random;

/**
 * Class representing a TrapGame server to connect on.
 * Holds informations displayed in the client about this server
 * Created by 1541869 on 2016-10-03.
 */
public class RemoteServer
{
	private ServerList serverList;
	private ServerPanel display;

	private PacketOutPong response;

	private String connectionString;
	private InetAddress address;
	private int port;
	private int ping;

	private boolean pinging = false;

	/**
	 * Instanciates a remote server from the serverlist and the connectionString
	 * Try parsing the connection strings Fail is silent
	 * At this states the server hasn't been ping
	 * @param serverList instance of the serverlist
	 * @param connectionString string input by user to connect
	 */
	public RemoteServer(ServerList serverList, String connectionString)
	{
		this.serverList = serverList;
		this.display = new ServerPanel(this, connectionString);
		this.connectionString = connectionString;

		try
		{
			String[] parts = connectionString.split(":");

			this.address = InetAddress.getByName(parts[0]);
			if(parts.length > 1)
				this.port = Integer.parseInt(parts[1]);
			else
				this.port = 1254;
		}
		catch(Exception ex)
		{
			display.setName(serverList.getJoinForm().getMenu().getLangLine("client_join_invalidip"));
			this.address = null;
			this.port = -1;
		}

		response = null;
		ping = Integer.MAX_VALUE;
	}

	/**
	 * Instanciates a remote server from an already pinged server (broadcast)
	 * @param serverList instance of the serverlist
	 * @param broadcast broadcastResponse with server infos
	 */
	public RemoteServer(ServerList serverList, BroadcastResponse broadcast)
	{
		this.serverList = serverList;
		this.response = broadcast.getPong();
		this.address = broadcast.getAddress();
		this.port = broadcast.getPort();
		this.ping = broadcast.getPingDelay();
		this.connectionString = address.getHostAddress() + ":" + port;


		this.display = new ServerPanel(this, connectionString);
		display.setPing(ping);
		display.setName(response.getName());
		display.setPlayers(response.getPlayers(), response.getSlots());
	}

	/**
	 * Pings the RemoteServer asynchroniously and updates it's display's info
	 */
	public void ping()
	{
		ping(x -> {});
	}

	/**
	 * Pings the RemoteServer asynchroniously and updates it's display's info
	 * @param callBack called when ping is finished, sends true if successful, otherwise false
	 */
	public void ping(Callback<Boolean> callBack)
	{
		if(pinging)
			return;

		final TextAnimation pingAnimation = new TextAnimation(display.getPingLabel(), ".  ", ".. ", "...");
		getServerList().getJoinForm().getMenu().getClient().getScheduler().addTask(pingAnimation);
		pinging = true;

		new Thread(() -> {

			display.setPing("...");

			try
			{
				long timestamp = System.nanoTime();
				response = serverList.getJoinForm().getMenu().getClient().getConnection().ping(address, port, 10_000);

				ping = (int)((System.nanoTime() - timestamp) / 1_000_000);
				display.setPing(ping);
				display.setName(response.getName());
				display.setPlayers(response.getPlayers(), response.getSlots());
				callBack.call(true);
			}
			catch(Exception ex)
			{
				display.setPing("X");
				display.getPingLabel().setForeground(Color.RED);
				callBack.call(false);
			}
			finally
			{
				pingAnimation.cancel();
				this.pinging = false;
			}

		}).start();
	}

	public void connectTo()
	{
		TrapGameClient client = getServerList().getJoinForm().getMenu().getClient();

		String name = getServerList().getJoinForm().getPlayerName().getText();

		if(name.length() < 3)
			name = "Guest" + new Random().nextInt(10000);

		if(name.length() > 20)
			name = name.substring(0, 20);
		client.getConnection().connectTo(address, port, name);

		display.getJoinButton().setForeground(new Color(0, 0, 0, 0));
	}

	public boolean isValid()
	{
		return address != null && port > 0;
	}

	public ServerPanel getDisplay()
	{
		return display;
	}

	public ServerList getServerList()
	{
		return serverList;
	}

	public PacketOutPong getResponse()
	{
		return response;
	}

	public int getPlayers()
	{
		return response == null ? 0 : response.getPlayers();
	}

	public String getName()
	{
		return response == null ? connectionString : response.getName();
	}

	public InetAddress getAddress()
	{
		return address;
	}

	public void setAddress(InetAddress address)
	{
		this.address = address;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public int getPing()
	{
		return ping;
	}
}
