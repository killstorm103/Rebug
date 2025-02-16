package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.Menu.Old.ItemsAndMenusUtils;


@SuppressWarnings("deprecation")
public class DebugRebugCMD extends Command {

	@Override
	public String getName() {
		return "debug";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "debug");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "debug things, helpful for the dev");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "debug");
	}

	@Override
	public String[] SubAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (RebugPlugin.getSettingsList().isEmpty()) ItemsAndMenusUtils.getINSTANCE().getRebugSettings();
		
		RebugPlugin.getSettingsBooleans().put("Debug", !RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false));
		sender.sendMessage(RebugPlugin.getRebugMessage() + "Debugging: " + (RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false) ? ChatColor.GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));
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
}
