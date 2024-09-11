package me.killstorm103.Rebug.Events;


import java.util.UUID;

import org.apache.commons.lang.StringUtils;
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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PTNormal;
import me.killstorm103.Rebug.Utils.TeleportUtils;
import me.killstorm103.Rebug.Utils.User;

public class EventPlayer implements Listener
{
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onProjectileHit (ProjectileHitEvent e)
	{
		if (e.getEntityType() == EntityType.ARROW && e.getEntity().getShooter() instanceof Player)
		{
			User user = Rebug.getUser((Player) e.getEntity().getShooter());
			if (user == null || user.ShouldTeleportByBow < 1) return;
			
			Location loc = e.getEntity().getLocation();
			user.getPlayer().teleport(new Location(user.getWorld(), loc.getX(), loc.getY(), loc.getZ(), user.getLocation().getYaw(), user.getLocation().getPitch()));
			e.getEntity().remove();
			user.ShouldTeleportByBow = user.ShouldTeleportByBow < 0 ? 0 : user.ShouldTeleportByBow --;
		}
	}
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onChat (AsyncPlayerChatEvent e)
	{
		Player player = e.getPlayer();
		if (!Rebug.hasAdminPerms(player) && !player.hasPermission("me.killstorm103.rebug.user.chat") && Config.Experimental_Features("ChatPerm"))
		{
			e.setCancelled(true);
			return;
		}
		String message = ChatColor.stripColor(e.getMessage());
		if (Bukkit.getOnlinePlayers().size() > 1)
		{
			message = message.trim();
			String[] inChat = StringUtils.split(message);
			for (int i = 0; i < inChat.length; i ++)
			{
				User used = Rebug.getUser(Bukkit.getPlayer(inChat[i]));
				if (used != null && !used.AllowMentions && used.getPlayer() != player && e.getRecipients().contains(used.getPlayer()))
				{
					player.sendMessage(Rebug.RebugMessage + used.getName() + " Has Mentions Disabled (they won't see your message)!");
					e.getRecipients().remove(used.getPlayer());
					// using this would cause a bug where if the player mentions more than one player they wouldn't be able to stop themselfs from getting the message!
					//break;
				}
			}
		}
		if (!Rebug.lockedList.isEmpty())
		{
			for (UUID uid : Rebug.lockedList) 
            {
                Player p = Bukkit.getPlayer(uid);
                if (p != null && p != player && p.isOnline())
                	e.getRecipients().remove(p);
            }
		}
		User user = Rebug.getUser(player);
		if (user == null) return;
		
		if (user.ClientCommandChecker && Rebug.lockedList.contains(user.getUUID()))
		{
			if (message.equals(user.Keycard))
			{
				Rebug.lockedList.remove(player.getUniqueId());
				user.sendMessage(ChatColor.GREEN + "Successfully passed Client Command Checker!");
			}
			e.setCancelled(true);
			return;
		}
		
		user.Yapper_Message_Count ++;
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	// TODO Fix teleport bow's arrows not being removed if the player dcs/gets kicked/banned!
	public void onShot (EntityShootBowEvent e)
	{
		if (e.getEntity() instanceof HumanEntity)
		{
			ItemStack Bow = e.getBow();
			if (!Bow.hasItemMeta() || !Bow.getItemMeta().hasDisplayName() || !Bow.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Teleport Bow")) return;
			
			User user = Rebug.getUser(PTNormal.getPlayerFromHumanEntity(e.getEntity()));
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
		if (!isAllowedInArea(player, e.getTo(), null, false))
		{
			player.teleport(e.getFrom());
			return;
		}
		User user = Rebug.getUser(player);
		if (user != null)
		{
			user.lastTickPosX = user.getLocation().getX();
			user.lastTickPosY = user.getLocation().getY();
			user.lastTickPosZ = user.getLocation().getZ();
		}
	}
	private boolean isAllowedInArea (Player player, Location GoingTo, User user, boolean BuildCheck) 
	{
		String Name = player.getWorld().getName();
		if (Rebug.hasAdminPerms(player) || user != null && user.Builder)
		{
			return true;
		}
		if (BuildCheck && user == null)
		{
			user = Rebug.getUser(player);
			if (user != null && user.Builder)
				return true;
		}

		if (Name.contains("nether") && (GoingTo.getY() < 55 || GoingTo.getY() > 120 || GoingTo.getX() < nether_minX || GoingTo.getY() < nether_minY || GoingTo.getZ() < nether_minZ || GoingTo.getX() > nether_maxX || GoingTo.getY() > nether_maxY || GoingTo.getZ() > nether_maxZ))
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
		
		if (!Rebug.hasAdminPerms(user.getPlayer()))
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
			user.sendMessage("Use /back to teleport to where you died!");
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
			if (user == null) return;
			
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
