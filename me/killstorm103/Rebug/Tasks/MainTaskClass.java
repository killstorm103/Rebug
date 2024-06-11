package me.killstorm103.Rebug.Tasks;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class MainTaskClass implements Runnable
{
	private static final MainTaskClass task = new MainTaskClass();
	
	public MainTaskClass () {}

	public static MainTaskClass getMainTask() 
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
				for (Player target : Bukkit.getOnlinePlayers())
				{
					if (user.getPlayer() != target)
					{
						if (user.HideOnlinePlayers || user.ProximityPlayerHider && user.getPlayer().getLocation().distance(target.getLocation()) <= 4)
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