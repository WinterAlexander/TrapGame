package me.winter.trapgame.shared.packet;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;

/**
 * From server to client
 * Informs other players that this player has moved his cursor
 *
 * Created by Alexander Winter on 2016-03-28.
 */
public class PacketOutCursorMove extends Packet
{
	private int playerId;
	private float cursorX, cursorY;

	public PacketOutCursorMove()
	{

	}

	public PacketOutCursorMove(int playerId, float cursorX, float cursorY)
	{
		this.playerId = playerId;
		this.cursorX = cursorX;
		this.cursorY = cursorY;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		DataInputStream dataStream = new DataInputStream(stream);
		setPlayerId(dataStream.readShort());
		setCursorX(dataStream.readFloat());
		setCursorY(dataStream.readFloat());
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);
		dataStream.writeShort((short)playerId);
		dataStream.writeFloat(getCursorX());
		dataStream.writeFloat(getCursorY());
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	public float getCursorX()
	{
		return cursorX;
	}

	public void setCursorX(float cursorX)
	{
		this.cursorX = cursorX;
	}

	public float getCursorY()
	{
		return cursorY;
	}

	public void setCursorY(float cursorY)
	{
		this.cursorY = cursorY;
	}
}
