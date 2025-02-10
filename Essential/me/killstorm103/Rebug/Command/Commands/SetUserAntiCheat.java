package me.killstorm103.Rebug.Command.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.QuickConfig;
import me.killstorm103.Rebug.Utils.User;

public class SetUserAntiCheat extends Command
{

	@Override
	public String getName() {
		return "setuseranticheat";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "setuseranticheat <player> <ac>");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "Manually set the AntiCheat of a user!");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "set-user-anticheat");
	}
	@Override
	public String[] SubAliases() {
		return new String[] {"setuseranticheat"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (sender instanceof Player) 
		{
			Player p = (Player) sender;
			if (!PT.hasPermission(p, getPermission())) {
				p.sendMessage(RebugPlugin.getRebugMessage() + "You don't have Permission to do that!");
				return;
			}
		}
		if (args.length < 3) {
			sender.sendMessage(RebugPlugin.getRebugMessage() + getUsage());
			return;
		}

		Player player = Bukkit.getPlayer(args[1]);
		if (player == null) {
			sender.sendMessage(RebugPlugin.getRebugMessage() + "Unknown Player!");
			return;
		}
		User user = RebugPlugin.getUser(player);
		if (user == null) 
		{
			sender.sendMessage(RebugPlugin.getRebugMessage() + "Unknown User (User was null)!");
			return;
		}
		PT.UpdateAntiCheat(user, args.length > 3 ? args : new String[] { args[2] }, null, sender,args.length > 3);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias)
	{
		List<String> list = new ArrayList<>(), list2 = new ArrayList<>();
		list.clear();
		list2.clear();
		boolean rebug = alias.equalsIgnoreCase(QuickConfig.MainLabel());
		if (alias.equalsIgnoreCase(getName()) || rebug)
		{
			if (ItemsAndMenusUtils.TabAntiCheats.isEmpty()) ItemsAndMenusUtils.getINSTANCE().getAntiCheats();

			if (alias.equalsIgnoreCase(getName()) && args.length == 1) return null;
			
			if (alias.equalsIgnoreCase(getName()) && args.length > 2 || rebug && args.length >= 3)
			{
				list2.addAll(ItemsAndMenusUtils.TabAntiCheats);
				list2.remove(0);
				StringUtil.copyPartialMatches(args[args.length - 1], list2, list);
				return list;
			}
			list2.addAll(ItemsAndMenusUtils.TabAntiCheats);
			StringUtil.copyPartialMatches(args[args.length - 1], list2, list);
			return list;
		}
		
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		if (alias.equalsIgnoreCase(getName()) || alias.equalsIgnoreCase(QuickConfig.MainLabel()))
			return true;
		
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
}
