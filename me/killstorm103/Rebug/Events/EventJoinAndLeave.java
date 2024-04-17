package me.killstorm103.Rebug.Events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
		if (Rebug.getGetMain().getConfig().getBoolean("force-gamemode-on-join") && !player.isOp())
			player.setGameMode(GameMode.SURVIVAL);
		
		final String message = Rebug.getGetMain().getConfig().getString("join-message");
		if (message != null && message.length() > 0 && player.isOp())
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%player%", player.getName())));
	} 
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit (PlayerQuitEvent e) 
	{
		Player player = e.getPlayer();
		User user = Rebug.getUser(player);
		if (user != null)
		{
			user.removeJoinTime(player.getUniqueId());
			Rebug.USERS.remove(player.getUniqueId(), user);
		}
	}
}
