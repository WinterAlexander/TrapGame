package me.winter.trapgame.client;

import java.util.List;
import java.awt.*;
import java.util.ArrayList;

/**
 * A layout that uses
 *
 * Created by Alexander Winter on 2016-04-07.
 */
public class SimpleLayout implements LayoutManager
{
	private List<ComponentGuide> guides;

	public SimpleLayout()
	{
		this.guides = new ArrayList<>();
	}

	@Override
	public void addLayoutComponent(String name, Component comp)
	{
		guides.add(new ComponentGuide(comp, name));
	}

	@Override
	public void removeLayoutComponent(Component comp)
	{
		guides.removeIf(guide -> guide.getComponent() == comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent)
	{
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent)
	{
		return new Dimension(0, 0);
	}

	@Override
	public void layoutContainer(Container parent)
	{
		synchronized(parent.getTreeLock())
		{
			for(ComponentGuide guide : guides)
			{
				int x = (int)(guide.getX() * parent.getWidth());
				int y = (int)(guide.getY() * parent.getHeight());
				int width = (int)(guide.getWidth() * parent.getWidth());
				int height = (int)(guide.getHeight() * parent.getHeight());

				guide.getComponent().setSize(width, height);
				guide.getComponent().setBounds(x, y, width, height);
			}
		}
	}

	public static String constraints(float x, float y, float width, float height)
	{
		return x + "," + y + "," + width + "," + height;
	}

	public static String constraints(double x, double y, double width, double height)
	{
		return constraints((float)x, (float)y, (float)width, (float)height);
	}

	public static String constraints(int x, int y, int width, int height)
	{
		return constraints((float)x / 100, (float)y / 100, (float)width / 100, (float)height / 100);
	}

	private static class ComponentGuide
	{
		private Component component;
		private float x, y, width, height;

		public ComponentGuide(Component component, String constraints)
		{
			this.component = component;
			try
			{
				String[] values = constraints.split(",");

				x = Float.parseFloat(values[0]);
				y = Float.parseFloat(values[1]);
				width = Float.parseFloat(values[2]);
				height = Float.parseFloat(values[3]);
			}
			catch(NumberFormatException | ArrayIndexOutOfBoundsException ex)
			{
				throw new IllegalArgumentException("Invalid constraints");
			}
		}

		public ComponentGuide(Component component, float x, float y, float width, float height)
		{
			this.component = component;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		public Component getComponent()
		{
			return component;
		}

		public void setComponent(Component component)
		{
			this.component = component;
		}

		public float getX()
		{
			return x;
		}

		public void setX(float x)
		{
			this.x = x;
		}

		public float getY()
		{
			return y;
		}

		public void setY(float y)
		{
			this.y = y;
		}

		public float getWidth()
		{
			return width;
		}

		public void setWidth(float width)
		{
			this.width = width;
		}

		public float getHeight()
		{
			return height;
		}

		public void setHeight(float height)
		{
			this.height = height;
		}
	}
}
