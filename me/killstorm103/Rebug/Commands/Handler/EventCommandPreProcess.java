package me.killstorm103.Rebug.Commands.Handler;


import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.killstorm103.Rebug.Commands.ShowCommands;
import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class EventCommandPreProcess implements Listener
{
	@EventHandler (priority = EventPriority.HIGHEST)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent e) 
	{
		Player player = e.getPlayer();
		if (!ShowCommands.OpPlayers.isEmpty())
		{
			for (Map.Entry<String, UUID> ops : ShowCommands.OpPlayers.entrySet())
			{
				Player op = Bukkit.getPlayerExact(ops.getKey());
				if (op == null || !op.isOnline())
					ShowCommands.OpPlayers.remove(ops.getKey(), ops.getValue());
				
				if (op != null && op.isOnline() && op != player && !ShowCommands.OpPlayers.containsValue(player.getUniqueId()))
				{
					op.sendMessage(Rebug.RebugMessage + player.getName() + " Executed the command:");
					op.sendMessage(Rebug.RebugMessage + e.getMessage());
				}
			}
		}
		
		String command = e.getMessage().toLowerCase();
		Rebug.Debug(player, "player= " + player.getName() + " msg= " + e.getMessage());
		User user = Rebug.getUser(player);
		if (user == null) return;
		
		
		if (command.startsWith("/rules"))
		{
			if (Config.getServerRules().isEmpty())
			{
				user.sendMessage("You have to add your server rules to the config!");
				e.setCancelled(true);
				return;
			}
			user.sendMessage("");
			for (int i = 0; i < Config.getServerRules().size(); i ++)
			{
				user.getPlayer().sendMessage(ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + "SERVER " + ChatColor.RED + "Rules " + ChatColor.DARK_GRAY + ">> " + (i == 0 ? 1 : i) + " " + ChatColor.RED + Config.getServerRules().get(i));
			}
			e.setCancelled(true);
			return;
		}
		if (!Rebug.hasAdminPerms(user))
		{
			boolean Flagged = false;
			// TODO Make a blocker for /plugins and /help and display my own version of /help
			if (Flagged)
			{
				e.setCancelled(true);
				user.sendMessage("Blocked " + command);
				return;
			}
		}
		
		if (!Rebug.getINSTANCE().getConfig().getBoolean("allow-sub-commands")) return;
		
		String[] Split = StringUtils.split(command);
		if (Split.length > 0)
		{
			Command c = Rebug.getINSTANCE().getCommandBySubName(Split[0]);
			if (c != null)
			{
				e.setCancelled(true);
				user.SentUpdatedCommand = true;
				Bukkit.dispatchCommand(user.getPlayer(), "rebug " + c.getName() + (c.RemoveSlash() ? " " : "") + (c.RemoveSlash() ? command.replace("/", "") : command.replace(Split[0], "")));
				user.SentUpdatedCommand = false;
			}
		}
    }
}
