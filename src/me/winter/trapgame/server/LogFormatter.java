package me.winter.trapgame.server;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * <p>A java.util.logging. Formatter I made to format logs in one line per messages with the date.</p>
 *
 * <p>Created by 1541869 on 2016-04-15.</p>
 */
public class LogFormatter extends Formatter
{
	@Override
	public String format(LogRecord record)
	{
		return new Date(record.getMillis()) + " [" + record.getLevel() + "] " + formatMessage(record) + System.getProperty("line.separator");
	}
}
