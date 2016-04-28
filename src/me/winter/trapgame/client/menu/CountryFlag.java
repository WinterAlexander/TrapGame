package me.winter.trapgame.client.menu;

import me.winter.trapgame.shared.Task;
import me.winter.trapgame.util.ColorTransformer;
import me.winter.trapgame.util.StringUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * Created by Alexander Winter on 2016-04-24.
 */
public class CountryFlag extends JButton
{
	private TrapGameMenu menu;

	private String lang;

	public CountryFlag(TrapGameMenu menu, String lang)
	{
		super();
		this.menu = menu;
		this.lang = lang;
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setForeground(ColorTransformer.TRANSPARENT);
		setCursor(new Cursor(Cursor.HAND_CURSOR));

		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				setForeground(new Color(0, 0, 0, 50));
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				setForeground(ColorTransformer.TRANSPARENT);
			}
		});

		addActionListener(event ->
					{
						setForeground(new Color(0, 0, 0, 100));
						menu.getClient().getScheduler().addTask(new Task(0, false, () ->
						{
							menu.getClient().getUserProperties().setLanguage(lang);
							menu.getClient().getUserProperties().save();
							try
							{
								menu.getClient().reloadLang();
							}
							catch(Exception ex)
							{
								JOptionPane.showMessageDialog(menu.getClient(), "We are sorry, an internal error occurred during your game session: \n\n" + StringUtil.getStackTrace(ex) + "\nPlease report this error to me at a.w1nter@hotmail.com", "TrapGame has crashed :(", JOptionPane.ERROR_MESSAGE);
								System.exit(-1);
							}
						}));
					});
	}

	@Override
	protected void paintComponent(Graphics graphics)
	{
		Image image = menu.getClient().getResourceManager().getImage("flag-" + lang);

		Graphics2D graphics2D = (Graphics2D)graphics;

		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		graphics2D.drawImage(menu.getClient().getResourceManager().getImage("background"), -getX(), -getY(), menu.getWidth(), menu.getHeight(), null);

		graphics2D.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		graphics2D.setColor(getForeground());
		graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), (int)(getWidth() * 0.4), (int)(getHeight() * 0.4));
	}
}
