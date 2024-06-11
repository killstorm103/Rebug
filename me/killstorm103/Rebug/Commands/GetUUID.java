package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class GetUUID extends Command
{

	@Override
	public String getName()
	{
		return "getuuid";
	}

	@Override
	public String getSyntax() {
		return "getuuid | getuuid <player>";
	}

	@Override
	public String getDescription()
	{
		return "gets the uuid of the given player";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String getPermission() {
		return StartOfPermission() + "getuuid";
	}

	@Override
	public String[] SubAliases() 
	{
		String[] s = {"/getuuid"};
		return s;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		Player player = null, target = null;
		if (sender instanceof Player)
		{
			player = (Player) sender;
			if (player.isOp() || player.hasPermission("me.killstorm103.rebug.server_owner") || player.hasPermission("me.killstorm103.rebug.server_admin"))
			{
				if (args.length < 2)
					player.sendMessage(Rebug.RebugMessage + player.getName() + "'s UUID: " + player.getUniqueId());
				else
				{
					target = Bukkit.getPlayer(args[1]);
					target = target == null ? Bukkit.getOfflinePlayer(args[1]).getPlayer() : target;
					if (target == null)
					{
						player.sendMessage(Rebug.RebugMessage + "Unknown Player!");
						return;
					}
					player.sendMessage(Rebug.RebugMessage + target.getName() + "'s UUID: " + target.getUniqueId());
				}
			}
			else
				player.sendMessage(Rebug.RebugMessage + "You can't run this command unless you have owner/admin perms!");
		}
		else
		{
			if (args.length < 2)
			{
				sender.sendMessage(Rebug.RebugMessage + "getuuid <player>");
				return;
			}
			target = Bukkit.getPlayer(args[1]);
			target = target == null ? Bukkit.getOfflinePlayer(args[1]).getPlayer() : target;
			if (target == null)
			{
				sender.sendMessage(Rebug.RebugMessage + "Unknown Player!");
				return;
			}
			sender.sendMessage(Rebug.RebugMessage + target.getName() + "'s UUID: " + target.getUniqueId());
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
