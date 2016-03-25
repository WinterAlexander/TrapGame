package me.winter.trapgame.server;

import me.winter.trapgame.shared.packet.Packet;
import me.winter.trapgame.shared.packet.PacketInChat;
import me.winter.trapgame.shared.packet.PacketInClick;
import me.winter.trapgame.shared.packet.PacketInLeave;

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
			player.chat(((PacketInChat)packet).getMessage());
			return;
		}

		if(packet instanceof PacketInClick)
		{
			State state = player.getServer().getState();

			if(!(state instanceof GameState))
			{
				return;
			}
		}

		if(packet instanceof PacketInLeave)
		{

		}
	}

	public Socket getSocket()
	{
		return socket;
	}

	public void setSocket(Socket socket)
	{
		this.socket = socket;
	}
}
