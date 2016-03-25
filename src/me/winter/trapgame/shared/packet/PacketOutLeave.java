package me.winter.trapgame.shared.packet;

/**
 * Represents a packet sent from server to client
 * Informs a player that another player left the game
 *
 * Created by winter on 25/03/16.
 */
public class PacketOutLeave
{
	private int playerId;

	public PacketOutLeave(int playerId)
	{
		this.playerId = playerId;
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}
}
