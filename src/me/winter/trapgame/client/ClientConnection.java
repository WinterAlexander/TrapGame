package me.winter.trapgame.client;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.Task;
import me.winter.trapgame.shared.packet.*;
import me.winter.trapgame.util.StringUtil;

import javax.swing.*;
import java.awt.Point;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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

	private DatagramSocket udpSocket;

	private InetAddress address;
	private int port;
	private byte[] inputBuffer;

	private List<Packet> toSend;
	private boolean welcomed;

	public ClientConnection(TrapGameClient client)
	{
		this.client = client;
		udpSocket = null;
		toSend = new ArrayList<>();
		welcomed = true;
		port = 1254;
		inputBuffer = new byte[1024];
	}

	public synchronized void connectTo(String addressName, String password, String playerName) throws IOException, TimeoutException
	{
		if(!welcomed || isOpen())
			return;

		String[] parts = addressName.split(":");

		if(parts.length == 2 && StringUtil.isInt(parts[1]))
		{
			addressName = parts[0];
			port = Integer.parseInt(parts[1]);
		}

		udpSocket = new DatagramSocket();
		udpSocket.setSoTimeout(30_000);
		address = InetAddress.getByName(addressName);
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
				close();
			}

		}));
	}

	public synchronized void sendPacketLater(Packet packet)
	{
		toSend.add(packet);
		notify();
	}

	public void sendPacket(Packet packet)
	{
		if(udpSocket == null)
			throw new IllegalStateException("Currently not connected to any server");


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

		if(packet instanceof PacketOutFill)
		{
			client.getBoard().fill(((PacketOutFill)packet).getPlayerId(), ((PacketOutFill)packet).getLocation());
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

		if(client.getUserProperties().isDebugMode())
			System.out.println("The packet sent by server isn't appropriate: " + packet.getClass().getName());
	}

	private void acceptInput()
	{
		while(isOpen()) try
		{

			DatagramPacket bufPacket = new DatagramPacket(inputBuffer, inputBuffer.length);

			udpSocket.receive(bufPacket);

			ByteArrayInputStream byteStream = new ByteArrayInputStream(inputBuffer);

			String packetName = new DataInputStream(byteStream).readUTF();

			Packet packet = (Packet)Class.forName("me.winter.trapgame.shared.packet." + packetName).newInstance();
			packet.readFrom(byteStream);

			//if(client.getUserProperties().isDebugMode()) ab00se
			//	System.out.println("Received " + packet.getClass().getSimpleName() + " from " + bufPacket.getAddress().toString() + " port: " + bufPacket.getPort());

			client.getScheduler().addTask(new Task(0, false, () -> receivePacket(packet)));
		}
		catch(SocketTimeoutException ex)
		{
			close();
			JOptionPane.showMessageDialog(client, "Sorry, you were disconnected because the server stopped responding.", "Server stopped responding", JOptionPane.ERROR_MESSAGE);
			return;
		}
		catch(SocketException ex)
		{

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

	public boolean isOpen()
	{
		return udpSocket != null;
	}

	public synchronized void close()
	{
		if(client.inBoard())
		{
			client.getBoard().dispose();
			client.goToMenu();
		}

		if(udpSocket == null)
			return;

		if(!udpSocket.isClosed())
		{
			sendPacket(new PacketInLeave("Disconnecting"));
			udpSocket.close();
		}
		udpSocket = null;

		notify();
	}

}
