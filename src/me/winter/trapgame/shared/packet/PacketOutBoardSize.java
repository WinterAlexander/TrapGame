package me.winter.trapgame.shared.packet;

/**
 * Packet sent from server to client
 * Used to inform clients the size of the board
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class PacketOutBoardSize extends Packet
{
	private int width, height;

	public PacketOutBoardSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}
}
