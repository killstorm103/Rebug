package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.PT;

public class FeedCMD extends Command {

	@Override
	public String getName() {
		return "feed";
	}
	@Override
	public @NotNull String getUsage ()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "feed | feed <player>");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "feed yourself or other players!");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "feedcmd");
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "feed" };
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		Player player = null;
		if (args.length < 2) {
			if (sender instanceof Player) {
				player = (Player) sender;
				player.setFoodLevel(20);
				player.sendMessage(RebugPlugin.getRebugMessage() + "Fed " + player.getName() + "!");
			} else
				sender.sendMessage(RebugPlugin.getRebugMessage() + "Only players can run this command!: do /feed <player>");
		} else {
			player = Bukkit.getServer().getPlayer(args[1]);
			if (player == null) {
				sender.sendMessage(RebugPlugin.getRebugMessage() + "Unknown Player!");
				return;
			}
			player.setFoodLevel(20);
			sender.sendMessage(RebugPlugin.getRebugMessage() + "Fed " + player.getName() + "!");
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
	public boolean HideFromCommandsList(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || PT.hasAdminPerms(player))
				return false;
			
			return true;
		} 

		return false;
	}
}
