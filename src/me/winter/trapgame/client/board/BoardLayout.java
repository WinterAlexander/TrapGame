package me.winter.trapgame.client.board;

import java.awt.*;

/**
 * <p>A custom layout made to organize the TrapGameBoard as needed</p>
 *
 * Created by 1541869 on 2016-04-07.
 */
public class BoardLayout implements LayoutManager
{
	public static final String BOARD = "board";
	public static final String RIGHT = "right";
	public static final String LEFT = "left";
	public static final String UP = "up";
	public static final String DOWN = "down";

	private Component board;
	private Component right;
	private Component left;
	private Component up;
	private Component down;


	@Override
	public void addLayoutComponent(String name, Component comp)
	{
		synchronized(comp.getTreeLock())
		{
			switch(name.toLowerCase())
			{
				case BOARD:
					board = comp;
					break;

				case RIGHT:
					right = comp;
					break;

				case LEFT:
					left = comp;
					break;

				case UP:
					up = comp;
					break;

				case DOWN:
					down = comp;
					break;
			}
		}
	}

	@Override
	public void removeLayoutComponent(Component comp)
	{
		synchronized(comp.getTreeLock())
		{
			if(comp == board)
				board = null;

			if(comp == right)
				right = null;

			if(comp == left)
				left = null;

			if(comp == up)
				up = null;

			if(comp == down)
				down = null;
		}
	}

	@Override
	public Dimension preferredLayoutSize(Container parent)
	{
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent)
	{
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public void layoutContainer(Container parent)
	{
		synchronized(parent.getTreeLock())
		{
			if(board == null)
				return;

			if(parent.getWidth() > parent.getHeight())
			{
				board.setSize(parent.getHeight(), parent.getHeight());

				int sideWidth = (parent.getWidth() - board.getWidth());

				if(left != null && right != null) //s'il y en a 2, les deux se partagent la moitié de l'espace
					sideWidth /= 2;

				if(left != null)
				{
					left.setSize(sideWidth, board.getHeight());
					left.setBounds(0, 0, sideWidth, board.getHeight());
				}

				if(right != null)
				{
					right.setSize(sideWidth, board.getHeight());
					right.setBounds(board.getWidth() + (left != null ? sideWidth : 0), 0, sideWidth, board.getHeight());
				}

				board.setBounds(left != null ? sideWidth : 0, 0, board.getWidth(), board.getHeight());

				if(up != null && up != left && up != right)
				{
					up.setSize(0, 0);
					up.setBounds(0, 0, 0, 0);
				}

				if(down != null && down != left && down != right)
				{
					down.setSize(0, 0);
					down.setBounds(0, 0, 0, 0);
				}
				return;
			}

			board.setSize(parent.getWidth(), parent.getWidth());

			int sideHeight = (parent.getHeight() - board.getHeight());

			if(up != null && down != null) //s'il y en a 2, les deux se partagent la moitié de l'espace
				sideHeight /= 2;

			if(up != null)
			{
				up.setSize(board.getWidth(), sideHeight);
				up.setBounds(0, 0, board.getWidth(), sideHeight);
			}

			if(down != null)
			{
				down.setSize(board.getWidth(), sideHeight);
				down.setBounds(0, board.getHeight() + (up != null ? sideHeight : 0), board.getWidth(), sideHeight);
			}

			board.setBounds(0, up != null ? sideHeight : 0, board.getWidth(), board.getHeight());

			if(left != null && left != up && left != down)
			{
				left.setSize(0, 0);
				left.setBounds(0, 0, 0, 0);
			}

			if(right != null && right != up && right != down)
			{
				right.setSize(0, 0);
				right.setBounds(0, 0, 0, 0);
			}
		}
	}
}
