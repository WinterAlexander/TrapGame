package me.winter.trapgame.client.menu.demo;

import me.winter.trapgame.client.menu.TrapGameMenu;
import me.winter.trapgame.server.TrapGameServer;
import me.winter.trapgame.shared.Task;
import me.winter.trapgame.util.ColorTransformer;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>A play board used in the menu to show a small part of the gameplay</p>
 *
 * <p>Created by winter on 20/04/16.</p>
 */
public class DemoPlayBoard extends JPanel
{
	private TrapGameMenu menu;

	private Map<Point, Color> board;
	private int boardWidth;

	public DemoPlayBoard(TrapGameMenu menu, int boardWidth)
	{
		this.menu = menu;
		setBackground(new Color(0, 0, 0, 0));
		board = new HashMap<>();
		this.boardWidth = boardWidth;

		reset();
	}

	@Override
	public void paintComponent(Graphics graphics)
	{
		int paneWidth = getWidth() * 15 / 16;

		int paneX = getWidth() / 2 - paneWidth / 2;
		int paneY = getHeight() / 2 - paneWidth / 2;

		Graphics2D g2draw = (Graphics2D) graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2draw.drawImage(menu.getClient().getResourceManager().getImage("background"), -getX(), 0, menu.getWidth(), menu.getHeight(), null);

		float buttonWidth = paneWidth / (float)boardWidth;

		for(int x = 0; x < boardWidth; x++)
		{
			for(int y = 0; y < boardWidth; y++)
			{
				Color color = board.get(new Point(x, y));

				int xCeil = (int)((x + 1) * buttonWidth) > (int)(x * buttonWidth) + (int)buttonWidth ? 1 : 0;
				int yCeil = (int)((y + 1) * buttonWidth) > (int)(y * buttonWidth) + (int)buttonWidth ? 1 : 0;

				int width = (int)buttonWidth + xCeil;
				int height = (int)buttonWidth + yCeil;

				if(color != null)
				{
					g2draw.setColor(new ColorTransformer(color, 200));
					g2draw.fillRoundRect(
							paneX + (int)(x * buttonWidth) + (int)(3 * buttonWidth / 256),
							paneY + (int)(y * buttonWidth) + (int)(3 * buttonWidth / 256),
							width - (int)(3 * buttonWidth / 128),
							height - (int)(3 * buttonWidth / 128),
							width / 6, height / 6);
				}

				g2draw.drawImage(menu.getClient().getResourceManager().getImage("game-button"), paneX + (int)(x * buttonWidth), paneY + (int)(y * buttonWidth), (int)buttonWidth + xCeil, (int)buttonWidth + yCeil, null);

			}
		}
	}

	public boolean isActive()
	{
		return getMenu().getRightPane() == this && getMenu() == getMenu().getClient().getMenu();
	}

	public void place(Point point, Color color)
	{
		board.put(point, color);
		revalidate();
		repaint();

		menu.getClient().getResourceManager().getSound("click0").play();
	}

	public void reset()
	{
		board.clear();

		menu.getClient().getScheduler().cancelTasks(DemoPlay.class);

		for(int i = 0; i < 4; i++)
		{
			Task task = new DemoPlay(this, TrapGameServer.COLORS[i]);
			menu.getClient().getScheduler().addTask(task);
			task.setLastWork(menu.getClient().getScheduler().getTimeMillis() - task.getDelay() + 50);
		}
	}

	public TrapGameMenu getMenu()
	{
		return menu;
	}

	public int getBoardWidth()
	{
		return boardWidth;
	}

	public Map<Point, Color> getBoard()
	{
		return board;
	}
}
