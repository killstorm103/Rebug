package me.killstorm103.Rebug.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import me.killstorm103.Rebug.Main.Rebug;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.World;

public class PT
{
	public static final PT INSTANCE = new PT();
	public final Location getSpawn ()
	{
		final Location loc = new Location(Bukkit.getWorld("world"), Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posX"), Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posY"),
	    Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posZ"),
		(float) Rebug.getINSTANCE().getConfig().getDouble("world-spawn.Yaw"), (float) Rebug.getINSTANCE().getConfig().getDouble("world-spawn.Pitch"));
		
		return loc;
	}
	
	@SuppressWarnings("deprecation")
	public void DebuggerChatMessage (Player player, PacketReceiveEvent e, String toDebug)
	{
		if (player == null || PT.isStringNull(toDebug)) return;
		
		int debuggercounter = Rebug.PacketDebuggerPlayers.get(player.getUniqueId());
		TextComponent component = new TextComponent(ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.RED + "DEBUGGER " + ChatColor.DARK_GRAY + ">> " + "[" + debuggercounter + "] " + e.getPacketName());
		BaseComponent[] baseComponents = new ComponentBuilder(e.getPacketName() + ":\n\n" + toDebug).create();
		HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, baseComponents);
		component.setHoverEvent(hoverEvent);
		player.spigot().sendMessage(component);
		Rebug.PacketDebuggerPlayers.put(player.getUniqueId(), debuggercounter + 1);
	}
	public static Class<?> getNMSClass(String name) 
	{
        try 
        {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        }
        catch (ClassNotFoundException e) 
        {
            e.printStackTrace();
            return null;
        }
    }
	public static String getTextFromComponent (Component component)
	{
		return LegacyComponentSerializer.legacyAmpersand().serialize(component);
	}
	public static boolean isHoldingSword (ItemStack item)
	{
		if (item == null || item.getType() == Material.AIR) return false;
		
		return item.getType() == Material.DIAMOND_SWORD || item.getType() == Material.GOLD_SWORD || item.getType() == Material.IRON_SWORD || item.getType() == Material.STONE_SWORD || item.getType() == Material.WOOD_SWORD;
	}
	public static boolean isStringNull_loop (String... s)
    {
		if (s.length < 1) return true;
		
    	for (int i = 0; i < s.length; i ++)
    	{
    		if (s[i] == null || s[i].length() < 1) return true;
    	}
    	
    	return false;
    }
	public static boolean isStringNull (String s)
	{
		if (s == null || s.length() < 1)
			return true;
		
		return false;
	}
	@SuppressWarnings("unused")
	public static boolean isNumber_Float (String strNum) 
	{
	    if (strNum == null)
	    {
	        return false;
	    }
	    try {
	        double d = Float.parseFloat(strNum);
	    }
	    catch (NumberFormatException nfe) 
	    {
	        return false;
	    }
	    return true;
	}
	@SuppressWarnings("unused")
	public static boolean isNumber_Double (String strNum) 
	{
	    if (strNum == null)
	    {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	    }
	    catch (NumberFormatException nfe) 
	    {
	        return false;
	    }
	    return true;
	}
	@SuppressWarnings("unused")
	public static boolean isNumber_Integer (String strNum) 
	{
	    if (strNum == null)
	    {
	        return false;
	    }
	    try {
	        double d = Integer.parseInt(strNum);
	    }
	    catch (NumberFormatException nfe) 
	    {
	        return false;
	    }
	    return true;
	}
	@SuppressWarnings("unused")
	public static boolean isNumber_Short (String strNum) 
	{
	    if (strNum == null)
	    {
	        return false;
	    }
	    try {
	        double d = Short.parseShort(strNum);
	    }
	    catch (NumberFormatException nfe) 
	    {
	        return false;
	    }
	    return true;
	}
	public static long getMaxMemory() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        return maxMemory / 0x100000L;
    }

    public static long getUsedMemory() {
        long totalMemory = Runtime.getRuntime().totalMemory(), freeMemory = Runtime.getRuntime().freeMemory(), usedMemory = totalMemory - freeMemory;
        return usedMemory / 0x100000L;
    }
	public static int getPing (Player player) 
	{
		try {
			Object craftPlayer = player.getClass().getMethod("getHandle", new Class[0]).invoke((Object)player, new Object[0]);
			int ping = (Integer)craftPlayer.getClass().getField("ping").get(craftPlayer);
			return ping;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return -1;
		}
	}
	public static void PlaceItem (Player player, ItemStack item, BlockPosition blockposition, EnumDirection enumdirection, float facingX, float facingY, float facingZ)
	{
		net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
		stack.placeItem(getEntityHuman(player), getWorld(player), blockposition, enumdirection, facingX, facingY, facingZ);
	}
	public static void IteminteractWith (Player player, ItemStack item, BlockPosition blockposition, EnumDirection enumdirection, float facingX, float facingY, float facingZ)
	{
		net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
		stack.getItem().interactWith(stack, getEntityHuman(player), getWorld(player), blockposition, enumdirection, facingX, facingY, facingZ);
	}
	public static CraftPlayer getCraftPlayer (Player player)
	{
		return ((CraftPlayer) player);
	}
	// TODO: Make
	public static void PlaceItemWithPacket (Player player, BlockPosition blockposition, Block block, int arg, int arg2)
	{
		//SendPacket(player, packet);
	}
	public static void Log (CommandSender sender, String tolog)
	{
		sender.sendMessage(tolog);
	}
	public static void LogToConsole (String tolog)
	{
		Rebug.getINSTANCE().getServer().getConsoleSender().sendMessage(tolog);
	}
	// Inventory Size can be: 9, 18, 27, 36, 45, 54
	
	public static Inventory createInventory (InventoryHolder holder, int size, String title)
	{
		Inventory inv = Bukkit.createInventory(holder, size, title);
		inv.clear();
		
		return inv;
	}
	public static Inventory createInventory (InventoryHolder holder, InventoryType type, String title)
	{
		Inventory inv = Bukkit.createInventory(holder, type, title);
		inv.clear();
		
		return inv;
	}
	public static EntityPlayer getEntityPlayer (Player player)
	{
		return ((CraftPlayer) player).getHandle();
	}
	public static EntityHuman getEntityHuman (Player player)
	{
		return (EntityHuman) getEntityPlayer(player);
	}
	public static EnumDirection getDirection (Player player)
	{
		return getEntityPlayer(player).aH();
	}
	public static void SendPacket(Player player, Packet<?> packet) 
	{
		if (player == null || !player.isOnline() || packet == null)
			return;
			
        try 
        {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke((Object) player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
	public static void SendPacket(Player player, Packet<?> packet, int loop) 
	{
		if (player == null || !player.isOnline() || packet == null)
			return;
		
		loop = loop < 1 ? 1 : loop;
		for (int i = 0;i < loop; i ++)
			SendPacket(player, packet);
    }
	public static int randomNumber (int max, int min) 
    {
        return (int) ((Math.random() * (max - min)) + min);
    }
	public static short randomNumber (short max, short min) 
    {
        return (short) ((Math.random() * (max - min)) + min);
    }
	public static float randomNumber (float max, float min) 
	{
        return (float) ((Math.random() * (max - min)) + min);
    }
    public static long randomNumber (long max, long min) 
    {
        return (long) ((Math.random() * (max - min)) + min);
    }
    public static double randomNumber (double max, double min) 
    {
        return (Math.random() * (max - min)) + min;
    }
    public static boolean nextBoolean () 
    {
        return new Random().nextBoolean();
    }
	public static int nextInt (final int startInclusive, final int endExclusive) 
	{
        if (endExclusive - startInclusive <= 0) 
            return startInclusive;
        
        return startInclusive + new Random().nextInt(endExclusive - startInclusive);
    }
    
    public static double nextDouble (final double startInclusive, final double endInclusive)
    {
        if (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0) 
            return startInclusive;
        
        return startInclusive + (endInclusive - startInclusive) * Math.random();
    }
    
    public static float nextFloat (final float startInclusive, final float endInclusive)
    {
        if (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0f)
            return startInclusive;
        
        return (float)(startInclusive + (endInclusive - startInclusive) * Math.random());
    }
     
    public static String randomNumber (final int length) 
    {
        return random(length, "123456789");
    }
    
    public static String randomString (final int length) 
    {
        return random(length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }
    
    public static String random (final int length, final String chars)
    {
        return random(length, chars.toCharArray());
    }
    
    public static String random (final int length, final char[] chars)
    {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            stringBuilder.append(chars[new Random().nextInt(chars.length)]);
        }
        return stringBuilder.toString();
    }
    public static int getPlayerVersion (Player player)
    {
    	return Via.getAPI().getPlayerVersion(player.getUniqueId());
    }
    public static String getPlayerVersion (int version)
    {
    	String v = "Protocol Number= " + version;
    	v = ProtocolVersion.getProtocol(version).getName();
    	return v;
    }
    
    public static String SubString (String sub, int begin, int max)
    {
    	String ss = sub.substring(begin, Math.min(sub.length(), max));
    	
    	return ss;
    }
	public static World getWorld(Player player) {
		return getEntityPlayer(player).getWorld();
	}
	public static String getServerVersion() 
	{
		return PT.SubString(Bukkit.getServer().getBukkitVersion().trim(), 0, 6).replace("-", "");
	}
	public static List<String> getPlayerNames() 
	{
		List<String> t = new ArrayList<>();
		t.clear();
		Player[] Players = new Player[Bukkit.getOnlinePlayers().size()];
		Bukkit.getOnlinePlayers().toArray(Players);
		for (int i = 0; i < Players.length; i ++)
		{
			t.add(Players[i].getName());
		}
		return t;
	}
	public static String RebugsUserWasNullErrorMessage (String AddOn)
	{
		return ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Rebug's " + ChatColor.RED + "\"User\" was somehow null, " + AddOn;
	}
	public static void RunTaskCommand(CommandSender sender, String command)
	{
		Bukkit.getServer().getScheduler().runTask(Rebug.getINSTANCE(), new Runnable()
		{
			@Override
			public void run() 
			{
				Bukkit.dispatchCommand(sender == null ? Bukkit.getServer().getConsoleSender() : sender, command);
			}
		});
	}
	public static void KickPlayer(Player player, String reason) 
	{
		if (player == null || !player.isOnline())
			return;
		
		if (!Rebug.KickList.contains(player.getUniqueId()))
			Rebug.KickList.add(player.getUniqueId());
			
		Bukkit.getScheduler().runTask(Rebug.getINSTANCE(), new Runnable()
		{
			
			@Override
			public void run()
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "kick " + player.getName() + " " + reason);
			}
		});
	}
	 
	public static void BanPlayer(Player player, String reason) 
	{
		if (player == null || !player.isOnline())
			return;
		
		Bukkit.getScheduler().runTask(Rebug.getINSTANCE(), new Runnable()
		{
			
			@Override
			public void run()
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ban " + player.getName() + " " + reason);
			}
		});
	}
	public static boolean isInventoryFull (Player player)
	{
		return player.getInventory().firstEmpty() ==- 1;
	}
	public static boolean isInventoryFull (Inventory inventory)
	{
		return inventory.firstEmpty() ==- 1;
	}
	public static ItemStack RepairItem(ItemStack item) 
	{
		ItemStack Stack = new ItemStack (item.getType());
		Stack.setItemMeta(item.getItemMeta());
		return Stack;
	}
	public static void PlaySound(Player player, Sound sound, float volume, float pitch)
	{
		player.playSound(player.getLocation(), sound, volume, pitch);
	}
	public static Player getPlayerFromHumanEntity(LivingEntity entity) 
	{
		return (Player) entity;
	}
}
