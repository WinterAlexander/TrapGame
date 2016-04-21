package me.winter.trapgame.client.menu;

import me.winter.trapgame.util.BetterRandom;

import javax.swing.*;
import java.awt.*;

/**
 * <p>A form listing the games to join</p>
 *
 * <p>Created by 1541869 on 2016-04-19.</p>
 */
public class HostForm extends JPanel
{
	public HostForm()
	{
		setBackground(new BetterRandom().nextColor());
	}

	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D g2draw = (Graphics2D) graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		//g2draw.drawImage(menu.getClient().getResourceManager().getImage("background"), -getX(), 0, menu.getClient().getWidth(), menu.getClient().getHeight(), null);
	}
}
