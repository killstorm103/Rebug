package me.killstorm103.Rebug.Shared.Command.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.Shared.Command.Command;
import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Utils.User;

public class BackCMD extends Command {

	@Override
	public @NotNull String getName() {
		return "back";
	}

	@Override
	public @NotNull String getSyntax() {
		return "back";
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public @NotNull String getDescription() {
		return "Teleports you back to where you died!";
	}

	@Override
	public @NotNull String getPermission() {
		return StartOfPermission() + "back";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (sender instanceof Player) {
			User user = Rebug.getUser((Player) sender);
			if (user == null)
				return;

			if (user.death_location != null) {
				user.getPlayer().teleport(user.death_location);
				user.death_location = null;
			} else
				user.getPlayer()
						.sendMessage(Rebug.RebugMessage + ChatColor.GRAY + "You don't have a last death location!");
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
			if (Rebug.hasAdminPerms(player) || player.hasPermission(getPermission()))
				return false;
		}

		return true;
	}

	@Override
	public @NotNull Types getType() {
		return Types.Player;
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "/back" };
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
}
