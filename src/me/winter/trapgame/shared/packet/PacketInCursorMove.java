package me.winter.trapgame.shared.packet;

import java.io.*;

/**
 * From client to server
 * Tells the server the client moved it's cursor
 *
 * Created by Alexander Winter on 2016-03-28.
 */
public class PacketInCursorMove extends Packet
{
	private float cursorX, cursorY;

	public PacketInCursorMove()
	{

	}

	public PacketInCursorMove(float cursorX, float cursorY)
	{
		this.cursorX = cursorX;
		this.cursorY = cursorY;
	}

	@Override
	public void readFrom(InputStream stream) throws IOException
	{
		DataInputStream dataStream = new DataInputStream(stream);
		setCursorX(dataStream.readFloat());
		setCursorY(dataStream.readFloat());
	}

	@Override
	public void writeTo(OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);
		dataStream.writeFloat(getCursorX());
		dataStream.writeFloat(getCursorY());
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
