package me.winter.trapgame.util;

import java.io.File;
import java.io.IOException;

/**
 *
 * Created by Alexander Winter on 2016-01-10.
 */
public class FileUtil
{
	private FileUtil() {}

	public static void createDirectory(File directory) throws IOException, IllegalArgumentException
	{
		if(directory == null)
			throw new IllegalArgumentException("Cannot create a null directory");

		if(directory.exists())
		{
			if(directory.isDirectory())
				return;

			if(!directory.delete())
				throw new IOException("Can't delete file " + directory.getAbsolutePath() + " to get equivalent directory");
		}

		if(!directory.mkdirs())
			throw new IOException("Can't create directory " + directory.getAbsolutePath());
	}

	public static void createFile(File file) throws IOException, IllegalArgumentException
	{
		if(file == null)
			throw new IllegalArgumentException("Cannot create a null file");

		if(file.exists())
		{
			if(file.isFile())
				return;

			if(!file.delete())
				throw new IOException("Can't delete directory " + file.getAbsolutePath() + " to get equivalent file");
		}

		if(!file.createNewFile())
			throw new IOException("Can't create file " + file.getAbsolutePath());
	}

	public static String getAppData()
	{
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN"))
			return System.getenv("APPDATA");

		else if (OS.contains("MAC"))
			return System.getProperty("user.home") + "/Library/Application Support";

		else if (OS.contains("NUX"))
			return System.getProperty("user.home");

		return System.getProperty("user.dir");
	}

	/*public static void copyFromArchive(String file, File directory, boolean force)
	{
		if(!ResourceLoader.resourceExists(file))
			return;

		try
		{

			File destination = new File(directory, file);

			if(destination.exists() && !force)
				return;

			createDirectory(destination.getParentFile());

			if(!destination.createNewFile())
				throw new RuntimeException("Can't create file " + destination.getAbsolutePath());

			InputStream input = ResourceLoader.getResourceAsStream(file);
			OutputStream output = new FileOutputStream(destination);

			byte[] buffer = new byte[1024];
			int length;
			while((length = input.read(buffer)) > 0)
				output.write(buffer, 0, length);

			output.close();
		}
		catch(Exception e)
		{
			throw new RuntimeException("Can't copy " + file + " from archive to " + directory.getAbsolutePath(), e);
		}
	}*/
}
