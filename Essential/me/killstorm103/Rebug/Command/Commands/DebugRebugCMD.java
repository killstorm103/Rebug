package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Main.RebugPlugin;
import me.killstorm103.Rebug.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.PT;


@SuppressWarnings("deprecation")
public class DebugRebugCMD extends Command {

	@Override
	public String getName() {
		return "debug";
	}

	@Override
	public String getSyntax() {
		return "debug";
	}

	@Override
	public String getDescription() {
		return "debug things, helpful for the dev";
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "debug";
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

	@Override
	public Types getType() {
		return Types.AnySender;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
}
