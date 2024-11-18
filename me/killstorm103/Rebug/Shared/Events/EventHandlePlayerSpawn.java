package me.killstorm103.Rebug.Shared.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.SoftWares.Spigot.Utils.PT_Spigot;

public class EventHandlePlayerSpawn implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent e) 
	{
		if (!Rebug.getINSTANCE().getConfig().getBoolean("events.bukkit.PlayerRespawnEvent")) return;
		
		Player player = e.getPlayer();
		if (Rebug.debug)
			Rebug.Debug(player, player.getWorld().getName());

		e.setRespawnLocation(
				Rebug.getINSTANCE().getConfig().getBoolean("world-spawn.use-this") ? PT_Spigot.INSTANCE.getSpawn()
						: (player.getBedSpawnLocation() != null ? player.getBedSpawnLocation()
								: player.getWorld().getSpawnLocation()));
	}
}
