package me.killstorm103.Rebug.Utils;

import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;


/**
 * All supported color values for chat
 */
public enum TheChatColor
{

	/**
	 * Represents black
	 */
	BLACK('0', 0x00),
	/**
	 * Represents dark blue
	 */
	DARK_BLUE('1', 0x1),
	/**
	 * Represents dark green
	 */
	DARK_GREEN('2', 0x2),
	/**
	 * Represents dark blue (aqua)
	 */
	DARK_AQUA('3', 0x3),
	/**
	 * Represents dark red
	 */
	DARK_RED('4', 0x4),
	/**
	 * Represents dark purple
	 */
	DARK_PURPLE('5', 0x5),
	/**
	 * Represents gold
	 */
	GOLD('6', 0x6),
	/**
	 * Represents gray
	 */
	GRAY('7', 0x7),
	/**
	 * Represents dark gray
	 */
	DARK_GRAY('8', 0x8),
	/**
	 * Represents blue
	 */
	BLUE('9', 0x9),
	/**
	 * Represents green
	 */
	GREEN('a', 0xA),
	/**
	 * Represents aqua
	 */
	AQUA('b', 0xB),
	/**
	 * Represents red
	 */
	RED('c', 0xC),
	/**
	 * Represents light purple
	 */
	LIGHT_PURPLE('d', 0xD),
	/**
	 * Represents yellow
	 */
	YELLOW('e', 0xE),
	/**
	 * Represents white
	 */
	WHITE('f', 0xF),
	/**
	 * Represents magical characters that change around randomly
	 */
	MAGIC('k', 0x10, true),
	/**
	 * Makes the text bold.
	 */
	BOLD('l', 0x11, true),
	/**
	 * Makes a line appear through the text.
	 */
	STRIKETHROUGH('m', 0x12, true),
	/**
	 * Makes the text appear underlined.
	 */
	UNDERLINE('n', 0x13, true),
	/**
	 * Makes the text italic.
	 */
	ITALIC('o', 0x14, true),
	/**
	 * Resets all previous chat colors or formats.
	 */
	RESET('r', 0x15);

	/**
	 * The special character which prefixes all chat colour codes. Use this if you
	 * need to dynamically convert colour codes from your custom format.
	 */
	public static final char COLOR_CHAR = '\u00A7';
	private static final Pattern STRIP_COLOR_PATTERN = Pattern
			.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");

	private final int intCode;
	private final char code;
	private final boolean isFormat;
	private final String toString;
	private final static Map<Integer, TheChatColor> BY_ID = Maps.newHashMap();
	private final static Map<Character, TheChatColor> BY_CHAR = Maps.newHashMap();

	private TheChatColor(char code, int intCode) {
		this(code, intCode, false);
	}

	private TheChatColor(char code, int intCode, boolean isFormat) {
		this.code = code;
		this.intCode = intCode;
		this.isFormat = isFormat;
		this.toString = new String(new char[] { COLOR_CHAR, code });
	}

	/**
	 * Gets the char value associated with this color
	 *
	 * @return A char value of this color code
	 */
	public char getChar() {
		return code;
	}

	@Override
	public String toString() {
		return toString;
	}

	/**
	 * Checks if this code is a format code as opposed to a color code.
	 */
	public boolean isFormat() {
		return isFormat;
	}

	/**
	 * Checks if this code is a color code as opposed to a format code.
	 */
	public boolean isColor() {
		return !isFormat && this != RESET;
	}

	

	/**
	 * Strips the given message of all color codes
	 *
	 * @param input String to strip of color
	 * @return A copy of the input string, without any coloring
	 */
	public static String stripColor(final String input) {
		if (input == null) 
			return null;
		

		return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
	}

	/**
	 * Translates a string using an alternate color code character into a string
	 * that uses the internal ChatColor.COLOR_CODE color code character. The
	 * alternate color code character will only be replaced if it is immediately
	 * followed by 0-9, A-F, a-f, K-O, k-o, R or r.
	 *
	 * @param altColorChar    The alternate color code character to replace. Ex: &
	 * @param textToTranslate Text containing the alternate color code character.
	 * @return Text containing the ChatColor.COLOR_CODE color code character.
	 */
	public static String translateAlternateColorCodes (char altColorChar, String textToTranslate, boolean ACInComing, String ItemName) 
	{
		try
		{
			char[] b = textToTranslate.toCharArray();
			for (int i = 0; i < b.length - 1; i++)
			{
				if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) 
				{
					b[i] = TheChatColor.COLOR_CHAR;
					b[i + 1] = Character.toLowerCase(b[i + 1]);
				}
			}
			return new String(b);
		}
		catch (Exception e) 
		{
			return ACInComing ? "Failed Next" : "NullPointer";
		}
	}
	public static String translateAlternateColorCodes (char altColorChar, String textToTranslate, String ItemName) 
	{
		try
		{
			char[] b = textToTranslate.toCharArray();
			for (int i = 0; i < b.length - 1; i++)
			{
				if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) 
				{
					b[i] = TheChatColor.COLOR_CHAR;
					b[i + 1] = Character.toLowerCase(b[i + 1]);
				}
			}
			return new String(b);
		}
		catch (Exception e) 
		{
			return "Full Fail";
		}
	}
	public static String translateAlternateColorCodes (char altColorChar, String textToTranslate)
	{
		char[] b = textToTranslate.toCharArray();
		for (int i = 0; i < b.length - 1; i++)
		{
			if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) 
			{
				b[i] = TheChatColor.COLOR_CHAR;
				b[i + 1] = Character.toLowerCase(b[i + 1]);
			}
		}
		return new String(b);
	}

	/**
	 * Gets the ChatColors used at the end of the given input string.
	 *
	 * @param input Input string to retrieve the colors from.
	 * @return Any remaining ChatColors to pass onto the next line.
	 */

	static {
		for (TheChatColor color : values()) {
			BY_ID.put(color.intCode, color);
			BY_CHAR.put(color.code, color);
		}
	}
}