package me.winter.trapgame.server;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.Scheduler;
import me.winter.trapgame.shared.packet.PacketOutBoardSize;
import me.winter.trapgame.shared.packet.PacketOutWelcome;
import me.winter.trapgame.shared.packet.PacketOutJoin;
import me.winter.trapgame.shared.packet.PacketOutLeave;
import me.winter.trapgame.util.StringUtil;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a server of TrapGame
 * Has a ServerConnection, a list of players and the state of the game
 * This class also has a static main() method to launch the game
 *
 * Created by winter on 25/03/16.
 */
public class TrapGameServer
{
	/**
	 * Starts the server with a new instance of TrapGameServer
	 *
	 * @param args Execution string arguments usage: java -jar TrapGameServer.jar port password minPlayers maxPlayers boardWidth boardHeight
	 */
	public static void main(String[] args)
	{
		try
		{
			int port = 1254;
			String password = "";
			int minPlayers = 2;
			int maxPlayers = 8;
			int boardWidth = 12;
			int boardHeight = 12;

			if(args.length > 0 && StringUtil.isInt(args[0]))
				port = Integer.parseInt(args[0]);

			if(args.length > 1 && !(args[1].equals("none") || args[1].equals("null")))
				password = args[1];

			if(args.length > 2 && StringUtil.isInt(args[2]))
				minPlayers = Integer.parseInt(args[2]);

			if(args.length > 3 && StringUtil.isInt(args[3]))
				maxPlayers = Integer.parseInt(args[3]);

			if(args.length > 4 && StringUtil.isInt(args[4]))
				boardWidth = Integer.parseInt(args[4]);

			if(args.length > 5 && StringUtil.isInt(args[5]))
				boardHeight = Integer.parseInt(args[5]);

			new TrapGameServer(port, password, minPlayers, maxPlayers, boardWidth, boardHeight).start();
		}
		catch(Throwable ex)
		{
			System.err.println("A fatal error occurred and stopped the server. Stack Trace:");
			ex.printStackTrace(System.err);
			System.exit(0);
		}
	}

	private static final Color[] COLORS = new Color[]{
			new Color(255, 0, 0),
			new Color(3, 140, 252),
			new Color(241, 223, 1),
			new Color(68, 173, 50),
			new Color(150, 45, 255),
			new Color(249, 128, 47),
			new Color(255, 85, 170),
			new Color(0, 251, 255),
			new Color(142, 1, 1),
			new Color(33, 27, 160),
			new Color(211, 162, 10),
			new Color(46, 255, 11),
			new Color(128, 18, 118),
			new Color(98, 61, 37),
			new Color(255, 119, 237),
			new Color(3, 189, 170),
			new Color(128, 128, 128),
			new Color(255, 157, 124)};

	private Scheduler scheduler;
	private State state;
	private List<Player> players;
	private ServerConnection connection;
	private StatsManager statsManager;
	private CommandManager commandManager;

	private String password;
	private int minPlayers, maxPlayers;
	private int boardWidth, boardHeight;

	public TrapGameServer(int port, String password, int minPlayers, int maxPlayers, int boardWidth, int boardHeight)
	{
		scheduler = new Scheduler();
		state = new StandbyState(this);
		players = new ArrayList<>();
		connection = new ServerConnection(this, port);
		statsManager = new StatsManager(this, new File("stats"));
		commandManager = new CommandManager(this);

		this.password = password;
		setMaxPlayers(maxPlayers);
		setMinPlayers(minPlayers);
		this.maxPlayers = maxPlayers;
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;

	}

	public void start()
	{
		System.out.println("TrapGame server should now be operational.");

		scheduler.start();
		while(true)
			scheduler.update();
	}

	public void join(Player player)
	{
		if(getPlayers().size() >= maxPlayers)
		{
			player.kick("Sorry, the server reached the maximum of players.");
			return;
		}

		getConnection().sendToAll(new PacketOutJoin(player.getInfo()));
		getPlayers().add(player);
		player.getConnection().sendPacket(new PacketOutWelcome(player.getId(), getPlayersInfo()));
		player.getConnection().sendPacket(new PacketOutBoardSize(this.boardWidth, this.boardHeight));
		broadcast(player.getName() + " has joined the game.");
		getState().join(player);
	}

	public void leave(Player player)
	{
		getPlayers().remove(player);
		broadcast(player.getName() + " has left the game.");
		getState().leave(player);
		getConnection().sendToAll(new PacketOutLeave(player.getId()));
	}

	public boolean isAvailable(String playerName)
	{
		for(Player player : getPlayers())
			if(player.getName().equalsIgnoreCase(playerName))
				return false;
		return true;
	}

	public int generateNewPlayerId()
	{
		int id = 0;

		while(!isAvailable(id))
			id++;

		return id;
	}

	private boolean isAvailable(int playerId)
	{
		for(Player player : getPlayers())
			if(player.getId() == playerId)
				return false;
		return true;
	}

	public List<PlayerInfo> getPlayersInfo()
	{
		List<PlayerInfo> players = new ArrayList<>();

		for(Player player : getPlayers())
			players.add(player.getInfo());

		return players;
	}

	public Color getColor(int id)
	{
		if(id < COLORS.length)
			return COLORS[id];

		return new Color(COLORS[id].getRed() + id, COLORS[id].getGreen() + id, COLORS[id].getBlue());
	}

	public void broadcast(String message)
	{
		System.out.println("[Broadcast] " + message);

		getPlayers().forEach(player -> player.sendMessage(message));
	}

	public Scheduler getScheduler()
	{
		return scheduler;
	}

	public State getState()
	{
		return state;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public ServerConnection getConnection()
	{
		return connection;
	}

	public StatsManager getStatsManager()
	{
		return statsManager;
	}

	public CommandManager getCommandManager()
	{
		return commandManager;
	}

	public String getPassword()
	{
		return password;
	}

	public int getMinPlayers()
	{
		return minPlayers;
	}

	public void setMinPlayers(int minPlayers)
	{
		if(minPlayers < 0 || minPlayers > maxPlayers)
			throw new IllegalArgumentException("min(" + minPlayers + ") should be > 0 and < max");

		this.minPlayers = minPlayers;
	}

	public int getMaxPlayers()
	{
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers)
	{
		if(maxPlayers < 0 || maxPlayers < minPlayers)
			throw new IllegalArgumentException("max(" + maxPlayers + ") should be > 0 and > min");

		this.maxPlayers = maxPlayers;
	}

	public void setBoardSize(int boardWidth, int boardHeight)
	{
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;

		getConnection().sendToAll(new PacketOutBoardSize(boardWidth, boardHeight));
	}

	public int getBoardHeight()
	{
		return boardHeight;
	}

	public int getBoardWidth()
	{
		return boardWidth;
	}
}
