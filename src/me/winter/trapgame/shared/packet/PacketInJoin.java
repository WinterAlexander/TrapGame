package me.winter.trapgame.shared.packet;

/**
 * Represents a packet sent from client to server
 * Used when a player wants to join a game
 * The player should give it's username
 * The player may be kicked (by receiving a PacketOutKick)
 * if the game has already started or if he is banned
 *
 * Created by winter on 25/03/16.
 */
public class PacketInJoin extends Packet
{
	private String password, playerName;

	public PacketInJoin(String password, String playerName)
	{
		this.password = password;
		this.playerName = playerName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}
}
