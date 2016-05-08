package me.winter.trapgame.client.menu.join;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * Created by Alexander Winter on 2016-04-29.
 */
public class RefreshButton extends JButton
{
	private JoinForm joinForm;

	private boolean hovered;

	public RefreshButton(JoinForm joinForm)
	{
		this.joinForm = joinForm;
		setMinimumSize(new Dimension(32, 32));
		setPreferredSize(new Dimension(80, 80));
		setMaximumSize(new Dimension(128, 128));
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setBackground(new Color(230, 230, 230));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addActionListener(event -> joinForm.getList().update());
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseExited(MouseEvent e)
			{
				hovered = false;
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				hovered = true;
				repaint();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics graphics)
	{
		Graphics2D g2draw = (Graphics2D) graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2draw.setColor(getBackground());
		g2draw.fillRect(0, 0, getWidth(), getHeight());

		Image image = joinForm.getMenu().getClient().getResourceManager().getImage(isHovered() ? "hovered-refresh" : "normal-refresh");

		g2draw.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

	public boolean isHovered()
	{
		return hovered;
	}


}
