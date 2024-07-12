package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;

public class Repair extends Command
{

	@Override
	public String getName() {
		return "repair";
	}

	@Override
	public String getSyntax() {
		return "repair";
	}

	@Override
	public String getDescription() {
		return "repairs items";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "repair";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return true;
	}
	@Override
	public String[] SubAliases() 
	{
		return new String[] {"/repair"};
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
				if (Item.getDurability() != Repair.getDurability())
				{
					player.setItemInHand(Repair);
					player.updateInventory();
					player.sendMessage(Rebug.RebugMessage + "Repaired your item!");
				}
				else
					player.sendMessage(Rebug.RebugMessage + "Your item doesn't need to be repaired!");
			}
			else
				player.sendMessage(Rebug.RebugMessage + " You haven't got a item in your hand to repair!");
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
