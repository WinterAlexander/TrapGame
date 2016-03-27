package me.winter.trapgame.shared.packet;

import me.winter.trapgame.shared.PlayerInfo;

import java.util.List;

/**
 * Packet sent from server to client
 * Used then a client join to confirm his connection,
 * to give him the board width/height and to give him
 * the list of existing players
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class PacketOutWelcome extends Packet
{
	private int playerId;
	private List<PlayerInfo> players;
	private int boardWidth, boardHeight;

	public PacketOutWelcome(int playerId, List<PlayerInfo> players, int boardWidth, int boardHeight)
	{
		this.playerId = playerId;
		this.players = players;
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
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

	public int getBoardWidth()
	{
		return boardWidth;
	}

	public void setBoardWidth(int boardWidth)
	{
		this.boardWidth = boardWidth;
	}

	public int getBoardHeight()
	{
		return boardHeight;
	}

	public void setBoardHeight(int boardHeight)
	{
		this.boardHeight = boardHeight;
	}
}
