package me.killstorm103.Rebug.Commands;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;


public class ShowCommands extends Command
{
	public static Map<String, UUID> OpPlayers = new HashMap<>();
	static
	{
		OpPlayers.clear();
	}

	@Override
	public String getName() {
		return "showcommands";
	}

	@Override
	public String getSyntax() {
		return "showcommands";
	}

	@Override
	public String getDescription() {
		return "shows what command a player used";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "showcommands_used";
	}

	@Override
	public String[] SubAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || Rebug.hasAdminPerms(player))
			{
				if (OpPlayers.containsValue(player.getUniqueId()))
					OpPlayers.remove(player.getName(), player.getUniqueId());
				else
					OpPlayers.put(player.getName(), player.getUniqueId());
				
				player.sendMessage(Rebug.RebugMessage + "You can " + (OpPlayers.containsValue(player.getUniqueId()) ? "now view commands other players executed!" : "no longer view commands other players executed!"));
			}
			else
				player.sendMessage(Rebug.RebugMessage + "You don't have Permission to use this!");
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return false;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || Rebug.hasAdminPerms(player))
				return false;
		}
		return true;
	}
	@Override
	public Types getType ()
	{
		return Types.Player;
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
	
}
