package me.winter.trapgame.shared.packet;

/**
 * Represents a packet sent from client to server
 * Used when a client sends a chat message to the server
 * Created by winter on 25/03/16.
 */
public class PacketInChat
{
	private String message;

	public PacketInChat(String message)
	{
		this.message = message;
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
