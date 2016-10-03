package me.winter.trapgame.client;

import me.winter.trapgame.shared.packet.PacketOutPong;

import java.net.InetAddress;

/**
 * Represents a response from a server that received a broadcast.
 * Created by 1541869 on 2016-10-03.
 */
public class BroadcastResponse
{
	private InetAddress address;
	private int port;
	private PacketOutPong pong;
	private int pingDelay;

	public BroadcastResponse(InetAddress address, int port, PacketOutPong pong, int pingDelay)
	{
		this.address = address;
		this.port = port;
		this.pong = pong;
		this.pingDelay = pingDelay;
	}

	public InetAddress getAddress()
	{
		return address;
	}

	public void setAddress(InetAddress address)
	{
		this.address = address;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public PacketOutPong getPong()
	{
		return pong;
	}

	public void setPong(PacketOutPong pong)
	{
		this.pong = pong;
	}

	public int getPingDelay()
	{
		return pingDelay;
	}

	public void setPingDelay(int pingDelay)
	{
		this.pingDelay = pingDelay;
	}
}
