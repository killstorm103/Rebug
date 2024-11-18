package me.killstorm103.Rebug.Shared.Command.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.killstorm103.Rebug.Shared.Command.Command;
import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Utils.User;

public class SetUserAntiCheat extends Command {

	@Override
	public String getName() {
		return "setuseranticheat";
	}

	@Override
	public String getSyntax() {
		return "setuseranticheat <player> <ac>";
	}

	@Override
	public String getDescription() {
		return "Manually set the AntiCheat of a user!";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "set-user-anticheat";
	}

	@Override
	public String[] SubAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (sender instanceof Player) 
		{
			Player p = (Player) sender;
			if (!Rebug.hasAdminPerms(p) && !p.hasPermission(getPermission())) {
				p.sendMessage(Rebug.RebugMessage + "You don't have Permission to do that!");
				return;
			}
		}
		if (args.length < 3) {
			sender.sendMessage(Rebug.RebugMessage + getSyntax());
			return;
		}

		Player player = Bukkit.getPlayer(args[1]);
		if (player == null) {
			sender.sendMessage(Rebug.RebugMessage + "Unknown Player!");
			return;
		}
		User user = Rebug.getUser(player);
		if (user == null) {
			sender.sendMessage(Rebug.RebugMessage + "Unknown User (User was null)!");
			return;
		}
		Rebug.getINSTANCE().UpdateAntiCheat(user, args.length > 3 ? args : new String[] { args[2] }, null, sender,args.length > 3);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		if (args.length > 2 && !Menu.TabAntiCheats.isEmpty())
		{
			final List<String> list = new ArrayList<>();
			list.clear();
			StringUtil.copyPartialMatches(args[args.length - 1], Menu.TabAntiCheats, list);
			return list;
		}

		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		if (args.length > 2 && !Menu.TabAntiCheats.isEmpty())
			return true;

		return false;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || Rebug.hasAdminPerms(player))
				return false;
		} 

		return false;
	}

	@Override
	public Types getType() {
		return Types.AnySender;
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
