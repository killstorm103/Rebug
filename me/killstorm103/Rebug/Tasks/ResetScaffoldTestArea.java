package me.killstorm103.Rebug.Tasks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Rebug;

public class ResetScaffoldTestArea implements Runnable
{
	private static final ResetScaffoldTestArea task = new ResetScaffoldTestArea();
	private final ArrayList<String> places = new ArrayList<>();
	public ResetScaffoldTestArea () {}

	public static ResetScaffoldTestArea getMainTask() 
	{
		return task;
	}
	
	@Override
	public void run () 
	{
		boolean clearlag = Rebug.getINSTANCE().getConfig().getBoolean("is-clear-lag-plugin-enabled"), clear_scaffold = Rebug.getINSTANCE().getConfig().getBoolean("scaffold-test-area.auto-clear");
		if (!clear_scaffold && !clearlag) return;
		
		if (!Bukkit.getOnlinePlayers().isEmpty() && (clearlag || clear_scaffold))
		{
			for (Player player : Bukkit.getOnlinePlayers())
			{
				if (clearlag)
					player.sendMessage(Rebug.RebugMessage + "Clearing Lagg!");
				
				if (clear_scaffold)
					player.sendMessage(Rebug.RebugMessage + "Clearing the Scaffold Test Area!");
			}
		}
		if (clearlag)
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Rebug.getINSTANCE().getConfig().getString("clear-lag-command"));
		
		if (clear_scaffold)
		{
			if (places.isEmpty())
			{
				double minX = Rebug.getINSTANCE().getConfig().getDouble("scaffold-test-area.minX"), minY = Rebug.getINSTANCE().getConfig().getDouble("scaffold-test-area.minY"), minZ =
						Rebug.getINSTANCE().getConfig().getDouble("scaffold-test-area.minZ"), maxX = Rebug.getINSTANCE().getConfig().getDouble("scaffold-test-area.maxX"), maxY = Rebug.getINSTANCE().getConfig().getDouble("scaffold-test-area.maxY"), maxZ = Rebug.getINSTANCE().getConfig().getDouble("scaffold-test-area.maxZ");
						final int upBy = 1;
						double y = minY, end = maxY - upBy;
						while (y < end)
						{
							places.add(minX + " " + y + " " + minZ + " " + maxX + " " + (y + upBy) + " " + maxZ + " air");
							y += upBy;
						}
						places.add(minX + " " + maxY + " " + minZ + " " + maxX + " " + maxY + " " + maxZ + " air");
			}
			if (places.isEmpty())
				Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "Places in Reset Scaffold Test Area was still Empty!");
			
			else
			{
				for (int i = 0; i < places.size(); i ++)
				{
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Rebug.getINSTANCE().getConfig().getString("scaffold-test-area.clear_blocks_command") + " " + places.get(i));
				}
			}
		}
		else
			places.clear();
		
	}
	
}
