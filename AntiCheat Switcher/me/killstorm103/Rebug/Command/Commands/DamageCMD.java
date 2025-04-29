package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.PT;


public class DamageCMD extends Command {

	@Override
	public String getName() {
		return "damage";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "damage | damage <amount> | damage <amount> <player>");
	}

	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "damages you or a given player if you have op!");
	}

	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "damage");
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "damage" };
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		Player player;
		double damage = 1;
		if (args.length <= 2) {
			if (sender instanceof Player) {
				player = (Player) sender;
				if (args.length == 2) {
					if (PT.isNumber_Double(args[1])) {
						damage = Double.parseDouble(args[1]);
					} else {
						Log(sender, RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "you have to put in a number!");
						return;
					}
				}
				player.damage(damage);
				player.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "dealt " + damage + " damage to " + player.getName());
			} else
				sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + " your servers not able to damage itself!");
		} else if (args.length == 3) {
			if (PT.isNumber_Double(args[1])) {
				damage = Double.parseDouble(args[1]);
			} else {
				Log(sender, RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "you have to put in a number!");
				return;
			}
			player = Bukkit.getServer().getPlayer(args[2]);
			if (player == null) {
				sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Unknown Player!");
				return;
			}
			player.damage(damage);
			sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "dealt " + damage + " damage to " + player.getName());
		} else
			sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + getUsage());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}
}
