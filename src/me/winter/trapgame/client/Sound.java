package me.winter.trapgame.client;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Created by 1541869 on 2016-10-05.
 */
public class Sound
{
	private Logger logger;

	private Clip[] clips;
	private int currentClip;

	public Sound(Logger logger, AudioInputStream inputStream, int channels) throws IOException, LineUnavailableException
	{
		this.logger = logger;

		this.clips = new Clip[channels];

		AudioFormat format = inputStream.getFormat();
		byte[] audio = new byte[format.getFrameSize() * (int)inputStream.getFrameLength()];
		inputStream.read(audio);

		for(int i = 0; i < channels; i++)
		{
			clips[i] = AudioSystem.getClip();
			clips[i].open(format, audio, 0, audio.length);
		}
	}

	public void play()
	{
		try
		{
			clips[currentClip].setFramePosition(0);
			clips[currentClip++].start();
			currentClip %= clips.length;
		}
		catch(Exception ex)
		{
			logger.log(Level.WARNING, "An error occured when playing sound", ex);
		}
	}
}
