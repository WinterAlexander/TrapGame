package me.winter.trapgame.shared.packet;

/**
 * Represents a packet sent from client to server
 * Used when the player wants to leave without losing connection
 * The player "can" customize his leave message
 *
 * Created by winter on 25/03/16.
 */
public class PacketInLeave
{
	private String message;

	public PacketInLeave(String message)
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
