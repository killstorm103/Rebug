package me.killstorm103.Rebug.Main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.CommandSender;

public abstract class Command 
{
	public abstract String getName();
	public abstract String getSyntax();
	public abstract String getDescription(); 
	public abstract String getPermission();
	public abstract String[] SubAliases();
	public abstract void onCommand (CommandSender sender, String command, String[] args) throws Exception;
	public abstract List<String> onTabComplete (CommandSender sender, org.bukkit.command.Command command, String[] args);
	public abstract boolean HasCustomTabComplete ();
	public abstract boolean HideFromCommandsList ();
	public abstract boolean HasToBeConsole ();
	public abstract boolean hasCommandCoolDown();
	
	public Map<UUID, Long> CoolDown = new HashMap<>();
	
	public Rebug getRebug ()
	{
		return Rebug.getINSTANCE();
	}
	public static void Log (CommandSender sender, String tolog)
	{
		sender.sendMessage(tolog);
	}
	public static void LogToConsole (String tolog)
	{
		Rebug.getINSTANCE().getServer().getConsoleSender().sendMessage(tolog);
	}
	public String StartOfPermission ()
	{
		return "me.killstorm103.rebug.command.";
	}
}
