package me.killstorm103.Rebug.Command.Handler;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import me.killstorm103.Rebug.Main.RebugPlugin;

public class ShortCutBasic implements TabExecutor
{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arg) 
	{
		me.killstorm103.Rebug.Command.Command commander = RebugPlugin.getINSTANCE().getCommandByName(command.getName());
		if (commander != null && commander.HasCustomTabComplete(sender, command, arg, label))
			return commander.onTabComplete(sender, command, arg, label);
		
		commander = null;
		commander = RebugPlugin.getINSTANCE().getCommandBySubName(command.getName());
		if (commander != null && commander.HasCustomTabComplete(sender, command, arg, label) && RebugPlugin.getINSTANCE().isCommandAllowedShortCut(commander.getName()))
			return commander.onTabComplete(sender, command, arg, label);
		
		return null;
	}
	@Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] arg) 
	{
		return true;
	}
}
