package me.killstorm103.Rebug.Command.Handler;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Command.Commands.ShowCommands;
import me.killstorm103.Rebug.Main.RebugPlugin;
import me.killstorm103.Rebug.Utils.PT;

public class EventCommandPreProcess implements Listener
{
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
				if (op != null && op.isOnline() && op != player && !ShowCommands.OpPlayers.containsValue(player.getUniqueId())) {
					op.sendMessage(RebugPlugin.getRebugMessage() + player.getName() + " Executed the command:");
					op.sendMessage(RebugPlugin.getRebugMessage() + message);
				}
			}
		}
		if (RebugPlugin.getINSTANCE().getConfig().get("commands.allow-sub-commands") == null || !RebugPlugin.getINSTANCE().getConfig().getBoolean("commands.allow-sub-commands")) return;
		
		String[] split = PT.SplitString(message.replace("/", ""));
		if (split.length < 1) return;
		
		Command command = RebugPlugin.getINSTANCE().getCommandBySubName(split[0]);
		if (command == null)
		{
			RebugPlugin.Debug(player, split[0] + " wasn't found as a Sub Command!");
			return;
		}
		if (RebugPlugin.getINSTANCE().getConfig().get("commands." + command.getName().toLowerCase()) == null || !RebugPlugin.getINSTANCE().getConfig().getBoolean("commands." + command.getName().toLowerCase())) 
		{
			RebugPlugin.Debug(player, command.getName() + "wasn't found or was Disabled in the config: commands: " + command.getName());
			return;
		}
		Bukkit.dispatchCommand(e.getPlayer(), "rebug " + command.getName() + (command.RemoveSlash() ? " " : "") + (command.RemoveSlash() ? message.replace("/", "") : message.replace(split[0], "")));
		e.setCancelled(true);
	}
}
