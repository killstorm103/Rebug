package me.killstorm103.Rebug.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Utils.PT;

public abstract class Command
{
	public abstract @NotNull String getName();
	public abstract @NotNull String getUsage();
	public abstract @NotNull String getDescription();
	public abstract @NotNull String getPermission();
	public abstract String[] SubAliases();
	public abstract void onCommand(CommandSender sender, String command, String[] args) throws Exception;
	public abstract List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias);
	
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias)
	{
		return false;
	}
	public boolean HideFromCommandsList (CommandSender sender)
	{
		if (sender instanceof Player) 
		{
			if (getType() == SenderType.Console) return true;
			
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || PT.hasAdminPerms(player))
				return false;
			
			return true;
		}
		return getType() != SenderType.Player;
	}
	public boolean BypassUserNullCheck ()
	{
		return false;
	}
	public boolean RemoveSlash ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.commands." + getName().toLowerCase() + ".remove-slash", false);
	}
	public @NotNull SenderType getType ()
	{
		return SenderType.Any;
	}
	public enum SenderType
	{
		Any, Player, Console;
	}
	public Map<UUID, Long> CoolDown = new HashMap<>();

	public void Log (CommandSender sender, String tolog) 
	{
		Log(sender, tolog, true);
	}
	public void Log (Level level, String msg)
	{
		RebugsAntiCheatSwitcherPlugin.Log(level, msg);
	}

	public void Log (CommandSender sender, String tolog, boolean addRebugMessage)
	{
		if (addRebugMessage) 
		{
			tolog = tolog.replace(RebugsAntiCheatSwitcherPlugin.getRebugMessage(), "");
			sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + tolog);
		}
		else
			sender.sendMessage(tolog);
	}

	public void LogToConsole (String tolog)
	{
		tolog = tolog.replace(RebugsAntiCheatSwitcherPlugin.getRebugMessage(), "");
		RebugsAntiCheatSwitcherPlugin.Log(Level.INFO, RebugsAntiCheatSwitcherPlugin.getRebugMessage() + tolog);
	}
	public final String StartOfPermission = "me.killstorm103.rebug.command.";
}