package me.killstorm103.Rebug.Tasks;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Utils.User;

@SuppressWarnings("deprecation")
public class OneSecondUpdater implements Runnable
{
	private static final OneSecondUpdater task = new OneSecondUpdater();
	private final DecimalFormat former = new DecimalFormat("#.###");
	
	public OneSecondUpdater () {}

	public static OneSecondUpdater getMainTask () 
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
			User user = RebugsAntiCheatSwitcherPlugin.getUser(player);
			if (user != null)
			{
				if (!user.isMoving())
				{
					user.setBPSY(former.format(0));
					user.setBPSXZ(former.format(0));
				}
				else
				{
					final double distX = user.getPlayer().getLocation().getX() - user.getLastTickPosX(),
							distY = user.getPlayer().getLocation().getY() - user.getLastTickPosY(),
							distZ = user.getPlayer().getLocation().getZ() - user.getLastTickPosZ(),
							bpsXZ = Math.sqrt(distX * distX + distZ * distZ) * 20.0, bpsY = Math.sqrt(distY * distY) * 20.0;
					
					if (user.getBPSXZ().equalsIgnoreCase("0"))
						user.setBPSXZ(former.format(bpsXZ));
					
					if (user.getBPSY().equalsIgnoreCase("0"))
						user.setBPSY(former.format(bpsY));
				}
				user.setClicksPerSecondLeft(user.getPreCPSLeft());
				user.setClicksPerSecondRight(user.getPreCPSRight());
				user.setSendPacketCounts(user.getPreSendPacket());
				user.setReceivePacketCounts(user.getPreReceivePacket());
				user.setPreSendPacket(0);
				user.setPreReceivePacket(0);
				user.setPreCPSLeft(0);
				user.setPreCPSRight(0);
				if (Bukkit.getOnlinePlayers().size() > 1)
				{
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
}
