package me.winter.trapgame.shared;

import me.winter.trapgame.util.MathUtil;

import java.io.Serializable;

/**
 * Represents the server statistics of the player
 * Made to be passed from server to client with packets
 *
 * Created by winter on 25/03/16.
 */
public class PlayerStats implements Serializable
{
	private int wins;
	private int loses;
	private int draws;

	public PlayerStats(int wins, int loses, int draws)
	{
		this.wins = wins;
		this.loses = loses;
		this.draws = draws;
	}

	@Override
	public String toString()
	{
		return "Wins: " + wins + " Loses: " + loses + " Draws: " + draws;
	}

	public double getWinLoseRatio()
	{
		return (double)wins / (double)(loses > 0 ? loses : 1);
	}

	public float getRoundedWinLoseRatio()
	{
		return (float)MathUtil.round(getWinLoseRatio(), 2);
	}

	public void addWin()
	{
		wins++;
	}

	public void addLose()
	{
		loses++;
	}

	public void addDraw()
	{
		draws++;
	}

	public int getWins()
	{
		return wins;
	}

	public void setWins(int wins)
	{
		this.wins = wins;
	}

	public int getLoses()
	{
		return loses;
	}

	public void setLoses(int loses)
	{
		this.loses = loses;
	}

	public int getDraws()
	{
		return draws;
	}

	public void setDraws(int draws)
	{
		this.draws = draws;
	}
}
