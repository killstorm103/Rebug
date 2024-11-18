package me.killstorm103.Rebug.Shared.Utils;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import me.killstorm103.Rebug.Shared.Main.Rebug;

public class PT 
{
	public static Class<?> getNMSClass(String name) {
		try {
			return Class.forName("net.minecraft.server."
					+ Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static ItemStack RepairItem(ItemStack item) 
	{
		ItemStack Stack = new ItemStack(item.getType());
		Stack.setItemMeta(item.getItemMeta());
		return Stack;
	}
	@SuppressWarnings("deprecation")
	public static int getMaterialID (Material material)
	{
		return material.getId();
	}
	@SuppressWarnings("deprecation")
	public static Material getMaterialByID (int ID)
	{
		if (PT.isServerNewerOrEquals(ServerVersion.V_1_14))
			return Material.DIRT;
		
		try
		{
			for (Material material : Material.values())
			{
				if (material != null && material != Material.AIR && material.getId() == ID)
					return material;
			}
		}
		catch (Exception e)
		{
			return Material.DIRT;
		}
		return Material.DIRT;
	}
	
	public static final String UnsupportedInVersion = Rebug.RebugMessage
			+ "That isn't supported in this Server Version!";
	public static int randomNumber(int max, int min) {
		return (int) ((Math.random() * (max - min)) + min);
	}

	public static short randomNumber(short max, short min) {
		return (short) ((Math.random() * (max - min)) + min);
	}

	public static float randomNumber(float max, float min) {
		return (float) ((Math.random() * (max - min)) + min);
	}
	public static boolean isHotbarInventoryFull(Player player) 
	{
		return player.getInventory().firstEmpty() == -1;
	}
	public static boolean isInventoryFull(Inventory inventory) 
	{
		if (inventory == null) return false;
		
		return inventory.firstEmpty() ==- 1;
	}
	public static final String getServerVersion() {
		return Rebug.getINSTANCE().getServerVersion();
		// return PTNormal.SubString(Bukkit.getServer().getBukkitVersion().trim(), 0,
		// 6).replace("-", "");
	}

	public static boolean isHoldingSword(ItemStack item) {
		if (item == null || item.getType() == Material.AIR)
			return false;

		return item.getType() == Material.DIAMOND_SWORD || item.getType() == Material.GOLD_SWORD
				|| item.getType() == Material.IRON_SWORD || item.getType() == Material.STONE_SWORD
				|| item.getType() == Material.WOOD_SWORD;
	}
	public static long randomNumber(long max, long min) {
		return (long) ((Math.random() * (max - min)) + min);
	}
	public static boolean isServerNewerOrEquals (ServerVersion server)
	{
		return PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(server);
	}
	public static boolean isServerOlderThan (ServerVersion server)
	{
		return PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(server);
	}
	public static double randomNumber(double max, double min) {
		return (Math.random() * (max - min)) + min;
	}
	public final Location getSpawn() {
		final Location loc = new Location(Bukkit.getWorld("world"),
				Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posX"),
				Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posY"),
				Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posZ")),
				locWithRots = new Location(Bukkit.getWorld("world"),
						Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posX"),
						Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posY"),
						Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posZ"),
						(float) Rebug.getINSTANCE().getConfig().getDouble("world-spawn.Yaw"),
						(float) Rebug.getINSTANCE().getConfig().getDouble("world-spawn.Pitch"));

		return Rebug.getINSTANCE().getConfig().getBoolean("world-spawn.set-rots") ? locWithRots : loc;
	}

	public static String RebugsUserWasNullErrorMessage(String AddOn) {
		return ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Rebug's " + ChatColor.RED
				+ "\"User\" was somehow null, " + AddOn;
	}

	public static boolean nextBoolean() {
		return new Random().nextBoolean();
	}
	public static String ReverseString(String name) {
		name = name.trim();
		StringBuilder reversedNameBuilder = new StringBuilder(), subNameBuilder = new StringBuilder();

		for (int i = 0; i < name.length(); i++) {

			char currentChar = name.charAt(i);

			if (currentChar != ' ' && currentChar != '-')
				subNameBuilder.append(currentChar);

			else {
				reversedNameBuilder.insert(0, currentChar + subNameBuilder.toString());
				subNameBuilder.setLength(0);
			}
		}

		return reversedNameBuilder.insert(0, subNameBuilder.toString()).toString();
	}
	public static int nextInt(final int startInclusive, final int endExclusive) {
		if (endExclusive - startInclusive <= 0)
			return startInclusive;

		return startInclusive + new Random().nextInt(endExclusive - startInclusive);
	}

	public static double nextDouble(final double startInclusive, final double endInclusive) {
		if (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0)
			return startInclusive;

		return startInclusive + (endInclusive - startInclusive) * Math.random();
	}

	public static float nextFloat(final float startInclusive, final float endInclusive) {
		if (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0f)
			return startInclusive;

		return (float) (startInclusive + (endInclusive - startInclusive) * Math.random());
	}

	public static String randomNumber(final int length) {
		return random(length, "123456789");
	}

	public static String randomString(final int length) {
		return random(length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
	}

	public static String random(final int length, final String chars) {
		return random(length, chars.toCharArray());
	}

	public static String random(final int length, final char[] chars) {
		final StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < length; ++i) {
			stringBuilder.append(chars[new Random().nextInt(chars.length)]);
		}
		return stringBuilder.toString();
	}

	public static int getPlayerVersion (UUID uid) {
		return Via.getAPI().getPlayerVersion(uid);
	}

	public static String getPlayerVersion(int version) {
		String v = "Protocol Number= " + version;
		v = ProtocolVersion.getProtocol(version).getName();
		return v;
	}

	public static String SubString(String sub, int begin, int max) {
		String ss = sub.substring(begin, Math.min(sub.length(), max));

		return ss;
	}
	public static boolean isStringNull(String s) {
		if (s == null || s.length() < 1)
			return true;

		return false;
	}
	public static boolean isStringNull_loop(String... s) {
		if (s.length < 1)
			return true;

		for (int i = 0; i < s.length; i++)
		{
			if (s[i] == null || s[i].length() < 1)
				return true;
		}

		return false;
	}

	@SuppressWarnings("unused")
	public static boolean isNumber_Float(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Float.parseFloat(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unused")
	public static boolean isNumber_Double(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unused")
	public static boolean isNumber_Integer(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unused")
	public static boolean isNumber_Short(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Short.parseShort(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static long getMaxMemory() {
		long maxMemory = Runtime.getRuntime().maxMemory();
		return maxMemory / 0x100000L;
	}

	public static long getUsedMemory() {
		long totalMemory = Runtime.getRuntime().totalMemory(), freeMemory = Runtime.getRuntime().freeMemory(),
				usedMemory = totalMemory - freeMemory;
		return usedMemory / 0x100000L;
	}

	public static int getPing (Object player) 
	{
		if (player == null) return -1;
		
		try
		{
			return PacketEvents.getAPI().getPlayerManager().getPing(player);
		}
		catch (Exception e) 
		{
			if (Rebug.debug)
				e.printStackTrace();
		}
		return -1;
	}
	public static Object  getChannel (Object player)
	{
		if (player == null) return null;
		
		return PacketEvents.getAPI().getPlayerManager().getChannel(player);
	}

	public static boolean isSameStringButDiffOrder(String str1, String str2)
	{
		String[] words1 = str1.split(" "), words2 = str2.split(" ");
		Arrays.sort(words1);
		Arrays.sort(words2);
		return Arrays.equals(words1, words2);
	}
}
