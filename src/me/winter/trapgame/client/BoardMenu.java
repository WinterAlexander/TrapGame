package me.winter.trapgame.client;

import me.winter.trapgame.shared.PlayerInfo;

import javax.swing.*;
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
		setLayout(new GridBagLayout());

		this.board = board;
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
		JButton leaveButton = new JButton("Disconnect");

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

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;

		add(leaveButton, constraints);

		constraints.gridx = 0;
		constraints.gridwidth = 1;

		for(PlayerInfo info : getBoard().getPlayers())
		{

			JLabel label = new JLabel("<html>" + info.getName() + ":<br>" + info.getStats() + "</html>");
			label.setForeground(info.getColor());

			constraints.gridy++;
			add(label, constraints);
		}

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
