package me.killstorm103.Rebug.Shared.Command.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Shared.Command.Command;
import me.killstorm103.Rebug.Shared.Main.Config;
import me.killstorm103.Rebug.Shared.Main.Rebug;

public class Discord extends Command {

	@Override
	public String getName() {
		return "discord";
	}

	@Override
	public String getSyntax() {
		return "discord";
	}

	@Override
	public String getDescription() {
		return "gives you the link to our discord server!";
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "discord";
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "/discord" };
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		sender.sendMessage(Rebug.RebugMessage + "Our discord server invite link:");
		sender.sendMessage(Rebug.RebugMessage + Config.DiscordInviteLink());
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
