package me.killstorm103.Rebug.Utils;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import me.killstorm103.Rebug.Main.Rebug;
import net.minecraft.server.v1_8_R3.*;

public class PT
{
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
	public static void Log (CommandSender sender, String tolog)
	{
		sender.sendMessage(tolog);
	}
	public static void LogToConsole (String tolog)
	{
		Rebug.getGetMain().getServer().getConsoleSender().sendMessage(tolog);
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
	public static void SendPacket(Player player, Packet<?> packet) 
	{
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
		for (int i = 0;i < loop; i ++)
			SendPacket(player, packet);
    }
	public static int randomNumber (int max, int min) 
    {
        return (int) ((Math.random() * (max - min)) + min);
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
    private static EntityLiving entity = null;
    public static void CrashPlayer (CommandSender sender, Player willBeCrashed, String mode)
    {
    	entity = null;
    	if (mode != null && mode.length() > 0)
    	{
    		final EntityPlayer px = PT.getEntityPlayer(willBeCrashed);
    		if (mode.equalsIgnoreCase("creeper"))
    			entity = new EntityCreeper(px.world);
    		
    		if (entity == null)
    		{
    			sender.sendMessage("Unsupported Entity");
    			return;
    		}
    		final DataWatcher dw = new DataWatcher((net.minecraft.server.v1_8_R3.Entity) entity);
            dw.a (mode.equalsIgnoreCase("creeper") ? 18 : 31, (Object) Integer.MAX_VALUE);
            Packet<?> packet_spawn;
            packet_spawn = new PacketPlayOutSpawnEntityLiving((EntityLiving) entity);
            px.playerConnection.sendPacket(packet_spawn);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Rebug.getGetMain(), new Runnable()
            {
                @Override
                public void run()
                {
                    PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(entity.getId(), dw, true);
                    px.playerConnection.sendPacket(meta);
                }
            }, 5L);
    	}
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
    public static int state (float seconds, float saturation, float brightness, long index)
    {
    	seconds = seconds < .002F ? .002F : seconds;
    	float hue = ((System.currentTimeMillis() + index) % (int) (seconds * 1000)) / (float) (seconds * 1000);
    	int color = java.awt.Color.HSBtoRGB(hue, saturation, brightness);
    	return color;
    }
	public static World getWorld(Player crash_Victim) {
		return getEntityPlayer(crash_Victim).getWorld();
	}
	public static String getServerVersion() 
	{
		return PT.SubString(Rebug.getGetMain().getServer().getBukkitVersion().trim(), 0, 6).replace("-", "");
	}
}
