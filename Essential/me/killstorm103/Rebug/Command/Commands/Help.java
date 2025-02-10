package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Command.Command;

@SuppressWarnings("deprecation")
public class Help extends Command {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "help | help <'command'> <command name>");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "gets a list of commands and Syntax of commands");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "help");
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
					Log(sender, ChatColor.GRAY + c.getUsage(), false);
					Log(sender, ChatColor.GRAY + c.getDescription(), false);
				} else
					Log(sender, "Unknown Command!");
			} else
				Log(sender, getUsage());

			return;
		}
		final String mainLabel = RebugPlugin.getINSTANCE().getConfig().getString("command.settings.main-command.label", "rebug");
		for (Command commands : RebugPlugin.getINSTANCE().getCommands()) {
			if (args.length == 1) {
				if (sender instanceof Player) {
					if (commands.HideFromCommandsList(sender)) {}
					else
						Log(sender, ChatColor.GRAY + "/" + mainLabel + " " + commands.getName(), false);
				} else
					Log(sender, ChatColor.GRAY + "/" + mainLabel + " " + commands.getName(), false);
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
	public String[] SubAliases() {
		return null;
	}
}
