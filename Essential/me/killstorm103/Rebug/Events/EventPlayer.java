package me.killstorm103.Rebug.Events;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;

import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Utils.FileUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.Scheduler;
import me.killstorm103.Rebug.Utils.ServerVersionUtil;
import me.killstorm103.Rebug.Utils.User;
import me.killstorm103.Rebug.Utils.Menu.Old.ItemsAndMenusUtils;

@SuppressWarnings("deprecation")
public class EventPlayer implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent e) 
	{
		Player player = e.getPlayer();
		User user = RebugPlugin.getUser(player);
		if (user == null) return;
		
		user.setTimeMove(System.currentTimeMillis());
		user.setLastTickPosX(user.getPlayer().getLocation().getX());
		user.setLastTickPosY(user.getPlayer().getLocation().getY());
		user.setLastTickPosZ(user.getPlayer().getLocation().getZ());
		if (!user.getSettingsBooleans().getOrDefault("Double Jump", false) || !user.getSettingsBooleans().getOrDefault("Modifi AllowFlight", true)) return;
		
		if (((Entity) user.getPlayer()).isOnGround())
		{
			long timeElapsed = System.currentTimeMillis() - LastJumped.getOrDefault(user.getUUID(), 0L);
			if (RebugPlugin.getSettingsList().isEmpty()) ItemsAndMenusUtils.getINSTANCE().getRebugSettings();
			
			double cooldown = RebugPlugin.getSettingsDoubles().getOrDefault("Double Jump Cooldown", 0D) * 1000;
			if (timeElapsed < cooldown)
				return;
			
			user.getPlayer().setAllowFlight(true);
		}
	}
	private final HashMap<UUID, Long> LastJumped = new HashMap<>();

	@SuppressWarnings("unused")
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onChat (AsyncPlayerChatEvent e) 
	{
		Player player = e.getPlayer();
		if (!PT.lockedList.isEmpty())
		{
			for (UUID uid : PT.lockedList)
			{
				Player p = Bukkit.getPlayer(uid);
				if (p != null && p != player && p.isOnline())
					e.getRecipients().remove(p);
			}
		}
		User user = RebugPlugin.getUser(player);
		if (user == null)
			return;
		
		String message = ChatColor.stripColor(e.getMessage()), commandedMessage = ChatColor.stripColor(e.getMessage().replace(user.getName(), "").replace("tellraw", "").trim());

		if (!commandedMessage.equals(user.Keycard) && PT.lockedList.contains(user.getUUID()))
		{
			if (ServerVersionUtil.isServerNewerOrEquals1_19())
				PT.sendTitle(user, ChatColor.RED + "Pass the Client Command Checker", ChatColor.WHITE.toString()  + "To talk in chat!", 1, 40, 1);
			else
				PT.sendChat(user, ChatColor.RED + "Pass the Client Command Checker " + ChatColor.WHITE.toString()  + " to talk in chat!", ChatTypes.GAME_INFO);
			
			e.setCancelled(true);
			return;
		}
		if (commandedMessage.equals(user.Keycard))
		{
			if (user.getSettingsBooleans().getOrDefault("Client Command Checker", false))
			{
				if (!PT.lockedList.contains(user.getUUID()))
					user.sendMessage(ChatColor.translateAlternateColorCodes('&', FileUtils.getPlayerSettingsConfig().getString("Client Command Checker.component.already-passed-message", "You already passed or failed this check, try again by doing /rebug clientcommandchecker check!")));
				else
				{
					PT.lockedList.remove(player.getUniqueId());
					user.sendMessage(ChatColor.translateAlternateColorCodes('&', FileUtils.getPlayerSettingsConfig().getString("Client Command Checker.component.successfully-passed-message", ChatColor.GREEN + "Successfully passed Client Command Checker!")));
				}
			}
			e.setCancelled(true);
			return;
		}
	}
	@EventHandler (priority =  EventPriority.HIGHEST)
	public void onFlyToggle (PlayerToggleFlightEvent e)
	{
		Player player = e.getPlayer();
		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
		
		if (!player.isFlying())
		{
			User user = RebugPlugin.getUser(player);
			if (user == null || !user.getSettingsBooleans().getOrDefault("Modifi AllowFlight", true)) return;
			
			e.setCancelled(true);
			player.setAllowFlight(false);
			if (!user.getSettingsBooleans().getOrDefault("Double Jump", false))
				return;
			
			long timeElapsed = System.currentTimeMillis() - LastJumped.getOrDefault(user.getUUID(), 0L);
			double cooldown = RebugPlugin.getSettingsDoubles().getOrDefault("Double Jump Cooldown", 0D) * 1000;
			if (timeElapsed < cooldown)
				return;
			
			LastJumped.put(user.getUUID(), System.currentTimeMillis());
			user.getPlayer().setVelocity(player.getLocation().getDirection().setY(user.getSettingsDoubles().getOrDefault("Double Jump Height", .5)));
			if (cooldown > 0)
				user.sendMessage("You must wait " + RebugPlugin.getSettingsDoubles().get("Double Jump Cooldown") + " seconds before you can Double Jump Again!");
		}
	}
	//@EventHandler (priority = EventPriority.HIGHEST)
