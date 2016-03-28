package me.winter.trapgame.client;

import me.winter.trapgame.shared.packet.PacketInClick;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

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
		setOpaque(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setContentAreaFilled(false);
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

				if(!(e.getX() > getWidth() / 8
				&& e.getX() <= getWidth() * 7 / 8
				&& e.getY() > getHeight() / 8
				&& e.getX() <= getHeight() * 7 / 8))
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

	@Override
	public void paintComponent(Graphics graphics)
	{
		graphics.setColor(getBackground());
		if(!getBackground().equals(getParent().getBackground()))
			graphics.fillRoundRect(getWidth() / 8, getHeight() / 8, getWidth() * 3 / 4, getHeight() * 3 / 4, getWidth() / 4, getHeight() / 4);

		graphics.setColor(Color.BLACK);
		graphics.drawRoundRect(getWidth() / 8, getHeight() / 8, getWidth() * 3 / 4, getHeight() * 3 / 4, getWidth() / 4, getHeight() / 4);

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
