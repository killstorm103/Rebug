package me.killstorm103.Rebug.Command.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Main.RebugPlugin;
import me.killstorm103.Rebug.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class AntiCheatMenuCMD extends Command
{

	@Override
	public @NotNull String getName() {
		return "ac";
	}

	@Override
	public @NotNull String getSyntax() {
		return "ac | ac <anticheat> | ac (anticheat) (anticheat) (...)";
	}

	@Override
	public @NotNull String getDescription() {
		return "opens a menu to select a AntiCHeat!";
	}

	@Override
	public @NotNull String getPermission() {
		return StartOfPermission() + "anticheat_select";
	}

	@Override
	public String[] SubAliases() {
		return new String[] {"ac"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		User user = RebugPlugin.getUser((Player) sender);
		if (user == null)
		{
			sender.sendMessage(PT.RebugsUserWasNullErrorMessage("anticheat menu cmd"));
			return;
		}
		if (args.length >= 2) 
		{
			PT.UpdateAntiCheat(user, args.length > 2 ? args : new String[] { args[1] }, null, null, args.length > 2);
			return;
		}
		user.getPlayer().openInventory(ItemsAndMenusUtils.getINSTANCE().getAntiCheats());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias)
	{
		List<String> list = new ArrayList<>(), list2 = new ArrayList<>();
		list.clear();
		list2.clear();
		boolean rebug = alias.equalsIgnoreCase("rebug");
		if (alias.equalsIgnoreCase("ac") || rebug)
		{
			if (ItemsAndMenusUtils.TabAntiCheats.isEmpty()) ItemsAndMenusUtils.getINSTANCE().getAntiCheats();
			
			if (alias.equalsIgnoreCase("ac") && args.length >= 2 || rebug && args.length >= 3)
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
		if (alias.equalsIgnoreCase("ac") || alias.equalsIgnoreCase("rebug"))
			return true;
		
		return false;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || PT.hasAdminPerms(player))
				return false;
		}
		
		return true;
	}

	@Override
	public @NotNull Types getType() {
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
