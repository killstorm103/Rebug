package me.killstorm103.Rebug.Command.Commands;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;

@SuppressWarnings("deprecation")
public class Version extends Command {

	@Override
	public String getName() {
		return "version";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "version");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "gets the version of rebug");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "version");
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		Log(sender, ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + "REBUG " + ChatColor.GRAY + "v" + RebugsAntiCheatSwitcherPlugin.getPluginVersion() + " " + ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + " | " + ChatColor.GRAY + " made by " + ChatColor.YELLOW + RebugsAntiCheatSwitcherPlugin.getAuthor() + " " + ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "|", false);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}
	@Override
	public String[] SubAliases() {
		return null;
	}
}