package me.killstorm103.Rebug.Shared.Command.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.killstorm103.Rebug.Shared.Command.Command;
import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Shared.Utils.User;
import me.killstorm103.Rebug.SoftWares.Spigot.Utils.PT_Spigot;

public class Menu extends Command 
{
	public static final List<String> TabAntiCheats = new ArrayList<>();

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias) 
	{
		List<String> list = new ArrayList<>(), list2 = new ArrayList<>();
		list.clear();
		list2.clear();
		boolean rebug = alias.equalsIgnoreCase("rebug");
		if (alias.equalsIgnoreCase("ac") || rebug && args.length > 1 && args[1].equalsIgnoreCase("ac"))
		{
			if (alias.equalsIgnoreCase("ac") && args.length >= 2 || rebug && args.length >= 4)
			{
				list2.addAll(TabAntiCheats);
				list2.remove(0);
				StringUtil.copyPartialMatches(args[args.length - 1], list2, list);
				return list;
			}
			
			StringUtil.copyPartialMatches(args[args.length - 1], TabAntiCheats, list);
			return list;
		}
		if (args.length >= 3 && (alias.equalsIgnoreCase("settings") || args[1].equalsIgnoreCase("settings")) && sender instanceof Player)
		{
			// TODO Make it so users can change their setting by /rebug menu settings <setting> <value> and /settings <setting> <value>
			list2.add("Hunger");
			list2.add("FallDamage");
			list2.add("ExterranlDamage");
			list2.add("DamageResistance");
			list2.add("FireResistance");
			StringUtil.copyPartialMatches(args[args.length - 1], list2, list);
			return list;
		}

		if (args.length == 2 && rebug) 
		{
			list2.add("exploits");
			list2.add("items");
			list2.add("potions");
			list2.add("settings");
			list2.add("ac");
			StringUtil.copyPartialMatches(args[args.length - 1], list2, list);
			return list;
		}

		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		boolean rebug = alias.equalsIgnoreCase("rebug");
		if (!TabAntiCheats.isEmpty() && (alias.equalsIgnoreCase("ac") || rebug && args.length > 1 && args[1].equalsIgnoreCase("ac")))
			return true;

		if (args.length == 2 && rebug)
			return true;

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

		return true;
	}

	@Override
	public Types getType() {
		return Types.Player;
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "/exploits", "/items", "/settings", "/ac", "/potions" };
	}

	@Override
	public String getName() {
		return "menu";
	}

	@Override
	public String getSyntax() {
		return "menu <menu name> <...args>";
	}

	@Override
	public String getDescription() {
		return "opens a menu";
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "menu";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (args.length < 2) {
			Log(sender, getSyntax());
			return;
		}
		if (sender instanceof Player) {
			if (args.length < 2) {
				sender.sendMessage(Rebug.RebugMessage + getSyntax());
				return;
			}
			User user = Rebug.getUser((Player) sender);
			if (user == null) {
				Rebug.getINSTANCE().getPT().KickPlayer((Player) sender,
						PT_Spigot.RebugsUserWasNullErrorMessage("when trying to use the rebug menu command"));
				return;
			}
			String menu = args[1].replace("%", " ").replace("_", " ").replace("-", " ");

			if (menu.equalsIgnoreCase("ac") || menu.equalsIgnoreCase("potions") || menu.equalsIgnoreCase("items")
					|| menu.equalsIgnoreCase("settings") || menu.equalsIgnoreCase("Rebug Settings")
					|| menu.equalsIgnoreCase("Vanilla Fly Checks")) {
			} else {
				if (args.length >= 3) {
					if (user.getCommandTarget(true) == null
							|| !user.getCommandTarget(false).getName().equals(args[2])) {
						user.Target = null;
						Player temp = Bukkit.getPlayer(args[2]);
						if (temp == null) {
							Log(sender, "Unknown Player!");
							return;
						}
						user.Target = temp.getUniqueId();
						temp = null;
					}
				} else
					user.Target = user.getUUID();
			}
			switch (menu.toLowerCase()) {
			case "potions":
				if (user.hasPermission("me.killstorm103.rebug.user.select_potions")
						|| Rebug.hasAdminPerms(user.getPlayer()))
					user.getPlayer().openInventory(user.getPotionsMenu());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");

				break;

			case "ac":
				if (user.hasPermission("me.killstorm103.rebug.user.select_anticheat")
						|| Rebug.hasAdminPerms(user.getPlayer())) {
					if (args.length >= 3) {
						Rebug.getINSTANCE().UpdateAntiCheat(user, args.length > 3 ? args : new String[] { args[2] },
								null, null, args.length > 3);
						return;
					}
					user.getPlayer().openInventory(ItemsAndMenusUtils.INSTANCE.getAntiCheats());
				} else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");

				break;

			case "exploits":
				if ((user.hasPermission("me.killstorm103.rebug.user.use_exploits")
						|| user.hasPermission("me.killstorm103.rebug.user.use_crashers"))
						|| Rebug.hasAdminPerms(user.getPlayer())) {
					if (user.getCommandTarget(true) == null) {
						user.sendMessage("Your Command Target was null so they must of left the server!");
						return;
					}

					if (user.getCommandTarget(false) == user.getPlayer()
							|| user.getCommandTarget(false) != user.getPlayer()
									&& (user.hasPermission("me.killstorm103.rebug.user.use_exploits.others")
											|| user.hasPermission("me.killstorm103.rebug.user.use_crashers.others")
											|| Rebug.hasAdminPerms(user.getPlayer())))
						user.getPlayer().openInventory(user.getExploits());
					else
						user.sendMessage("You don't have permission to use this on other players!");
				} else
					user.sendMessage("You don't have permission to use that!");

				break;

			case "vanilla fly checks":
				if (user.hasPermission("me.killstorm103.rebug.user.player_settings")
						|| Rebug.hasAdminPerms(user.getPlayer()))
					user.getPlayer().openInventory(user.getVanillaFlyChecks());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				break;

			case "settings":
				if (user.hasPermission("me.killstorm103.rebug.user.player_settings")
						|| Rebug.hasAdminPerms(user.getPlayer()))
					user.getPlayer().openInventory(user.getSettings());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				break;

			case "items":
				if (user.hasPermission("me.killstorm103.rebug.user.menu.items")
						|| Rebug.hasAdminPerms(user.getPlayer()))
					user.getPlayer().openInventory(ItemsAndMenusUtils.INSTANCE.getItemPickerMenu());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				break;

			case "rebug settings":
				if (Rebug.hasAdminPerms(user.getPlayer()))
					user.getPlayer().openInventory(ItemsAndMenusUtils.INSTANCE.getRebugSettingsMenu());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to do that!");
				break;

			default:
				break;
			}
		}
	}

	@Override
	public boolean RemoveSlash() {
		return true;
	}

}
