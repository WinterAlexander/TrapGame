package me.winter.trapgame.client.menu;

import me.winter.trapgame.client.ImagePanel;
import me.winter.trapgame.client.ResourceManager;
import me.winter.trapgame.client.SimpleLayout;
import me.winter.trapgame.server.TrapGameServer;
import me.winter.trapgame.util.ColorTransformer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
	private JTextPane textPane;

	public TutorialPane(TrapGameMenu menu)
	{
		this.menu = menu;

		textPane = new JTextPane();
		setBackground(ColorTransformer.TRANSPARENT);
		textPane.setBackground(new Color(220, 220, 220));

		textPane.setContentType("text/html");
		textPane.setEditable(false);

		ResourceManager res = menu.getClient().getResourceManager();

		String page = res.getText("tutorial-page-" + menu.getClient().getLang().getName());
		page = page.replace("<!--style-->", res.getText("tutorial-style"));

		textPane.setText(page);

		setLayout(new SimpleLayout());

		JScrollPane scroll = new JScrollPane();

		scroll.setViewportView(textPane);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setBorder(new EmptyBorder(0, 0, 0, 0));


		add(scroll, SimpleLayout.constraints(8, 9, 0.25, 0.75, 7.5, 7.5));

		insertIcons();
		//menu.getClient().getScheduler().addTask(new Task(2000, false, this::insertIcons));
		revalidate();

	}

	@Override
	protected void paintComponent(Graphics graphics)
	{
		Graphics2D g2draw = (Graphics2D) graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2draw.drawImage(menu.getClient().getResourceManager().getImage("background"), -getX(), 0, menu.getWidth(), menu.getHeight(), null);

		super.paintComponent(graphics);
	}

	private void insertIcons()
	{
		Pattern regex = Pattern.compile("<!--\\s*Buttons:\\{([^{}]*)\\}\\s*-->");
		Matcher matcher = regex.matcher(textPane.getText());

		if(!matcher.find())
		{
			menu.getClient().getLogger().warning("Didn't find icon insertion tag.");
			return;
		}

		for(String insert : matcher.group(1).split(","))
		{
			int caret = Integer.parseInt(insert.split(":")[1]);

			int colorAlpha = 255;

			switch(insert.split(":")[0])
			{
				case "blank":
					colorAlpha = 0;
					break;

				case "semi":
					colorAlpha = 100;
					break;

				case "color":
					colorAlpha = 200;
					break;
			}

			int width = 40;

			BufferedImage button = (BufferedImage)menu.getClient().getResourceManager().getImage("game-button");
			Image image = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);



			Graphics2D g2draw = ((BufferedImage)image).createGraphics();

			g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			g2draw.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

			g2draw.setColor(Color.WHITE);
			g2draw.fillRoundRect(   3 * width / 256,
									3 * width / 256,
									width - 3 * width / 128,
									width - 3 * width / 128,
									width / 6, width / 6);

			g2draw.setColor(new ColorTransformer(TrapGameServer.COLORS[0], colorAlpha));
			g2draw.fillRoundRect(   3 * width / 256,
									3 * width / 256,
									width - 3 * width / 128,
									width - 3 * width / 128,
									width / 6, width / 6);
			g2draw.drawImage(button, 0, 0, width, width, null);


			g2draw.dispose();

			ImagePanel icon = new ImagePanel(image);
			icon.setBackground(new Color(220, 220, 220));

			icon.setPreferredSize(new Dimension(48, 48));

			icon.setMinimumSize(icon.getPreferredSize());
			icon.setMaximumSize(icon.getPreferredSize());

			icon.setAlignmentY(0.75f);
			icon.setAlignmentX(0.5f);

			textPane.setCaretPosition(caret);
			textPane.insertComponent(icon);
		}
	}
}
