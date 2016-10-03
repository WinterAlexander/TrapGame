package me.winter.trapgame.util;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Static class with methods for network utilities
 * Created by 1541869 on 2016-10-03.
 */
public class NetUtil
{
	private NetUtil() {}

	/**
	 * Retrives all the broadcast address of the machine (depending of the different network interfaces)
	 * @return an arraylist of broadcast addresses
	 */
	public static List<InetAddress> getBroadcastAddresses()
	{
		List<InetAddress> addresses = new ArrayList<>();
		Enumeration<NetworkInterface> interfaces;
		try
		{
			interfaces = NetworkInterface.getNetworkInterfaces();
		}
		catch(SocketException ex)
		{
			return addresses;
		}

		for(NetworkInterface networkInterface = interfaces.nextElement(); interfaces.hasMoreElements(); networkInterface = interfaces.nextElement()) try
		{
			if(networkInterface.isLoopback())
				continue;

			for(InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
			{
				InetAddress broadcast = interfaceAddress.getBroadcast();
				if (broadcast == null)
					continue;

				addresses.add(broadcast);
			}
		}
		catch(SocketException ex)
		{
			//empty
		}

		return addresses;
	}

	public static boolean isLocal(InetAddress addr)
	{
		// Check if the address is a valid special local or loop back
		if(addr.isAnyLocalAddress() || addr.isLoopbackAddress())
			return true;

		// Check if the address is defined on any interface
		try
		{
			return NetworkInterface.getByInetAddress(addr) != null;
		}
		catch(SocketException e)
		{
			return false;
		}
	}
}
