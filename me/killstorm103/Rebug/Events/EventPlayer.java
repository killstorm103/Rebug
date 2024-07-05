package me.killstorm103.Rebug.Events;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.TeleportUtils;
import me.killstorm103.Rebug.Utils.User;

public class EventPlayer implements Listener
{
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onProjectileHit (ProjectileHitEvent e)
	{
		if (e.getEntityType() == EntityType.ARROW && e.getEntity().getShooter() instanceof Player)
		{
			User user = Rebug.getUser((Player) e.getEntity().getShooter());
			if (user == null || user.ShouldTeleportByBow < 1) return;
			
			
			Location loc = e.getEntity().getLocation();
			loc.setYaw(user.getPlayer().getLocation().getYaw());
			loc.setPitch(user.getPlayer().getLocation().getPitch());
			user.getPlayer().teleport(loc);
			e.getEntity().remove();
			user.ShouldTeleportByBow = user.ShouldTeleportByBow < 0 ? 0 : user.ShouldTeleportByBow --;
		}
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	// TODO Fix teleport bow's arrows not being removed if the player dcs/gets kicked/banned!
	public void onShot (EntityShootBowEvent e)
	{
		if (e.getEntity() instanceof HumanEntity)
		{
			ItemStack Bow = e.getBow();
			if (!Bow.hasItemMeta() || !Bow.getItemMeta().hasDisplayName() || !Bow.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Teleport Bow")) return;
			
			User user = Rebug.getUser(PT.getPlayerFromHumanEntity(e.getEntity()));
			if (user == null) return;
			
			
			user.ShouldTeleportByBow ++;
		}
	}
	private final double nether_minX =- 100, nether_minY = 54, nether_minZ =- 83, nether_maxX = 96, nether_maxY = 120, nether_maxZ = 122;
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onMove (PlayerMoveEvent e)
	{
		Player player = e.getPlayer();
		Location getTo = e.getTo();
		if (player.getWorld().getName().equalsIgnoreCase("world"))
		{
			if (getTo.getBlockX() >= 58 && getTo.getBlockX() <= 59 && getTo.getBlockZ() >= 328 && getTo.getBlockZ() <= 329 && getTo.getBlockY() > 57 && getTo.getBlockY() < 61)
			{
				player.teleport(new Location(Bukkit.getWorld("world_nether"), 21, 55, 49));
				return;
			}
		}
		if (!isAllowedInArea(player, e.getTo()))
		{
			player.teleport(e.getFrom());
			return;
		}
		
	}
	private boolean isAllowedInArea (Player player, Location GoingTo) 
	{
		if (!player.getWorld().getName().contains("nether") && Rebug.hasAdminPerms(player))
			return true;
		
		if (player.getWorld().getName().contains("nether") && (GoingTo.getY() < 55 || GoingTo.getY() > 120 || GoingTo.getX() < nether_minX || GoingTo.getY() < nether_minY || GoingTo.getZ() < nether_minZ || GoingTo.getX() > nether_maxX || GoingTo.getY() > nether_maxY || GoingTo.getZ() > nether_maxZ))
			return false;
			
		return true;
	}
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEnchantItem (EnchantItemEvent e)
	{
		Player player = e.getEnchanter();
		User user = Rebug.getUser(player);
		if (user == null)
		{
			e.setCancelled(true);
			return;
		}
		
		if (!user.getPlayer().isOp() && !user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") && !user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin"))
			e.setCancelled(true);
		else
			e.setExpLevelCost(0);
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerDeath (PlayerDeathEvent e)
	{
		User user = Rebug.getUser(e.getEntity());
		if (user != null)
		{
			user.death_location = user.getPlayer().getLocation();
			user.getPlayer().sendMessage(Rebug.RebugMessage + "Use /back to teleport to where you died!");
		}
	}
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onHunger (FoodLevelChangeEvent e)
	{
		User user = Rebug.getUser((Player) e.getEntity());
		if (user != null && e.getFoodLevel() < user.getPlayer().getFoodLevel() && !user.Hunger)
			e.setCancelled(true);
	}
	private final double 
	
	minX = 299, minZ = 222,
	
	maxX = 341, maxZ = 240;
	
	private boolean AllowedIn (String world, Location loc)
	{
		if (world.equalsIgnoreCase("world"))
		{
			if (loc.getBlockX() >= minX && loc.getBlockZ() >= minZ && loc.getBlockX() <= maxX && loc.getBlockZ() <= maxZ)
				return true;
		}
		
		return false;
	}
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onWorldChange (PlayerChangedWorldEvent e)
	{
		Player player = e.getPlayer();
		if (player.getWorld().getName().equalsIgnoreCase("world_nether"))
		{
			Location loc = TeleportUtils.findSafeLocation(player);
			if (loc != null)
				player.teleport(loc);
		}
	}
	/*
	@EventHandler
	public void onChat (AsyncPlayerChatEvent e)
	{
		Player player = e.getPlayer();
		String message = e.getMessage();
		player.sendMessage(Rebug.RebugMessage + "User= " + player.getName() + " msg= " + message);
	}
	*/
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onDamage (EntityDamageEvent e)
	{
		if (e.getEntity() instanceof Player)
		{
			Player player = (Player) e.getEntity();
			World world = player.getWorld();
			if (world.getName().equalsIgnoreCase("world_nether"))
			{
				if (e.getCause() == DamageCause.FALL)
					e.setCancelled(true);
				
				return;
			}
			User user = Rebug.getUser(player);
			if (user != null)
			{
				Rebug.Debug(user.getPlayer(), user.getDebugLocation() + " Cause= " + e.getCause().name());
				
				if (e.getCause() == DamageCause.ENTITY_EXPLOSION)
				{
					if (AllowedIn(world.getName(), user.getLocation())) {}
					else
						e.setDamage(e.getDamage(DamageModifier.ABSORPTION));
					
					return;
				}
				
				if (e.getCause() == DamageCause.FALL && !user.FallDamage || e.getCause() != DamageCause.FALL && !user.Exterranl_Damage)
					e.setCancelled(true);
				
			}
		}
	}
}
