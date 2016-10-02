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
	private int players, slots;
	private String motd;

	public PacketOutPong()
	{

	}

	public PacketOutPong(String version, int players, int slots, String motd)
	{
		this.version = version;
		this.players = players;
		this.slots = slots;
		this.motd = motd;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		DataInputStream dataStream = new DataInputStream(stream);
		setVersion(dataStream.readUTF());
		setPlayers(dataStream.readShort());
		setSlots(dataStream.readShort());
		setMotd(dataStream.readUTF());
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);
		dataStream.writeUTF(getVersion());
		dataStream.writeShort((short)getPlayers());
		dataStream.writeShort((short)getSlots());
		dataStream.writeUTF(getMotd());
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
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

	public String getMotd()
	{
		return motd;
	}

	public void setMotd(String motd)
	{
		this.motd = motd;
	}
}
