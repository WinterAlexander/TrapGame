package me.winter.trapgame.shared.packet;

import java.io.*;

/**
 * Packet sent from server to client to
 * give them the size of the board
 *
 * Created by Alexander Winter on 2016-03-28.
 */
public class PacketOutBoardSize extends Packet
{
	private int boardWidth, boardHeight;

	public PacketOutBoardSize()
	{

	}

	public PacketOutBoardSize(int boardWidth, int boardHeight)
	{
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		DataInputStream dataStream = new DataInputStream(stream);
		setBoardWidth(dataStream.readShort());
		setBoardHeight(dataStream.readShort());
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);
		dataStream.writeShort(getBoardWidth());
		dataStream.writeShort(getBoardHeight());
	}

	public int getBoardWidth()
	{
		return boardWidth;
	}

	public void setBoardWidth(int boardWidth)
	{
		this.boardWidth = boardWidth;
	}

	public int getBoardHeight()
	{
		return boardHeight;
	}

	public void setBoardHeight(int boardHeight)
	{
		this.boardHeight = boardHeight;
	}
}
