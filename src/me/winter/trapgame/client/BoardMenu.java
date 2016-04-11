package me.winter.trapgame.client;

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
public class BoardMenu extends JPanel
{
	private TrapGameBoard board;

	public BoardMenu(TrapGameBoard board)
	{
		this.board = board;
		setLayout(new BorderLayout());
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
		JButton leaveButton = new JButton("Disconnect from server");

		leaveButton.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{

			}

			@Override
			public void mousePressed(MouseEvent e)
			{

			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				board.getContainer().getConnection().close();
				board.dispose();
				board.getContainer().goToMenu();
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{

			}

			@Override
			public void mouseExited(MouseEvent e)
			{

			}
		});

		setBackground(new Color(0, 0, 0, 0));

		JPanel buttonContainer = new JPanel();
		buttonContainer.setBackground(new Color(0, 0, 0, 0));
		buttonContainer.add(leaveButton);

		JPanel statsContainer = new JPanel();

		statsContainer.setBackground(new Color(0, 0, 0, 0));

		JLabel leaderBoard = new JLabel("Scores");
		leaderBoard.setFont(new Font("Verdana", Font.BOLD, 20));

		//leaderBoard.setPreferredSize(new Dimension(Integer.MAX_VALUE, 128));
		leaderBoard.setForeground(Color.WHITE);

		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		separator.setForeground(Color.WHITE);
		separator.setPreferredSize(new Dimension(Integer.MAX_VALUE / 2, 1));

		statsContainer.add(leaderBoard);
		statsContainer.add(separator);

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

			statsContainer.add(playerStats);
		}

		statsContainer.setPreferredSize(statsContainer.getPreferredSize());

		add(buttonContainer, BorderLayout.SOUTH);
		add(statsContainer, BorderLayout.CENTER);

		revalidate();
		repaint();
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
