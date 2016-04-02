package me.winter.trapgame.shared;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Used on both client and server side to auto-fill zones that belong to a player
 * Should only be called when all players have placed their first block
 *
 * Created by Alexander Winter on 2016-04-02.
 */
public interface BoardFiller
{
	static <T> boolean tryFill(Point source, T owner, Map<Point, T> board, int boardWidth, int boardHeight)
	{
		if(board.get(source) != null)
			return false;

		List<List<Point>> layers = new ArrayList<>();
		layers.add(new ArrayList<>());
		layers.get(0).add(source);

		for(int layer = 0; ; layer++)
		{
			List<Point> currentLayer = layers.get(layer);
			List<Point> nextLayer = new ArrayList<>();

			for(Point currentPoint : currentLayer)
			{
				directions:
				for(int direction = 0; direction < 4; direction++)
				{
					int x = 0, y = 0;

					if(direction < 2)
						x = (int)Math.pow(-1, direction);
					else
						y = (int)Math.pow(-1, direction);

					Point newPoint = new Point(currentPoint);
					newPoint.translate(x, y);

					for(Point point : nextLayer)
						if(point.equals(newPoint))
							continue directions;

					for(List<Point> previousLayer : layers)
						for(Point point : previousLayer)
							if(point.equals(newPoint))
								continue directions;

					if(newPoint.getX() < 0
					|| newPoint.getY() < 0
					|| newPoint.getX() >= boardWidth
					|| newPoint.getY() >= boardHeight)
						continue;

					if(board.get(newPoint) == null)
						nextLayer.add(newPoint);

					else if(board.get(newPoint) != owner)
						return false;
				}
			}

			if(nextLayer.size() == 0)
				break;
			layers.add(nextLayer);
		}

		for(List<Point> layer : layers)
			for(Point point : layer)
				board.put(point, owner);

		return true;
	}
}
