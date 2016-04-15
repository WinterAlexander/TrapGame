package me.winter.trapgame.client.board;

import me.winter.trapgame.client.SimpleLayout;
import me.winter.trapgame.shared.PlayerInfo;

import javax.swing.*;
import java.util.List;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a board menu displayed at the left and at the top of the board
 *
 * Created by 1541869 on 2016-04-07.
 */
public class Scoreboard extends JPanel
{
	private TrapGameBoard board;

	public Scoreboard(TrapGameBoard board)
	{
		this.board = board;
		setLayout(new SimpleLayout());
		setBackground(new Color(0, 0, 0, 0));
	}

	/**
	 * Builds the menu adding any elements needed
	 *
	 * Loops the player list to add all the players infos as labels
	 *
	 */
	public void build()
	{
		removeAll();

		List<PlayerInfo> leaderBoard = new ArrayList<>(getBoard().getPlayers());
		leaderBoard.sort((player1, player2) -> board.getPlayBoard().getScore(player2) - board.getPlayBoard().getScore(player1));

		for(int index = 0; index < leaderBoard.size(); index++)
		{
			PlayerInfo player = leaderBoard.get(index);


			JPanel playerStats = new JPanel();
			playerStats.setBackground(new Color(0, 0, 0, 0));
			playerStats.setLayout(new BoxLayout(playerStats, BoxLayout.PAGE_AXIS));

			JLabel name = new JLabel((index + 1) + ": " + player.getName());
			name.setFont(new Font("Verdana", Font.BOLD, board.getContainer().getHeight() / 32));
			name.setForeground(player.getColor());

			JLabel scores = new JLabel("Score: " + board.getPlayBoard().getScore(player));
			scores.setFont(new Font("Verdana", Font.PLAIN, board.getContainer().getHeight() / 36));
			scores.setForeground(player.getColor());

			JLabel winsloses = new JLabel("Stats: " + player.getStats().getWins() + " / " + player.getStats().getLoses() + " (" + player.getStats().getRoundedWinLoseRatio() + ")");
			winsloses.setFont(new Font("Verdana", Font.PLAIN, board.getContainer().getHeight() / 35));
			winsloses.setForeground(player.getColor());

			playerStats.add(Box.createVerticalGlue());
			playerStats.add(name);
			playerStats.add(Box.createVerticalGlue());
			playerStats.add(scores);
			playerStats.add(Box.createVerticalGlue());
			playerStats.add(winsloses);
			playerStats.add(Box.createVerticalGlue());

			add(playerStats, SimpleLayout.constraints(1d / 16d, 1d / 6d + index * 3d / 20d, 7d / 8d, 1d / 8d));
		}

		revalidate();
		repaint();
	}

	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2draw = (Graphics2D) g;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2draw.drawImage(board.getContainer().getResourceManager().getImage("background"), 0, 0, board.getWidth(), board.getHeight(), null);
		g2draw.drawImage(board.getContainer().getResourceManager().getImage("scoreboard"), 0, 0, getWidth(), getHeight(), null);

		super.paint(g);
	}

	public TrapGameBoard getBoard()
	{
		return board;
	}

	public void setBoard(TrapGameBoard board)
	{
		this.board = board;
	}
}
