package me.winter.trapgame.client;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 * Centralizes any uses of archive resources to this class.
 * Loads the resources at startup and gives access to all of them
 * using their names. The {@code ResourceManager} needs to scan
 * a .properties file first to retrieve all the paths and names.
 * </p>
 *
 * <p>Created by winter on 11/04/16.</p>
 */
public interface ResourceManager
{
	/**
	 *  <p>
	 *  Scans the file passed in parameter to retreive all the resource paths.
	 *  The file's format should be java properties.
	 *  </p>
	 *
	 * @param index The name of the index file to scan
	 */
	void scan(String index) throws IOException;

	/**
	 * Loads the resources if necessary.
	 *
	 */
	void load() throws IOException;

	/**
	 * @return All the scanned resources name
	 */
	Set<String> nameSet();

	/**
	 * Disposes of the resources if necessary
	 */
	void dispose();

	Image getImage(String name);
	Clip getSound(String name);
	String getText(String name);
}
