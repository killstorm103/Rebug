package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;
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
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "debug");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "debug things, helpful for the dev");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "debug");
	}

	@Override
	public String[] SubAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (RebugsAntiCheatSwitcherPlugin.getSettingsList().isEmpty()) ItemsAndMenusUtils.getINSTANCE().getRebugSettings();
		
		RebugsAntiCheatSwitcherPlugin.getSettingsBooleans().put("Debug", !RebugsAntiCheatSwitcherPlugin.getSettingsBooleans().getOrDefault("Debug", false));
		sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Debugging: " + (RebugsAntiCheatSwitcherPlugin.getSettingsBooleans().getOrDefault("Debug", false) ? ChatColor.GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}
}
