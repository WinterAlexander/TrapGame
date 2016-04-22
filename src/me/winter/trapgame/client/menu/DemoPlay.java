package me.winter.trapgame.client.menu;

import me.winter.trapgame.shared.Task;
import me.winter.trapgame.util.BetterRandom;

import java.awt.*;
import java.util.*;

/**
 * <p>Undocumented :(</p>
 *
 * <p>Created by 1541869 on 2016-04-22.</p>
 */
public class DemoPlay extends Task
{
	private DemoPlayBoard demo;
	private Color color;
	private BetterRandom random;

	public DemoPlay(DemoPlayBoard demo, Color color)
	{
		super(750, true);
		this.color = color;
		this.demo = demo;
		random = new BetterRandom();
	}

	@Override
	public void run()
	{
		if(demo.isActive())
			return;

		if(demo.getBoard().size() == demo.getBoardWidth() * demo.getBoardWidth())
			return;

		if(!demo.getBoard().containsValue(color))
		{
			Point point;
			do
			{
				point = random.nextPoint(demo.getBoardWidth(), demo.getBoardWidth());
			}
			while(demo.getBoard().containsKey(point));

			demo.place(point, color);
			return;
		}

		java.util.List<Map.Entry<Point, Color>> entries = new ArrayList<>(demo.getBoard().entrySet());
		Collections.reverse(entries);

		for(Map.Entry<Point, Color> entry : entries)
		{
			if(!entry.getValue().equals(color))
				continue;

			java.util.List<Point> directions = new ArrayList<>(Arrays.asList(new Point(0, 1), new Point(0, -1), new Point(1, 0), new Point(-1, 0)));
			Collections.shuffle(directions, random);


			for(Point direction : directions)
			{
				Point point = new Point(entry.getKey());
				point.translate((int)direction.getX(), (int)direction.getY());

				if(point.getX() >= demo.getBoardWidth() || point.getY() >= demo.getBoardWidth()
				|| point.getX() < 0 || point.getY() < 0)
					continue;

				if(demo.getBoard().get(point) == null)
				{
					demo.place(point, color);
					return;
				}
			}
		}
	}
}
