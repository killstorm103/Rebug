package me.killstorm103.Rebug.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class EventWeather implements Listener
{
	@EventHandler
	public void onWeather (WeatherChangeEvent e)
	{
		e.setCancelled(true);
	}
}
