package me.killstorm103.Rebug.Main;

import org.bukkit.command.CommandSender;

public abstract class Command 
{
	public abstract String getName();
	public abstract String getSyntax();
	public abstract String getDescription(); 
	public abstract String getPermission();
	public abstract void onCommand (CommandSender sender, String[] args) throws Exception;
	
	public Rebug getRebug ()
	{
		return Rebug.getGetMain();
	}
	public void Log (CommandSender sender, String tolog)
	{
		sender.sendMessage(tolog);
	}
	public String StartOfPermission ()
	{
		return "me.killstorm103.rebug.command.";
	}
}
