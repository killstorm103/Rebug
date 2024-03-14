package me.killstorm103.Rebug.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class EventJoinAndLeave implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin (PlayerJoinEvent e) 
	{
		if (!e.getPlayer().isOp())
			e.getPlayer().setGameMode(GameMode.SURVIVAL);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit (PlayerQuitEvent e) {}
}
