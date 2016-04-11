package me.winter.trapgame.client;

import me.winter.trapgame.shared.BoardFiller;
import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.PlayerStats;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
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

	private PlayBoard playBoard;
	private BoardMenu boardMenu;
	private Chat chat;

	private List<PlayerInfo> players;
	private int playerId;

	private Image buttonFrame, background;

	public TrapGameBoard(TrapGameClient container)
	{
		this.container = container;

		try
		{
			buttonFrame = ImageIO.read(ClassLoader.class.getResourceAsStream("/frame.png"));
			background = ImageIO.read(ClassLoader.class.getResourceAsStream("/background.png"));
		}
		catch(IOException ex)
		{
			System.err.println("Failed to load image(s)");
			ex.printStackTrace(System.err);
		}

		setBackground(new Color(0, 0, 0, 0));

		chat = new Chat(this);
		boardMenu = new BoardMenu(this);
		playBoard = null;

		setLayout(new BoardLayout());

		add(chat, BoardLayout.RIGHT);
		add(boardMenu, BoardLayout.LEFT);
	}

	@Override
	public void paintComponent(Graphics graphics)
	{
		graphics.drawImage(background, 0, 0, getWidth(), getHeight(), null);
		super.paintComponent(graphics);
	}

	public void init(int playerId, List<PlayerInfo> players, int width, int height)
	{
		this.playerId = playerId;
		this.players = players;

		playBoard = new PlayBoard(this, width, height);
		add(playBoard, BoardLayout.BOARD);
		boardMenu.build();
		revalidate();
		repaint();
	}

	public void updateStats(Map<Integer, PlayerStats> stats)
	{
		for(int index : stats.keySet())
			getPlayer(index).setStats(stats.get(index));

		boardMenu.build();
	}

	public void setBoardSize(int boardWidth, int boardHeight)
	{
		playBoard.prepare(boardWidth, boardHeight);
		revalidate();
		repaint();
	}

	public void start()
	{
		reset();
		playBoard.setBoardLocked(false);
	}

	public void stop()
	{
		playBoard.setBoardLocked(true);
		playBoard.setSpectator(false);
	}

	public void reset()
	{
		playBoard.getScores().clear();
		playBoard.setBoardLocked(true);

		revalidate();
		repaint();
	}

	public void dispose()
	{
		if(playBoard != null)
			remove(playBoard);
		playBoard = null;
		players = null;
		playerId = -1;
		chat.reset();
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
		return players != null && playBoard != null && playerId >= 0;
	}

	public void join(PlayerInfo info)
	{
		if(!inGame())
			throw new IllegalStateException("Game board not initialized");
		players.add(info);
		boardMenu.build();
	}

	public void leave(int playerId)
	{
		if(this.playerId == playerId)
			return;

		players.removeIf(player -> player.getPlayerId() == playerId);
		boardMenu.build();
	}


	public TrapGameClient getContainer()
	{
		return container;
	}

	public List<PlayerInfo> getPlayers()
	{
		return players;
	}

	public PlayBoard getPlayBoard()
	{
		return playBoard;
	}

	public Chat getChat()
	{
		return chat;
	}

	public Image getButtonFrame()
	{
		return buttonFrame;
	}
}
