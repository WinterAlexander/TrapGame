package me.winter.trapgame.client;

import me.winter.trapgame.shared.packet.*;
import me.winter.trapgame.util.StringUtil;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Represents a connection established by the client and server
 *
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class ClientConnection
{
	private TrapGameClient client;

	private Socket socket;
	private String playerName;

	public ClientConnection(TrapGameClient client)
	{
		this.client = client;
		socket = null;

		playerName = null;
	}

	public void connectTo(String address, String playerName) throws IOException
	{
		int port = 1254;

		String[] parts = address.split(":");

		if(parts.length == 2 && StringUtil.isInt(parts[1]))
		{
			address = parts[0];
			port = Integer.parseInt(parts[1]);
		}

		socket = new Socket(InetAddress.getByName(address), port);
		sendPacket(new PacketInJoin(this.playerName = playerName));
		new Thread(this::acceptInput).start();
	}

	public void sendPacket(Packet packet)
	{
		if(socket == null)
			throw new IllegalStateException("Currently not connected to any server");

		try
		{
			ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
			new ObjectOutputStream(byteBuffer).writeObject(packet);
			byteBuffer.writeTo(socket.getOutputStream());

			socket.getOutputStream().flush();
		}
		catch(IOException e)
		{
			e.printStackTrace(System.err);
		}
	}

	public void receivePacket(Packet packet)
	{
		if(packet instanceof PacketOutWelcome)
		{
			PacketOutWelcome packetWelcome = (PacketOutWelcome)packet;

			client.getBoard().init(packetWelcome.getPlayerId(), packetWelcome.getPlayers(), packetWelcome.getBoardWidth(), packetWelcome.getBoardHeight());
			client.goToBoard();
			return;
		}

		if(packet instanceof PacketOutJoin)
		{
			client.getBoard().join(((PacketOutJoin)packet).getPlayer());
			return;
		}

		if(packet instanceof PacketOutLeave)
		{
			client.getBoard().leave(((PacketOutLeave)packet).getPlayerId());
			return;
		}

		if(packet instanceof PacketOutPlace)
		{
			client.getBoard().place(((PacketOutPlace)packet).getPlayerId(), ((PacketOutPlace)packet).getLocation());
			return;
		}

		if(packet instanceof PacketOutStatus)
		{
			if(((PacketOutStatus)packet).getStatus() == PacketOutStatus.GAME_START)
				client.getBoard().unlockBoard();
			return;
		}

		if(packet instanceof PacketOutChat)
		{
			client.getBoard().getChat().sendMessage(((PacketOutChat)packet).getMessage());
			return;
		}

		if(packet instanceof PacketOutKick)
		{
			client.getBoard().dispose();
			client.goToMenu();
			JOptionPane.showMessageDialog(client, "You have been kicked out of the server, reason: \n" + ((PacketOutKick)packet).getMessage(), "You have been kicked !", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void acceptInput()
	{
		while(socket.isConnected() && !socket.isClosed() && !socket.isInputShutdown() && !socket.isOutputShutdown()) try
		{
			receivePacket((Packet)new ObjectInputStream(socket.getInputStream()).readObject());
		}
		catch(ClassNotFoundException | ClassCastException notAPacketEx)
		{
			System.err.println("Server sent a socket with unknown data.");
			notAPacketEx.printStackTrace(System.err);
		}
		catch(IOException ex)
		{
			System.err.println("An internal exception occurred while trying to read socket input data, closing it.");
			ex.printStackTrace();
			break;
		}
		close();
	}

	public void close()
	{
		client.goToMenu();

		if(socket == null || socket.isClosed())
			return;

		try
		{
			socket.close();
		}
		catch(IOException ex)
		{
			System.err.println("An internal error occurred while closing socket with server");
			ex.printStackTrace(System.err);
		}

		socket = null;
	}

}
