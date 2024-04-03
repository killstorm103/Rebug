package me.killstorm103.Rebug.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;



public class EventJoinAndLeave implements Listener
{
	public static class BrandListener implements PluginMessageListener
	{
	    public void onPluginMessageReceived(String channel, Player player, byte[] msg) 
	    {
	    	final User user = Rebug.getUser(player);
	    	if (user == null)
	    	{
	    		player.kickPlayer("Rebug's \"User\" was somehow null, when reading your client brand!");
	    		return;
	    	}
	    	if (user.getIsBrandSet())
	    	{
	    		player.kickPlayer("Rebug's already collected your client brand you shound't be sending another client brand Packet!");
	    		return;
	    	}
	    	try
	    	{
	    		final String magic = new String(msg, "UTF-8").substring(1);
				if (!user.getIsBrandSet())
	    		{
					user.setBrand(magic);
					user.setBrandSet(true);
	    		}
	    	}
	    	catch (Exception e) 
	    	{
	    		if (user.getBrand() == null || !user.getIsBrandSet() || user.getBrand().length() <= 0)
	    			player.kickPlayer("Rebug kicked you due to failing to load your client brand!");
	    		
	    		e.printStackTrace();
			}
	    	
	    	if (user.getBrand() == null || !user.getIsBrandSet() || user.getBrand().length() <= 0)
    			player.kickPlayer("Rebug kicked you due to failing to load your client brand!");
	    }
	}
		
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin (PlayerJoinEvent e) 
	{
		Player player = e.getPlayer();
		PT.addChannel(player, "MC|Brand");
		Rebug.USERS.put(player.getUniqueId(), new User(player));
		Bukkit.getScheduler().scheduleSyncDelayedTask(Rebug.getGetMain(), new Runnable()
		{
			
			@Override
			public void run() 
			{
				Bukkit.dispatchCommand(Rebug.getGetMain().getServer().getConsoleSender(), "rebug client " + player.getName());
			}
			
		}, 10 * 50);
		
		if (Rebug.getGetMain().getConfig().getBoolean("force-gamemode-on-join") && !player.isOp())
			player.setGameMode(GameMode.SURVIVAL);
		
		final String message = Rebug.getGetMain().getConfig().getString("join-message");
		if (message != null && message.length() > 0 && player.isOp())
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%player%", player.getName())));
	} 
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit (PlayerQuitEvent e) 
	{
		Player player = e.getPlayer();
		User user = Rebug.getUser(player);
		if (user != null)
			Rebug.USERS.remove(player.getUniqueId(), user);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onKicked (PlayerKickEvent e)
	{
		Player player = e.getPlayer();
		User user = Rebug.getUser(player);
		if (user != null)
			Rebug.USERS.remove(player.getUniqueId(), user);
	}
}
