package me.winter.trapgame.client;

import me.winter.trapgame.shared.packet.PacketInClick;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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

				if(!board.canClick(point))
					return;

				board.getContainer().getConnection().sendPacketLater(new PacketInClick(point));
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

		addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{
				MouseEvent event = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX() + getX(), e.getY() + getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());


				getParent().dispatchEvent(event);
			}

			@Override
			public void mouseMoved(MouseEvent e)
			{
				MouseEvent event = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX() + getX(), e.getY() + getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());

				getParent().dispatchEvent(event);
			}
		});
	}

	@Override
	public void paintComponent(Graphics graphics)
	{
		graphics.setColor(getBackground());

		graphics.fillRect(0, 0, getWidth(), getHeight());

		graphics.drawImage(getBoard().getButtonFrame(), 0, 0, getWidth(), getHeight(), null);

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
