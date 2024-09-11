package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class PluginLookUp extends Command
{

	@Override
	public String getName() {
		return "pluginlookup";
	}

	@Override
	public String getSyntax() {
		return "pluginlookup <plugin>";
	}

	@Override
	public String getDescription() {
		return "look up plugins";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "plugin_lookup";
	}

	@Override
	public String[] SubAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		if (args.length < 2)
		{
			Log(sender, getSyntax());
			return;
		}
		Plugin plugin = Bukkit.getPluginManager().getPlugin(args[1]);
		if (plugin == null)
		{
			Log(sender, "Unknown Plugin!");
			return;
		}
		Log(sender, plugin.getName());
		if (plugin.getDescription() != null)
		{
			if (!plugin.getDescription().getPermissions().isEmpty())
			{
				for (Permission perms : plugin.getDescription().getPermissions())
				{
					Log(sender, "perm: " + perms.getName());
				}
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
	public boolean HideFromCommandsList(CommandSender sender) 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || Rebug.hasAdminPerms(player))
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
	public boolean hasCommandCoolDown() 
	{
		return false;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
	
}
