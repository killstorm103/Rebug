package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class Enchant extends Command
{

	@Override
	public String getName() {
		return "enchant";
	}

	@Override
	public String getSyntax() {
		return "enchant <id> <level>";
	}

	@Override
	public String getDescription() {
		return "enchant any item in your hand!";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String getPermission() {
		return StartOfPermission() + "enchant";
	}

	@Override
	public String[] SubAliases() 
	{
		return new String[] {"/enchant", "/enchantment"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (args.length == 3)
			{
				if (player.getItemInHand() == null)
				{
					player.sendMessage(Rebug.RebugMessage + "You must have a item in your hand!");
					return;
				}
				if (!PT.isNumber_Integer(args[1])) 
				{
					player.sendMessage(Rebug.RebugMessage + "You must put in a number!");
					return;
				}
				User user = Rebug.getUser(player);
				if (user == null)
				{
					player.sendMessage(PT.RebugsUserWasNullErrorMessage("When trying to enchant!"));
					return;
				}
				if (user.getPlayer().getItemInHand().getItemMeta().hasDisplayName() && user.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Teleport Bow")) 
				{
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You can't enchant this item!");
					return;
				}
				ItemStack item = player.getItemInHand(), Copy = item.clone();
				int level;
				@SuppressWarnings("deprecation")
				Enchantment enchantment = Enchantment.getById(Integer.parseInt(args[1]));
				if (enchantment == null)
				{
					user.getPlayer().sendMessage(Rebug.RebugMessage + "Unknown Enchantment!");
					return;
				}
				String name = enchantment.getName().toLowerCase();
				
				if (PT.isNumber_Integer(args[2]))
				{
					int Added;
					level = Integer.parseInt(args[2]);
					if (!item.getEnchantments().isEmpty() && item.getEnchantments().containsKey(enchantment))
					{
						Added = 1;
						item.removeEnchantment(enchantment);
						if (Copy.getEnchantments().containsKey(enchantment) && !Copy.getEnchantments().containsValue(level))
						{
							if (level > enchantment.getMaxLevel() && !player.isOp() && !player.hasPermission("me.killstorm103.rebug.server_owner") && !player.hasPermission("me.killstorm103.rebug.server_admin"))
							{
								user.getPlayer().sendMessage(Rebug.RebugMessage + name + "'s Max Level Reached you can't go > " + enchantment.getMaxLevel() + " you don't have perms!");
								item.setItemMeta(item.getItemMeta());
								user.getPlayer().setItemInHand(item);
								user.getPlayer().updateInventory();
								user.getPlayer().sendMessage(Rebug.RebugMessage + (Added == 3 ? "Added " + name + " " + level + " to your item!" : Added == 2 ? "Updated " + name : "Removed your item's " + name + " Enchantment!"));
								return;
							}
							Added = 2;
							item.addUnsafeEnchantment(enchantment, level);
							item.getItemMeta().addEnchant(enchantment, level, true);
						}
					}
					else
					{
						if (level > enchantment.getMaxLevel() && !player.isOp() && !player.hasPermission("me.killstorm103.rebug.server_owner") && !player.hasPermission("me.killstorm103.rebug.server_admin"))
						{
							user.getPlayer().sendMessage(Rebug.RebugMessage + name + "'s Max Level Reached you can't go > " + enchantment.getMaxLevel() + " you don't have perms!");
							return;
						}
						Added = 3;
						item.addUnsafeEnchantment(enchantment, level);
						item.getItemMeta().addEnchant(enchantment, level, true);
					}
					
					item.setItemMeta(item.getItemMeta());
					user.getPlayer().setItemInHand(item);
					user.getPlayer().updateInventory();
					
					user.getPlayer().sendMessage(Rebug.RebugMessage + (Added == 3 ? "Added " + name + " " + level + " to your item!" : Added == 2 ? "Updated " + name : "Removed your item's " + name + " Enchantment!"));
				}
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You must put in a number!");
			}
			else
				player.sendMessage(Rebug.RebugMessage + getSyntax());
		}
		else
			sender.sendMessage(Rebug.RebugMessage + "Your not able to use this command only players can!");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args) {
		return null;
	}

	@Override
	public boolean HasCustomTabComplete() {
		return false;
	}

	@Override
	public boolean HideFromCommandsList() {
		return false;
	}

	@Override
	public boolean HasToBeConsole() {
		return false;
	}
	
}
