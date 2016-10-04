package me.winter.trapgame.client.menu.join;

import me.winter.trapgame.client.SimpleLayout;
import me.winter.trapgame.client.UserProperties;
import me.winter.trapgame.client.menu.TrapGameMenu;
import me.winter.trapgame.util.ColorTransformer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

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

	private JTextField playerName, address;
	private JComboBox<String> sorter;

	public JoinForm(TrapGameMenu menu)
	{
		this.menu = menu;

		setBackground(ColorTransformer.TRANSPARENT);
		setLayout(new SimpleLayout());

		list = new ServerList(this);

		GridBagConstraints constraints = new GridBagConstraints(); //used for everything

		JPanel top = new JPanel(new GridBagLayout());
		JPanel bottom = new JPanel(new GridBagLayout());

		top.setBackground(new Color(230, 230, 230));
		top.setBorder(new EmptyBorder(10, 10, 10, 10));

		bottom.setBorder(new EmptyBorder(10, 10, 10, 10));
		bottom.setBackground(new Color(240, 240, 240));


		//Top elements declaration

		JLabel join = new JLabel(getMenu().getLangLine("client_serverlist"));
		join.setFont(new Font("Arial", Font.BOLD, 18));

		JLabel nameLabel = new JLabel(getMenu().getLangLine("client_username") + " ");
		nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

		playerName = new JTextField();
		playerName.setPreferredSize(new Dimension(150, 25));
		playerName.setMinimumSize(playerName.getPreferredSize());
		playerName.setText(getMenu().getClient().getUserProperties().getLastName());
		//playerName.setBackground(new Color(230, 230, 230));

		JLabel sortLabel = new JLabel(getMenu().getLangLine("client_sort") + " ");
		sortLabel.setFont(new Font("Arial", Font.BOLD, 14));

		sorter = new JComboBox<>(new String[]{
				getMenu().getLangLine("client_sort_players"),
				getMenu().getLangLine("client_sort_name"),
				getMenu().getLangLine("client_sort_ping")});

		sorter.setPreferredSize(new Dimension(150, 25));
		sorter.setMinimumSize(sorter.getPreferredSize());

		sorter.addActionListener(event -> getList().updateDisplay());
		//sorter.setBackground(new Color(230, 230, 230));

		RefreshButton refresh = new RefreshButton(this);

		//Top elements add

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


		//bottom elements declaration

		JLabel addserverLabel = new JLabel(getMenu().getLangLine("client_privateserver"));
		addserverLabel.setFont(new Font("Arial", Font.BOLD, 18));

		JLabel addressLabel = new JLabel(getMenu().getLangLine("client_privateserver_address") + " ");
		addressLabel.setFont(new Font("Arial", Font.BOLD, 14));

		address = new JTextField();
		address.setPreferredSize(new Dimension(150, 25));
		address.setMinimumSize(address.getPreferredSize());
		//playerName.setBackground(new Color(230, 230, 230));


		JButton add = new JButton(getMenu().getLangLine("client_privateserver_connect"));
		add.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add.addActionListener(event -> this.addServer());

		//Bottom elements add

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 2;
		constraints.weightx = 0;
		constraints.anchor = GridBagConstraints.LINE_START;

		bottom.add(addserverLabel, constraints);

		constraints.gridx = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.5;
		constraints.anchor = GridBagConstraints.LINE_END;

		bottom.add(addressLabel, constraints);

		constraints.gridy = 0;
		constraints.gridx = 3;
		constraints.anchor = GridBagConstraints.LINE_START;

		bottom.add(address, constraints);

		constraints.gridx = 4;
		constraints.gridy = 0;
		constraints.gridheight = 2;
		constraints.weightx = 0;
		constraints.anchor = GridBagConstraints.LINE_END;

		bottom.add(add, constraints);

		//dark

		JPanel dark = new JPanel()
		{
			@Override
			protected void paintComponent(Graphics graphics)
			{
				Graphics2D g2draw = (Graphics2D) graphics;

				g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
				g2draw.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

				g2draw.setColor(new Color(0, 0, 0, 50));
				g2draw.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
			}
		};

		//border

		JPanel border = new JPanel();
		border.setBackground(new Color(80, 80, 80));


		//final add

		add(top, SimpleLayout.constraints(10, 10, 80, 10));
		add(list, SimpleLayout.constraints(10, 20, 80, 60));
		add(bottom, SimpleLayout.constraints(10, 80, 80, 10));

		add(border, SimpleLayout.constraints(100, 100, 9.85, 9.85, 80.3, 80.3));
		add(dark, SimpleLayout.constraints(100, 100, 11.5, 11.5, 80, 80));
	}

	public void addServer()
	{
		List<String> servers = new ArrayList<>(getMenu().getClient().getUserProperties().getServers());

		String newServerAddress = address.getText().trim();

		servers.add(newServerAddress);
		RemoteServer server = new RemoteServer(list, newServerAddress);

		if(!server.isValid())
		{
			JOptionPane.showMessageDialog(getMenu().getClient(),
					getMenu().getClient().getLang().getLine("client_invalidserver"),
					getMenu().getClient().getLang().getLine("client_invalidserver_title"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		list.addServer(server);
		list.update();
		address.setText("");
		getMenu().getClient().getUserProperties().setServers(servers);
		getMenu().getClient().getUserProperties().save();
	}

	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D g2draw = (Graphics2D) graphics;

		g2draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2draw.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2draw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2draw.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		g2draw.drawImage(menu.getClient().getResourceManager().getImage("background"), -getX(), -getY(), menu.getWidth(), menu.getHeight(), null);

		super.paintComponent(graphics);
	}

	public Comparator<RemoteServer> getSortComparator()
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
