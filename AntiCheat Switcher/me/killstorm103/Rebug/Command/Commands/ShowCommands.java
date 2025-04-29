package me.killstorm103.Rebug.Command.Commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.PT;

public class ShowCommands extends Command {
	public static Map<String, UUID> OpPlayers = new HashMap<>();;

	@Override
	public String getName() {
		return "showcommands";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "showcommands");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "shows what command a player used");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "showcommands_used");
	}
	@Override
	public String[] SubAliases() {
		return new String[] {"showcommands"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || PT.hasAdminPerms(player)) {
				if (OpPlayers.containsValue(player.getUniqueId()))
					OpPlayers.remove(player.getName(), player.getUniqueId());
				else
					OpPlayers.put(player.getName(), player.getUniqueId());

				player.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "You can "
						+ (OpPlayers.containsValue(player.getUniqueId()) ? "now view commands other players executed!"
								: "no longer view commands other players executed!"));
			} else
				player.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "You don't have Permission to use this!");
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}
	@Override
	public SenderType getType() {
		return SenderType.Player;
	}
}
