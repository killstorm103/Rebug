package me.killstorm103.Rebug.Events;


import java.text.DecimalFormat;

import org.bukkit.Bukkit;
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

import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;


public class EventBlockHandling implements Listener
{
	public boolean isAllowedToBuild (Player player, World world, Location loc, User user, boolean BuilderCheck)
	{
		if (player.isOnline()) 
		{
			if (Rebug.hasAdminPerms(player) || user != null && user.Builder)
				return true;
			
			if (BuilderCheck && user == null)
			{
				user = Rebug.getUser(player);
				if (user != null && user.Builder) return true;
			}
			
		}
		
		if (world.getName().equalsIgnoreCase("world") &&
		(loc.getBlockX() >= Rebug.getINSTANCE().getConfig().getDouble("scaffold-test-area.minX") &&
	    loc.getBlockY() >= Rebug.getINSTANCE().getConfig().getDouble("scaffold-test-area.minY") &&
	    loc.getBlockZ() >= Rebug.getINSTANCE().getConfig().getDouble("scaffold-test-area.minZ") && loc.getBlockX() <= Rebug.getINSTANCE().getConfig().getDouble("scaffold-test-area.maxX") && loc.getBlockZ() <= Rebug.getINSTANCE().getConfig().getDouble("scaffold-test-area.maxZ")))
			return true;
		
		return false;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onBreakBlock (BlockBreakEvent e)
	{
		Player player = e.getPlayer();
		if (!isAllowedToBuild(player, e.getBlock().getWorld(), e.getBlock().getLocation(), null, true))
			e.setCancelled(true);
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onInteract (PlayerInteractEvent e) 
	{
		Player player = e.getPlayer();
		World world = player.getWorld();
		Block block = e.getClickedBlock();
		ItemStack item = e.getItem();
		if (item != null && item.getType() != Material.AIR)
		{
			if (item.getType() == Material.WATER_BUCKET && Config.Experimental_Features("nofall water bucket"))
			{
			}
		}
		
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
		if (user == null)
		{
			e.setCancelled(true);
			return;
		}
		
		Block block = e.getBlockPlaced();
		Location location = block.getLocation();
		ItemStack item = user.getPlayer().getItemInHand().clone();
		World world = e.getPlayer().getWorld();
		
		if (item != null)
		{
			if (Rebug.getINSTANCE().getConfig().getBoolean("infinite-blocks-enabled"))
			{
				if (user.Infinite_Blocks &&
						(user.getPlayer().hasPermission("me.killstorm103.rebug.user.infinite_blocks") || Rebug.hasAdminPerms(user.getPlayer())) && user.getPlayer().getGameMode() != GameMode.CREATIVE)
							user.getPlayer().setItemInHand(item);
			}
			if (Rebug.getINSTANCE().getConfig().getBoolean("special-tnt-enabled"))
			{
				if (block != null && block.getType().equals(Material.TNT) && item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&
						item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Special TNT"))
						{
							Location loc = block.getLocation();
							if (loc.getWorld().getName().equalsIgnoreCase("world_nether"))
							{
								user.sendMessage("You can't use that here!");
								e.setCancelled(true);
								return;
							}
							if (Rebug.getINSTANCE().getConfig().getBoolean("special-tnt-enabled"))
							{
								Entity tnt = block.getLocation().getWorld().spawn(loc, TNTPrimed.class);
								tnt.setCustomNameVisible(true);
								((TNTPrimed) tnt).setFuseTicks(Rebug.getINSTANCE().getConfig().getInt("special-tnt-fuseticks"));
								
								new BukkitRunnable() 
								{
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
							else
								user.sendMessage("Sorry but Special TNT is Disabled!");
							
						}
			}
		}
		if (!isAllowedToBuild(player, world, location, user, true))
		{
			e.setCancelled(true);
			return;
		}
		if (!Rebug.AutoRefillBlocks || Rebug.hasAdminPerms(user.getPlayer()) || user.Builder) return;
		
		Bukkit.getScheduler().runTaskLater(Rebug.getINSTANCE(), new Runnable() 
		{
			@Override
			public void run() 
			{
				world.getBlockAt(location).setType(Material.AIR);
				if (!user.Infinite_Blocks && user.AutoRefillBlocks && (user.getPlayer().getGameMode() == GameMode.ADVENTURE || user.getPlayer().getGameMode() == GameMode.SURVIVAL))
				{
					item.setAmount(1);
					user.getPlayer().getInventory().addItem(item);
				}
			}
		}, Rebug.getINSTANCE().getConfig().getLong("auto-refill-blocks-ticks"));
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onExplosion (EntityExplodeEvent e)
	{
		e.blockList().clear();
	}
}
