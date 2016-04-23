package me.winter.trapgame.client;

import java.util.List;
import java.awt.*;
import java.util.ArrayList;

/**
 * A layout that uses proportions to set bounds of it's elements
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

	public static String constraints(float scaleX, float scaleY, float x, float y, float width, float height)
	{
		return constraints((double)scaleX, (double)scaleY, (double)x, (double)y, (double)width, (double)height);
	}

	public static String constraints(float x, float y, float width, float height)
	{
		return constraints((double)x, (double)y, (double)width, (double)height);
	}

	public static String constraints(double scaleX, double scaleY, double x, double y, double width, double height)
	{
		return constraints(x / scaleX, y / scaleY, width / scaleX, height / scaleY);
	}

	public static String constraints(double x, double y, double width, double height)
	{
		return x + "," + y + "," + width + "," + height;
	}

	public static String constraints(int scaleX, int scaleY, int x, int y, int width, int height)
	{
		return constraints((double)scaleX, (double)scaleY, (double)x, (double)y, (double)width, (double)height);
	}

	public static String constraints(int x, int y, int width, int height)
	{
		return constraints(100, 100, x, y, width, height);
	}

	private static class ComponentGuide
	{
		private Component component;
		private double x, y, width, height;

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

		public ComponentGuide(Component component, double x, double y, double width, double height)
		{
			this.component = component;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		@Override
		public String toString()
		{
			return x + ", " + y + ", " + width + ", " + height;
		}

		public Component getComponent()
		{
			return component;
		}

		public void setComponent(Component component)
		{
			this.component = component;
		}

		public double getX()
		{
			return x;
		}

		public void setX(double x)
		{
			this.x = x;
		}

		public double getY()
		{
			return y;
		}

		public void setY(double y)
		{
			this.y = y;
		}

		public double getWidth()
		{
			return width;
		}

		public void setWidth(double width)
		{
			this.width = width;
		}

		public double getHeight()
		{
			return height;
		}

		public void setHeight(double height)
		{
			this.height = height;
		}
	}
}
