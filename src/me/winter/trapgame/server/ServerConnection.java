package me.winter.trapgame.server;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.packet.Packet;
import me.winter.trapgame.shared.packet.PacketInJoin;
import me.winter.trapgame.shared.packet.PacketOutKick;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represents a connection from the server side accepting new clients
 * Is an handler for a ServerSocket
 *
 * Created by winter on 25/03/16.
 */
public class ServerConnection
{
	private TrapGameServer server;

	private ServerSocket serverSocket;
	private boolean acceptNewClients;

	public ServerConnection(TrapGameServer server, int port)
	{
		try
		{
			this.server = server;
			serverSocket = new ServerSocket(port);

			new Thread(this::acceptClients).start();
			acceptNewClients = true;
			System.out.println("The server is listening on " + port);
		}
		catch(IOException exception)
		{
			throw new ExceptionInInitializerError(exception);
		}
	}

	private void acceptClients()
	{
		while(isAcceptingNewClients()) try
		{
			Socket socket = serverSocket.accept();

			PacketInJoin packet = new PacketInJoin();
			if(!new DataInputStream(socket.getInputStream()).readUTF().equals("PacketInJoin"))
				continue;

			packet.readFrom(socket.getInputStream());

			if(server.getPassword() != null && server.getPassword().length() > 0 && !server.getPassword().equals(packet.getPassword()))
			{
				new DataOutputStream(socket.getOutputStream()).writeUTF("PacketOutKick");
				new PacketOutKick("Invalid password.").writeTo(socket.getOutputStream());

				socket.getOutputStream().flush();
				socket.close();
				continue;
			}

			String name = packet.getPlayerName();

			while(!server.isAvailable(name))
				name += "_";

			int id = server.generateNewPlayerId();

			PlayerInfo info = new PlayerInfo(id, name, server.getColor(id), server.getStatsManager().load(name), 0.5f, 0.5f);

			server.join(new Player(server, info, socket));

		}
		catch(Exception ex)
		{
			if(server.isDebugMode())
				System.err.println(ex);
		}
	}

	public void sendToAll(Packet packet)
	{
		server.getPlayers().forEach(player -> player.getConnection().sendPacket(packet));
	}

	public void sendToAllLater(Packet packet)
	{
		server.getPlayers().forEach(player -> player.getConnection().sendPacketLater(packet));
	}

	public void close()
	{
		try
		{
			getServerSocket().close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace(System.err);
		}
	}

	public ServerSocket getServerSocket()
	{
		return serverSocket;
	}

	public boolean isAcceptingNewClients()
	{
		return acceptNewClients;
	}

	public void setAcceptingNewClients(boolean acceptNewClients)
	{
		this.acceptNewClients = acceptNewClients;
	}
}
