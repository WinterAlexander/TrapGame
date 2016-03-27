package me.winter.trapgame.server;

import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.Scheduler;
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
	 * @param args Execution string arguments usage: java -jar TrapGameServer.jar port minPlayers maxPlayers boardWidth boardHeight
	 */
	public static void main(String[] args)
	{
		try
		{
			int port = 1254;
			int minPlayers = 2;
			int maxPlayers = 8;
			int boardWidth = 12;
			int boardHeight = 12;

			if(args.length > 0 && StringUtil.isInt(args[0]))
				port = Integer.parseInt(args[0]);

			if(args.length > 1 && StringUtil.isInt(args[1]))
				minPlayers = Integer.parseInt(args[1]);

			if(args.length > 2 && StringUtil.isInt(args[2]))
				maxPlayers = Integer.parseInt(args[2]);

			if(args.length > 3 && StringUtil.isInt(args[3]))
				boardWidth = Integer.parseInt(args[3]);

			if(args.length > 4 && StringUtil.isInt(args[4]))
				boardHeight = Integer.parseInt(args[4]);

			new TrapGameServer(port, minPlayers, maxPlayers, boardWidth, boardHeight).start();
		}
		catch(Throwable ex)
		{
			System.err.println("A fatal error occured and stopped the server. Stack Trace:");
			ex.printStackTrace(System.err);
		}
	}

	private static final Color[] COLORS = new Color[]{Color.RED, Color.CYAN, Color.YELLOW, Color.GREEN, Color.PINK, Color.ORANGE, Color.BLUE, Color.BLACK};

	private Scheduler scheduler;
	private State state;
	private List<Player> players;
	private ServerConnection connection;
	private StatsManager statsManager;

	private int minPlayers, maxPlayers;
	private int boardWidth, boardHeight;

	public TrapGameServer(int port, int minPlayers, int maxPlayers, int boardWidth, int boardHeight)
	{
		scheduler = new Scheduler();
		state = new StandbyState(this);
		players = new ArrayList<>();
		connection = new ServerConnection(this, port);
		statsManager = new StatsManager(this, new File("stats"));
		setMaxPlayers(maxPlayers);
		setMinPlayers(minPlayers);
		this.maxPlayers = maxPlayers;
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
	}

	public void start()
	{
		System.out.println("TrapGame server should now be operational.");

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

		if(getState() instanceof GameState)
		{
			player.kick("Sorry, please wait the end of that game to join.");
			return;
		}

		getConnection().sendToAll(new PacketOutJoin(player.getInfo()));
		getPlayers().add(player);
		player.getConnection().sendPacket(new PacketOutWelcome(player.getId(), getPlayersInfo(), this.boardWidth, this.boardHeight));
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

	public int getMinPlayers()
	{
		return minPlayers;
	}

	public void setMinPlayers(int minPlayers)
	{
		if(minPlayers < 0 || minPlayers > maxPlayers)
			throw new IllegalArgumentException("min should be > 0 and < max");

		this.minPlayers = minPlayers;
	}

	public int getMaxPlayers()
	{
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers)
	{
		if(maxPlayers < 0 || maxPlayers < minPlayers)
			throw new IllegalArgumentException("max should be > 0 and > min");

		this.maxPlayers = maxPlayers;
	}

	public int getBoardHeight()
	{
		return boardHeight;
	}

	public void setBoardHeight(int boardHeight)
	{
		this.boardHeight = boardHeight;
	}

	public int getBoardWidth()
	{
		return boardWidth;
	}

	public void setBoardWidth(int boardWidth)
	{
		this.boardWidth = boardWidth;
	}
}
