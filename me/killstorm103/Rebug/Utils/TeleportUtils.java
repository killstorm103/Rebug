package me.killstorm103.Rebug.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class TeleportUtils 
{
 
    public static final HashSet<Material> bad_blocks = new HashSet<>();
    static
    {
        bad_blocks.add(Material.LAVA);
        bad_blocks.add(Material.FIRE);
        bad_blocks.add(Material.CACTUS);
        bad_blocks.add(Material.BEDROCK);
        bad_blocks.add(Material.BARRIER);
    }
    public static Location GenerateCrashLocation (Player player)
    {
    	final Location To = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
    	double y = To.getY();
    	while (y > -2.147483648E9D)
    	{
    		y -= 1000000;
    		To.setY(y);
    		Location randomLocation = new Location(player.getWorld(), player.getLocation().getX(), To.getY(), player.getLocation().getZ());
          	player.teleport(randomLocation);
    	}
    	return null;
    }
 
    public static Location GenerateLocation (Player player)
    {
        int x = 0;
        int z = 0;
        int y = 0;
        World world = player.getWorld();
        if (world.getName().equalsIgnoreCase("world_nether"))
        {
        	y = PTNormal.randomNumber(59, 55);
        	x = PTNormal.randomNumber(-100, 97);
        	z = PTNormal.randomNumber(123, -83);
        	Location randomLocation = new Location(player.getWorld(), x, y, z);
        	y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
            randomLocation.setY(y);
        	return randomLocation;
        }
        return null;
    }
 
    public static Location findSafeLocation (Player player)
    {
 
        Location randomLocation = GenerateLocation(player);
 
        while (!isLocationSafe(randomLocation))
        {
            randomLocation = GenerateLocation(player);
        }
        return randomLocation;
    }
 
    public static boolean isLocationSafe (Location location)
    {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);
        
        return !(bad_blocks.contains(below.getType())) || (block.getType().isSolid()) || (above.getType().isSolid());
    }
}
