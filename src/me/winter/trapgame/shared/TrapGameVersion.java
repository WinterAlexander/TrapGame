package me.winter.trapgame.shared;

/**
 * <p>Static class storing version and static methods to check version compatibility.</p>
 * <p>Created by Alexander Winter on 2016-10-01 at 23:35.</p>
 */
public class TrapGameVersion
{
	private static final char MAJOR = '1';
	private static final char MINOR = '1';
	private static final char FIX   = '2';
	private static final char BUILD = '0';
	public static final String GAME_VERSION = "" + MAJOR + '.' + MINOR + '.' + FIX + '.' + BUILD;


	private TrapGameVersion() {}

	public static boolean isCompatible(String version)
	{
		String[] verParts = version.split("\\.");

		return verParts[0].equals(MAJOR) && verParts[1].equals(MINOR); //for now, everything is compatible TODO: 2016-10-01 check version changes and protect
	}
}
