package me.winter.trapgame.client.board;

import me.winter.trapgame.shared.PlayerInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
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

		for(PlayerInfo info : getBoard().getPlayers())
		{
			JPanel playerStats = new JPanel();
			playerStats.setBackground(new Color(0, 0, 0, 64));
			playerStats.setBorder(new EmptyBorder(10, 10, 10, 10));
			playerStats.setLayout(new BoxLayout(playerStats, BoxLayout.Y_AXIS));
			playerStats.setPreferredSize(new Dimension(160, 128));

			JLabel name = new JLabel(info.getName());
			name.setFont(new Font("Verdana", Font.BOLD, 20));
			name.setForeground(info.getColor());
			name.setPreferredSize(new Dimension(128, 64));

			Font font = new Font("Verdana", Font.PLAIN, 18);

			JLabel wins = new JLabel("Wins: " + info.getStats().getWins());
			wins.setFont(font);
			wins.setForeground(info.getColor());

			JLabel loses = new JLabel("Loses: " + info.getStats().getLoses());
			loses.setFont(font);
			loses.setForeground(info.getColor());

			JLabel draws = new JLabel("Draws: " + info.getStats().getDraws());
			draws.setFont(font);
			draws.setForeground(info.getColor());

			JLabel ratio = new JLabel("Ratio: " + info.getStats().getRoundedWinLoseRatio());
			ratio.setFont(font);
			ratio.setForeground(info.getColor());

			JSeparator statsSeparator = new JSeparator(JSeparator.HORIZONTAL);
			statsSeparator.setForeground(info.getColor());

			playerStats.add(Box.createVerticalGlue());
			playerStats.add(name);
			playerStats.add(statsSeparator);
			playerStats.add(wins);
			playerStats.add(Box.createVerticalGlue());
			playerStats.add(loses);
			playerStats.add(Box.createVerticalGlue());
			playerStats.add(draws);
			playerStats.add(Box.createVerticalGlue());
			playerStats.add(ratio);
			playerStats.add(Box.createVerticalGlue());

			add(playerStats);
		}

		revalidate();
		repaint();
	}

	@Override
	public void paint(Graphics g)
	{
		g.drawImage(board.getContainer().getResourceManager().getImage("scoreboard"), 0, 0, getWidth(), getHeight(), null);

		super.paintComponent(g);
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
