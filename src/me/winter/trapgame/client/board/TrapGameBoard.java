package me.winter.trapgame.client.board;

import me.winter.trapgame.client.TrapGameClient;
import me.winter.trapgame.server.Player;
import me.winter.trapgame.server.TrapGameServer;
import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.PlayerStats;

import javax.swing.*;
import java.awt.*;
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
	private TrapGameServer server;

	private PlayBoard playBoard;
	private Scoreboard scoreboard;
	private Chat chat;

	private List<PlayerInfo> players;
	private int playerId;


	public TrapGameBoard(TrapGameClient container)
	{
		this.container = container;

		setBackground(new Color(0, 0, 0, 0));

		chat = new Chat(this);
		scoreboard = new Scoreboard(this);
		playBoard = null;
		server = null;

		setLayout(new BoardLayout());

		add(chat, BoardLayout.RIGHT);
		add(scoreboard, BoardLayout.LEFT);

		getLayout().addLayoutComponent(BoardLayout.DOWN, chat);
		getLayout().addLayoutComponent(BoardLayout.UP, scoreboard);
	}

	public void setHostedServer(TrapGameServer server)
	{
		this.server = server;
	}

	public void init(int playerId, List<PlayerInfo> players, int width, int height)
	{
		this.playerId = playerId;
		this.players = players;

		playBoard = new PlayBoard(this, width, height);
		add(playBoard, BoardLayout.BOARD);

		SwingUtilities.invokeLater(() -> {
			scoreboard.build();
			revalidate();
			repaint();
		});
	}

	public void updateStats(Map<Integer, PlayerStats> stats)
	{
		for(int id : stats.keySet())
		{
			getPlayers().forEach(player -> {
				if(player.getPlayerId() == id)
					player.setStats(stats.get(id));
			});
		}

		scoreboard.build();
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
		playBoard.requestFocusInWindow();
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
		scoreboard.build();

		revalidate();
		repaint();
	}

	public void dispose()
	{
		if(server != null)
			server.getScheduler().addTask(server::stop, 0);

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

	public void join(PlayerInfo info)
	{
		players.add(info);
		scoreboard.build();
	}

	public void leave(int playerId)
	{
		if(this.playerId == playerId)
			return;

		players.remove(getPlayer(playerId));
		scoreboard.build();
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

	public Scoreboard getScoreboard()
	{
		return scoreboard;
	}

	public Chat getChat()
	{
		return chat;
	}
}
