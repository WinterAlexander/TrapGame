package me.winter.trapgame.client;

import me.winter.trapgame.util.FileUtil;

import java.io.*;
import java.util.Properties;

/**
 * Client-side properties file used to save some user settings
 * such as last name used, last server, password etc.
 *
 * Created by Alexander Winter on 2016-03-31.
 */
public class UserProperties extends Properties
{
	private File file;

	public UserProperties(File file)
	{
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
			System.err.println("Failed to load proprieties file " + file.getName());
			ex.printStackTrace(System.err);
		}
	}

	public void save()
	{
		try
		{
			FileUtil.createDirectory(file.getParentFile());
			FileUtil.createFile(file);
			store(new BufferedOutputStream(new FileOutputStream(file)), "Information saved on user's demand");
		}
		catch(IOException ex)
		{
			System.err.println("Failed to save proprieties file " + file.getName());
			ex.printStackTrace(System.err);
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
}
