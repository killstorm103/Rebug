package me.killstorm103.Rebug.Command.Commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;

public class Credits extends Command {
	private static final Map<String, String> others = new HashMap<>();
	static {
		others.clear();
		others.put("Retrooper", "Packet Events!");
		others.put("Matej (kangarko)", "a little help with Folia Support");
	}

	@Override
	public String getName() {
		return "credits";
	}
	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "credits");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "gives credit to other devs who helped");
	}

	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "creditscmd");
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "credits" };
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		for (Map.Entry<String, String> map : others.entrySet())
			sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + map.getKey() + ": " + map.getValue());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}
}
