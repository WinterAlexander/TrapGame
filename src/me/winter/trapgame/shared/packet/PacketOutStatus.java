package me.winter.trapgame.shared.packet;

/**
 * Packet sent from server to client
 * Used to inform the players the game is starting
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class PacketOutStatus extends Packet
{
	public static final int GAME_START = 0;

	private int status;

	public PacketOutStatus(int status)
	{
		this.status = status;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}
}
