package me.winter.trapgame.server;

import me.winter.trapgame.shared.Task;
import me.winter.trapgame.shared.packet.PacketOutSpectator;
import me.winter.trapgame.shared.packet.PacketOutStatus;
import me.winter.trapgame.util.CollectionUtil;
import me.winter.trapgame.util.SortingUtil;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Represents a state of the game when players are playing (clicking the board)
 *
 * Created by winter on 25/03/16.
 */
public class GameState extends State
{
	private List<Player> spectators;
	private Map<Point, Player> boardContent;

	public GameState(TrapGameServer server)
	{
		super(server);
		this.boardContent = new HashMap<>();
		this.spectators = new ArrayList<>();
	}

	@Override
	public void join(Player player)
	{
		spectators.add(player);
		player.getConnection().sendPacket(new PacketOutSpectator(boardContent));
	}

	@Override
	public void leave(Player player)
	{
		spectators.remove(player);
		if(getServer().getPlayers().size() < getServer().getMinPlayers())
		{
			getServer().getConnection().sendToAll(new PacketOutStatus(PacketOutStatus.GAME_STOP));
			getServer().setState(new StandbyState(getServer()));
			getServer().getState().start();
		}
	}

	@Override
	public void start()
	{
		getServer().broadcast("The game starts, good luck !");
		getServer().getConnection().sendToAll(new PacketOutStatus(PacketOutStatus.GAME_START));
	}


	public boolean place(Player player, Point point)
	{
		if(spectators.contains(player))
			return false;

		if(point.getX() < 0 || point.getY() < 0
		|| point.getX() >= getServer().getBoardWidth()
		|| point.getY() >= getServer().getBoardHeight()
		|| boardContent.containsKey(point))
			return false;

		if(boardContent.values().contains(player)
				&& boardContent.get(new Point(point.x + 1, point.y)) != player
				&& boardContent.get(new Point(point.x - 1, point.y)) != player
				&& boardContent.get(new Point(point.x, point.y + 1)) != player
				&& boardContent.get(new Point(point.x, point.y - 1)) != player)
			return false;

		boardContent.put(point, player);

		if(boardContent.size() == getServer().getBoardWidth() * getServer().getBoardHeight())
			getServer().getScheduler().addTask(new Task(0, false, this::skip));


		return true;
	}

	@Override
	public void skip()
	{
		Set<Player> uniquePlayers = new HashSet<>(boardContent.values());

		Player[] players = uniquePlayers.toArray(new Player[uniquePlayers.size()]);
		int[] scores = new int[players.length];

		for(int i = 0; i < players.length; i++)
			scores[i] = CollectionUtil.occurrences(players[i], boardContent.values());

		SortingUtil.quickSort(players, scores);
		SortingUtil.reverse(players);
		SortingUtil.reverse(scores);

		String message = "Game is finished, " + (players.length > 0 && players[0] != null ? players[0].getName() : null) + " has won the game !";

		for(int i = 0; i < players.length; i++)
			message += "\n " + (i + 1) + ": " + players[i].getName() + " Score: " + scores[i];

		getServer().broadcast(message);

		getServer().getConnection().sendToAll(new PacketOutStatus(PacketOutStatus.GAME_STOP));
		getServer().setState(new WaitingState(getServer()));
		getServer().getState().start();
	}
}
