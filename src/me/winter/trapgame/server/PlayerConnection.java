package me.winter.trapgame.server;

import me.winter.trapgame.server.state.GameState;
import me.winter.trapgame.server.state.State;
import me.winter.trapgame.shared.Task;
import me.winter.trapgame.shared.packet.*;

import java.awt.*;
import java.net.InetAddress;

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

	public PlayerConnection(Player player, InetAddress address, int port)
	{
		this.player = player;
		this.address = address;
		this.port = port;
		keepAlive();
	}

	public void sendPacketLater(Packet packet)
	{
		getPlayer().getServer().getConnection().sendPacketLater(packet, address, port);
	}

	public void sendPacket(Packet packet)
	{
		getPlayer().getServer().getConnection().sendPacket(packet, address, port);
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
