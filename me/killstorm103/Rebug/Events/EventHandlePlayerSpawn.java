package me.killstorm103.Rebug.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class EventHandlePlayerSpawn implements Listener
{
	
	 @EventHandler
	 public void onPlayerSpawn(PlayerSpawnLocationEvent e)
	 {
		 if (Rebug.debug)
			 Rebug.Debug(e.getPlayer(), e.getPlayer().getWorld().getName());
		 
		 if (!e.getPlayer().hasPlayedBefore())
			 e.setSpawnLocation(new Location(Bukkit.getServer().getWorld("world"), 41, 58, 319, -91.200165F, -0.5999501F));
	 }
	 @EventHandler
	 public void onRespawn (PlayerRespawnEvent e)
	 {
		 Player player = e.getPlayer();
		 User user = Rebug.getUser(player);
		 if (user != null)
		 {
			 user.removeJoinTime(player.getUniqueId());
			 user.setJoinTime(player.getUniqueId(), System.currentTimeMillis());
		 }
		 if (Rebug.debug)
			 Rebug.Debug(player, player.getWorld().getName());
		 
		 e.setRespawnLocation(new Location(Bukkit.getServer().getWorld("world"), 41, 58, 319, -91.200165F, -0.5999501F));
	 }
}
