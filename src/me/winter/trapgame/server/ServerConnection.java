package me.winter.trapgame.server;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.packet.Packet;
import me.winter.trapgame.shared.packet.PacketInJoin;
import me.winter.trapgame.shared.packet.PacketOutKick;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Represents a connection from the server side accepting new clients
 * Is an handler for a ServerSocket
 *
 * Created by winter on 25/03/16.
 */
public class ServerConnection
{
	private TrapGameServer server;

	private List<DatagramPacket> toSend;
	private DatagramSocket udpSocket;
	private byte[] inputBuffer;
	private boolean acceptNewClients;

	public ServerConnection(TrapGameServer server, int port)
	{
		try
		{
			this.server = server;

			toSend = new ArrayList<>();
			inputBuffer = new byte[8 * 1024];
			if(port > 0)
				udpSocket = new DatagramSocket(port);
			else
				udpSocket = new DatagramSocket();

			new Thread(this::acceptInput).start();
			new Thread(this::sendOutput).start();
			new Thread(this::lookForAlive).start();

			acceptNewClients = true;
			server.getLogger().info("The server is listening on " + udpSocket.getLocalPort());
		}
		catch(IOException exception)
		{
			throw new ExceptionInInitializerError(exception);
		}
	}

	private void acceptInput()
	{
		while(isOpen()) try
		{
			DatagramPacket bufPacket = new DatagramPacket(inputBuffer, inputBuffer.length);

			udpSocket.receive(bufPacket);

			Player player = getPlayer(bufPacket.getAddress(), bufPacket.getPort());

			ByteArrayInputStream byteStream = new ByteArrayInputStream(inputBuffer);

			String packetName = new DataInputStream(byteStream).readUTF();

			if(packetName.equals("KeepAlive"))
			{
				keepAlive(bufPacket.getAddress(), bufPacket.getPort());
				if(player != null)
					player.getConnection().keepAlive();
				continue;
			}


			Packet packet = (Packet)Class.forName("me.winter.trapgame.shared.packet." + packetName).newInstance();
			packet.readFrom(byteStream);

			//if(server.isDebugMode()) ab00se
			//	System.out.println("Received " + packet.getClass().getSimpleName() + " from " + bufPacket.getAddress().toString() + " port: " + bufPacket.getPort());

			if(player != null)
			{
				player.getConnection().receivePacketLater(packet);
				continue;
			}

			if(!(packet instanceof PacketInJoin) || !isAcceptingNewClients())
				continue;

			if(server.getPassword() != null && server.getPassword().length() > 0 && !server.getPassword().equals(((PacketInJoin)packet).getPassword()))
			{
				sendPacket(new PacketOutKick("Invalid password."), bufPacket.getAddress(), bufPacket.getPort());
				continue;
			}

			String name = ((PacketInJoin)packet).getPlayerName();

			String invalidReason = getInvalidNameReason(name);
			if(invalidReason != null)
			{
				sendPacket(new PacketOutKick(invalidReason), bufPacket.getAddress(), bufPacket.getPort());
				continue;
			}

			while(!server.isAvailable(name))
				name += "_";

			int id = server.generateNewPlayerId();

			PlayerInfo info = new PlayerInfo(id, name, server.getColor(id), server.getStatsManager().load(name), 0.5f, 0.5f);

			server.join(new Player(server, info, bufPacket.getAddress(), bufPacket.getPort()));

		}
		catch(SocketException ex)
		{

		}
		catch(Exception ex)
		{
			server.getLogger().log(Level.WARNING, "Unexpected exception", ex);
		}
	}

	private void sendOutput()
	{
		while(isOpen())
		{
			for(DatagramPacket packet : new ArrayList<>(toSend))
			{
				try
				{
					if(packet != null)
						udpSocket.send(packet);
					toSend.remove(packet);
				}
				catch(IOException ex)
				{
					ex.printStackTrace(System.err);
				}
			}

			synchronized(this)
			{
				try
				{
					wait();
				}
				catch(InterruptedException ex)
				{
					ex.printStackTrace(System.err);
				}
			}
		}
	}

	private void lookForAlive()
	{
		while(isOpen())
		{
			for(Player player : new ArrayList<>(server.getPlayers()))
			{
				if(!server.getPlayers().contains(player))
					continue;

				if(System.currentTimeMillis() - player.getConnection().getLastPacketReceived() > 30_000)
					player.timeOut();
			}

			try
			{
				Thread.sleep(5000);
			}
			catch(InterruptedException ex)
			{
				ex.printStackTrace(System.err);
			}
		}
	}

	public void keepAlive(InetAddress address, int port)
	{
		try
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			new DataOutputStream(byteStream).writeUTF("KeepAlive");

			DatagramPacket data = new DatagramPacket(byteStream.toByteArray(), byteStream.size(), address, port);

			synchronized(this)
			{
				toSend.add(data);
				notify();
			}
		}
		catch(Exception ex)
		{
			if(server.isDebugMode())
				ex.printStackTrace(System.err);
		}
	}

	public void sendPacketLater(Packet packet, InetAddress address, int port)
	{
		try
		{

			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			new DataOutputStream(byteStream).writeUTF(packet.getClass().getSimpleName());
			packet.writeTo(byteStream);

			DatagramPacket data = new DatagramPacket(byteStream.toByteArray(), byteStream.size(), address, port);

			synchronized(this)
			{
				toSend.add(data);
				notify();
			}
		}
		catch(Exception ex)
		{
			if(server.isDebugMode())
				ex.printStackTrace(System.err);
		}
	}

	public void sendPacket(Packet packet, InetAddress address, int port)
	{
		try
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			new DataOutputStream(byteStream).writeUTF(packet.getClass().getSimpleName());
			packet.writeTo(byteStream);

			DatagramPacket data = new DatagramPacket(byteStream.toByteArray(), byteStream.size(), address, port);

			udpSocket.send(data);
		}
		catch(Exception ex)
		{
			if(server.isDebugMode())
				ex.printStackTrace(System.err);
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

	public Player getPlayer(InetAddress address, int port)
	{
		for(Player player : server.getPlayers())
			if(player.getConnection().getAddress().equals(address)
			&& player.getConnection().getPort() == port)
				return player;

		return null;
	}

	public boolean isOpen()
	{
		return !udpSocket.isClosed();
	}

	public synchronized void close()
	{
		getUdpSocket().close();
		notify();
	}

	public DatagramSocket getUdpSocket()
	{
		return udpSocket;
	}

	public boolean isAcceptingNewClients()
	{
		return acceptNewClients;
	}

	public void setAcceptingNewClients(boolean acceptNewClients)
	{
		this.acceptNewClients = acceptNewClients;
	}

	public static String getInvalidNameReason(String name)
	{
		if(name.length() < 3)
			return "Your name should have at least 3 characters.";

		if(name.length() > 20)
			return "Your name can't have more than 20 characters.";

		if(!name.matches("^[A-Za-z0-9_]+$"))
			return "Your name can only have letters, numbers and underscores.";

		return null;
	}
}
