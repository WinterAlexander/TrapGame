package me.winter.trapgame.server;

import me.winter.trapgame.util.FileUtil;

import java.io.*;
import java.util.Properties;

/**
 * Represents a configuration file for TrapGame
 * Can be used by server or client
 *
 * Created by Alexander Winter on 2016-03-31.
 */
public class ServerProperties extends Properties
{
	private File file;

	public ServerProperties(File file)
	{
		this.file = file;
	}

	public void loadIfPresent()
	{
		if(!file.exists() || file.isDirectory())
			return;

		try
		{
			load(new BufferedInputStream(new FileInputStream(file)));
		}
		catch(IOException ex)
		{
			System.err.println("Failed to load proprieties file " + file.getName());
			ex.printStackTrace(System.err);
		}
	}

	public void save()
	{
		try
		{
			FileUtil.createFile(file);
			store(new BufferedOutputStream(new FileOutputStream(file)), "Default server.proprieties");
		}
		catch(IOException ex)
		{
			System.err.println("Failed to save proprieties file " + file.getName());
			ex.printStackTrace(System.err);
		}
	}

	public int getPort()
	{
		try
		{
			return Integer.parseInt(getProperty("port"));
		}
		catch(NumberFormatException ex)
		{
			return 1254;
		}
	}

	public void setPort(int port)
	{
		setProperty("port", port + "");
	}

	public String getPassword()
	{
		return getProperty("password", null);
	}

	public void setPassword(String password)
	{
		setProperty("password", password);
	}

	public int getMinPlayers()
	{
		try
		{
			return Integer.parseInt(getProperty("min-players"));
		}
		catch(NumberFormatException ex)
		{
			return 2;
		}
	}

	public void setMinPlayers(int minPlayers)
	{
		setProperty("min-players", minPlayers + "");
	}

	public int getMaxPlayers()
	{
		try
		{
			return Integer.parseInt(getProperty("max-players"));
		}
		catch(NumberFormatException ex)
		{
			return 8;
		}
	}

	public void setMaxPlayers(int maxPlayers)
	{
		setProperty("max-players", maxPlayers + "");
	}

	public int getBoardWidth()
	{
		try
		{
			return Integer.parseInt(getProperty("board-width"));
		}
		catch(NumberFormatException ex)
		{
			return 8;
		}
	}

	public void setBoardWidth(int boardWidth)
	{
		setProperty("board-width", boardWidth + "");
	}

	public int getBoardHeight()
	{
		try
		{
			return Integer.parseInt(getProperty("board-height"));
		}
		catch(NumberFormatException ex)
		{
			return 8;
		}
	}

	public void setBoardHeight(int boardHeight)
	{
		setProperty("board-height", boardHeight + "");
	}

	public boolean isDebugMode()
	{
		return Boolean.parseBoolean(getProperty("debug-mode"));
	}

	public void setDebugMode(boolean debugMode)
	{
		setProperty("debug-mode", debugMode + "");
	}

	public int getTimer()
	{
		try
		{
			return Integer.parseInt(getProperty("timer"));
		}
		catch(NumberFormatException ex)
		{
			return 30;
		}
	}

	public void setTimer(int timer)
	{
		setProperty("timer", timer + "");
	}

	public String getSuperPassword()
	{
		return getProperty("super-password", "");
	}

	public void setSuperPassword(String superPassword)
	{
		setProperty("super-password", superPassword);
	}
}
