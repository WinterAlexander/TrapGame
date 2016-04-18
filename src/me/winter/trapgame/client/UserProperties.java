package me.winter.trapgame.client;

import me.winter.trapgame.util.FileUtil;

import java.io.*;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client-side properties file used to save some user settings
 * such as last name used, last server, password etc.
 *
 * Created by Alexander Winter on 2016-03-31.
 */
public class UserProperties extends Properties
{
	private Optional<Logger> logger;
	private File file;

	public UserProperties(File file)
	{
		this(null, file);
	}

	public UserProperties(Logger logger, File file)
	{
		this.logger = Optional.ofNullable(logger);
		this.file = file;
	}

	public boolean exists()
	{
		return file.exists() && !file.isDirectory();
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
			FileUtil.createDirectory(file.getParentFile());
			FileUtil.createFile(file);
			store(new BufferedOutputStream(new FileOutputStream(file)), "User settings");
		}
		catch(IOException ex)
		{
			logger.ifPresent(logger -> logger.log(Level.WARNING, "Failed to save properties file " + file.getName(), ex));
		}
	}

	public String getLastName()
	{
		return getProperty("last-name", "");
	}

	public void setLastName(String lastName)
	{
		setProperty("last-name", lastName);
	}

	public String getLastServer()
	{
		return getProperty("last-server", "");
	}

	public void setLastServer(String lastServer)
	{
		setProperty("last-server", lastServer);
	}

	public String getLastPassword()
	{
		return getProperty("last-password", "");
	}

	public void setLastPassword(String lastPassword)
	{
		setProperty("last-password", lastPassword);
	}

	public boolean isDebugMode()
	{
		return Boolean.parseBoolean(getProperty("debug-mode"));
	}

	public void setDebugMode(boolean debugMode)
	{
		setProperty("debug-mode", debugMode + "");
	}

	public String getLanguage()
	{
		return getProperty("language", "english");
	}

	public void setLanguage(String language)
	{
		setProperty("language", language);
	}
}
