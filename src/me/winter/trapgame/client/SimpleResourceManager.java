package me.winter.trapgame.client;

import me.winter.trapgame.util.FileUtil;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * <p>A simple resource manager method-blocking and not multi-threaded
 * that loads all the resources available when load method is called.</p>
 *
 * <p>Created by winter on 11/04/16.</p>
 */
public class SimpleResourceManager implements ResourceManager
{
	private Properties paths;
	private Map<String, Object> resources;

	public SimpleResourceManager()
	{
		paths = new Properties();
		resources = new HashMap<>();
	}

	@Override
	public void scan(String index) throws FileNotFoundException
	{
		try
		{
			paths.load(new BufferedInputStream(FileUtil.resourceAsStream(index)));

		}
		catch(FileNotFoundException ex)
		{
			throw ex;
		}
		catch(IOException ex)
		{
			ex.printStackTrace(System.err);
		}
	}

	@Override
	public void load()
	{

	}

	@Override
	public Set<String> nameSet()
	{
		return paths.stringPropertyNames();
	}

	@Override
	public void dispose()
	{

	}

	@Override
	public Image getImage(String name)
	{
		try
		{
			return (Image)resources.get(name);
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
				lineUnavailable.printStackTrace(System.err);
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
