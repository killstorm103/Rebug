package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.PT;

public class GetUUID extends Command {

	@Override
	public String getName() {
		return "getuuid";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "getuuid | getuuid <player>");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "gets the uuid of the given player");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "getuuid");
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "getuuid" };
	}
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		Player player = null, target = null;
		if (sender instanceof Player) 
		{
			player = (Player) sender;
			if (args.length < 2)
				player.sendMessage(RebugPlugin.getRebugMessage() + player.getName() + "'s UUID: " + player.getUniqueId());
			else 
			{
				target = Bukkit.getPlayer(args[1]);
				target = target == null ? Bukkit.getOfflinePlayer(args[1]).getPlayer() : target;
				if (target == null) {
					player.sendMessage(RebugPlugin.getRebugMessage() + "Unknown Player!");
					return;
				}
				player.sendMessage(RebugPlugin.getRebugMessage() + target.getName() + "'s UUID: " + target.getUniqueId());
			}
		} 
		else
		{
			if (args.length < 2)
			{
				sender.sendMessage(RebugPlugin.getRebugMessage() + "getuuid <player>");
				return;
			}
			target = Bukkit.getPlayer(args[1]);
			target = target == null ? Bukkit.getOfflinePlayer(args[1]).getPlayer() : target;
			if (target == null) 
			{
				sender.sendMessage(RebugPlugin.getRebugMessage() + "Unknown Player!");
				return;
			}
			sender.sendMessage(RebugPlugin.getRebugMessage() + target.getName() + "'s UUID: " + target.getUniqueId());
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
			if (player.hasPermission(getPermission()) || PT.hasAdminPerms(player))
				return false;
			
			return true;
		}

		return false;
	}
}
