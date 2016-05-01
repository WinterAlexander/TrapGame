package me.winter.trapgame.server;

import me.winter.trapgame.util.FileUtil;

import java.io.*;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a configuration file for TrapGame
 * Can be used by server or client
 *
 * Created by Alexander Winter on 2016-03-31.
 */
public class ServerProperties extends Properties
{
	private Optional<Logger> logger;
	private File file;

	public ServerProperties(File file)
	{
		this(null, file);
	}

	public ServerProperties(Logger logger, File file)
	{
		this.logger = Optional.ofNullable(logger);
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
			logger.ifPresent(logger -> logger.log(Level.WARNING, "Failed to load properties file " + file.getName(), ex));
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
			logger.ifPresent(logger -> logger.log(Level.WARNING, "Failed to save properties file " + file.getName(), ex));
		}
	}

	public String getServerName()
	{
		return getProperty("server-name", "TrapGame Server");
	}

	public void setServerName(String name)
	{
		setProperty("server-name", name);
	}

	public String getWelcomeMessage()
	{
		return getProperty("welcome-message", "Welcome ${PLAYER} to ${SERVER}, have fun !");
	}

	public void setWelcomeMessage(String message)
	{
		setProperty("welcome-message", message);
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
			return 12;
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
			return 9;
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
			return 9;
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

	public boolean isLoggingToDisk()
	{
		return Boolean.parseBoolean(getProperty("log-to-disk"));
	}

	public void setLogToDisk(boolean log)
	{
		setProperty("log-to-disk", log + "");
	}

	public boolean enableConsole()
	{
		if(getProperty("enable-console", "").equalsIgnoreCase("false"))
			return false;

		return true;
	}

	public void setEnableConsole(boolean enableConsole)
	{
		setProperty("enable-console", enableConsole + "");
	}

	public boolean isPublic()
	{
		if(getProperty("public", "").equalsIgnoreCase("false"))
			return false;

		return true;
	}

	public void setPublic(boolean publicServer)
	{
		setProperty("public", publicServer + "");
	}
}
