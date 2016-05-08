package me.winter.trapgame.server;

import me.winter.trapgame.server.state.GameState;
import me.winter.trapgame.server.state.State;
import me.winter.trapgame.shared.Task;
import me.winter.trapgame.shared.packet.*;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 *
 * Created by winter on 25/03/16.
 */
public class PlayerConnection
{
	private Player player;

	private InetAddress address;
	private int port;

	private long lastPacketReceived;
	private java.util.List<DatagramPacket> toSend;

	public PlayerConnection(Player player, InetAddress address, int port)
	{
		toSend = new ArrayList<>();

		this.player = player;
		this.address = address;
		this.port = port;
		keepAlive();

		new Thread(this::sendOutput).start();
	}

	private void sendOutput()
	{
		while(getPlayer().getServer().getConnection().isOpen())
		{
			for(DatagramPacket packet : new ArrayList<>(toSend))
			{
				try
				{
					if(packet != null)
						getPlayer().getServer().getConnection().getUdpSocket().send(packet);
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

	public void sendPacketLater(Packet packet)
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
			if(getPlayer().getServer().isDebugMode())
				ex.printStackTrace(System.err);
		}
	}

	public void sendPacket(Packet packet)
	{
		try
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			new DataOutputStream(byteStream).writeUTF(packet.getClass().getSimpleName());
			packet.writeTo(byteStream);

			DatagramPacket data = new DatagramPacket(byteStream.toByteArray(), byteStream.size(), address, port);

			getPlayer().getServer().getConnection().getUdpSocket().send(data);
		}
		catch(Exception ex)
		{
			if(getPlayer().getServer().isDebugMode())
				ex.printStackTrace(System.err);
		}
	}

	public void receivePacketLater(Packet packet)
	{
		getPlayer().getServer().getScheduler().addTask(new Task(0, false, () -> receivePacket(packet)));
	}

	public void keepAlive()
	{
		this.lastPacketReceived = System.currentTimeMillis();
	}

	public void receivePacket(Packet packet)
	{
		keepAlive();
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
			else
			{
				int id = ((GameState)state).getOwner(location) != null ? ((GameState)state).getOwner(location).getId() : -1;

				getPlayer().getConnection().sendPacketLater(new PacketOutPlace(id, location));
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
			player.leave();
			return;
		}

		getPlayer().getServer().getLogger().warning("The packet sent by " + getPlayer().getName() + " isn't appropriate: " + packet.getClass().getName());
	}

	public Player getPlayer()
	{
		return player;
	}

	public InetAddress getAddress()
	{
		return address;
	}

	public int getPort()
	{
		return port;
	}

	public long getLastPacketReceived()
	{
		return lastPacketReceived;
	}
}
