package me.killstorm103.Rebug.Shared.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Shared.Command.Command;
import me.killstorm103.Rebug.Shared.Main.Rebug;

public class FeedCMD extends Command {

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
	public boolean hasCommandCoolDown() {
		return true;
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "feedcmd";
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "/feed" };
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		Player player = null;
		if (args.length < 2) {
			if (sender instanceof Player) {
				player = (Player) sender;
				player.setFoodLevel(20);
				player.sendMessage(Rebug.RebugMessage + "Fed " + player.getName() + "!");
			} else
				sender.sendMessage(Rebug.RebugMessage + "Only players can run this command!: do /feed <player>");
		} else {
			player = Bukkit.getServer().getPlayer(args[1]);
			if (player == null) {
				sender.sendMessage(Rebug.RebugMessage + "Unknown Player!");
				return;
			}
			player.setFoodLevel(20);
			sender.sendMessage(Rebug.RebugMessage + "Fed " + player.getName() + "!");
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
		boolean s = true;
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (Rebug.hasAdminPerms(player) || player.hasPermission(getPermission()))
				s = false;
		} else
			s = false;

		return s;
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
