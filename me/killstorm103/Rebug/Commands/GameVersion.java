package me.killstorm103.Rebug.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;

public class GameVersion extends Command
{

	@Override
	public String getName() {
		return "gameversion";
	}

	@Override
	public String getSyntax() {
		return "gameversion [nothing to check yours otherwise player name or %server% to check the servers version]";
	}

	@Override
	public String getDescription() {
		return "tells you the game version you or another player is playing on";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "gameversion";
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) throws Exception 
	{
		if (args.length < 2)
		{
			if (sender instanceof Player)
				sender.sendMessage(ChatColor.RED + "Your " + ChatColor.AQUA + "game version " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + PT.getPlayerVersion(PT.getPlayerVersion((Player) sender)) + " (" + PT.getPlayerVersion((Player) sender) + ")");
			else
				sender.sendMessage(ChatColor.GRAY + "Server version's: " + PT.getServerVersion());
			return;
		}
		String typed = args[1];
		if (typed.equalsIgnoreCase("%server%"))
		{
			sender.sendMessage(ChatColor.GRAY + "Server version's: " + PT.getServerVersion());
			return;
		}
		Player player = Rebug.getGetMain().getServer().getPlayer(typed);
		if (player == null)
		{
			sender.sendMessage("Unknown Player!");
			return;
		}
		sender.sendMessage(ChatColor.RED + player.getName() + "'s " + ChatColor.AQUA + "game version " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + PT.getPlayerVersion(PT.getPlayerVersion((Player) sender)) + " (" + PT.getPlayerVersion((Player) sender) + ")");
	}
	
}
