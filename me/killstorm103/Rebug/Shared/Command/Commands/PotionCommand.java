package me.killstorm103.Rebug.Shared.Command.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import me.killstorm103.Rebug.Shared.Command.Command;
import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Shared.Utils.User;

public class PotionCommand extends Command {

	@Override
	public String getName() {
		return "potion";
	}

	@Override
	public String getSyntax() {
		return "potion level <level> | potion timer <seconds>";
	}

	@Override
	public String getDescription() {
		return "Potion Settings";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "potion_settings";
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "/potion" };
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (sender instanceof Player) {
			if (args.length < 3) {
				sender.sendMessage(Rebug.RebugMessage + getSyntax());
				return;
			}
			Player player = (Player) sender;
			User user = Rebug.getUser(player);
			if (user == null) {
				player.sendMessage(Rebug.RebugMessage + "Try again Later User was null!");
				return;
			}
			switch (args[1].toLowerCase()) {
			case "level":
				try {
					int level = Integer.parseInt(args[2]);
					int max = Rebug.getINSTANCE().getConfig().getInt("potion-settings-max-level");
					level = level > max ? max : level < 1 ? 1 : level;
					user.potionlevel = level;
					user.sendMessage(Rebug.RebugMessage + "Changed the Level of the potions to " + user.potionlevel);
					for (int i = 0; i < user.PotionsMenu.getSize(); i++) {
						ItemStack item = user.PotionsMenu.getItem(i);
						if (item != null && item.getType() != Material.MILK_BUCKET && item.hasItemMeta()
								&& item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore()) {
							for (int i2 = 0; i2 < item.getItemMeta().getLore().size(); i2++) {
								if (item.getItemMeta().getLore().get(i2) != null
										&& item.getItemMeta().getLore().get(i2).contains("Level")) {
									ItemsAndMenusUtils.INSTANCE.UpdateMenuLore(user.PotionsMenu, i, i2,
											ChatColor.BLUE + "Level: " + user.potionlevel);
									break;
								}
							}
						}
					}
				} catch (Exception e) {
					user.sendMessage(Rebug.RebugMessage + "you have to put a number!");
				}

				break;

			case "timer":
				try {
					int seconds = Integer.parseInt(args[2]);
					int max = Rebug.getINSTANCE().getConfig().getInt("potion-settings-max-timer");
					seconds = seconds > max ? max : seconds < 1 ? 1 : seconds;
					user.potion_effect_seconds = seconds;
					user.sendMessage(Rebug.RebugMessage + "Changed the Seconds you have the potion effects for to "
							+ user.potion_effect_seconds);
					for (int i = 0; i < user.PotionsMenu.getSize(); i++) {
						ItemStack item = user.PotionsMenu.getItem(i);
						if (item != null && item.getType() != Material.MILK_BUCKET && item.hasItemMeta()
								&& item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore()) {
							for (int i2 = 0; i2 < item.getItemMeta().getLore().size(); i2++) {
								if (item.getItemMeta().getLore().get(i2) != null
										&& item.getItemMeta().getLore().get(i2).contains("Seconds")) {
									ItemsAndMenusUtils.INSTANCE.UpdateMenuLore(user.PotionsMenu, i, i2,
											ChatColor.BLUE + "Seconds: " + user.potion_effect_seconds);
									break;
								}
							}
						}
					}
				} catch (Exception e) {
					user.sendMessage(Rebug.RebugMessage + "you have to put a number!");
				}

				break;

			default:
				user.sendMessage(Rebug.RebugMessage + getSyntax());
				break;
			}

		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		if (args.length == (alias.equalsIgnoreCase("rebug") ? 2 : 1))
		{
			List<String> list = new ArrayList<>(), list2 = new ArrayList<>();
			list.clear();
			list2.clear();
			list2.add("level");
			list2.add("timer");
			StringUtil.copyPartialMatches(args[args.length], list2, list);
			
			return list;
		}

		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias)
	{
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
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}

}
