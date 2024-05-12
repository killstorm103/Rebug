package me.killstorm103.Rebug.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventCustomEventTest extends Event
{

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
