package me.killstorm103.Rebug.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class EventBlockHandling implements Listener
{
	private final String permission_toBlock = "me.killstorm103.rebug.handleblock";
	private double 
	
	minX = 3.0, minY = 53, minZ = 222,
	
	maxX = 61, maxY = 95, maxZ = 302;
	
	private boolean NotAllowed (Location loc)
	{
		if (loc.getBlockX() >= minX && loc.getBlockY() >= minY && loc.getBlockZ() >= minZ && loc.getBlockX() <= maxX && loc.getBlockY() <= maxY && loc.getBlockZ() <= maxZ)
			return true;
		
		return false;
	}
	@EventHandler
	public void onBreakBlock (BlockBreakEvent e)
	{
		Player player = e.getPlayer();
		Location location = e.getBlock().getLocation();
		if (!NotAllowed(location) && (!player.isOp() || !player.hasPermission(permission_toBlock) || !player.hasPermission("me.killstorm103.rebug.*")))
		{
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlaceBlock (BlockPlaceEvent e)
	{
		Player player = e.getPlayer();
		Location location = e.getBlock().getLocation();
		if (!NotAllowed(location) && (!player.isOp() || !player.hasPermission(permission_toBlock) || !player.hasPermission("me.killstorm103.rebug.*")))
		{
			e.setCancelled(true);
		}
	}
}
