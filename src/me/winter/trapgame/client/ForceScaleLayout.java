package me.winter.trapgame.client;

import me.winter.trapgame.util.MathUtil;

import java.awt.*;

/**
 * <p>A panel used to force it's child component to be scale in defined proportions</p>
 *
 * <p>Created by 1541869 on 2016-04-22.</p>
 */
public class ForceScaleLayout implements LayoutManager
{
	private float scaleX, scaleY;

	public ForceScaleLayout(int scaleX, int scaleY)
	{
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	@Override
	public void addLayoutComponent(String name, Component comp)
	{

	}

	@Override
	public void removeLayoutComponent(Component comp)
	{

	}

	@Override
	public Dimension preferredLayoutSize(Container parent)
	{
		if(parent.getComponents().length == 0)
			return new Dimension(0, 0);

		return parent.getComponents()[0].getPreferredSize();
	}

	@Override
	public Dimension minimumLayoutSize(Container parent)
	{
		if(parent.getComponents().length == 0)
			return new Dimension(0, 0);

		return parent.getComponents()[0].getMinimumSize();
	}

	@Override
	public void layoutContainer(Container parent)
	{
		synchronized(parent.getTreeLock())
		{
			for(Component child : parent.getComponents())
			{
				double scale = MathUtil.round(scaleX / scaleY, 2);
				double screen = MathUtil.round((double)parent.getWidth() / (double)parent.getHeight(), 2);

				if(scale == screen) //same
				{
					child.setSize(parent.getWidth(), parent.getHeight());
					child.setBounds(0, 0, parent.getWidth(), parent.getHeight());
					return;
				}

				if(scale > screen) //higher
				{
					int height = (int)(parent.getWidth() * scaleY / scaleX);
					child.setSize(parent.getWidth(), height);
					child.setBounds(0, (parent.getHeight() - height) / 2, parent.getWidth(), height);
					return;
				}
				//wider

				int width = (int)(parent.getHeight() * scaleX / scaleY);
				child.setSize(width, parent.getHeight());
				child.setBounds((parent.getWidth() - width) / 2, 0, width, parent.getHeight());
			}
		}
	}
}
