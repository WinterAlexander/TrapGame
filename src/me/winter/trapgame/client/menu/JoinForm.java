package me.winter.trapgame.client.menu;

import me.winter.trapgame.client.SimpleLayout;
import me.winter.trapgame.util.ColorTransformer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * <p>Represents a form containing the list of available servers
 *
 * Users can also join using ip directly</p>
 *
 * <p>Created by 1541869 on 2016-04-22.</p>
 */
public class JoinForm extends JPanel
{
	private TrapGameMenu menu;

	private ServerList list;
	private JTextField playerName;
	private JComboBox<String> sorter;

	public JoinForm(TrapGameMenu menu)
	{
		this.menu = menu;

		setBackground(ColorTransformer.TRANSPARENT);

		setLayout(new SimpleLayout());

		list = new ServerList(this);

		JPanel top = new JPanel();
		top.setLayout(new GridBagLayout());
		top.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel join = new JLabel(getMenu().getLangLine("client_serverlist"));
		join.setFont(new Font("Arial", Font.BOLD, 18));

		JLabel nameLabel = new JLabel(getMenu().getLangLine("client_username") + " ");
		nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

		playerName = new JTextField();
		playerName.setPreferredSize(new Dimension(150, 25));
		playerName.setMinimumSize(playerName.getPreferredSize());

		JLabel sortLabel = new JLabel(getMenu().getLangLine("client_sort") + " ");
		sortLabel.setFont(new Font("Arial", Font.BOLD, 14));

		sorter = new JComboBox<>(new String[]{
				getMenu().getLangLine("client_sort_players"),
				getMenu().getLangLine("client_sort_name"),
				getMenu().getLangLine("client_sort_ping")});

		sorter.setPreferredSize(new Dimension(150, 25));
		sorter.setMinimumSize(sorter.getPreferredSize());

		sorter.addActionListener(event -> getList().placeServers());

		RefreshButton refresh = new RefreshButton(this);

		GridBagConstraints constraints = new GridBagConstraints();


		constraints.gridwidth = 2;
		constraints.gridheight = 2;
		constraints.weightx = 0;

		constraints.anchor = GridBagConstraints.LINE_START;

		top.add(join, constraints);

		constraints.gridx = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.5;

		constraints.anchor = GridBagConstraints.LINE_END;

		top.add(nameLabel, constraints);

		constraints.gridy = 1;

		top.add(sortLabel, constraints);

		constraints.gridy = 0;
		constraints.gridx = 3;
		constraints.anchor = GridBagConstraints.LINE_START;

		top.add(playerName, constraints);

		constraints.gridy = 1;

		top.add(sorter, constraints);

		constraints.gridx = 4;
		constraints.gridy = 0;
		constraints.gridheight = 2;
		constraints.weightx = 0;

		constraints.anchor = GridBagConstraints.LINE_END;

		top.add(refresh, constraints);

		JPanel bottom = new JPanel();


		add(top, SimpleLayout.constraints(10, 10, 80, 10));
		add(list, SimpleLayout.constraints(10, 20, 80, 60));
		add(bottom, SimpleLayout.constraints(10, 80, 80, 10));
	}

	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D g2draw = (Graphics2D) graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2draw.drawImage(menu.getClient().getResourceManager().getImage("background"), -getX(), -getY(), menu.getWidth(), menu.getHeight(), null);

		super.paintComponent(graphics);
	}

	public Comparator<ServerPanel> getSortComparator()
	{
		switch(getSorter().getSelectedIndex())
		{
			case 0:
				return (a, b) -> b.getPlayers() - a.getPlayers();

			case 1:
				return (a, b) -> a.getName().compareToIgnoreCase(b.getName());

			case 2:
			default:
				return (a, b) -> a.getPing() - b.getPing();
		}
	}

	public TrapGameMenu getMenu()
	{
		return menu;
	}

	public ServerList getList()
	{
		return list;
	}

	public void setList(ServerList list)
	{
		this.list = list;
	}

	public JTextField getPlayerName()
	{
		return playerName;
	}

	public JComboBox<String> getSorter()
	{
		return sorter;
	}
}
