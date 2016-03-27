package me.winter.trapgame.server;

import me.winter.trapgame.shared.packet.PacketOutStatus;
import me.winter.trapgame.util.CollectionUtil;
import me.winter.trapgame.util.SortingUtil;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a state of the game when players are playing (clicking the board)
 *
 * Created by winter on 25/03/16.
 */
public class GameState extends State
{
	private Map<Point, Player> boardContent;

	public GameState(TrapGameServer server)
	{
		super(server);
		this.boardContent = new HashMap<>();
	}

	@Override
	public void join(Player player)
	{
		throw new IllegalStateException("Can't join during game");
	}

	@Override
	public void leave(Player player)
	{
		if(getServer().getPlayers().size() < getServer().getMinPlayers())
		{
			getServer().setState(new StandbyState(getServer()));
			getServer().getState().start();
		}
	}

	@Override
	public void start()
	{
		getServer().getConnection().sendToAll(new PacketOutStatus(PacketOutStatus.GAME_START));
	}


	public boolean place(Player player, Point point)
	{
		if(point.getX() < 0 || point.getY() < 0
		|| point.getX() >= getServer().getBoardWidth()
		|| point.getY() >= getServer().getBoardHeight()
		|| boardContent.containsKey(point))
			return false;


		boardContent.put(point, player);

		if(boardContent.size() == getServer().getBoardWidth() * getServer().getBoardHeight())
		{
			Player[] players = boardContent.values().toArray(new Player[boardContent.values().size()]);
			int[] scores = new int[players.length];

			for(int i = 0; i < players.length; i++)
				scores[i] = CollectionUtil.occurrences(players[i], boardContent.values());

			SortingUtil.quickSort(players, scores);

			String message = "Game is finished, " +  players[0] + " has won the game !";

			for(int i = 0; i < players.length; i++)
				message += "\n " + (i + 1) + ": " + players[i] + " Score: " + scores[i];

			getServer().broadcast(message);

			getServer().setState(new WaitingState(getServer()));
			getServer().getState().start();
		}
		return true;
	}
}
