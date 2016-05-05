package me.winter.trapgame.util;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 *
 * Created by Alexander Winter on 2016-01-12.
 */
public class StringUtil
{
	private StringUtil() {}

	public static String join(List<?> list)
	{
		return join(list.toArray());
	}

	public static String join(List<?> list, String separator)
	{
		return join(list.toArray(), separator);
	}

	public static String join(Object[] array)
	{
		return join(array, ", ");
	}

	public static String join(Object[] array, String separator)
	{
		if(array.length == 0)
			return "";

		String result = array[0].toString();

		for(int index = 1; index < array.length; index++)
			result += separator + array[index];

		return result;
	}

	public static String getStackTrace(Throwable throwable)
	{
		StringWriter stringWriter = new StringWriter();
		throwable.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	public static String capitalize(String string)
	{
		char[] letters = string.toCharArray();

		for(int index = 0; index < letters.length; index++)
		{
			if(index != 0 && !Character.isWhitespace(letters[index - 1]))
				continue;

			if(!Character.isLowerCase(letters[index]))
				continue;

			letters[index] = Character.toUpperCase(letters[index]);
		}

		return new String(letters);
	}

	public static String getLastPart(String toSplit, String regex)
	{
		String[] parts = toSplit.split(regex);

		return parts[parts.length - 1];
	}

	public static String backspace(String string, int index)
	{
		if(index <= 0 || index > string.length())
			return string;

		if(index == 1)
			return string.substring(index);

		if(index == string.length())
			return string.substring(0, index - 1);

		return string.substring(0, index - 1) + string.substring(index);
	}

	public static String insert(String string, int index, char input)
	{
		return insert(string, index, input + "");
	}

	public static String insert(String string, int index, String input)
	{
		if(index <= 0)
			return input + string;

		if(index >= string.length())
			return string + input;

		return string.substring(0, index) + input + string.substring(index);
	}

	public static String getClipboardContent()
	{
		try
		{
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			return clipboard.getContents(null) == null ? "" : clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor).toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}

	public static void setClipboardContent(String content)
	{
		try
		{
			StringSelection stringSelection = new StringSelection(content);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean isInt(String string)
	{
		try
		{
			Integer.parseInt(string);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public static boolean isLong(String string)
	{
		try
		{
			Long.parseLong(string);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public static boolean isDouble(String string)
	{
		try
		{
			Double.parseDouble(string);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public static boolean isFloat(String string)
	{
		try
		{
			Float.parseFloat(string);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public static String htmlSpecialChars(String html)
	{
		StringBuilder builder = new StringBuilder(html);

		for(int index = 0; index < builder.length(); index++)
		{
			String toInsert;

			switch(builder.charAt(index))
			{
				case '<':
					toInsert = "&lt;";
					break;

				case '>':
					toInsert = "&gt;";
					break;

				case '\'':
					toInsert = "&#39;";
					break;

				case '"':
					toInsert = "&quot;";
					break;

				case '&':
					toInsert = "&amp;";
					break;

				default:
					continue;
			}

			builder.deleteCharAt(index);
			builder.insert(index, toInsert);

			index += toInsert.length() - 1;
		}

		return builder.toString();
	}

	public static String toCSS(Color color)
	{
		return "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
	}

	public static String noHTML(String html)
	{
		return html.replaceAll("\n|\r", "").replaceAll("<br\\s*/?>", "\n").replaceAll("<[^<>]+>", "");
	}
}
