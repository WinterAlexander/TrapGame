package me.winter.trapgame.server;

import me.winter.trapgame.shared.PlayerStats;
import me.winter.trapgame.util.FileUtil;

import java.io.*;

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

	public StatsManager(TrapGameServer server, File directory)
	{
		this.server = server;
		this.directory = directory;
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
			System.err.println("An internal exception occurred when trying to load a player stats file (" + name + ").");
			ex.printStackTrace(System.err);
		}
		catch(ClassNotFoundException classNotFoundEx)
		{
			System.err.println("Stats file for " + name + " is corrupted ! Adding .corrupt suffix to file name to prevent future problems.");


			if(!file.renameTo(new File(file.getAbsolutePath() + ".corrupt")))
				System.err.println("Couldn't rename, please delete " + file.getName() + " manually.");
		}

		return new PlayerStats(0, 0, 0);
	}

	public void save(String name, PlayerStats stats)
	{
		File file = getFile(name);

		try
		{
			FileUtil.createFile(file);
		}
		catch(IOException ex)
		{
			System.err.println("An internal exception occurred when trying to create a player stats file (" + name + ").");
			ex.printStackTrace(System.err);
			return;
		}

		try
		{
			ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

			outputStream.writeObject(stats);
			outputStream.flush();
			outputStream.close();
		}
		catch(IOException ex)
		{
			System.err.println("An internal exception occurred when trying to save a player stats file (" + name + ").");
			ex.printStackTrace(System.err);
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