package me.killstorm103.Rebug.Events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;


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
	public void onOpening (PlayerInteractEvent e)
	{
		Block block = e.getClickedBlock();
		if (block != null && block.getType() == Material.SPRUCE_FENCE_GATE && !e.getPlayer().isOp())
		{
			if (block.getLocation().getBlockX() <= 59 && block.getLocation().getBlockY() == 58 && block.getLocation().getBlockZ() == 328 && block.getLocation().getBlockX() >= 58 && block.getLocation().getBlockY() == 58 && block.getLocation().getBlockZ() == 328)
				e.setCancelled(true);
		}
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
