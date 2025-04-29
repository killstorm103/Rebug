package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;

public class HealAndFeedCMD extends Command {

	@Override
	public String getName() {
		return "healandfeed";
	}
	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "healandfeed | healandfeed <player>");
	}

	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "heals and feeds players!");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "healandfeed");
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "healandfeed" };
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		Player player = null;
		if (args.length < 2) {
			if (sender instanceof Player) {
				player = (Player) sender;
				player.setFoodLevel(20);
				player.setHealth(player.getMaxHealth());
				player.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Healed and Fed " + player.getName() + "!");
			} else
				sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Only players can run this command!: do /healandfeed <player>");
		} else {
			player = Bukkit.getServer().getPlayer(args[1]);
			if (player == null) {
				sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Unknown Player!");
				return;
			}
			player.setFoodLevel(20);
			player.setHealth(player.getMaxHealth());
			sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Healed and Fed " + player.getName() + "!");
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}
}