//	public void onInteract (PlayerInteractEvent e) {}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlaceBlock(BlockPlaceEvent e) 
	{
		User user = RebugPlugin.getUser(e.getPlayer());
		if (user == null || user.getPlayer().getGameMode() != GameMode.SURVIVAL && user.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
		
 		World world = user.getPlayer().getWorld();
		Block block = e.getBlockPlaced();
		Location location = block.getLocation();
		ItemStack item = 
		ServerVersionUtil.isServerNewerThan1_8() ? e.getHand() == EquipmentSlot.HAND ? user.getPlayer().getInventory().getItemInMainHand().clone() : e.getHand() == EquipmentSlot.OFF_HAND ? 
		user.getPlayer().getInventory().getItemInOffHand().clone() : null : user.getPlayer().getItemInHand().clone();
		
		if (user.getSettingsBooleans().getOrDefault("Infinite Blocks", false))
		{
			if (ServerVersionUtil.isServerNewerThan1_8())
			{
				switch (e.getHand())
				{
				case HAND:
					user.getPlayer().getInventory().setItemInMainHand(item);
					break;
					
				case OFF_HAND:
					user.getPlayer().getInventory().setItemInOffHand(item);
					break;
					
				default:
					break;
				}
			}
			else
				user.getPlayer().setItemInHand(item);
		}
		if (!RebugPlugin.getSettingsBooleans().getOrDefault("Auto Clear Scaffold After Place", false)) return;
		
		if (user.getSettingsBooleans().getOrDefault("Auto Refill Blocks", false))
		{
			int minX = FileUtils.getPlayerSettingsConfig().getInt("Auto Refill Blocks.area.minX", 10),
			minY = FileUtils.getPlayerSettingsConfig().getInt("Auto Refill Blocks.area.minY", 60),
			minZ = FileUtils.getPlayerSettingsConfig().getInt("Auto Refill Blocks.area.minZ", 50),
			
			maxX = FileUtils.getPlayerSettingsConfig().getInt("Auto Refill Blocks.area.maxX", 50),
			maxY = FileUtils.getPlayerSettingsConfig().getInt("Auto Refill Blocks.area.maxY", 100),
			maxZ = FileUtils.getPlayerSettingsConfig().getInt("Auto Refill Blocks.area.maxZ", 100);
			boolean zone = (location.getBlockX() >= Math.min(minX, maxX) && location.getBlockX() <= Math.max(minX, maxX)) && (location.getBlockY() >= Math.min(minY, maxY) && location.getBlockY() <= Math.max(minY, maxY)) && (location.getBlockZ() >= Math.min(minZ, maxZ) && location.getBlockZ() <= Math.max(minZ, maxZ));

			if (FileUtils.getPlayerSettingsConfig().getBoolean("Auto Refill Blocks.locked-to-area", false) && !zone || FileUtils.getPlayerSettingsConfig().getBoolean("Auto Refill Blocks.locked-to-world", false) && world.getName().equalsIgnoreCase(FileUtils.getPlayerSettingsConfig().getString("Auto Refill Blocks.area.world-name", "world")))
			{
				if (FileUtils.getPlayerSettingsConfig().getBoolean("Auto Refill Blocks.cancel", false))
					e.setCancelled(true);
				
				return;
			}
			Scheduler.runLater(new Runnable() 
			{
				@Override
				public void run() 
				{
					world.getBlockAt(location).setType(Material.AIR);
					if (item == null || user.getSettingsBooleans().getOrDefault("Infinite Blocks", false) || !user.getSettingsBooleans().getOrDefault("Auto Refill Blocks", false))
						return;
					
					item.setAmount(1);
					user.getPlayer().getInventory().addItem(item);
				}
			}, FileUtils.getPlayerSettingsConfig().getLong("Auto Refill Blocks.ticks", 200));
		}
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent e) 
	{
		if (e.getEntityType() != EntityType.PLAYER) return;
		
		User user = RebugPlugin.getUser (e.getEntity());
		if (user != null) 
		{
			user.setDeath_location(user.getPlayer().getLocation());
			user.sendMessage("Use /rebug back or /back to teleport to where you died!");
		}
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onHunger(FoodLevelChangeEvent e) 
	{
		if (e.getEntityType() != EntityType.PLAYER) return;
		
		User user = RebugPlugin.getUser((Player) e.getEntity());
		if (user != null && e.getFoodLevel() < user.getPlayer().getFoodLevel() && !user.getSettingsBooleans().getOrDefault("Hunger", true))
			e.setCancelled(true);
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageEvent e)
	{
		if (e.getEntityType() != EntityType.PLAYER) return;
		
		User user = RebugPlugin.getUser((Player) e.getEntity());
		if (user == null)
			return;

		if (e.getCause() == DamageCause.FALL && !user.getSettingsBooleans().getOrDefault("Fall Damage", true)
				|| e.getCause() != DamageCause.FALL && !user.getSettingsBooleans().getOrDefault("Exterranl Damage", true))
			e.setCancelled(true);
	}
}
