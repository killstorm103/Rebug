package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.Menu.Old.ItemsAndMenusUtils;

public class DebugItemCMD extends Command
{

	@Override
	public @NotNull String getName() {
		return "debugitem";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "debugitem");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "opens a debugitem menu");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "debugitemcmd");
	}

	@Override
	public String[] SubAliases() {
		return new String[] {"debugitem"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		((Player) sender).openInventory(ItemsAndMenusUtils.getINSTANCE().getDebugItemMenu());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}
	@Override
	public @NotNull SenderType getType() 
	{
		return SenderType.Player;
	}
}
