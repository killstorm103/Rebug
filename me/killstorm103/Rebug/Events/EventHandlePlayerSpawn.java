package me.killstorm103.Rebug.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PTNormal;

public class EventHandlePlayerSpawn implements Listener
{
	
	 @EventHandler (priority = EventPriority.HIGHEST)
	 public void onRespawn (PlayerRespawnEvent e)
	 {
		 Player player = e.getPlayer();
		 if (Rebug.debug)
			 Rebug.Debug(player, player.getWorld().getName());
		 
		 e.setRespawnLocation(Rebug.getINSTANCE().getConfig().getBoolean("world-spawn.use-this") ? PTNormal.INSTANCE.getSpawn() : (player.getBedSpawnLocation() != null ? player.getBedSpawnLocation() : player.getWorld().getSpawnLocation()));
	 }
}
