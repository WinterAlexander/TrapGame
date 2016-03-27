package me.winter.trapgame.server;

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

	}

	@Override
	public void leave(Player player)
	{

	}

	@Override
	public void start()
	{

	}

	public boolean place(Player player, Point point)
	{
		if(boardContent.containsKey(point))
			return false;

		boardContent.put(point, player);
		return true;
	}
}
