package me.killstorm103.Rebug.Command.Commands;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Main.RebugPlugin;
import me.killstorm103.Rebug.Utils.PT;

@SuppressWarnings("deprecation")
public class Version extends Command {

	@Override
	public String getName() {
		return "version";
	}

	@Override
	public String getSyntax() {
		return "version";
	}

	@Override
	public String getDescription() {
		return "gets the version of rebug";
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "version";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		Log(sender,
				ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + "REBUG " + ChatColor.GRAY
						+ "v" + RebugPlugin.getPluginVersion() + " " + ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + " | "
						+ ChatColor.GRAY + " made by " + ChatColor.YELLOW + RebugPlugin.getAuthor() + " "
						+ ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "|",
				false);
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
	public String[] SubAliases() {
		return null;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
}