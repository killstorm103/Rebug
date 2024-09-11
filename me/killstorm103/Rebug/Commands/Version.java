package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String getPermission() {
		return StartOfPermission() + "version";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		Log(sender, ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + "REBUG " + ChatColor.GRAY + "v" + Rebug.PluginVersion() + " " + ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.GRAY + Rebug.PluginEdition() + ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + " | " + ChatColor.GRAY + " made by " + ChatColor.YELLOW + Rebug.getAuthor() + " " + ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "|", false);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias) {
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return false;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender)
	{
		boolean s = true;
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (Rebug.hasAdminPerms(player) || player.hasPermission(getPermission()))
				s = false;
		}
		else
			s = false;
		
		return s;
	}
	@Override
	public Types getType ()
	{
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
