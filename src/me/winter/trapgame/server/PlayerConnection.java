package me.winter.trapgame.server;

import me.winter.trapgame.shared.packet.*;

import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 *
 * Created by winter on 25/03/16.
 */
public class PlayerConnection
{
	private Player player;

	private Socket socket;

	public PlayerConnection(Player player, Socket socket)
	{
		this.player = player;
		this.socket = socket;

		new Thread(this::acceptInput).start();
	}

	private void acceptInput()
	{
		while(socket.isConnected() && !socket.isClosed() && !socket.isInputShutdown() && !socket.isOutputShutdown()) try
		{
			receivePacket((Packet)new ObjectInputStream(socket.getInputStream()).readObject());
		}
		catch(ClassNotFoundException | ClassCastException notAPacketEx)
		{
			System.err.println("Player " + getPlayer().getName() + " sent a socket with unknown data.");
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

	public void sendPacket(Packet packet)
	{
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
				getPlayer().getServer().getConnection().sendToAll(new PacketOutPlace(getPlayer().getId(), location));
			return;
		}

		if(packet instanceof PacketInCursorMove)
		{
			getPlayer().getInfo().setCursor(((PacketInCursorMove)packet).getCursor());
			for(Player player : getPlayer().getServer().getPlayers())
				if(player != getPlayer())
					player.getConnection().sendPacket(new PacketOutCursorMove(getPlayer().getId(), ((PacketInCursorMove)packet).getCursor()));
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
