package me.killstorm103.Rebug.Command.Handler;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Command.Commands.ShowCommands;
import me.killstorm103.Rebug.Utils.PT;

public class EventCommandPreProcess implements Listener
{
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onServerCommand (ServerCommandEvent e)
	{
		if (!RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.settings.allow-sub-commands.for-console", false)) return;
		
		String message = e.getCommand();
		String[] split = PT.SplitString(message.replace("/", ""));
		if (split.length < 1) return;
		
		Command command = RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommandBySubName(split[0]);
		if (command == null) return;
		
		if (!RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.commands." + command.getName().toLowerCase() + ".shortcut-enabled", false)) 
		{
			RebugsAntiCheatSwitcherPlugin.Debug(e.getSender(), command.getName() + "wasn't found or was Disabled in the config: command: " + command.getName());
			return;
		}
		e.setCommand(RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.settings.main-command.label", "rebugsanticheatswitcher") + " " + command.getName() + (command.RemoveSlash() ? " " : "") + (command.RemoveSlash() ? message.replace("/", "") : message.replace(split[0], "")));
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPreCommand (PlayerCommandPreprocessEvent e)
	{
		Player player = e.getPlayer();
		String message = e.getMessage();
		if (!ShowCommands.OpPlayers.isEmpty())
		{
			for (Map.Entry<String, UUID> ops : ShowCommands.OpPlayers.entrySet()) 
			{
				Player op = Bukkit.getPlayerExact(ops.getKey());
				if (op == null || !op.isOnline())
				{
					ShowCommands.OpPlayers.remove(ops.getKey(), ops.getValue());
					return;
				}
				if (op != null && op.isOnline() && op != player && !ShowCommands.OpPlayers.containsValue(player.getUniqueId())) 
				{
					op.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + player.getName() + " Executed the command:");
					op.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + message);
				}
			}
		}
		if (!RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.settings.allow-sub-commands.for-players", false)) return;
		
		String[] split = PT.SplitString(message.replace("/", ""));
		if (split.length < 1) return;
		
		Command command = RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommandBySubName(split[0]);
		if (command == null)
		{
			RebugsAntiCheatSwitcherPlugin.Debug(player, split[0] + " wasn't found as a Sub Command!");
			return;
		}
		if (!RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.commands." + command.getName().toLowerCase() + ".shortcut-enabled", false)) 
		{
			RebugsAntiCheatSwitcherPlugin.Debug(player, command.getName() + "wasn't found or was Disabled in the config: command: " + command.getName());
			return;
		}
		Bukkit.dispatchCommand(e.getPlayer(), RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.settings.main-command.label", "rebugsanticheatswitcher") + " " + command.getName() + (command.RemoveSlash() ? " " : "") + (command.RemoveSlash() ? message.replace("/", "") : message.replace(split[0], "")));
		e.setCancelled(true);
	}
}
