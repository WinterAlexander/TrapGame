package me.winter.trapgame.client;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.Task;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the board used to play the game
 * TrapGameClient should switch to this content pane after being connected to a server
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class TrapGameBoard extends JPanel
{
	private TrapGameClient container;

	private JPanel playBoard;
	private Chat chat;

	private Map<Point, PlayerInfo> boardContent;
	private List<PlayerInfo> players;
	private int playerId;

	private int boardWidth, boardHeight;
	private boolean boardLocked;

	public TrapGameBoard(TrapGameClient container)
	{
		this.container = container;

		setLayout(new BorderLayout());

		chat = new Chat(this);

		JPanel chatContainer = new JPanel();
		chatContainer.setLayout(new BorderLayout());

		chatContainer.add(chat, BorderLayout.CENTER);
		chatContainer.add(new ChatInput(this), BorderLayout.SOUTH);

		add(chatContainer, BorderLayout.EAST);

		playBoard = new JPanel();

		add(playBoard, BorderLayout.CENTER);

		dispose();
	}

	public void init(int playerId, List<PlayerInfo> players, int boardWidth, int boardHeight)
	{
		this.playerId = playerId;
		this.players = players;

		boardContent = new HashMap<>();
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;

		playBoard.setLayout(new GridLayout(boardWidth, boardHeight));

		for(int i = 0; i < boardWidth; i++)
		{
			for(int j = 0; j < boardHeight; j++)
			{
				playBoard.add(new TrapButton(this, new Point(i, j)));
			}
		}
	}

	public void reset()
	{
		boardContent.clear();
		boardLocked = true;
	}

	public void dispose()
	{
		boardContent = null;
		players = null;
		playerId = -1;
		boardWidth = -1;
		boardHeight = -1;
		boardLocked = true;
	}

	public PlayerInfo getClient()
	{
		return getPlayer(playerId);
	}

	public PlayerInfo getPlayer(int playerId)
	{
		for(PlayerInfo player : players)
			if(player.getPlayerId() == playerId)
				return player;
		return null;
	}

	public boolean inGame()
	{
		return players != null && boardContent != null && playerId >= 0;
	}

	public void join(PlayerInfo info)
	{
		if(!inGame())
			throw new IllegalStateException("Game board not initialized");
		players.add(info);

		//repaint ?
	}

	public void leave(int playerId)
	{
		if(this.playerId == playerId)
			return;

		players.removeIf(player -> player.getPlayerId() == playerId);
	}

	public boolean canClick(Point point)
	{
		return !boardContent.containsKey(point);
	}

	public void place(int playerId, Point point)
	{
		if(boardLocked)
			return;

		if(point.getX() < 0 || point.getY() < 0
				|| point.getX() >= getBoardWidth()
				|| point.getY() >= getBoardHeight()
				|| boardContent.containsKey(point))
			return;


		boardContent.put(point, getPlayer(playerId));

		for(Component component : playBoard.getComponents())
			if(component instanceof TrapButton && ((TrapButton)component).getPoint().equals(point))
				component.setBackground(getPlayer(playerId).getColor());

		if(boardContent.size() == getBoardWidth() * getBoardHeight())
		{
			boardLocked = true;
			getContainer().getScheduler().addTask(new Task(5000, false, this::reset));
		}

	}

	public TrapGameClient getContainer()
	{
		return container;
	}

	public boolean isBoardLocked()
	{
		return boardLocked;
	}

	public void unlockBoard()
	{
		boardLocked = false;
	}

	public int getBoardWidth()
	{
		return boardWidth;
	}

	public int getBoardHeight()
	{
		return boardHeight;
	}

	public Chat getChat()
	{
		return chat;
	}
}
