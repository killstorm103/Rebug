package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Main.RebugPlugin;
import me.killstorm103.Rebug.Utils.PT;

public class HealAndFeedCMD extends Command {

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
	public String[] SubAliases() {
		return new String[] { "/healandfeed" };
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		Player player = null;
		if (args.length < 2) {
			if (sender instanceof Player) {
				player = (Player) sender;
				player.setFoodLevel(20);
				player.setHealth(player.getMaxHealth());
				player.sendMessage(RebugPlugin.getRebugMessage() + "Healed and Fed " + player.getName() + "!");
			} else
				sender.sendMessage(RebugPlugin.getRebugMessage() + "Only players can run this command!: do /healandfeed <player>");
		} else {
			player = Bukkit.getServer().getPlayer(args[1]);
			if (player == null) {
				sender.sendMessage(RebugPlugin.getRebugMessage() + "Unknown Player!");
				return;
			}
			player.setFoodLevel(20);
			player.setHealth(player.getMaxHealth());
			sender.sendMessage(RebugPlugin.getRebugMessage() + "Healed and Fed " + player.getName() + "!");
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

	@Override
	public Types getType() {
		return Types.AnySender;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
}
