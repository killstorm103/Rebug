package me.killstorm103.Rebug.Events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class EventPlayerSpawnLocation implements Listener
{
	 @EventHandler
	 public void onPlayerSpawn(PlayerSpawnLocationEvent e)
	 {
		 e.setSpawnLocation(new Location(e.getPlayer().getWorld(), 41, 58, 319));
	 }
}
