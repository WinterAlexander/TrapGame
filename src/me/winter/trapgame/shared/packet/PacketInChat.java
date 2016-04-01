package me.winter.trapgame.shared.packet;

import java.io.*;

/**
 * Represents a packet sent from client to server
 * Used when a client sends a chat message to the server
 * Created by winter on 25/03/16.
 */
public class PacketInChat extends Packet
{
	private String message;

	public PacketInChat()
	{

	}

	public PacketInChat(String message)
	{
		this.message = message;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		message = new DataInputStream(stream).readUTF();
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		new DataOutputStream(stream).writeUTF(message);
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
