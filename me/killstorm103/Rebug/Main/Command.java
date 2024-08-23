package me.killstorm103.Rebug.Main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public abstract class Command 
{
	public abstract String getName();
	public abstract String getSyntax();
	public abstract String getDescription(); 
	public abstract String getPermission();
	public abstract String[] SubAliases();
	public abstract void onCommand (CommandSender sender, String command, String[] args) throws Exception;
	public abstract List<String> onTabComplete (CommandSender sender, org.bukkit.command.Command command, String[] args, String alias);
	public abstract boolean HasCustomTabComplete (CommandSender sender, org.bukkit.command.Command command, String[] args, String alias);
	public abstract boolean HideFromCommandsList (CommandSender sender);
	public abstract boolean HasToBeConsole ();
	public abstract boolean hasCommandCoolDown();
	public abstract boolean RemoveSlash ();
	
	public Map<UUID, Long> CoolDown = new HashMap<>();
	
	public void Log (CommandSender sender, String tolog)
	{
		Log(sender, tolog, true);
	}
	public void Log (CommandSender sender, String tolog, boolean addRebugMessage)
	{
		if (addRebugMessage)
		{
			tolog = tolog.replace(Rebug.RebugMessage, "");
			sender.sendMessage(Rebug.RebugMessage + tolog);
		}
		else
			sender.sendMessage(tolog);
	}
	public void LogToConsole (String tolog)
	{
		tolog = tolog.replace(Rebug.RebugMessage, "");
		Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + tolog);
	}
	public final String StartOfPermission ()
	{
		return "me.killstorm103.rebug.command.";
	}
}
