package me.killstorm103.Rebug.Tasks;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;
import net.md_5.bungee.api.ChatColor;

public class OneSecondUpdater_Task implements Runnable
{
	private static final OneSecondUpdater_Task task = new OneSecondUpdater_Task();
	private final DecimalFormat former = new DecimalFormat("#.###");
	
	public OneSecondUpdater_Task () {}

	public static OneSecondUpdater_Task getMainTask() 
	{
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
				user.ClicksPerSecond = user.preCPS;
				user.sendPacketCounts = user.preSend;
				user.receivePacketCounts = user.preReceive;
				user.preSend = user.preReceive = user.preCPS = 0;
				final double distX = user.getLocation().getX() - user.lastTickPosX;
				final double distY = user.getLocation().getY() - user.lastTickPosY;
				final double distZ = user.getLocation().getZ() - user.lastTickPosZ;
				final double bpsXZ = Math.sqrt(distX * distX + distZ * distZ) * 20.0;  
				final double bpsY = Math.sqrt(distY * distY) * 20.0;  
				if (Config.RebugScoreBoard() && user.ScoreBoard != null)
				{
					user.ScoreBoard.set(ChatColor.DARK_RED + "PPS " + ChatColor.WHITE + user.sendPacketCounts + "/in " + user.receivePacketCounts + "/out", 4);
					user.ScoreBoard.set(ChatColor.DARK_RED + "BPS (Y) " + ChatColor.WHITE + former.format(bpsY), 5);
		   			user.ScoreBoard.set(ChatColor.DARK_RED + "BPS (XZ) " + ChatColor.WHITE + former.format(bpsXZ), 6);
		   			user.ScoreBoard.set(ChatColor.DARK_RED + "CPS " + ChatColor.WHITE + user.ClicksPerSecond, 7);
				}
				if (Bukkit.getOnlinePlayers().size() < 2) return;
				
				for (Player target : Bukkit.getOnlinePlayers())
				{
					if (user.getPlayer() != target)
					{
						if (user.HideOnlinePlayers || user.ProximityPlayerHider && user.getPlayer().getLocation().distance(target.getLocation()) <= Rebug.GetMain().getConfig().getDouble("proximity-player-hider"))
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