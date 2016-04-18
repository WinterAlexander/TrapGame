package me.winter.trapgame.server;

import me.winter.trapgame.shared.PlayerStats;
import me.winter.trapgame.util.FileUtil;

import java.io.*;
import java.util.logging.Level;

/**
 * Manages all the server stats for players
 * Loads and saves stats to disk
 *
 * Created by Alexander Winter on 2016-03-26.
 */
public class StatsManager
{
	private TrapGameServer server;
	private File directory;
	private boolean save;

	public StatsManager(TrapGameServer server, File directory, boolean save)
	{
		this.server = server;
		this.directory = directory;
		this.save = save;
	}

	private File getFile(String name)
	{
		return new File(directory, name + ".stats");
	}

	public PlayerStats load(String name)
	{
		File file = getFile(name);
		try
		{
			ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
			PlayerStats stats = (PlayerStats)inputStream.readObject();

			inputStream.close();
			return stats;

		}
		catch(FileNotFoundException fileNotFoundEx)
		{

		}
		catch(IOException ex)
		{
			getServer().getLogger().log(Level.SEVERE, "An internal exception occurred when trying to load a player stats file (\" + name + \").", ex);
		}
		catch(ClassNotFoundException classNotFoundEx)
		{
			getServer().getLogger().warning("Stats file for " + name + " is corrupted ! Adding .corrupt suffix to file name to prevent future problems.");

			if(!file.renameTo(new File(file.getAbsolutePath() + ".corrupt")))
				getServer().getLogger().severe("Couldn't rename, please delete " + file.getName() + " manually.");
		}

		return new PlayerStats(0, 0, 0);
	}

	public void save(String name, PlayerStats stats)
	{
		if(!save)
			return;

		File file = getFile(name);

		try
		{
			FileUtil.createDirectory(directory);
			FileUtil.createFile(file);
		}
		catch(IOException ex)
		{
			getServer().getLogger().log(Level.SEVERE, "An internal exception occurred when trying to create a player stats file (" + name + ").", ex);
			return;
		}

		try
		{
			ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

			outputStream.writeObject(stats);
			outputStream.flush();
			outputStream.close();
		}
		catch(Exception ex)
		{
			getServer().getLogger().log(Level.SEVERE, "An internal exception occurred when trying to save a player stats file (\" + name + \").", ex);
		}

	}

	public TrapGameServer getServer()
	{
		return server;
	}

	public void setServer(TrapGameServer server)
	{
		this.server = server;
	}

	public File getDirectory()
	{
		return directory;
	}

	public void setDirectory(File directory)
	{
		this.directory = directory;
	}
}
