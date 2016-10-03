package me.winter.trapgame.client;

import me.winter.trapgame.shared.Task;

import javax.swing.*;

/**
 * A simple text animation used in trapgame to animate loading dots ...
 *
 * Created by Alexander Winter on 2016-04-30.
 */
public class TextAnimation extends Task
{
	private JLabel component;
	private String[] values;

	private int i;

	public TextAnimation(JLabel component, String... values)
	{
		super(500, true);
		this.component = component;
		this.values = values;

		i = 0;
	}

	@Override
	public void run()
	{
		component.setText(values[i]);

		i++;
		i %= values.length;
	}
}
