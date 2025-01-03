package me.killstorm103.Rebug.Shared.Command.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.killstorm103.Rebug.Shared.Command.Command;
import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Utils.User;

public class PacketDebugger extends Command {

	@Override
	public String getName() {
		return "packetdebugger";
	}

	@Override
	public String getSyntax() {
		return "packetdebugger <toggle, config>";
	}

	@Override
	public String getDescription() {
		return "debug packets";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "packetdebugger";
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "/packetdebugger" };
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (args.length < 2) {
			sender.sendMessage(Rebug.RebugMessage + getSyntax());
			return;
		}

		User user = Rebug.getUser((Player) sender);
		if (user == null)
			return;

		if (args[1].equalsIgnoreCase("toggle")) {
			if (!Rebug.hasAdminPerms(user.getPlayer())
					&& !user.hasPermission("me.killstorm103.rebug.user.packet_debugger.use")) {
				user.sendMessage("You don't have Permission to use this!");
				return;
			}
			if (!user.AllowedToDebug) {
				user.sendMessage("This client doesn't allow you to debug it's modules!");
				return;
			}
			if (Rebug.PacketDebuggerPlayers.containsKey(user.getPlayer().getUniqueId()))
				Rebug.PacketDebuggerPlayers.remove(user.getPlayer().getUniqueId(),
						Rebug.PacketDebuggerPlayers.get(user.getPlayer().getUniqueId()));
			else
				Rebug.PacketDebuggerPlayers.put(user.getPlayer().getUniqueId(), 1);

			user.sendMessage(
					"PacketDebugger " + (Rebug.PacketDebuggerPlayers.containsKey(user.getPlayer().getUniqueId())
							? ChatColor.GREEN + "Enabled"
							: ChatColor.DARK_RED + "Disabled") + ChatColor.GRAY + "!");
		} else if (args[1].equalsIgnoreCase("config"))
			user.getPlayer().openInventory(user.getPacketDebuggerMenu());

		else
			user.sendMessage(getSyntax());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		if (args.length == (alias.equalsIgnoreCase("rebug") ? 2 : 1)) 
		{
			final List<String> list = new ArrayList<>(), list2 = new ArrayList<>();
			list.clear ();
			list2.clear();
			list2.add("toggle");
			list2.add("config");
			StringUtil.copyPartialMatches(args[args.length - 1], list2, list);

			return list;
		}

		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias)
	{
		return args.length == 1 || args.length == 2;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || Rebug.hasAdminPerms(player))
				return false;
		}

		return true;
	}

	@Override
	public Types getType() {
		return Types.Player;
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
}
