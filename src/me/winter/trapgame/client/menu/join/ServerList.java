package me.winter.trapgame.client.menu.join;

import me.winter.trapgame.util.ColorTransformer;
import me.winter.trapgame.util.NetUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

/**
 * <p>A simple panel containing the list of available servers</p>
 *
 * <p>Created by 1541869 on 2016-04-22.</p>
 */
public class ServerList extends JPanel
{
	private JoinForm joinForm;

	private JPanel content;
	private List<RemoteServer> servers;

	public ServerList(JoinForm joinForm)
	{
		this.joinForm = joinForm;

		servers = new ArrayList<>();

		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(new BorderLayout());

		JScrollPane scroller = new JScrollPane();

		content = new JPanel();
		content.setBackground(new Color(110, 110, 110));
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBorder(new EmptyBorder(0, 0, 0, 0));

		scroller.setViewportView(content);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroller.setBorder(new EmptyBorder(0, 0, 0, 0));
		scroller.setViewportBorder(new EmptyBorder(0, 0, 0, 0));

		add(scroller, BorderLayout.CENTER);

		joinForm.getMenu().getClient().getUserProperties().getServers().forEach(x -> addServer(new RemoteServer(this, x)));
	}

	public void update()
	{
		servers.forEach(RemoteServer::ping);
		NetUtil.getBroadcastAddresses().forEach(x -> {
			new Thread(() -> {
				try
				{
					joinForm.getMenu().getClient().getConnection().broadcast(x, 1254, 100).forEach(server -> addServer(new RemoteServer(this, server)));
					SwingUtilities.invokeLater(this::updateDisplay);
				}
				catch(Exception ex)
				{

				}
			}).start();
		});

		updateDisplay();
	}

	/**
	 * Clears the ServerPanels and readd them
	 */
	public void updateDisplay()
	{
		content.removeAll();
		content.add(Box.createRigidArea(new Dimension(0, 5)));

		if(servers.size() == 0)
		{
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.setBackground(ColorTransformer.TRANSPARENT);
			panel.add(new JLabel(joinForm.getMenu().getLangLine("client_join_emptylist"), JLabel.CENTER), BorderLayout.CENTER);


			content.add(panel);
		}
		else
		{
			servers.sort(joinForm.getSortComparator());

			servers.forEach(server -> {
				content.add(server.getDisplay());
				content.add(Box.createRigidArea(new Dimension(0, 5)));
			});
		}


		content.setPreferredSize(content.getPreferredSize());

		content.revalidate();
		content.repaint();
	}

	public void addServer(RemoteServer server)
	{
		if(server.getAddress() == null)
			return;

		if(NetUtil.isLocal(server.getAddress()) && !server.getAddress().isLoopbackAddress())
			return;

		for(RemoteServer current : servers)
			if(current.getPort() == server.getPort() && current.getAddress().equals(server.getAddress()))
				return; //don't add
		servers.add(server);
	}

	public JPanel getContent()
	{
		return content;
	}

	public JoinForm getJoinForm()
	{
		return joinForm;
	}
}
