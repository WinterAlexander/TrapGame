package me.winter.trapgame.shared.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>Packet send by client to indicate it wants the infos about the server.</p>
 *
 * <p>Created by Alexander Winter on 2016-10-01.</p>
 */
public class PacketInPing extends Packet
{
	public PacketInPing()
	{
		//nothing for now
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{

	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{

	}
}
