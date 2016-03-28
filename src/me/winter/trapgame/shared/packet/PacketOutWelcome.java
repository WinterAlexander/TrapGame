package me.winter.trapgame.shared.packet;

import me.winter.trapgame.shared.PlayerInfo;

import java.util.List;

/**
 * Packet sent from server to client
 * Used then a client join to confirm his connection,
 * and to give him the list of already connected
 * players
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class PacketOutWelcome extends Packet
{
	private int playerId;
	private List<PlayerInfo> players;


	public PacketOutWelcome(int playerId, List<PlayerInfo> players)
	{
		this.playerId = playerId;
		this.players = players;
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	public List<PlayerInfo> getPlayers()
	{
		return players;
	}

	public void setPlayers(List<PlayerInfo> players)
	{
		this.players = players;
	}
}
