package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class NoBreak extends Command
{

	@Override
	public String getName() {
		return "nobreak";
	}

	@Override
	public String getSyntax() {
		return "nobreak";
	}

	@Override
	public String getDescription() {
		return "adds/removes the unbreakable effect from a given item!";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "nobreak";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String[] SubAliases() 
	{
		String[] s = {"/nobreak"};
		return s;
	}
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			User user = Rebug.getUser(player);
			if (user.getPlayer().getItemInHand() != null)
			{
				ItemStack Item = user.getPlayer().getItemInHand(), Repair = PT.RepairItem(Item);
				if (Item.getItemMeta().hasDisplayName() && Item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Teleport Bow")) 
				{
					player.sendMessage(Rebug.RebugMessage + "You can't use this command on this item!");
					return;
				}
                ItemMeta ItemM = Item.getItemMeta();
                Item.setDurability(Repair.getDurability());
                ItemM.spigot().setUnbreakable(!ItemM.spigot().isUnbreakable());
                Item.setItemMeta(ItemM);
                user.getPlayer().setItemInHand(Item);
                user.getPlayer().updateInventory();
                user.getPlayer().sendMessage(Rebug.RebugMessage + " Your item is now " + (player.getItemInHand().getItemMeta().spigot().isUnbreakable() ? "unbreakable" : "breakable") + "!");
			}
			else
				user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have a item to make unbreakable/breakable!");
		}
		else
			sender.sendMessage(Rebug.RebugMessage + "Only Players can run this command!");
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
