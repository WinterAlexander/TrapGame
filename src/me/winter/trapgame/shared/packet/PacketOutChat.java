package me.winter.trapgame.shared.packet;

import java.io.*;

/**
 * Represents a packet meant to be send from server to client
 * Contains only a chat message, this message should be displayed in the user's chat
 *
 * Created by winter on 25/03/16.
 */
public class PacketOutChat extends Packet
{
	private String message;

	public PacketOutChat()
	{

	}

	public PacketOutChat(String message)
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
