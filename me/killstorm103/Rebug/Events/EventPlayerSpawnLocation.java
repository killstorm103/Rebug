package me.killstorm103.Rebug.Events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class EventHandlePlayerSpawn implements Listener
{
	
	 @EventHandler
	 public void onPlayerSpawn(PlayerSpawnLocationEvent e)
	 {
		 if (!e.getPlayer().isOp())
			 e.setSpawnLocation(new Location(e.getPlayer().getWorld(), 41, 58, 319, -91.200165F, -0.5999501F));
	 }
	 @EventHandler
	 public void onRespawn (PlayerRespawnEvent e)
	 {
		 e.setRespawnLocation(new Location(e.getPlayer().getWorld(), 41, 58, 319, -91.200165F, -0.5999501F));
	 }
}
