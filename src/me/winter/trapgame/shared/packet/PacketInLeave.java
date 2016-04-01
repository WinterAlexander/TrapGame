package me.winter.trapgame.shared.packet;

import java.io.*;

/**
 * Represents a packet sent from client to server
 * Used when the player wants to leave without losing connection
 * The player "can" customize his leave message
 *
 * Created by winter on 25/03/16.
 */
public class PacketInLeave extends Packet
{
	private String message;

	public PacketInLeave()
	{

	}

	public PacketInLeave(String message)
	{
		this.message = message;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		DataInputStream dataStream = new DataInputStream(stream);
		setMessage(dataStream.readUTF());
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);
		dataStream.writeUTF(getMessage());
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
