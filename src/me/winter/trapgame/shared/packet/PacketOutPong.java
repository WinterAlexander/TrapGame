package me.winter.trapgame.shared.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>Packet send from the server to the client to gives infos about the server.
 * <br />
 * (Displayed in the client serverlist)</p>
 * <p>Created by Alexander Winter on 2016-10-01 at 23:17.</p>
 */
public class PacketOutPong extends Packet
{
	private String version;
	private String name;
	private int players, slots;


	public PacketOutPong()
	{

	}

	public PacketOutPong(String version, String name, int players, int slots)
	{
		this.version = version;
		this.players = players;
		this.slots = slots;
		this.name = name;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		DataInputStream dataStream = new DataInputStream(stream);
		setVersion(dataStream.readUTF());
		setName(dataStream.readUTF());
		setPlayers(dataStream.readShort());
		setSlots(dataStream.readShort());
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);
		dataStream.writeUTF(getVersion());
		dataStream.writeUTF(getName());
		dataStream.writeShort((short)getPlayers());
		dataStream.writeShort((short)getSlots());
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getPlayers()
	{
		return players;
	}

	public void setPlayers(int players)
	{
		this.players = players;
	}

	public int getSlots()
	{
		return slots;
	}

	public void setSlots(int slots)
	{
		this.slots = slots;
	}
}
