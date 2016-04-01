package me.winter.trapgame.client;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.Task;
import me.winter.trapgame.shared.packet.*;
import me.winter.trapgame.util.StringUtil;

import javax.swing.*;
import java.awt.Point;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
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
	private List<Packet> toSend;
	private boolean welcomed;

	public ClientConnection(TrapGameClient client)
	{
		this.client = client;
		socket = null;
		toSend = new ArrayList<>();
		welcomed = true;
	}

	public void connectTo(String address, String password, String playerName) throws IOException, TimeoutException
	{
		if(socket != null)
			return;

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
		new Thread(this::sendOutput).start();

		client.getScheduler().addTask(new Task(2000, false, () ->
		{
			if(!welcomed)
			{
				JOptionPane.showMessageDialog(client, "Sorry but the server isn't responding.", "Connection failed", JOptionPane.ERROR_MESSAGE);
				welcomed = true;
			}

		}));
	}

	public void sendPacketLater(Packet packet)
	{
		toSend.add(packet);
	}

	public void sendPacket(Packet packet)
	{
		if(socket == null)
			throw new IllegalStateException("Currently not connected to any server");

		try
		{
			new DataOutputStream(socket.getOutputStream()).writeUTF(packet.getClass().getSimpleName());
			packet.writeTo(socket.getOutputStream());

			socket.getOutputStream().flush();
		}
		catch(Exception ex)
		{
			if(client.getUserProperties().isDebugMode())
				ex.printStackTrace(System.err);
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
			PacketOutCursorMove movePacket = (PacketOutCursorMove)packet;

			PlayerInfo player = client.getBoard().getPlayer(movePacket.getPlayerId());
			player.setCursorX(movePacket.getCursorX());
			player.setCursorY(movePacket.getCursorY());
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
		while(isOpen()) try
		{
			String packetName = new DataInputStream(socket.getInputStream()).readUTF();

			Packet packet = (Packet)Class.forName("me.winter.trapgame.shared.packet." + packetName).newInstance();
			packet.readFrom(socket.getInputStream());

			client.getScheduler().addTask(new Task(0, false, () -> receivePacket(packet)));
		}
		catch(Exception ex)
		{
			if(client.getUserProperties().isDebugMode())
				ex.printStackTrace(System.err);
		}
		close();
	}

	private void sendOutput()
	{
		while(isOpen())
		{
			for(Packet packet : new ArrayList<>(toSend))
			{
				if(packet != null)
					sendPacket(packet);
				toSend.remove(packet);
			}
		}
	}

	public boolean isOpen()
	{
		return socket != null && socket.isConnected() && !socket.isClosed() && !socket.isInputShutdown() && !socket.isOutputShutdown();
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
