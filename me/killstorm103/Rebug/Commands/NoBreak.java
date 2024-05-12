package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;

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
			if (player.getItemInHand() != null)
			{
				ItemStack Item = player.getItemInHand(), Repair = PT.RepairItem(Item);
                ItemMeta ItemM = Item.getItemMeta();
                Item.setDurability(Repair.getDurability());
                ItemM.spigot().setUnbreakable(!ItemM.spigot().isUnbreakable());
                Item.setItemMeta(ItemM);
                player.setItemInHand(Item);
                player.updateInventory();
				player.sendMessage(Rebug.RebugMessage + " Your item is now " + (player.getItemInHand().getItemMeta().spigot().isUnbreakable() ? "unbreakable" : "breakable") + "!");
			}
			else
				player.sendMessage(Rebug.RebugMessage + "You don't have a item to make unbreakable/breakable!");
		}
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
