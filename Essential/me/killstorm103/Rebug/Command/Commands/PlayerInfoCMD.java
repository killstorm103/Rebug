package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.retrooper.packetevents.PacketEvents;

import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Main.RebugPlugin;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class PlayerInfoCMD extends Command {

	@Override
	public String getName() {
		return "player";
	}

	@Override
	public String getSyntax() {
		return "player | player <player name>";
	}

	@Override
	public String getDescription() {
		return "get info about a player";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "playerinfocmd";
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "/player" };
	}
	// TODO Improve how much info you get

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		User user;
		if (sender instanceof Player) {
			if (args.length < 2) {
				user = RebugPlugin.getUser((Player) sender);
				user.sendMessage(RebugPlugin.getRebugMessage() + "" + user.getPlayer().getName());
				user.sendMessage(RebugPlugin.getRebugMessage() + "Your client is: " + user.getClientBrand() + " " + PacketEvents.getAPI().getPlayerManager().getClientVersion(user.getPlayer()).getReleaseName() + " ("
				+ PacketEvents.getAPI().getPlayerManager().getClientVersion(user.getPlayer()).getProtocolVersion() + ")");
				user.sendMessage(RebugPlugin.getRebugMessage() + "Selected AntiCheat: " + user.getAntiCheat());
			} else {
				user = RebugPlugin.getUser(Bukkit.getPlayer(args[1]));
				if (user == null) {
					sender.sendMessage(RebugPlugin.getRebugMessage() + "Unknown Player!");
					return;
				}
				sender.sendMessage(RebugPlugin.getRebugMessage() + user.getPlayer().getName() + "'s");
				sender.sendMessage(RebugPlugin.getRebugMessage() + "Client is: " + user.getClientBrand() + " "
						+ PacketEvents.getAPI().getPlayerManager().getClientVersion(user.getPlayer()).getReleaseName() + " ("
						+ PacketEvents.getAPI().getPlayerManager().getClientVersion(user.getPlayer()).getProtocolVersion() + ")");
				sender.sendMessage(RebugPlugin.getRebugMessage() + "Selected AntiCheat: " + user.getAntiCheat());
			}
		} else {
			if (args.length < 2) {
				sender.sendMessage(RebugPlugin.getRebugMessage() + "player <player name>");
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if (player == null) {
				Log(sender, "Unknown Player!");
				return;
			}

			user = RebugPlugin.getUser(player);
			if (user == null) {
				Log(sender, "Unknown User!");
				return;
			}
			sender.sendMessage(RebugPlugin.getRebugMessage() + "" + user.getPlayer().getName() + "'s");
			sender.sendMessage(RebugPlugin.getRebugMessage() + "Client is: " + user.getClientBrand() + " "
					+ PacketEvents.getAPI().getPlayerManager().getClientVersion(user.getPlayer()).getProtocolVersion() + " (" + PacketEvents.getAPI().getPlayerManager().getClientVersion(user.getPlayer()).getProtocolVersion()
					+ ")");
			sender.sendMessage(RebugPlugin.getRebugMessage() + "Selected AntiCheat: " + user.getAntiCheat());
		}
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
