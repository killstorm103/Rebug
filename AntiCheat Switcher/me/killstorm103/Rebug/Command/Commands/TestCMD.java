package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;

public class TestCMD extends Command
{

	@Override
	public String getName() {
		return "test";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "test");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "test things");
	}

	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "test.cmd");
	}

	@Override
	public String[] SubAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
		String alias) {
		return null;
	}
}
