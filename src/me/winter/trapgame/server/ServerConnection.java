package me.winter.trapgame.server;

import me.winter.trapgame.shared.packet.Packet;
import me.winter.trapgame.shared.packet.PacketInJoin;

import java.io.IOException;
import java.io.ObjectInputStream;
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

	public ServerConnection(TrapGameServer server, int port) throws IOException
	{
		this.server = server;
		serverSocket = new ServerSocket(port);

		new Thread(this::acceptClients).start();
		acceptNewClients = true;
	}

	public ServerSocket getServerSocket()
	{
		return serverSocket;
	}

	private void acceptClients()
	{
		while(acceptNewClients) try
		{
			Socket socket = serverSocket.accept();

			PacketInJoin packet = (PacketInJoin)new ObjectInputStream(socket.getInputStream()).readObject();

			String name = packet.getPlayerName();

			while(!server.isAvailable(name))
				name += "_";

		}
		catch(ClassCastException classCastEx)
		{
			System.err.println("Something sent a socket that isn't a PacketInJoin");
			classCastEx.printStackTrace(System.err);
		}
		catch(Exception ex)
		{
			System.err.println("An internal error occured while trying to accept a new client's connection");
			ex.printStackTrace(System.err);
		}
	}
}
