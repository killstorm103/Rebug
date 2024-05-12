package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class Version extends Command
{

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
	public String getPermission() {
		return StartOfPermission() + "version";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		Log(sender, ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + "REBUG " + ChatColor.GRAY + "v" + Rebug.PluginVersion() + " made by " + ChatColor.YELLOW + Rebug.getAuthor() + " " + ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "|");
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

	@Override
	public String[] SubAliases() {
		return null;
	}
}
