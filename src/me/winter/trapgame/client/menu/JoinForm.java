package me.winter.trapgame.client.menu;

import me.winter.trapgame.client.SimpleLayout;
import me.winter.trapgame.util.ColorTransformer;

import javax.swing.*;
import java.awt.*;

/**
 * <p>Represents a form containing the list of available servers
 *
 * Users can also join using ip directly</p>
 *
 * <p>Created by 1541869 on 2016-04-22.</p>
 */
public class JoinForm extends JPanel
{
	private TrapGameMenu menu;

	public JoinForm(TrapGameMenu menu)
	{
		this.menu = menu;

		setBackground(ColorTransformer.TRANSPARENT);

		setLayout(new SimpleLayout());

		add(new ServerList(this), SimpleLayout.constraints(10, 10, 1, 2, 8, 6));
	}

	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D g2draw = (Graphics2D) graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2draw.drawImage(menu.getClient().getResourceManager().getImage("background"), -getX(), -getY(), menu.getWidth(), menu.getHeight(), null);

		super.paintComponent(graphics);
	}

	public TrapGameMenu getMenu()
	{
		return menu;
	}
}
