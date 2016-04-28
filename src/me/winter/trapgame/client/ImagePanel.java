package me.winter.trapgame.client;

import javax.swing.*;
import java.awt.*;

/**
 * <p>Simple JPanel just drawing an image passed</p>
 *
 * <p>Created by 1541869 on 2016-04-19.</p>
 */
public class ImagePanel extends JPanel
{
	private Image image;

	public ImagePanel(Image image)
	{
		this.image = image;
		setBackground(new Color(0, 0, 0, 0));
		setForeground(new Color(0, 0, 0, 0));
	}

	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D g2draw = (Graphics2D) graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2draw.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		super.paintComponent(graphics);
	}

	public Image getImage()
	{
		return image;
	}

	public void setImage(Image image)
	{
		this.image = image;
	}
}
