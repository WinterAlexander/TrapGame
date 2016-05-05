package me.winter.trapgame.shared;

import com.sun.istack.internal.NotNull;
import me.winter.trapgame.util.FileUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Represents a game translation such as a french.lang or english.lang file
 * Both client and server should use this class as their needs.
 *
 * Created by Alexander Winter on 2016-04-16.
 */
public class GameTranslation extends Properties
{
	private String name;

	public GameTranslation(@NotNull String name)
	{
		this.name = name;
	}

	public void load() throws IOException
	{
		InputStream stream = FileUtil.resourceAsStream(getPath());

		if(stream == null)
			throw new FileNotFoundException("Lang file " + name + " couldn't be found");

		this.load(new InputStreamReader(stream, "UTF-8"));
	}

	public String getLine(@NotNull String id)
	{
		return getProperty(id, id);
	}

	public String getPath()
	{
		return "/lang/" + name + ".lang";
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
