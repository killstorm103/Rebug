package me.killstorm103.Rebug.Commands.Handler;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class EventCommandPreProcess implements Listener
{
	
	@EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent e) 
	{
		if (e.getMessage() == null || e.getMessage().length() < 1)
			return;
		
        String command = e.getMessage().toLowerCase();
        if (command.startsWith("/exploits") || command.startsWith("/crashers") || command.startsWith("/items") || command.startsWith("/settings"))
        {
        	Bukkit.dispatchCommand(e.getPlayer(), "rebug menu " + command.replace("/", ""));
        	e.setCancelled(true);
        }
        Command commander = null;
        for (Command cmd : Rebug.getGetMain().getCommands())
        {
        	if (cmd.SubAliases() != null && cmd.SubAliases().length > 0)
        	{
        		for (int i = 0; i < cmd.SubAliases().length; i ++)
        		{
        			if (command.startsWith(cmd.SubAliases()[i].toLowerCase()))
        			{
        				commander = cmd;
        				break;
        			}
        		}
        	}
        }
     
        if (commander != null)
        {
        	for (int i = 0; i < commander.SubAliases().length; i++)
        	{
				command = command.replace(commander.SubAliases()[i], "");
			}
        	Bukkit.dispatchCommand(e.getPlayer(), "rebug " + commander.getName() + command);
        	e.setCancelled(true);
        }
        /*
        for (Command cmd : Rebug.getGetMain().getCommands())
        {
        	if (cmd.SubAliases() != null && cmd.SubAliases().length > 0)
        	{
        		for (int i = 0; i < cmd.SubAliases().length; i ++)
        		{
        			if (command.startsWith(cmd.SubAliases()[i].toLowerCase()))
        			{
        				Bukkit.dispatchCommand(e.getPlayer(), "rebug " + command.replace("/", ""));
        				e.setCancelled(true);
        				break;
        			}
        		}
        	}
        }
        
        */
    }
}
