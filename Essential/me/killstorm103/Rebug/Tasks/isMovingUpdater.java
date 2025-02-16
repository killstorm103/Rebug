package me.killstorm103.Rebug.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Utils.User;

public class isMovingUpdater implements Runnable
{
	private static final isMovingUpdater task = new isMovingUpdater();
	public isMovingUpdater () {}

	public static isMovingUpdater getMainTask () 
	{
		return task;
	}

	@Override
	public void run ()
	{
		if (Bukkit.getOnlinePlayers().isEmpty())
			return;
		
		long current = System.currentTimeMillis();
		for (Player player : Bukkit.getOnlinePlayers()) 
		{
			User user = RebugPlugin.getUser(player);
			if (user != null)
			{
				long last = user.getTimeMove(), diff = current - last;
		        user.setMoving(diff < 1000);
			}
		}
	}
}
