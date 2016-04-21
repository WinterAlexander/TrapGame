package me.winter.trapgame.server;

import me.winter.trapgame.server.state.StandbyState;
import me.winter.trapgame.server.state.State;
import me.winter.trapgame.shared.PlayerInfo;
import me.winter.trapgame.shared.Scheduler;
import me.winter.trapgame.shared.TrapGameLogFormatter;
import me.winter.trapgame.shared.packet.PacketOutBoardSize;
import me.winter.trapgame.shared.packet.PacketOutWelcome;
import me.winter.trapgame.shared.packet.PacketOutJoin;
import me.winter.trapgame.shared.packet.PacketOutLeave;
import me.winter.trapgame.util.FileUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.*;

/**
 * Represents a server of TrapGame
 * Has a ServerConnection, a list of players and the state of the game
 * This class also has a static main() method to launch the game
 *
 * Created by winter on 25/03/16.
 */
public class TrapGameServer
{
	private static Logger trapGameLogger;

	/**
	 * Starts the server with a new instance of TrapGameServer
	 *
	 * @param args Execution string arguments usage: java -jar TrapGameServer.jar port password minPlayers maxPlayers boardWidth boardHeight
	 */
	public static void main(String[] args)
	{
		try
		{
			/*int port = 1254;
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
				boardHeight = Integer.parseInt(args[5]);*/

			trapGameLogger = Logger.getLogger("Server");

			trapGameLogger.setUseParentHandlers(false);

			ConsoleHandler consoleHandler = new ConsoleHandler()
			{
				{
					setOutputStream(System.out);
				}
			};

			consoleHandler.setFormatter(new TrapGameLogFormatter());
			trapGameLogger.addHandler(consoleHandler);

			ServerProperties properties = new ServerProperties(trapGameLogger, new File("server.properties"));
			properties.loadIfPresent();

			new TrapGameServer(properties, trapGameLogger).start();
			trapGameLogger.info("Thanks for playing TrapGame ! :)");
		}
		catch(Throwable ex)
		{
			trapGameLogger.log(Level.SEVERE, "A fatal error occurred and stopped the server.", ex);
			System.exit(-1);
		}
		finally
		{
			for(Handler handler : trapGameLogger.getHandlers())
				handler.close();
			trapGameLogger = null;
		}
	}

	public static final Color[] COLORS = new Color[]{
			new Color(255, 0, 0),       //1
			new Color(3, 140, 252),     //2
			new Color(241, 223, 1),     //3
			new Color(68, 173, 50),     //4
			new Color(150, 45, 255),    //5
			new Color(249, 128, 47),    //6
			new Color(255, 85, 170),    //7
			new Color(0, 251, 255),     //8
			new Color(142, 1, 1),       //9
			new Color(33, 27, 160),     //10
			new Color(211, 162, 10),    //11
			new Color(46, 255, 11),     //12
			new Color(128, 18, 118),    //13
			new Color(98, 61, 37),      //14
			new Color(255, 119, 237),   //15
			new Color(3, 139, 130),     //16
			new Color(128, 128, 128),   //17
			new Color(255, 157, 124)};  //18

	private Scheduler scheduler;
	private State state;
	private List<Player> players;
	private ServerConnection connection;
	private StatsManager statsManager;
	private CommandManager commandManager;
	private ServerConsole console;
	private Logger logger;

	private String password, superPassword;
	private int minPlayers, maxPlayers;
	private int boardWidth, boardHeight;
	private int waitingTimer;
	private boolean debugMode;

	private boolean stop;

	public TrapGameServer(ServerProperties properties, Logger logger)
	{
		this.logger = logger;

		if(properties.isLoggingToDisk())
		{
			try
			{
				File logDir = new File("logs");

				FileUtil.createDirectory(logDir);

				File[] files = logDir.listFiles(file -> file.getName().endsWith(".log"));

				int id = files.length;

				String fileName;

				do
				{
					id++;
					fileName = "logs/" +
							Calendar.getInstance().get(Calendar.YEAR) + "-" +
							Calendar.getInstance().get(Calendar.MONTH) + "-" +
							Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "_" + id + ".log";
				}
				while(new File(fileName).exists());

				FileHandler fileHandler = new FileHandler(fileName);

				fileHandler.setFormatter(new TrapGameLogFormatter());
				logger.addHandler(fileHandler);
			}
			catch(IOException ex)
			{
				logger.log(Level.SEVERE, "An internal error occurred while trying to add an handler file to logging.", ex);
			}
		}

		scheduler = new Scheduler(logger);
		state = new StandbyState(this);
		players = new ArrayList<>();
		connection = new ServerConnection(this, properties.getPort());
		statsManager = new StatsManager(this, new File("stats"), false);
		commandManager = new CommandManager(this);
		console = new ServerConsole(this);
		stop = false;

		this.password = properties.getPassword();
		this.superPassword = properties.getSuperPassword();
		setMaxPlayers(properties.getMaxPlayers());
		setMinPlayers(properties.getMinPlayers());
		setBoardSize(properties.getBoardWidth(), properties.getBoardHeight());
		setDebugMode(properties.isDebugMode());
		this.waitingTimer = properties.getTimer();
	}

