package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;

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
	public String getPermission() {
		return StartOfPermission() + "enchant";
	}

	@Override
	public String[] SubAliases() 
	{
		String[] s = {"/enchant", "/enchantment"};
		return s;
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
				ItemStack item = player.getItemInHand(), Copy = item.clone();
				int level;
				@SuppressWarnings("deprecation")
				Enchantment enchantment = Enchantment.getById(Integer.parseInt(args[1]));
				if (enchantment == null)
				{
					player.sendMessage(Rebug.RebugMessage + "Unknown Enchantment!");
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
							if (level > enchantment.getMaxLevel() && !player.isOp())
							{
								player.sendMessage(Rebug.RebugMessage + name + "'s Max Level Reached you can't go > " + enchantment.getMaxLevel() + " unless your opped!");
								item.setItemMeta(item.getItemMeta());
								player.setItemInHand(item);
								player.updateInventory();
								player.sendMessage(Rebug.RebugMessage + (Added == 3 ? "Added " + name + " " + level + " to your item!" : Added == 2 ? "Updated " + name : "Removed your item's " + name + " Enchantment!"));
								return;
							}
							Added = 2;
							item.addUnsafeEnchantment(enchantment, level);
							item.getItemMeta().addEnchant(enchantment, level, true);
						}
					}
					else
					{
						if (level > enchantment.getMaxLevel() && !player.isOp())
						{
							player.sendMessage(Rebug.RebugMessage + name + "'s Max Level Reached you can't go > " + enchantment.getMaxLevel() + " unless your opped!");
							return;
						}
						Added = 3;
						item.addUnsafeEnchantment(enchantment, level);
						item.getItemMeta().addEnchant(enchantment, level, true);
					}
					
					item.setItemMeta(item.getItemMeta());
					player.setItemInHand(item);
					player.updateInventory();
					
					player.sendMessage(Rebug.RebugMessage + (Added == 3 ? "Added " + name + " " + level + " to your item!" : Added == 2 ? "Updated " + name : "Removed your item's " + name + " Enchantment!"));
				}
				else
					player.sendMessage(Rebug.RebugMessage + "You must put in a number!");
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
