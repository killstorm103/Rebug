package me.killstorm103.Rebug.Events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import me.killstorm103.Rebug.Main.Rebug;



public class EventJoinAndLeave implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin (PlayerJoinEvent e) 
	{
		if (!e.getPlayer().isOp())
			e.getPlayer().setGameMode(GameMode.SURVIVAL);
		
		String message = Rebug.getGetMain().getConfig().getString("join-message").replace("%player%", e.getPlayer().getName());
		if (message != null && message.length() > 0 && e.getPlayer().isOp())
			e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit (PlayerQuitEvent e) {}
}
