package me.winter.trapgame.server.state;

import me.winter.trapgame.server.Player;
import me.winter.trapgame.server.TrapGameServer;
import me.winter.trapgame.shared.BoardFiller;
import me.winter.trapgame.shared.Task;
import me.winter.trapgame.shared.packet.PacketOutFill;
import me.winter.trapgame.shared.packet.PacketOutSpectator;
import me.winter.trapgame.shared.packet.PacketOutStatus;
import me.winter.trapgame.shared.packet.PacketOutUpdateStats;
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
		getServer().broadcast(Color.red, "Go !");
		getServer().getConnection().sendToAll(new PacketOutStatus(PacketOutStatus.GAME_START));
	}

	public void resize()
	{
		boardContent.clear();
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

	public Player getOwner(Point location)
	{
		return boardContent.get(location);
	}

	public void tryFilling(Player player, Point point)
	{
		if(boardContent.size() == getServer().getBoardWidth() * getServer().getBoardHeight())
			return;

		for(Player current : getServer().getPlayers())
		{
			if(spectators.contains(current))
				continue;

			if(!boardContent.containsValue(current))
				return;
		}

		for(int direction = 0; direction < 4; direction++)
		{
			int x = 0, y = 0;

			if(direction < 2)
				x = (int)Math.pow(-1, direction);
			else
				y = (int)Math.pow(-1, direction);

			Point newPoint = new Point(point);
			newPoint.translate(x, y);

			if(boardContent.get(newPoint) != null)
				continue;

			if(newPoint.getX() < 0
			|| newPoint.getY() < 0
			|| newPoint.getX() >= getServer().getBoardWidth()
			|| newPoint.getY() >= getServer().getBoardHeight())
				continue;

			if(BoardFiller.tryFill(newPoint, player, boardContent, getServer().getBoardWidth(), getServer().getBoardHeight()))
				getServer().getConnection().sendToAllLater(new PacketOutFill(player.getId(), newPoint));
		}


		if(boardContent.size() == getServer().getBoardWidth() * getServer().getBoardHeight())
			getServer().getScheduler().addTask(new Task(0, false, this::skip));
	}

	@Override
	public void skip()
	{
		getServer().broadcast(gameEnd());

		getServer().getConnection().sendToAllLater(new PacketOutStatus(PacketOutStatus.GAME_STOP));
		getServer().setState(new WaitingState(getServer()));
		getServer().getState().start();
	}

	public String gameEnd()
	{
		Set<Player> uniquePlayers = new HashSet<>(boardContent.values());

		if(uniquePlayers.size() == 0)
			return "Game is finished.";

		Player[] players = uniquePlayers.toArray(new Player[uniquePlayers.size()]);
		int[] scores = new int[players.length];

		for(int i = 0; i < players.length; i++)
			scores[i] = CollectionUtil.occurrences(players[i], boardContent.values());

		SortingUtil.quickSort(players, scores);
		SortingUtil.reverse(players);
		SortingUtil.reverse(scores);

		String message = "Game is finished, ";

		for(int i = 0; i < players.length; i++)
		{
			int playersWithThatScore = 0;

			for(int playerIndex = i; playerIndex < players.length; playerIndex++)
				if(scores[playerIndex] == scores[i])
					playersWithThatScore++;

			if(i == 0)
			{
				if(playersWithThatScore == 1)
				{
					message += players[0].getFormattedName() + " has won !";
					players[0].getInfo().getStats().addWin();
				}
				else
				{
					message += "it's a draw !";
					players[0].getInfo().getStats().addDraw();
				}
			}
			else if(scores[i] == scores[0])
				players[i].getInfo().getStats().addDraw();
			else
				players[i].getInfo().getStats().addLose();

			message += "<br /> " + (i + playersWithThatScore) + ": " + players[i].getFormattedName() + " (Score: " + scores[i] + ")";
		}

		getServer().getConnection().sendToAllLater(new PacketOutUpdateStats(getServer().getPlayersInfo()));

		return message;
	}
}
