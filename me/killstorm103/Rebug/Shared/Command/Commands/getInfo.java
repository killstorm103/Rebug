package me.killstorm103.Rebug.Shared.Command.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.killstorm103.Rebug.Shared.Command.Command;
import me.killstorm103.Rebug.Shared.Main.Rebug;

public class getInfo extends Command {

	@Override
	public String getName() {
		return "getinfo";
	}

	@Override
	public String getSyntax() {
		return "getinfo <command or server>";
	}

	@Override
	public String getDescription() {
		return "get info about a command or the server";
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "getinfo";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (args.length == 1) {
			Log(sender, Rebug.RebugMessage + getSyntax());
			return;
		}
		Server server = Bukkit.getServer();
		if (args[1].equalsIgnoreCase("command")) {
			
			if (args.length < 4) {
				Log(sender, ChatColor.GRAY + "/rebug getinfo command <command name> <info>");
				return;
			}
			Command cmd = Rebug.getINSTANCE().getCommandByName(args[2]);
			if (cmd == null) {
				Log(sender, Rebug.RebugMessage + "Unknown command!");
				return;
			}
			if (!args[3].equalsIgnoreCase("Syntax") && !args[3].equalsIgnoreCase("Description")
					&& !args[3].equalsIgnoreCase("Permission")) {
				Log(sender, ChatColor.GRAY + "<info= Description, Permission, Syntax>");
				return;
			}

			if (args[3].equalsIgnoreCase("Description")) {
				Log(sender, cmd.getName() + "'s Description: " + cmd.getDescription(), false);
			}
			if (args[3].equalsIgnoreCase("Permission")) {
				Log(sender, cmd.getName() + "'s Permission: " + cmd.getPermission(), false);
			}
			if (args[3].equalsIgnoreCase("Syntax")) {
				Log(sender, cmd.getName() + "'s Syntax: " + cmd.getSyntax(), false);
			}
		}
		if (args[1].equalsIgnoreCase("server")) {
			Log(sender, Rebug.RebugMessage + "BukkitVersion: " + server.getBukkitVersion(), false);
			Log(sender, Rebug.RebugMessage + "Version: " + server.getVersion(), false);
		}
		if (args[1].equalsIgnoreCase("player")) {
			if (args.length < 2) {
				Log(sender, ChatColor.GRAY + "/rebug getinfo player <player name>");
				return;
			}
			Player player = server.getPlayer(args[2]);
			if (player == null) {
				Log(sender, Rebug.RebugMessage + "Player not found!");
				return;
			}
			if (args.length < 3) {
				Log(sender,
						ChatColor.GRAY + "/rebug getinfo player <player name> <info= op, allowedEdit, allowedFlight>");
				return;
			}
			if (args[3].equalsIgnoreCase("op")) {
				Log(sender, Rebug.RebugMessage + "is " + player.getName() + " op= " + player.isOp());
			}
			if (args[3].equalsIgnoreCase("allowedFlight")) {
				Log(sender, Rebug.RebugMessage + "is " + player.getName() + " AllowFlight= " + player.getAllowFlight());
			}
			if (args[3].equalsIgnoreCase("Location")) {
				Log(sender, Rebug.RebugMessage + "is " + player.getName() + " Location= " + player.getLocation());
			}
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) 
	{
		final List<String> list = new ArrayList<>(), list2 = new ArrayList<>();
		list.clear();
		list2.clear ();
		if (args.length == 2)
		{
			list2.add("player");
			list2.add("command");
			list2.add("server");
			StringUtil.copyPartialMatches(args[args.length - 1], list2, list);
			return list;
		}
		if (args.length > 2)
		{
			if (args[1].equalsIgnoreCase("player") && args.length == 4)
			{
				list2.add("op");
				list2.add("allowedFlight");
				list2.add("Location");
				StringUtil.copyPartialMatches(args[args.length - 1], list2, list);
				return list;
			}
			if (args[1].equalsIgnoreCase("command") && (args.length == 3 || args.length == 4))
			{
				if (args.length == 3)
					StringUtil.copyPartialMatches(args[args.length - 1], Rebug.getINSTANCE().getCMDList(), list);
				
				else
				{
					list2.add("Syntax");
					list2.add("Permission");
					list2.add("Description");
					StringUtil.copyPartialMatches(args[args.length - 1], list2, list);
				}
				
				return list;
			}
		}
		
		
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) 
	{
		if (args.length == 2) return true;
		
		if (args.length > 2)
		{
			if (args[1].equalsIgnoreCase("player") && args.length == 4 || args[1].equalsIgnoreCase("command") && (args.length == 3 || args.length == 4)) return true;
		}
		
		return false;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || Rebug.hasAdminPerms(player))
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
	public String[] SubAliases() {
		return null;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
}
