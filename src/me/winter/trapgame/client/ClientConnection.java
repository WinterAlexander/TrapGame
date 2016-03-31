package me.winter.trapgame.client;

import me.winter.trapgame.shared.packet.*;
import me.winter.trapgame.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

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
	private boolean welcomed;

	public ClientConnection(TrapGameClient client)
	{
		this.client = client;
		socket = null;
	}

	public void connectTo(String address, String password, String playerName) throws IOException, TimeoutException
	{
		int port = 1254;

		String[] parts = address.split(":");

		if(parts.length == 2 && StringUtil.isInt(parts[1]))
		{
			address = parts[0];
			port = Integer.parseInt(parts[1]);
		}

		socket = new Socket(InetAddress.getByName(address), port);
		sendPacket(new PacketInJoin(password, playerName));

		welcomed = false;
		new Thread(this::acceptInput).start();
		long waitBegin = System.currentTimeMillis();


		while(!welcomed)
		{
			try
			{
				Thread.sleep(50);
			}
			catch(InterruptedException ex)
			{

			}

			if(System.currentTimeMillis() - waitBegin > 2000)
			{
				close();
				throw new TimeoutException("The server isn't responding.");
			}
		}
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

			welcomed = true;
			client.getBoard().init(packetWelcome.getPlayerId(), packetWelcome.getPlayers());
			client.goToBoard();
			return;
		}

		if(packet instanceof PacketOutBoardSize)
		{
			client.getBoard().setBoardSize(((PacketOutBoardSize)packet).getBoardWidth(), ((PacketOutBoardSize)packet).getBoardHeight());
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

		if(packet instanceof PacketOutCursorMove)
		{
			client.getBoard().getPlayer(((PacketOutCursorMove)packet).getPlayerId()).setCursor(((PacketOutCursorMove)packet).getCursor());
			client.getBoard().getPlayBoard().revalidate();
			client.getBoard().getPlayBoard().repaint();
			return;
		}

		if(packet instanceof PacketOutSpectator)
		{
			client.getBoard().setSpectator(true);
			for(Map.Entry<Point, Integer> entry : ((PacketOutSpectator)packet).getBoardContent().entrySet())
				client.getBoard().place(entry.getValue(), entry.getKey());
			return;
		}

		if(packet instanceof PacketOutStatus)
		{
			if(((PacketOutStatus)packet).getStatus() == PacketOutStatus.GAME_START)
				client.getBoard().start();

			if(((PacketOutStatus)packet).getStatus() == PacketOutStatus.GAME_STOP)
				client.getBoard().stop();
			return;
		}

		if(packet instanceof PacketOutChat)
		{
			client.getBoard().getChat().sendMessage(((PacketOutChat)packet).getMessage());
			return;
		}

		if(packet instanceof PacketOutKick)
		{
			welcomed = true;
			close();
			JOptionPane.showMessageDialog(client, "You have been kicked out of the server, reason: \n" + ((PacketOutKick)packet).getMessage(), "You have been kicked !", JOptionPane.WARNING_MESSAGE);
			return;
		}

		System.err.println("The packet sent by server isn't appropriate:");
		System.err.println(packet.getClass().getName());
	}

	private void acceptInput()
	{
		while(socket != null && socket.isConnected() && !socket.isClosed() && !socket.isInputShutdown() && !socket.isOutputShutdown()) try
		{
			receivePacket((Packet)new ObjectInputStream(socket.getInputStream()).readObject());
		}
		catch(ClassNotFoundException | ClassCastException notAPacketEx)
		{
			System.err.println("Server sent a socket with unknown data.");
			notAPacketEx.printStackTrace(System.err);
		}
		catch(EOFException | SocketException ex)
		{
			break;
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
		client.getBoard().dispose();
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
