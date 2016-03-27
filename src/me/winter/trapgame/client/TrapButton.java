package me.winter.trapgame.client;

import me.winter.trapgame.shared.packet.PacketInClick;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Represents a button in TrapGameBoard used to play the game
 * Will send PacketInClick packets when clicked
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class TrapButton extends JButton
{
	private TrapGameBoard board;
	private Point point;

	public TrapButton(TrapGameBoard board, Point point)
	{
		super(point.getX() + ", " + point.getY());
		this.board = board;
		this.point = point;

		addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{

			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(board.isBoardLocked())
					return;

				if(!board.canClick(point))
					return;

				board.getContainer().getConnection().sendPacket(new PacketInClick(point));
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{

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
	}

	public TrapGameBoard getBoard()
	{
		return board;
	}

	public Point getPoint()
	{
		return point;
	}
}
