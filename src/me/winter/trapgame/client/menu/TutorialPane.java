package me.winter.trapgame.client.menu;

import me.winter.trapgame.client.ResourceManager;
import me.winter.trapgame.client.SimpleLayout;
import me.winter.trapgame.server.TrapGameServer;
import me.winter.trapgame.util.ColorTransformer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>A pane displayed when clicked the button "How to play" in the menu</p>
 *
 * <p>Created by Alexander Winter on 2016-04-20.</p>
 */
public class TutorialPane extends JPanel
{
	private TrapGameMenu menu;

	public TutorialPane(TrapGameMenu menu)
	{
		try
		{
			this.menu = menu;

			JTextPane textPane = new JTextPane()
			{
				@Override
				public void paint(Graphics graphics)
				{
					Graphics2D g2draw = (Graphics2D)graphics;

					g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

					g2draw.drawImage(menu.getClient().getResourceManager().getImage("background"), -TutorialPane.this.getX() - getX(), -TutorialPane.this.getY() - getY(), menu.getClient().getWidth(), menu.getClient().getHeight(), null);

					g2draw.setColor(new Color(0, 0, 0, 20));
					g2draw.fillRoundRect(0, 0, getWidth(), getHeight(), getWidth() / 8, getHeight() / 8);

					super.paint(graphics);
				}
			};

			setBackground(ColorTransformer.TRANSPARENT);
			textPane.setBackground(ColorTransformer.TRANSPARENT);

			textPane.setContentType("text/html");
			textPane.setEditable(false);

			ResourceManager res = menu.getClient().getResourceManager();

			String page = res.getText("tutorial-page-" + menu.getClient().getLang().getName());
			page = page.replace("<!--style-->", res.getText("tutorial-style"));

			textPane.setText(page);
			Pattern regex = Pattern.compile("<!--([^<>]+)-->");

			Matcher matcher = regex.matcher(page);


			while(matcher.find() && !matcher.hitEnd())
			{
				Image image;

				switch(matcher.group(1).toLowerCase())
				{
					case "blank_button":
						image = res.getImage("game-button");
						break;

					case "color_button":
						BufferedImage button = (BufferedImage)res.getImage("game-button");
						image = new BufferedImage(button.getWidth(), button.getHeight(), BufferedImage.TYPE_INT_ARGB);

						Graphics2D g2draw = ((BufferedImage)image).createGraphics();

						g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
						g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

						g2draw.setColor(new ColorTransformer(TrapGameServer.COLORS[0], 200));
						g2draw.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), button.getWidth() / 4, button.getHeight() / 4);
						g2draw.drawImage(button, 0, 0, button.getWidth(), button.getHeight(), null);

						g2draw.dispose();
						break;

					default:
						image = null;
				}

				if(image != null)
					textPane.insertComponent(new JLabel(new ImageIcon(image)));
			}

			setLayout(new SimpleLayout());
			add(textPane, SimpleLayout.constraints(8, 9, 0.25, 0.75, 7.5, 7.5));
			revalidate();
			repaint();
		}
		catch(Throwable debug)
		{
			debug.printStackTrace(System.out);
		}
	}

	@Override
	public void paint(Graphics graphics)
	{
		Graphics2D g2draw = (Graphics2D) graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2draw.drawImage(menu.getClient().getResourceManager().getImage("background"), -getX(), 0, menu.getClient().getWidth(), menu.getClient().getHeight(), null);

		super.paint(graphics);
	}
}
