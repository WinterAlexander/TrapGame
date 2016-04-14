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

	private JPanel statsContainer;

	public Scoreboard(TrapGameBoard board)
	{
		this.board = board;
		setLayout(new SimpleLayout());
		setBackground(new Color(0, 0, 0, 0));

		statsContainer = new JPanel();
		statsContainer.setLayout(new BoxLayout(statsContainer, BoxLayout.PAGE_AXIS));

		add(statsContainer, SimpleLayout.constraints(1d / 4, 1d / 6d, 1d / 2d, 5d / 6d));
		statsContainer.setBackground(new Color(0, 0, 0, 0));
	}

	/**
	 * Builds the menu adding any elements needed
	 *
	 * Loops the player list to add all the players infos as labels
	 *
	 */
	public void build()
	{
		statsContainer.removeAll();

		List<PlayerInfo> leaderBoard = new ArrayList<>(getBoard().getPlayers());
		leaderBoard.sort((player1, player2) -> board.getPlayBoard().getScore(player2) - board.getPlayBoard().getScore(player1));


		for(int index = 0; index < leaderBoard.size(); index++)
		{
			PlayerInfo player = leaderBoard.get(index);


			JPanel playerStats = new JPanel();
			playerStats.setBackground(new Color(0, 0, 0, 0));
			playerStats.setBorder(new EmptyBorder(10, 10, 10, 10));
			playerStats.setLayout(new BoxLayout(playerStats, BoxLayout.PAGE_AXIS));

			JLabel name = new JLabel((index + 1) + ": " + player.getName());
			name.setFont(new Font("Verdana", Font.BOLD, 20));
			name.setForeground(player.getColor());

			Font font = new Font("Verdana", Font.PLAIN, 18);

			JLabel scores = new JLabel("Scores: " + board.getPlayBoard().getScore(player));
			scores.setFont(font);
			scores.setForeground(player.getColor());

			JLabel wins = new JLabel("Wins: " + player.getStats().getWins());
			wins.setFont(font);
			wins.setForeground(player.getColor());

			JLabel loses = new JLabel("Loses: " + player.getStats().getLoses());
			loses.setFont(font);
			loses.setForeground(player.getColor());

			JLabel draws = new JLabel("Draws: " + player.getStats().getDraws());
			draws.setFont(font);
			draws.setForeground(player.getColor());

			JLabel ratio = new JLabel("Ratio: " + player.getStats().getRoundedWinLoseRatio());
			ratio.setFont(font);
			ratio.setForeground(player.getColor());

			JSeparator statsSeparator = new JSeparator(JSeparator.HORIZONTAL);
			statsSeparator.setForeground(player.getColor());

			playerStats.add(Box.createVerticalGlue());
			playerStats.add(name);
			playerStats.add(statsSeparator);
			playerStats.add(scores);
			playerStats.add(Box.createVerticalGlue());
			playerStats.add(wins);
			playerStats.add(Box.createVerticalGlue());
			playerStats.add(loses);
			playerStats.add(Box.createVerticalGlue());
			playerStats.add(draws);
			playerStats.add(Box.createVerticalGlue());
			playerStats.add(ratio);
			playerStats.add(Box.createVerticalGlue());

			statsContainer.add(playerStats);
			statsContainer.add(Box.createVerticalGlue());
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
