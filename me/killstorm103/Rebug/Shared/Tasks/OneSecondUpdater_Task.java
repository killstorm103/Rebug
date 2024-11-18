package me.killstorm103.Rebug.Shared.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Utils.User;

public class OneSecondUpdater_Task implements Runnable {
	private static final OneSecondUpdater_Task task = new OneSecondUpdater_Task();

	public OneSecondUpdater_Task() {
	}

	public static OneSecondUpdater_Task getMainTask() {
		return task;
	}

	@Override
	public void run ()
	{
		if (Bukkit.getOnlinePlayers().isEmpty())
			return;

		for (Player player : Bukkit.getOnlinePlayers()) 
		{
			User user = Rebug.getUser(player);
			if (user != null)
			{
				user.ClicksPerSecondLeft = user.preCPSLeft;
				user.ClicksPerSecondRight = user.preCPSRight;
				user.sendPacketCounts = user.preSend;
				user.receivePacketCounts = user.preReceive;
				Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "PPS " + ChatColor.WHITE + user.sendPacketCounts + "/in " + user.receivePacketCounts + "/out", 5, "pps");
				Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "CPS " + ChatColor.WHITE + user.ClicksPerSecondLeft + " : " + user.ClicksPerSecondRight, 8, "cps");
				user.preSend = user.preReceive = user.preCPSLeft = user.preCPSRight = 0;
				if (Bukkit.getOnlinePlayers().size() > 1) 
				{
					for (Player target : Bukkit.getOnlinePlayers())
					{
						if (user.getPlayer() != target) 
						{
							if (user.HideOnlinePlayers || user.ProximityPlayerHider && user.getPlayer().getLocation().distance(target.getLocation()) <= Rebug.getINSTANCE().getConfig().getDouble("proximity-player-hider"))
							{
								if (user.getPlayer().canSee(target))
									user.getPlayer().hidePlayer(target);
							} 
							else
							{
								if (!user.getPlayer().canSee(target))
									user.getPlayer().showPlayer(target);
							}
						}
					}
				}
			}
		}
	}
}