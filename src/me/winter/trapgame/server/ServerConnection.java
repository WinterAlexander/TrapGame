package me.winter.trapgame.server;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.packet.Packet;
import me.winter.trapgame.shared.packet.PacketInJoin;
import me.winter.trapgame.shared.packet.PacketOutJoin;

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

	public ServerConnection(TrapGameServer server, int port)
	{
		try
		{
			this.server = server;
			serverSocket = new ServerSocket(port);

			new Thread(this::acceptClients).start();
			acceptNewClients = true;
		}
		catch(IOException exception)
		{
			throw new ExceptionInInitializerError(exception);
		}
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

			int id = server.generateNewPlayerId();

			PlayerInfo info = new PlayerInfo(id, name, server.getColor(id), server.getStatsManager().load(name));

			server.join(new Player(server, info, socket));

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

	public void sendToAll(Packet packet)
	{
		server.getPlayers().forEach(player -> player.getConnection().sendPacket(packet));
	}

	public ServerSocket getServerSocket()
	{
		return serverSocket;
	}
}