	public synchronized void start()
	{
		getLogger().info("TrapGame server should now be operational.");

		synchronized(getScheduler())
		{
			getScheduler().start();

			while(!stop)
			{
				long toWait = getScheduler().getWaitingDelay();
				if(toWait > 0)
				{
					try
					{
						if(toWait == Long.MAX_VALUE)
							getScheduler().wait(0);
						else
							getScheduler().wait(toWait);
					}
					catch(InterruptedException ex)
					{
						ex.printStackTrace(System.err);
					}
				}
				getScheduler().update();
			}
		}
	}

	public void join(Player player)
	{
		if(getPlayers().size() >= getMaxPlayers())
		{
			player.kick("Sorry, the server reached the maximum of players.");
			return;
		}

		getConnection().sendToAll(new PacketOutJoin(player.getInfo()));
		getPlayers().add(player);
		player.getConnection().sendPacket(new PacketOutWelcome(player.getId(), getPlayersInfo(), boardWidth, boardHeight));
		broadcast(player.getFormattedName() + " has joined the game.");
		getState().join(player);
	}

	public void leave(Player player)
	{
		getStatsManager().save(player.getName(), player.getInfo().getStats());
		getPlayers().remove(player);
		broadcast(player.getFormattedName() + " has left the game.");
		getState().leave(player);
		getConnection().sendToAll(new PacketOutLeave(player.getId()));
	}

	public void stop()
	{
		getConnection().setAcceptingNewClients(false);

		for(Player player : new ArrayList<>(players))
			player.kick("Server is closing.");

		getConnection().close();
		stop = true;
		getScheduler().notify();
	}

	public boolean isStopped()
	{
		return stop;
	}

	public boolean isAvailable(String playerName)
	{
		return getPlayer(playerName) == null;
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

	public Player getPlayer(String name)
	{
		for(Player player : getPlayers())
			if(player.getName().equalsIgnoreCase(name))
				return player;
		return null;
	}

	public List<PlayerInfo> getPlayersInfo()
	{
		List<PlayerInfo> players = new ArrayList<>();

		getPlayers().forEach(player -> players.add(player.getInfo()));

		return players;
	}

	public Color getColor(int id)
	{
		return COLORS[id % COLORS.length];
	}

	public void broadcast(String message)
	{
		getConsole().getConsoleSender().sendMessage(message);

		getPlayers().forEach(player -> player.sendMessage(message));
	}

	public void broadcast(Color color, String message)
	{
		getConsole().getConsoleSender().sendMessage(message);

		getPlayers().forEach(player -> player.sendMessage(color, message));
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

	public ServerConsole getConsole()
	{
		return console;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public String getPassword()
	{
		return password;
	}

	public String getSuperPassword()
	{
		return superPassword;
	}

	public int getMinPlayers()
	{
		return minPlayers;
	}

	public void setMinPlayers(int minPlayers)
	{
		this.minPlayers = minPlayers;
	}

	public int getMaxPlayers()
	{
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers)
	{
		this.maxPlayers = maxPlayers;
	}

	public void setBoardSize(int boardWidth, int boardHeight)
	{

		this.boardWidth = boardWidth > 0 ? boardWidth : 1;
		this.boardHeight = boardHeight > 0 ? boardHeight : 1;

		getConnection().sendToAll(new PacketOutBoardSize(this.boardWidth, this.boardHeight));
	}

	public int getBoardHeight()
	{
		return boardHeight;
	}

	public int getBoardWidth()
	{
		return boardWidth;
	}

	public int getWaitingTimer()
	{
		return waitingTimer;
	}

	public void setWaitingTimer(int waitingTimer)
	{
		this.waitingTimer = waitingTimer;
	}

	public boolean isDebugMode()
	{
		return debugMode;
	}

	public void setDebugMode(boolean debugMode)
	{
		this.debugMode = debugMode;
	}
}
