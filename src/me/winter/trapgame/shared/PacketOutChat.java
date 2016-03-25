package me.winter.trapgame.shared;

/**
 * Represents a packet meant to be send from server to client
 * Contains only a chat message, this message should be displayed in the user's chat
 *
 * Created by winter on 25/03/16.
 */
public class PacketOutChat
{
	private String message;

	public PacketOutChat(String message)
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
