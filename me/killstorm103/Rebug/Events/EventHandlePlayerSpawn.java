package me.killstorm103.Rebug.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.killstorm103.Rebug.Main.Rebug;

public class EventHandlePlayerSpawn implements Listener
{
	
	/*
	 @EventHandler (priority = EventPriority.HIGHEST)
	 public void onPlayerSpawn(PlayerSpawnLocationEvent e)
	 {
		 Player player = e.getPlayer();
		 if (Rebug.debug)
			 Rebug.Debug(player, player.getWorld().getName());
		 
	//	 if (!e.getPlayer().hasPlayedBefore())
		 e.setSpawnLocation(new Location(Bukkit.getServer().getWorld("world"), 41, 58, 319, -91.200165F, -0.5999501F));
	 }
	 */
	 @EventHandler 
	 public void onRespawn (PlayerRespawnEvent e)
	 {
		 Player player = e.getPlayer();
		 if (Rebug.debug)
			 Rebug.Debug(player, player.getWorld().getName());
		 
		 e.setRespawnLocation(new Location(Bukkit.getServer().getWorld("world"), 41, 58, 319, -91.200165F, -0.5999501F));
	 }
}
