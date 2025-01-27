package me.killstorm103.Rebug.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;
import me.killstorm103.Rebug.Utils.User;

public class EventUpdateScore extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();

	@Override
	public HandlerList getHandlers () {
		return HANDLERS;
	}
	@Getter
	private final Player player;
	@Getter @Setter
	private String[] text;
	@Getter @Setter
	private int[] score;
	
	public EventUpdateScore (@NotNull Object object, @NotNull String[] text, int[] score) 
	{
		if (object instanceof Player)
			this.player = (Player) object;
		else
			this.player = ((User) object).getPlayer();
		
		this.text = text;
		this.score = score;
	}
}
