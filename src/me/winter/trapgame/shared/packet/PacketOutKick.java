package me.winter.trapgame.shared.packet;

import java.io.*;

/**
 * Represents a packet sent from server to client
 * Used to inform a client that he has been kicked out of the server
 * Other players should be informed with PacketOutLeave
 *
 * Created by winter on 25/03/16.
 */
public class PacketOutKick extends Packet
{
	private String message;

	public PacketOutKick()
	{

	}

	public PacketOutKick(String message)
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
