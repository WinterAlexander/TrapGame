package me.winter.trapgame.server;

import me.winter.trapgame.shared.Task;
import me.winter.trapgame.shared.packet.*;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.List;

/**
 *
 * Created by winter on 25/03/16.
 */
public class PlayerConnection
{
	private Player player;

	private List<Packet> toSend;
	private Socket socket;

	public PlayerConnection(Player player, Socket socket)
	{
		this.player = player;
		this.socket = socket;

		toSend = new ArrayList<>();

		new Thread(this::acceptInput).start();
		new Thread(this::sendOutput).start();
	}

	private void acceptInput()
	{
		while(isOpen()) try
		{
			String packetName = new DataInputStream(socket.getInputStream()).readUTF();

			Packet packet = (Packet)Class.forName("me.winter.trapgame.shared.packet." + packetName).newInstance();
			packet.readFrom(socket.getInputStream());

			getPlayer().getServer().getScheduler().addTask(new Task(0, false, () -> receivePacket(packet)));

		}
		catch(Exception ex)
		{
			if(getPlayer().getServer().isDebugMode())
				ex.printStackTrace(System.err);
			break;
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

	public synchronized void sendPacketLater(Packet packet)
	{
		toSend.add(packet);
		notify();
	}

	public synchronized void sendPacket(Packet packet)
	{
		try
		{
			new DataOutputStream(socket.getOutputStream()).writeUTF(packet.getClass().getSimpleName());
			packet.writeTo(socket.getOutputStream());
			socket.getOutputStream().flush();
		}
		catch(IOException e)
		{
			e.printStackTrace(System.err);
		}
	}

	public void receivePacket(Packet packet)
	{
		if(packet instanceof PacketInChat)
		{
			player.getServer().getCommandManager().execute(player, ((PacketInChat)packet).getMessage());
			return;
		}

		if(packet instanceof PacketInClick)
		{
			State state = player.getServer().getState();

			if(!(state instanceof GameState))
				return;

			Point location = ((PacketInClick)packet).getLocation();

			if(((GameState)state).place(getPlayer(), location))
			{
				getPlayer().getServer().getConnection().sendToAllLater(new PacketOutPlace(getPlayer().getId(), location));
				((GameState)state).tryFilling(getPlayer(), location);
			}


			return;
		}

		if(packet instanceof PacketInCursorMove)
		{
			getPlayer().getInfo().setCursor(((PacketInCursorMove)packet).getCursorX(), ((PacketInCursorMove)packet).getCursorY());

			for(Player player : getPlayer().getServer().getPlayers())
				if(player != getPlayer())
					player.getConnection().sendPacketLater(new PacketOutCursorMove(getPlayer().getId(), ((PacketInCursorMove)packet).getCursorX(), ((PacketInCursorMove)packet).getCursorY()));
			return;
		}

		if(packet instanceof PacketInLeave)
		{
			close();
			return;
		}

		System.err.println("The packet sent by " + getPlayer().getName() + " isn't appropriate:");
		System.err.println(packet.getClass().getName());
	}

	public boolean isOpen()
	{
		return socket != null && socket.isConnected() && !socket.isClosed() && !socket.isInputShutdown() && !socket.isOutputShutdown();
	}

	public void close()
	{
		if(player.getServer().getPlayers().contains(player))
			player.getServer().leave(player);

		if(socket.isClosed())
			return;

		try
		{
			getSocket().close();
		}
		catch(IOException ex)
		{
			System.err.println("An internal error occurred while closing client socket of " + player.getName());
			ex.printStackTrace(System.err);
		}
	}

	public Player getPlayer()
	{
		return player;
	}

	public Socket getSocket()
	{
		return socket;
	}
}
