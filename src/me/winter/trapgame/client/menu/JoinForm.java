package me.winter.trapgame.client.menu;

import me.winter.trapgame.client.SimpleLayout;

import javax.swing.*;
import java.awt.*;

/**
 * <p>Represents a form containing the list of available servers
 *
 * Users can also join using ip directly</p>
 *
 * <p>Created by 1541869 on 2016-04-22.</p>
 */
public class JoinForm extends JPanel
{
	public JoinForm()
	{
		setLayout(new SimpleLayout());

		add(new ServerList(), SimpleLayout.constraints(10, 10, 1, 2, 8, 6));

	}


}
