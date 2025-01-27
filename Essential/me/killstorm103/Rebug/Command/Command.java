package me.killstorm103.Rebug.Command;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.Main.RebugPlugin;

public abstract class Command
{
	public abstract @NotNull String getName();

	public abstract @NotNull String getSyntax();

	public abstract @NotNull String getDescription();

	public abstract @NotNull String getPermission();

	public abstract String[] SubAliases();

	public abstract void onCommand(CommandSender sender, String command, String[] args) throws Exception;

	public abstract List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias);

	public abstract boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias);

	public abstract boolean HideFromCommandsList(CommandSender sender);

	public abstract @NotNull Types getType ();

	public abstract boolean hasCommandCoolDown ();

	public abstract boolean RemoveSlash ();
	
	public enum Types
	{
		AnySender, Player, Console;
	}

	public Map<UUID, Long> CoolDown = new HashMap<>();

	public void Log (CommandSender sender, String tolog) 
	{
		Log(sender, tolog, true);
	}

	public void Log (CommandSender sender, String tolog, boolean addRebugMessage)
	{
		if (addRebugMessage) {
			
			tolog = tolog.replace(RebugPlugin.getRebugMessage(), "");
			sender.sendMessage(RebugPlugin.getRebugMessage() + tolog);
		}
		else
			sender.sendMessage(tolog);
	}

	public void LogToConsole(String tolog) {
		tolog = tolog.replace(RebugPlugin.getRebugMessage(), "");
		RebugPlugin.getINSTANCE().Log(Level.INFO, RebugPlugin.getRebugMessage() + tolog);
	}

	public final String StartOfPermission() {

		return "me.killstorm103.rebug.command.";
	}
}