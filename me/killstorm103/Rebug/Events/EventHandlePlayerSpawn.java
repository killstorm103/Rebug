package me.killstorm103.Rebug.Events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import me.killstorm103.Rebug.Main.Rebug;

public class EventHandlePlayerSpawn implements Listener
{
	
	 @EventHandler
	 public void onPlayerSpawn(PlayerSpawnLocationEvent e)
	 {
		 if (Rebug.getGetMain().Debug())
			 e.getPlayer().sendMessage(e.getPlayer().getWorld().getName());
		 
		 if (!e.getPlayer().isOp())
			 e.setSpawnLocation(new Location(Rebug.getGetMain().getServer().getWorld("world"), 41, 58, 319, -91.200165F, -0.5999501F));
	 }
	 @EventHandler
	 public void onRespawn (PlayerRespawnEvent e)
	 {
		 if (Rebug.getGetMain().Debug())
			 e.getPlayer().sendMessage(e.getPlayer().getWorld().getName());
		 
		 e.setRespawnLocation(new Location(Rebug.getGetMain().getServer().getWorld("world"), 41, 58, 319, -91.200165F, -0.5999501F));
	 }
}
