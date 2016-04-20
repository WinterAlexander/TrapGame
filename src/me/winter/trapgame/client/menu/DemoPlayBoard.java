package me.winter.trapgame.client.menu;

import javax.swing.*;
import java.awt.*;

/**
 * <p>A play board used in the menu to show a small part of the gameplay</p>
 *
 * <p>Created by winter on 20/04/16.</p>
 */
public class DemoPlayBoard extends JPanel
{
	private TrapGameMenu menu;

	public DemoPlayBoard(TrapGameMenu menu)
	{
		this.menu = menu;
		setBackground(new Color(0, 0, 0, 0));
	}

	@Override
	public void paint(Graphics graphics)
	{
		int width = getWidth() * 15 / 16;

		int x = getWidth() / 2 - width / 2;
		int y = getHeight() / 2 - width / 2;

		Graphics2D g2draw = (Graphics2D) graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2draw.drawImage(menu.getClient().getResourceManager().getImage("background"), -getX(), 0, menu.getClient().getWidth(), menu.getClient().getHeight(), null);
	}
}
