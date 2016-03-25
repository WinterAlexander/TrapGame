package me.winter.trapgame.server;

import me.winter.trapgame.shared.Packet;

import java.io.*;
import java.net.Socket;

/**
 *
 * Created by winter on 25/03/16.
 */
public class PlayerConnection
{
	private Socket socket;
	private InputStream input;
	private OutputStream output;

	public PlayerConnection()
	{

	}

	public void sendPacket(Packet packet)
	{
		try
		{
			ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
			new ObjectOutputStream(byteBuffer).writeObject(packet);
			byteBuffer.writeTo(output);
		}
		catch(IOException e)
		{
			e.printStackTrace(System.err);
		}
	}

	public void receivePacket(Packet packet)
	{

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
