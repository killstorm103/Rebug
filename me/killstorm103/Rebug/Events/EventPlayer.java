package me.killstorm103.Rebug.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class EventPlayer implements Listener
{
	@EventHandler
	public void onPlayerDeath (PlayerDeathEvent e)
	{
		Player player = e.getEntity();
		User user = Rebug.getUser(player);
		if (user != null)
			user.setDeath_location(player.getLocation());
	}
}
