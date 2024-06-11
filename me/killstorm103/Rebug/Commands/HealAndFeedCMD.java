package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class HealAndFeedCMD extends Command
{

	@Override
	public String getName() {
		return "healandfeed";
	}

	@Override
	public String getSyntax() {
		return "healandfeed | healandfeed <player>";
	}

	@Override
	public String getDescription() {
		return "heals and feeds players!";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return true;
	}
	@Override
	public String getPermission() {
		return StartOfPermission() + "healandfeed";
	}

	@Override
	public String[] SubAliases() 
	{
		String s[] = {"/healandfeed"};
		
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
				player.setHealth(player.getMaxHealth());
				player.sendMessage(Rebug.RebugMessage + "Healed and Fed " + player.getName() + "!");
			}
			else
				sender.sendMessage(Rebug.RebugMessage + "Only players can run this command!: do /healandfeed <player>");
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
			player.setHealth(player.getMaxHealth());
			sender.sendMessage(Rebug.RebugMessage + "Healed and Fed " + player.getName() + "!");
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
