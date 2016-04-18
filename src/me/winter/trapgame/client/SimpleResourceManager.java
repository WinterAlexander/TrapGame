package me.winter.trapgame.client;

import me.winter.trapgame.util.FileUtil;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>A simple resource manager method-blocking and not multi-threaded
 * that loads all the resources available when load method is called.</p>
 *
 * <p>Created by winter on 11/04/16.</p>
 */
public class SimpleResourceManager implements ResourceManager
{
	private Logger logger;
	private Properties paths;
	private Map<String, Object> resources;

	public SimpleResourceManager(Logger logger)
	{
		this.logger = logger;
		paths = new Properties();
		resources = new HashMap<>();
	}

	@Override
	public void scan(String index) throws IOException
	{
		try
		{
			paths.load(new BufferedInputStream(FileUtil.resourceAsStream(index)));

		}
		catch(FileNotFoundException ex)
		{
			logger.log(Level.SEVERE, "Index of resources was not found.", ex);
			throw ex;
		}
		catch(IOException ex)
		{
			logger.log(Level.SEVERE, "An internal exception occurred while scanning index of resources.", ex);
			throw ex;
		}
	}

	@Override
	public void load()
	{
		for(String name : nameSet()) try
		{
			String path = paths.getProperty(name).split(":")[1];

			switch(paths.getProperty(name).split(":")[0].toLowerCase())
			{
				case "image":
					resources.put(name, ImageIO.read(FileUtil.resourceAsStream(path)));
					break;

				case "sound":
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(FileUtil.resourceAsStream(path)));
					resources.put(name, clip);
					break;

				case "text":
				default:
					BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtil.resourceAsStream(path)));
					StringBuilder builder = new StringBuilder();
					String line;
					while((line = reader.readLine()) != null)
						builder.append(line);
					resources.put(name, builder.toString());
			}
		}
		catch(IOException | LineUnavailableException | UnsupportedAudioFileException ex)
		{
			logger.log(Level.WARNING, "An internal exception occurred while loading a file fetch from indexer.", ex);
		}
	}

	@Override
	public Set<String> nameSet()
	{
		return paths.stringPropertyNames();
	}

	@Override
	public void dispose()
	{
		paths.clear();
		resources.clear();
	}

	@Override
	public BufferedImage getImage(String name)
	{
		try
		{
			return (BufferedImage)resources.get(name);
		}
		catch(ClassCastException | NullPointerException ex)
		{
			return new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
		}
	}

	@Override
	public Clip getSound(String name)
	{
		try
		{
			return (Clip)resources.get(name);
		}
		catch(ClassCastException | NullPointerException ex)
		{
			try
			{
				return AudioSystem.getClip();
			}
			catch(LineUnavailableException lineUnavailable)
			{
				logger.log(Level.WARNING, "An internal exception occurred while making an empty clip.", ex);
				return null;
			}
		}
	}

	@Override
	public String getText(String name)
	{
		try
		{
			return resources.get(name).toString();
		}
		catch(NullPointerException ex)
		{
			return "null";
		}
	}
}
