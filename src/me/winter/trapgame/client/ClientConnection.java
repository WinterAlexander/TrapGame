package me.winter.trapgame.client;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.Task;
import me.winter.trapgame.shared.packet.*;
import me.winter.trapgame.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

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
	private Task keepAliveTask;

	public ClientConnection(TrapGameClient client)
	{
		this.client = client;
		udpSocket = null;
		toSend = new ArrayList<>();
		welcomed = true;
		port = 1254;
		inputBuffer = new byte[8 * 1024];

		keepAliveTask = new Task(4000, true, this::keepAlive);
	}

	public void connectTo(InetAddress first, int port, String playerName)
	{
		try
		{
			client.getConnection().connectTo(first, port, playerName, 3000);
		}
		catch(Exception publicEx)
		{
			client.getLogger().log(Level.INFO, "Couldn't connect to server");

			JOptionPane.showMessageDialog(client,
					client.getLang().getLine("client_connection_failed_message"),
					client.getLang().getLine("client_connection_failed_title"),
					JOptionPane.ERROR_MESSAGE);

		}
	}

	public void connectTo(String addressName, String playerName) throws IOException, TimeoutException
	{
		String[] parts = addressName.split(":");
		int port = 1254;

		if(parts.length == 2 && StringUtil.isInt(parts[1]))
		{
			addressName = parts[0];
			port = Integer.parseInt(parts[1]);
		}

		connectTo(InetAddress.getByName(addressName), port, playerName, 2000);
	}

	public synchronized void connectTo(InetAddress address, int port, String playerName, int timeout) throws IOException, TimeoutException
	{
		if(address == null)
			throw new IllegalArgumentException("Address cannot be null !");

		if(!welcomed || isOpen())
			return;

		this.port = port;
		this.address = address;

		udpSocket = new DatagramSocket();
		udpSocket.setSoTimeout(30_000);
		sendPacket(new PacketInJoin(playerName));

		welcomed = false;
		new Thread(this::acceptInput).start();
		new Thread(this::sendOutput).start();

		try
		{
			synchronized(this)
			{
				wait(timeout);
			}
		}
		catch(InterruptedException ex)
		{
			client.getLogger().log(Level.WARNING, "An exception occurred while waiting for server's response", ex);
		}

		if(!welcomed)
		{
			welcomed = true;
			close();
			throw new TimeoutException("No answer after " + timeout + " milliseconds.");
		}
	}

	/**
	 * Pings a TrapGame server by sending the PacketInPing and waits for response
	 */
	public PacketOutPong ping(InetAddress address, int port, int timeout) throws IOException, TimeoutException
	{
		if(address == null)
			throw new IllegalArgumentException("Address cannot be null !");

		DatagramSocket pingSocket = new DatagramSocket();
		pingSocket.setSoTimeout(timeout);

		Packet packet = new PacketInPing();

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		new DataOutputStream(byteStream).writeUTF(packet.getClass().getSimpleName());
		packet.writeTo(byteStream); //ping parameters (may be empty)

		DatagramPacket data = new DatagramPacket(byteStream.toByteArray(), byteStream.size(), address, port);

		pingSocket.send(data);


		try
		{
			DatagramPacket bufPacket = new DatagramPacket(inputBuffer, inputBuffer.length);

			pingSocket.receive(bufPacket);

			ByteArrayInputStream receiveStream = new ByteArrayInputStream(inputBuffer);

			String packetName = new DataInputStream(receiveStream).readUTF();

			if(!packetName.equals("PacketOutPong"))
				return null;

			PacketOutPong pong = new PacketOutPong();
			pong.readFrom(receiveStream);
			return pong;
		}
		finally
		{
			pingSocket.close();
		}
	}

	/**
	 *
	 * @param address
	 * @param port
	 * @param timeout
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public List<BroadcastResponse> broadcast(InetAddress address, int port, int timeout) throws IOException, TimeoutException
	{
		if(address == null)
			throw new IllegalArgumentException("Address cannot be null !");

		List<BroadcastResponse> pongs = new ArrayList<>();

		DatagramSocket pingSocket = new DatagramSocket();
		pingSocket.setSoTimeout(timeout);

		Packet packet = new PacketInPing();

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		new DataOutputStream(byteStream).writeUTF(packet.getClass().getSimpleName());
		packet.writeTo(byteStream); //ping parameters (may be empty)

		DatagramPacket data = new DatagramPacket(byteStream.toByteArray(), byteStream.size(), address, port);

		pingSocket.send(data);

		long start = System.nanoTime();

		try
		{
			while(true) //while you don't get a timeout exception
			{
				DatagramPacket bufPacket = new DatagramPacket(inputBuffer, inputBuffer.length);

				pingSocket.receive(bufPacket);

				ByteArrayInputStream receiveStream = new ByteArrayInputStream(inputBuffer);

				String packetName = new DataInputStream(receiveStream).readUTF();

				if(!packetName.equals("PacketOutPong"))
					continue;

				PacketOutPong pong = new PacketOutPong();
				pong.readFrom(receiveStream);
				pongs.add(new BroadcastResponse(bufPacket.getAddress(), bufPacket.getPort(), pong, (int)((System.nanoTime() - start) / 1_000_000)));
			}
		}
		catch(SocketTimeoutException ex)
		{
			return pongs;
		}
		finally
		{
			pingSocket.close();
		}
	}

	public synchronized void sendPacketLater(Packet packet)
	{
		toSend.add(packet);
		notify();
	}

	public void keepAlive()
	{
		if(!isOpen())
			return;

		try
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			new DataOutputStream(byteStream).writeUTF("KeepAlive");

			DatagramPacket data = new DatagramPacket(byteStream.toByteArray(), byteStream.size(), address, port);

			udpSocket.send(data);
		}
		catch(SocketException ex)
		{
			close();
		}
		catch(Exception ex)
		{
			client.getLogger().log(Level.SEVERE, "An unexpected exception occurred while trying to keep connection alive", ex);
		}
	}

	public void sendPacket(Packet packet)
	{
		if(!isOpen())
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
				client.getLogger().log(Level.WARNING, "An exception occurred when trying to send packet", ex);
		}
	}


	public void receivePacket(Packet packet)
	{
		if(!isOpen())
			return;


		if(packet instanceof PacketOutWelcome)
		{
			PacketOutWelcome packetWelcome = (PacketOutWelcome)packet;

			synchronized(this)
			{
				notify();
			}

			welcomed = true;
			client.getBoard().init(packetWelcome.getPlayerId(), packetWelcome.getPlayers(), packetWelcome.getBoardWidth(), packetWelcome.getBoardHeight());
			client.goToBoard();
			client.getScheduler().addTask(keepAliveTask);
			return;
		}

		if(packet instanceof PacketOutUpdateStats)
		{
			client.getBoard().updateStats(((PacketOutUpdateStats)packet).getStats());
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
			client.getBoard().getPlayBoard().place(((PacketOutPlace)packet).getPlayerId(), ((PacketOutPlace)packet).getLocation());
			return;
		}

		if(packet instanceof PacketOutFill)
		{
			client.getBoard().getPlayBoard().fill(((PacketOutFill)packet).getPlayerId(), ((PacketOutFill)packet).getLocation());
			return;
		}

		if(packet instanceof PacketOutCursorMove)
		{
			PacketOutCursorMove movePacket = (PacketOutCursorMove)packet;

			PlayerInfo player = client.getBoard().getPlayer(movePacket.getPlayerId());

			if(player == null)
				return;

			player.setCursorX(movePacket.getCursorX());
			player.setCursorY(movePacket.getCursorY());
			client.getBoard().getPlayBoard().revalidate();
			client.getBoard().getPlayBoard().repaint();
			return;
		}

		if(packet instanceof PacketOutSpectator)
		{
			client.getBoard().getPlayBoard().setSpectator(true);
			for(Map.Entry<Point, Integer> entry : ((PacketOutSpectator)packet).getBoardContent().entrySet())
				client.getBoard().getPlayBoard().place(entry.getValue(), entry.getKey());
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
			if(client.inBoard())
			{
				client.getBoard().dispose();
				client.getBoard().getContainer().goToMenu();
			}
			close();
			JOptionPane.showMessageDialog(client, "You have been kicked out of the server, reason: \n" + ((PacketOutKick)packet).getMessage(), "You have been kicked !", JOptionPane.WARNING_MESSAGE);
			return;
		}

		client.getLogger().log(Level.INFO, "The packet sent by server isn't appropriate: " + packet.getClass().getName());
	}

	private void acceptInput()
	{
		while(isOpen()) try
		{
			DatagramPacket bufPacket = new DatagramPacket(inputBuffer, inputBuffer.length);

			udpSocket.receive(bufPacket);

			ByteArrayInputStream byteStream = new ByteArrayInputStream(inputBuffer);

			String packetName = new DataInputStream(byteStream).readUTF();

			if(packetName.equals("KeepAlive"))
				continue;

			Packet packet = (Packet)Class.forName("me.winter.trapgame.shared.packet." + packetName).newInstance();
			packet.readFrom(byteStream);

			//if(client.getUserProperties().isDebugMode()) ab00se
			//	System.out.println("Received " + packet.getClass().getSimpleName() + " from " + bufPacket.getAddress().toString() + " port: " + bufPacket.getPort());

			client.getScheduler().addTask(() -> receivePacket(packet), 0);
		}
		catch(SocketTimeoutException ex)
		{
			close();
			JOptionPane.showMessageDialog(client, "Sorry, you were disconnected because the server stopped responding.", "Server stopped responding", JOptionPane.ERROR_MESSAGE);
		}
		catch(SocketException ex)
		{
			close();
		}
		catch(Exception ex)
		{
			client.getLogger().log(Level.WARNING, "An unexpected exception occurred while accepting input", ex);
			close();
		}
	}

	private void sendOutput()
	{
		while(isOpen())
		{
			while(toSend.size() != 0)
			{
				Packet packet = toSend.get(0);
				if(packet != null)
					sendPacket(packet);
				toSend.remove(0);
			}

			synchronized(this)
			{
				try
				{
					wait();
				}
				catch(InterruptedException ex)
				{
					client.getLogger().log(Level.WARNING, "An unexpected exception occurred while waiting new output to send in client connection", ex);
				}
			}
		}
	}

	public boolean isOpen()
	{
		return udpSocket != null && !udpSocket.isClosed();
	}

	public void close()
	{

		if(keepAliveTask.isRunning())
			keepAliveTask.cancel();

		if(udpSocket == null)
			return;

		if(!udpSocket.isClosed())
		{
			sendPacket(new PacketInLeave("Disconnecting"));
			udpSocket.close();
		}
		udpSocket = null;

		synchronized(this)
		{
			notify();
		}
	}

}
