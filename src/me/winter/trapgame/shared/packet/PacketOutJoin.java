package me.winter.trapgame.shared.packet;

import me.winter.trapgame.shared.PlayerInfo;

/**
 * Represents a packet sent from server to client
 * Informs the user(s) that a client has join
 *
 * Created by winter on 25/03/16.
 */
public class PacketOutJoin extends Packet
{
	private PlayerInfo player;

	public PacketOutJoin(PlayerInfo player)
	{
		this.player = player;
	}

	public PlayerInfo getPlayer()
	{
		return player;
	}

	public void setPlayer(PlayerInfo player)
	{
		this.player = player;
	}
}
