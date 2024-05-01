package me.killstorm103.Rebug.Events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.*;

public class EventJoinAndLeave implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin (PlayerJoinEvent e) 
	{
		Player player = e.getPlayer();
		Rebug.USERS.put(player.getUniqueId(), new User(player));
		User user = Rebug.getUser(player);
		user.setJoinTime(user.getPlayer().getUniqueId(), System.currentTimeMillis());
		if (Config.ShouldForceGameMode() && !user.getPlayer().isOp())
			user.getPlayer().setGameMode(GameMode.SURVIVAL);
		
		final String message = Rebug.getGetMain().getConfig().getString("join-message");
		if (message != null && message.length() > 0 && user.getPlayer().isOp())
			user.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%player%", user.getPlayer().getName())));
	} 
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit (PlayerQuitEvent e) 
	{
		Player player = e.getPlayer();
		User user = Rebug.getUser(player);
		if (user != null)
		{
			user.removeJoinTime(user.getPlayer().getUniqueId());
			Rebug.USERS.remove(user.getPlayer().getUniqueId(), user);
		}
	}
}
