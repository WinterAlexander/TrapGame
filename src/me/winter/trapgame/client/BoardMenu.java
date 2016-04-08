package me.winter.trapgame.client;

import me.winter.trapgame.shared.PlayerInfo;

import javax.swing.*;
import javax.swing.border.LineBorder;
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

		setBackground(Color.BLACK);

		JPanel buttonContainer = new JPanel();
		buttonContainer.setBackground(new Color(0, 0, 0, 0));
		buttonContainer.add(leaveButton);

		JPanel statsContainer = new JPanel();
		statsContainer.setBackground(new Color(255, 0, 0, 0));
		statsContainer.setLayout(new GridLayout(0, 1, 5, 5));


		for(PlayerInfo info : getBoard().getPlayers())
		{
			JTextPane label = new JTextPane();
			label.setContentType("text/html");
			label.setText(  "<html>" +
								"<body>" +
									"<center><div>" +
										"<center>" +
											"<b>" + info.getName() + "</b><br>" +
										"</center>" +
										"Wins: " + info.getStats().getWins() + "<br>" +
										"Loses: " + info.getStats().getLoses() + "<br>" +
										"Draws: " + info.getStats().getDraws() + "<br>" +
										"Ratio: " + info.getStats().getRoundedWinLoseRatio() +
									"</div></center>" +
								"</body>" +
							"</html>");
			label.setEditable(false);
			label.setFont(new Font("Arial", Font.PLAIN, 18));
			label.setForeground(info.getColor());
			label.setBackground(new Color(0, 0, 0, 0));
			label.setBorder(new LineBorder(info.getColor(), 1, true));

			statsContainer.add(label);
		}

		add(buttonContainer, BorderLayout.NORTH);
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
