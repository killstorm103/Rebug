package me.killstorm103.Rebug.Commands.Handler;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.killstorm103.Rebug.Commands.Menu;
import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class EventCommandPreProcess implements Listener
{
	@EventHandler (priority = EventPriority.HIGHEST)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent e) 
	{
		String command = e.getMessage().toLowerCase();
		Rebug.Debug(e.getPlayer(), "player= " + e.getPlayer().getName() + " msg= " + e.getMessage());
		
		User user = Rebug.getUser(e.getPlayer());
		if (user == null) return;
		
		
		if (command.startsWith("/rules"))
		{
			if (Config.getServerRules().isEmpty())
			{
				user.getPlayer().sendMessage(Rebug.RebugMessage + "You have to add your server rules to the config!");
				e.setCancelled(true);
				return;
			}
			user.getPlayer().sendMessage(Rebug.RebugMessage);
			for (int i = 0; i < Config.getServerRules().size(); i ++)
			{
				user.getPlayer().sendMessage(ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + "SERVER " + ChatColor.RED + "Rules " + ChatColor.DARK_GRAY + ">> " + (i == 0 ? 1 : i) + " " + ChatColor.RED + Config.getServerRules().get(i));
			}
			e.setCancelled(true);
			return;
		}
		if (!Rebug.hasAdminPerms(user.getPlayer()))
		{
			boolean Flagged = false;
			// TODO Make a blocker for /plugins and /help and display my own version of /help
			if (Flagged)
			{
				e.setCancelled(true);
				user.getPlayer().sendMessage(Rebug.RebugMessage + "Blocked " + command);
				return;
			}
		}
		
		if (!Config.AllowSubCommands()) return;
		
        for (Command cmd : Rebug.getINSTANCE().getCommands())
        {
        	if (cmd.SubAliases() != null && cmd.SubAliases().length > 0)
        	{
        		for (int i = 0; i < cmd.SubAliases().length; i ++)
        		{
        			if (command.startsWith(cmd.SubAliases()[i]))
        			{
        				user.SentUpdatedCommand = true;
        				e.setCancelled(true);
        				if (cmd instanceof Menu)
        					Bukkit.dispatchCommand(e.getPlayer(), "rebug " + cmd.getName() + " " + command.replace("/", ""));
        				else
        					Bukkit.dispatchCommand(e.getPlayer(), "rebug " + cmd.getName() + command.replace(cmd.SubAliases()[i], ""));
        				user.SentUpdatedCommand = false;
        				return;
        			}
        		}
        	}
        }
    }
}
