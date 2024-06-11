package me.killstorm103.Rebug.Commands;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class InvSeeCMD extends Command
{

	@Override
	public String getName() {
		return "invsee";
	}

	@Override
	public String getSyntax() {
		return "invsee <player>";
	}

	@Override
	public String getDescription() {
		return "see players invs";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "invseecmd";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String[] SubAliases()
	{
		String[] s = {"/invsee"};
		return s;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			User user = Rebug.getUser(player);
			if (user == null)
			{
				player.sendMessage(PT.RebugsUserWasNullErrorMessage("in invsee - cmd"));
				return;
			}
			if (args.length < 2)
			{
				user.getPlayer().sendMessage(Rebug.RebugMessage + getSyntax());
				return;
			}
			Player target = Bukkit.getPlayer(args[1]);
			if (target == null)
			{
				user.getPlayer().sendMessage(Rebug.RebugMessage + "Unknown Player!");
				return;
			}
			if (user.getPlayer().isOp() || user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin"))
			{
				if (target == user.getPlayer())
				{
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You can't target yourself!");
					user.InvSeed = false;
					user.CommandTarget = null;
					return;
				}
				user.CommandTarget = target;
				user.getPlayer().updateInventory();
				user.OldItems.put(user.getPlayer().getInventory().getItem(8), 8);
				user.OldItems.put(user.getPlayer().getInventory().getItem(17), 17);
				for (Map.Entry<ItemStack, Integer> map : User.TempItemsInvSee.entrySet())
				{
					user.getPlayer().getInventory().setItem(map.getValue(), map.getKey());
				}
				user.getPlayer().updateInventory();
				user.getPlayer().openInventory(user.CommandTarget.getInventory());
				user.InvSeed = true;
			}
			else
				player.sendMessage(Rebug.RebugMessage + "You don't have perms to do that!");
		}
		else
			sender.sendMessage(Rebug.RebugMessage + "Only players can run this command!");
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
