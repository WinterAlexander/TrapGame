package me.winter.trapgame.client.menu;

import javax.swing.*;
import java.awt.*;

/**
 * <p>A button used in the menu. Looks cooler than normal buttons.</p>
 *
 * <p>Created by 1541869 on 2016-04-21.</p>
 */
public class MenuButton extends JButton
{
	private TrapGameMenu menu;

	public MenuButton(TrapGameMenu menu, String text)
	{
		super(menu.getLangLine(text));

		this.menu = menu;

		setFont(new Font("Verdana", Font.PLAIN, 18));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
}
