package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.User;


public class CheckAC extends Command {

	@Override
	public String getName() {
		return "checkac";
	}
	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "checkac | checkac <user>");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "check the ac of a user");
	}

	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "checkac");
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "checkac" };
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (sender instanceof Player) {
			User user = null;
			if (args.length > 1) 
			{
				Player player = Bukkit.getPlayer(args[1]);
				if (player == null) {
					Log(sender, "Unknown Player!");
					return;
				}
				user = RebugsAntiCheatSwitcherPlugin.getUser(player);
			} else
				user = RebugsAntiCheatSwitcherPlugin.getUser((Player) sender);

			if (user == null) {
				Log(sender, "Unknown User!");
				return;
			}
			Log(sender, user.getName() + ":");
			Log(sender, "AC" + (user.getSelectedAntiCheats() > 1 ? "s" : "") + ": " + user.getAntiCheat().replace(" ", ", "));
			Log(sender, "Amount of AntiCheats: " + user.getSelectedAntiCheats());
			Log(sender, "NumberID: "
					+ (user.getSelectedAntiCheats() > 1 ? user.getNumberIDs() : "null cause this is used for multi acs"));
		} else {
			if (args.length < 2) {
				Log(sender, getUsage());
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if (player == null) {
				Log(sender, "Unknown Player!");
				return;
			}
			User user = RebugsAntiCheatSwitcherPlugin.getUser(player);
			if (user == null) {
				Log(sender, "Unknown User!");
				return;
			}
			Log(sender, user.getName() + ":");
			Log(sender, "AC" + (user.getSelectedAntiCheats() > 1 ? "s" : "") + ": " + user.getAntiCheat().replace(" ", ", "));
			Log(sender, "Amount of AntiCheats: " + user.getSelectedAntiCheats());
			Log(sender, "NumberID: "
					+ (user.getSelectedAntiCheats() > 1 ? user.getNumberIDs() : "null cause this is used for multi acs"));

		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}
}
