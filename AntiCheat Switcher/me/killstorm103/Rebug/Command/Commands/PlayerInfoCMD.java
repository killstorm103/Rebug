package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.github.retrooper.packetevents.PacketEvents;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.User;

public class PlayerInfoCMD extends Command {

	@Override
	public String getName() {
		return "player";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "player | player <player name>");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "get info about a player");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "playerinfocmd");
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "player" };
	}
	// TODO Improve how much info you get

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		User user;
		if (sender instanceof Player) {
			if (args.length < 2) {
				user = RebugsAntiCheatSwitcherPlugin.getUser((Player) sender);
				user.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "" + user.getPlayer().getName());
				user.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Your client is: " + user.getClientBrand() + " " + PacketEvents.getAPI().getPlayerManager().getClientVersion(user.getPlayer()).getReleaseName() + " ("
				+ PacketEvents.getAPI().getPlayerManager().getClientVersion(user.getPlayer()).getProtocolVersion() + ")");
				user.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Selected AntiCheat: " + user.getAntiCheat());
			} else {
				user = RebugsAntiCheatSwitcherPlugin.getUser(Bukkit.getPlayer(args[1]));
				if (user == null) {
					sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Unknown Player!");
					return;
				}
				sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + user.getPlayer().getName() + "'s");
				sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Client is: " + user.getClientBrand() + " "
						+ PacketEvents.getAPI().getPlayerManager().getClientVersion(user.getPlayer()).getReleaseName() + " ("
						+ PacketEvents.getAPI().getPlayerManager().getClientVersion(user.getPlayer()).getProtocolVersion() + ")");
				sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Selected AntiCheat: " + user.getAntiCheat());
			}
		} else {
			if (args.length < 2) {
				sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "player <player name>");
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if (player == null) {
				Log(sender, "Unknown Player!");
				return;
			}

			user = RebugsAntiCheatSwitcherPlugin.getUser(player);
			if (user == null) {
				Log(sender, "Unknown User!");
				return;
			}
			sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "" + user.getPlayer().getName() + "'s");
			sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Client is: " + user.getClientBrand() + " "
					+ PacketEvents.getAPI().getPlayerManager().getClientVersion(user.getPlayer()).getProtocolVersion() + " (" + PacketEvents.getAPI().getPlayerManager().getClientVersion(user.getPlayer()).getProtocolVersion()
					+ ")");
			sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Selected AntiCheat: " + user.getAntiCheat());
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}
}
