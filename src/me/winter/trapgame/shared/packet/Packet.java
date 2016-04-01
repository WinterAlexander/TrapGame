package me.winter.trapgame.shared.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a small packet of information send from client to server or vice versa
 * Should try to minimize memory usage
 *
 * Created by winter on 25/03/16.
 */
public abstract class Packet
{
	public abstract void readFrom(InputStream stream) throws IOException;

	public abstract void writeTo(OutputStream stream) throws IOException;
}
