package me.killstorm103.Rebug.Command.Handler.Bukkit;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;

public class ShortCutBasic implements TabExecutor
{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arg) 
	{
		me.killstorm103.Rebug.Command.Command commander = RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommandByName(command.getName());
		if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onTabComplete-sub-commands", false))
			RebugsAntiCheatSwitcherPlugin.Log(Level.SEVERE, "label= " + label + " args: " + arg.length);
			
		if (commander != null && commander.HasCustomTabComplete(sender, command, arg, label))
			return commander.onTabComplete(sender, command, arg, label);
		
		commander = null;
		commander = RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommandBySubName(command.getName());
		if (commander != null && commander.HasCustomTabComplete(sender, command, arg, label) && RebugsAntiCheatSwitcherPlugin.getINSTANCE().isCommandAllowedShortCut(commander.getName()))
			return commander.onTabComplete(sender, command, arg, label);
		
		return null;
	}
	@Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] arg) 
	{
		return true;
	}
}
