package me.killstorm103.Rebug.Tasks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Rebug;

public class ResetScaffoldTestArea implements Runnable
{
	private static final ResetScaffoldTestArea task = new ResetScaffoldTestArea();
	private final ArrayList<String> places = new ArrayList<>();
	public ResetScaffoldTestArea () 
	{
		int y = 52, upBy = 1, end = 255 - upBy;
		while (y < end)
		{
			places.add("3 " + y + " 222 61 " + (y + upBy) + " 302 air");
			y += upBy;
		}
		places.add("3 255 222 61 255 302 air");
	}

	public static ResetScaffoldTestArea getMainTask() 
	{
		return task;
	}
	
	@Override
	public void run() 
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			player.sendMessage(Rebug.RebugMessage + "Clearing the Scaffold Test Area!");
			player.sendMessage(Rebug.RebugMessage + "Clearing Lagg!");
		}
		
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lagg clear");
		for (int i = 0; i < places.size(); i ++)
		{
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fill " + places.get(i));
		}
	}
	
}
