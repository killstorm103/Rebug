package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Main.RebugPlugin;

@SuppressWarnings("deprecation")
public class Help extends Command {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getSyntax() {
		return "help | help <'command'> <command name>";
	}

	@Override
	public String getDescription() {
		return "gets a list of commands and Syntax of commands";
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "help";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (args.length == 1) {
			Log(sender,
					ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + "REBUG "
							+ ChatColor.GRAY + "v" + RebugPlugin.getPluginVersion() + " " +  ChatColor.BOLD.toString()
							+ ChatColor.DARK_GRAY + "| " + ChatColor.GRAY + RebugPlugin.getEdition() + ChatColor.BOLD.toString()
							+ ChatColor.DARK_GRAY + " | " + ChatColor.GRAY + " made by " + ChatColor.YELLOW
							+ RebugPlugin.getAuthor() + " " + ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "|",
					false);
			Log(sender,
					ChatColor.GRAY + "commands (" + "You have access to "
							+ RebugPlugin.getINSTANCE().getAccessToCommandsNumber(sender) + " / "
							+ RebugPlugin.getINSTANCE().getCommands().size() + ")" + ChatColor.RESET + ":",
					false);
		}
		if (args.length == 3) {
			args[1] = args[1].replace("cmd", "command");
			if (args[1].equalsIgnoreCase("command")) {
				Command c = RebugPlugin.getINSTANCE().getCommandByName(args[2]);
				c = c == null ? RebugPlugin.getINSTANCE().getCommandBySubName(args[2]) : c;
				if (c != null) {
					Log(sender, ChatColor.GRAY + c.getSyntax(), false);
					Log(sender, ChatColor.GRAY + c.getDescription(), false);
				} else
					Log(sender, "Unknown Command!");
			} else
				Log(sender, getSyntax());

			return;
		}
		for (Command commands : RebugPlugin.getINSTANCE().getCommands()) {
			if (args.length == 1) {
				if (sender instanceof Player) {
					if (commands.HideFromCommandsList(sender)
							&& !((Player) sender).hasPermission(RebugPlugin.AllCommands_Permission)) {
					} else
						Log(sender, ChatColor.GRAY + "/rebug " + commands.getName(), false);
				} else
					Log(sender, ChatColor.GRAY + "/rebug " + commands.getName(), false);
			}
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
