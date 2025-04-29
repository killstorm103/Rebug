package me.killstorm103.Rebug.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;

public class EventWorld implements Listener
{
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onExplosion (EntityExplodeEvent e)
	{
		if (!RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("world.explosions.modifier", false)) return;
		
		if (!RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("world.explosions.break-blocks", true))
			e.blockList().clear();
	}
}
