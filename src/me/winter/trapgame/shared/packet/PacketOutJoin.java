package me.winter.trapgame.shared.packet;

import me.winter.trapgame.shared.PlayerInfo;

/**
 * Represents a packet sent from server to client
 * Informs the user(s) that a client has join
 * Also serves as a connection confirmation for the joining client
 *
 * Created by winter on 25/03/16.
 */
public class PacketOutJoin
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
