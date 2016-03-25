package me.winter.trapgame.shared.packet;

/**
 * Represents a packet sent from server to client
 * Used to inform a client that he has been kicked out of the server
 * Other players should be informed with PacketOutLeave
 *
 * Created by winter on 25/03/16.
 */
public class PacketOutKick
{
	private String message;

	public PacketOutKick(String message)
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
