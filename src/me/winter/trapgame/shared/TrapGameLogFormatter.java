package me.winter.trapgame.shared;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * <p>A java.util.logging. Formatter I made to format logs in one line per messages with the date.</p>
 *
 * <p>Created by 1541869 on 2016-04-15.</p>
 */
public class TrapGameLogFormatter extends Formatter
{
	@Override
	public String format(LogRecord record)
	{
		if(record.getThrown() == null)
		{
			return record.getLoggerName() + ": " +
					new Date(record.getMillis()) +
					" [" + record.getLevel() + "] " +
					formatMessage(record) +
					System.getProperty("line.separator");
		}

		StringWriter stringWriter = new StringWriter();
		PrintWriter stream = new PrintWriter(stringWriter);

		record.getThrown().printStackTrace(stream);

		return record.getLoggerName() + ": " +
				new Date(record.getMillis()) +
				" [" + record.getLevel() + "] " +
				formatMessage(record) +
				System.getProperty("line.separator") +
				stringWriter.toString() +
				System.getProperty("line.separator");
	}
}
