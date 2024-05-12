package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class FeedCMD extends Command
{

	@Override
	public String getName() {
		return "feed";
	}

	@Override
	public String getSyntax() {
		return "feed | feed <player>";
	}

	@Override
	public String getDescription() {
		return "feed yourself or other players!";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "feedcmd";
	}

	@Override
	public String[] SubAliases() 
	{
		String[] s = {"/feed"};
		return s;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		Player player = null;
		if (args.length < 2)
		{
			if (sender instanceof Player)
			{
				player = (Player) sender;
				player.setFoodLevel(20);
				player.sendMessage(Rebug.RebugMessage + "Fed " + player.getName() + "!");
			}
			else
				sender.sendMessage(Rebug.RebugMessage + "Only players can run this command!: do /feed <player>");
		}
		else
		{
			player = Bukkit.getServer().getPlayer(args[1]);
			if (player == null)
			{
				sender.sendMessage(Rebug.RebugMessage + "Unknown Player!");
				return;
			}
			player.setFoodLevel(20);
			sender.sendMessage(Rebug.RebugMessage + "Fed " + player.getName() + "!");
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args) {
		return null;
	}

	@Override
	public boolean HasCustomTabComplete() {
		return false;
	}

	@Override
	public boolean HideFromCommandsList() {
		return false;
	}

	@Override
	public boolean HasToBeConsole() {
		return false;
	}
	
}
