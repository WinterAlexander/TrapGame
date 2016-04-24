package me.winter.trapgame.client.menu;

import javax.swing.*;

/**
 * <p>A simple panel containing the list of available servers</p>
 *
 * <p>Created by 1541869 on 2016-04-22.</p>
 */
public class ServerList extends JPanel
{
	private JoinForm joinForm;

	public ServerList(JoinForm joinForm)
	{
		this.joinForm = joinForm;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	}

	public JoinForm getJoinForm()
	{
		return joinForm;
	}
}
