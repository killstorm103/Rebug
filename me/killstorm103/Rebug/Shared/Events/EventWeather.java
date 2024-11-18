package me.killstorm103.Rebug.Shared.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import me.killstorm103.Rebug.Shared.Main.Config;
import me.killstorm103.Rebug.Shared.Main.Rebug;

public class EventWeather implements Listener
{
	@EventHandler
	public void onWeather(WeatherChangeEvent e)
	{
		if (!Rebug.getINSTANCE().getConfig().getBoolean("events.bukkit.WeatherChangeEvent")) return;
		
		e.setCancelled(Config.ShouldCancelWeatherChanges());
	}
}
