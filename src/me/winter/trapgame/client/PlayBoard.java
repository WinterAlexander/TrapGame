package me.winter.trapgame.client;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.packet.PacketInCursorMove;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the panel where players clicks and contains the buttons
 *
 * Created by Alexander Winter on 2016-03-28.
 */
public class PlayBoard extends JPanel
{
	private BufferedImage baseCursor;
	private TrapGameBoard container;

	private Map<Color, BufferedImage> preloaded;

	public PlayBoard(TrapGameBoard container)
	{
		this.container = container;
		preloaded = new HashMap<>();

		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor"));

		try
		{
			baseCursor = ImageIO.read(ClassLoader.class.getResourceAsStream("/cursor.png"));
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}

		addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{
				declareMouseMove(e.getX(), e.getY());
			}

			@Override
			public void mouseMoved(MouseEvent e)
			{
				declareMouseMove(e.getX(), e.getY());
			}

			private void declareMouseMove(int x, int y)
			{
				container.getClient().setCursor(new Point2D.Double((double)x / getWidth(), (double)y / getHeight()));
				container.getContainer().getConnection().sendPacket(new PacketInCursorMove(container.getClient().getCursor()));
				revalidate();
				repaint();
			}
		});
	}

	@Override
	public void paint(Graphics graphics)
	{
		super.paint(graphics);

		for(PlayerInfo player : container.getPlayers())
		{
			graphics.drawImage(getCursorImage(player.getColor()),
					(int)(player.getCursor().getX() * getWidth()) - 16,
					(int)(player.getCursor().getY() * getHeight()) - 16, null);
		}
	}

	public BufferedImage getCursorImage(Color color)
	{
		if(preloaded.containsKey(color))
			return preloaded.get(color);

		BufferedImage image = new BufferedImage(baseCursor.getWidth(), baseCursor.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics2D = image.createGraphics();

		for(int x = 0; x < baseCursor.getWidth(); x++)
		{
			for(int y = 0; y < baseCursor.getHeight(); y++)
			{
				Color currentColor = new Color(baseCursor.getRGB(x, y));
				graphics2D.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), currentColor.getRed()));
				graphics2D.drawRect(x, y, 1, 1);
			}
		}


		graphics2D.dispose();


		preloaded.put(color, image);
		return image;
	}
}
