package me.winter.trapgame.shared.packet;

/**
 * Packet sent from server to client to
 * give them the size of the board
 *
 * Created by Alexander Winter on 2016-03-28.
 */
public class PacketOutBoardSize extends Packet
{
	private int boardWidth, boardHeight;

	public PacketOutBoardSize(int boardWidth, int boardHeight)
	{
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
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
