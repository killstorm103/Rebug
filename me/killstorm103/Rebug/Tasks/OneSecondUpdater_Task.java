package me.killstorm103.Rebug.Tasks;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
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
				final double
				distX = user.getLocation().getX() - user.lastTickPosX,
				distY = user.getLocation().getY() - user.lastTickPosY,
				distZ = user.getLocation().getZ() - user.lastTickPosZ,
				bpsXZ = Math.sqrt(distX * distX + distZ * distZ) * 20.0,
				bpsY = Math.sqrt(distY * distY) * 20.0;  
				if (Config.RebugScoreBoard() && !Rebug.Reloading)
				{
					BPlayerBoard board = Netherboard.instance().getBoard(user.getPlayer());
			   		if (board != null)
			   		{
			   			if (!PT.isStringNull(board.get(5)))
				   			board.remove(5);
				   		
				   		board.set(ChatColor.DARK_RED + "PPS " + ChatColor.WHITE + user.sendPacketCounts + "/in " + user.receivePacketCounts + "/out", 5);
			   			
			   			if (!PT.isStringNull(board.get(6)))
				   			board.remove(6);
				   		
				   		board.set(ChatColor.DARK_RED + "BPS (Y) " + ChatColor.WHITE + former.format(bpsY), 6);
				   		
				   		if (!PT.isStringNull(board.get(7)))
				   			board.remove(7);
				   		
				   		board.set(ChatColor.DARK_RED + "BPS (XZ) " + ChatColor.WHITE + former.format(bpsXZ), 7);
				   		
				   		
				   		if (!PT.isStringNull(board.get(8)))
				   			board.remove(8);
				   		
				   		board.set(ChatColor.DARK_RED + "CPS " + ChatColor.WHITE + user.ClicksPerSecond, 8);
			   		}
				}
				
				
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