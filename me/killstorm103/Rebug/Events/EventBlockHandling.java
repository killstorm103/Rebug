package me.killstorm103.Rebug.Events;


import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;


public class EventBlockHandling implements Listener
{
	//private final String permission_toBlock = "me.killstorm103.rebug.handleblock";
	private double 
	
	minX = 3.0, minY = 52, minZ = 222,
	
	maxX = 61, maxY = 256, maxZ = 302;
	
	private boolean NotAllowed (Location loc)
	{
		if (loc.getBlockX() >= minX && loc.getBlockY() >= minY && loc.getBlockZ() >= minZ && loc.getBlockX() <= maxX && loc.getBlockY() <= maxY && loc.getBlockZ() <= maxZ)
			return true;
		
		return false;
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onBreakBlock (BlockBreakEvent e)
	{
		Player player = e.getPlayer();
		Location location = e.getBlock().getLocation();
		if (!NotAllowed(location) && !Rebug.hasAdminPerms(player))
		{
			e.setCancelled(true);
		}
		User user = Rebug.getUser(player);
		if (!e.isCancelled() && user != null && user.getPlayer() != null && user.BlockPlaced.containsKey(e.getBlock().getLocation()))
		{
			user.BlockPlaced.remove(e.getBlock(), e.getBlock().getLocation());
		}
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onInteract (PlayerInteractEvent e) 
	{
		Player player = e.getPlayer();
		World world = player.getWorld();
		Block block = e.getClickedBlock();
		if (block != null && block.getType() == Material.STONE_BUTTON)
		{
			Location loc = block.getLocation(), PlaceAT = new Location(world, loc.getBlockX(), 58, 225);
			if ((loc.getBlockX() == 310 || loc.getBlockX() == 315 || loc.getBlockX() == 320 || loc.getBlockX() == 325 || loc.getBlockX() == 330) && loc.getBlockY() == 58 && loc.getBlockZ() == 226)
			{
				Entity tnt = block.getLocation().getWorld().spawn(PlaceAT, TNTPrimed.class);
				tnt.setCustomNameVisible(true);
				((TNTPrimed) tnt).setFuseTicks(PlaceAT.getBlockX() == 310 ? 80 : PlaceAT.getBlockX() == 315 ? 60 : PlaceAT.getBlockX() == 320 ? 40 : PlaceAT.getBlockX() == 325 ? 20 : 0);
				new BukkitRunnable() {

	                @Override
	                public void run()
	                {
	                	  if (((TNTPrimed) tnt).getFuseTicks() <= 0) 
	                          cancel();
	                	  
	                    tnt.setCustomName(DECIMAL_FORMAT.format(((TNTPrimed) tnt).getFuseTicks() / 20.0));
	                }
	            }.runTaskTimer(Rebug.getINSTANCE(), 0, 1);
			}
		}
	}
	
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlaceBlock (BlockPlaceEvent e)
	{
		Player player = e.getPlayer();
		User user = Rebug.getUser(player);
		Block block = e.getBlockPlaced();
		Location location = block.getLocation();
		ItemStack item = e.getPlayer().getItemInHand().clone();
		if (item != null)
		{
			if (user.Infinite_Blocks &&
					(user.getPlayer().hasPermission("me.killstorm103.rebug.user.infinite_blocks") || Rebug.hasAdminPerms(user.getPlayer())) && user.getPlayer().getGameMode() != GameMode.CREATIVE)
						user.getPlayer().setItemInHand(item);
			
			if (block != null && block.getType().equals(Material.TNT) && item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&
			item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Special TNT"))
			{
				Location loc = block.getLocation();
				if (loc.getWorld().getName().equalsIgnoreCase("world_nether"))
				{
					e.setCancelled(true);
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You can't use that here!");
					return;
				}
				Entity tnt = block.getLocation().getWorld().spawn(loc, TNTPrimed.class);
				tnt.setCustomNameVisible(true);
				((TNTPrimed) tnt).setFuseTicks(71);
				new BukkitRunnable() {

	                @Override
	                public void run()
	                {
	                	  if (((TNTPrimed) tnt).getFuseTicks() <= 0) 
	                          cancel();
	                	  
	                    tnt.setCustomName(DECIMAL_FORMAT.format(((TNTPrimed) tnt).getFuseTicks() / 20.0));
	                }
	            }.runTaskTimer(Rebug.getINSTANCE(), 0, 1);
	            
				loc.getBlock().setType(Material.AIR);
				return;
			}
		}
		if (!NotAllowed(location) && !Rebug.hasAdminPerms(player))
		{
			e.setCancelled(true);
		}
		if (!e.isCancelled() && user != null && user.getPlayer() != null && !user.BlockPlaced.containsKey(e.getBlockPlaced().getLocation()))
		{
			user.BlockPlaced.put(e.getBlockPlaced().getLocation(), e.getBlockPlaced());
		}
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onExplosion (EntityExplodeEvent e)
	{
		e.blockList().clear();
	}
}
