package me.killstorm103.Rebug.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Events.EventUpdateScore;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

@SuppressWarnings("deprecation")
public class OneSecondUpdater implements Runnable
{
	private static final OneSecondUpdater task = new OneSecondUpdater();

	public OneSecondUpdater() {
	}

	public static OneSecondUpdater getMainTask() {
		return task;
	}

	@Override
	public void run ()
	{
		if (Bukkit.getOnlinePlayers().isEmpty())
			return;
		
		for (Player player : Bukkit.getOnlinePlayers()) 
		{
			User user = RebugPlugin.getUser(player);
			if (user != null)
			{
				user.setClicksPerSecondLeft(user.getPreCPSLeft());
				user.setClicksPerSecondRight(user.getPreCPSRight());
				user.setSendPacketCounts(user.getPreSend());
				user.setReceivePacketCounts(user.getPreReceive());
				PT.CallEvent(new EventUpdateScore(user.getPlayer(), new String[] {ChatColor.DARK_RED + "PPS " + ChatColor.WHITE + user.getSendPacketCounts() + "/in " + user.getReceivePacketCounts() + "/out", ChatColor.DARK_RED + "CPS " + ChatColor.WHITE + user.getClicksPerSecondLeft() + " : " + user.getClicksPerSecondRight()
				}, new int[] {5, 8}));
				PT.UpdateScoreBoard(user, new String[] {ChatColor.DARK_RED + "PPS " + ChatColor.WHITE + user.getSendPacketCounts() + "/in " + user.getReceivePacketCounts() + "/out", ChatColor.DARK_RED + "CPS " + ChatColor.WHITE + user.getClicksPerSecondLeft() + " : " + user.getClicksPerSecondRight()}
				, new int[] {5, 8}, new String[] {"pps", "cps"});
				user.setPreSend(0);
				user.setPreReceive(0);
				user.setPreCPSLeft(0);
				user.setPreCPSRight(0);
				if (Bukkit.getOnlinePlayers().size() < 2) return;
				
				for (Player target : Bukkit.getOnlinePlayers()) 
				{
					if (target != user.getPlayer())
					{
						if (user.getSettingsBooleans().getOrDefault("Hide Online Players", false) || user.getSettingsBooleans().getOrDefault("Proximity Player Hider", false) && user.getPlayer().getLocation().distance(target.getLocation()) <= user.getSettingsDoubles().getOrDefault("Proximity Hider Distance", 6D))
							user.getPlayer().hidePlayer(target);
						else
							user.getPlayer().showPlayer(target);
					}
				}
			}
		}
	}
}
