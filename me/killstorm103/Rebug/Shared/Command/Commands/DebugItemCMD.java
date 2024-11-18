package me.killstorm103.Rebug.Shared.Command.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Shared.Command.Command;
import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Utils.ItemsAndMenusUtils;

public class DebugItemCMD extends Command
{

	@Override
	public String getName() {
		return "debugitem";
	}

	@Override
	public String getSyntax() {
		return "debugitem | debugitem <player>";
	}

	@Override
	public String getDescription() {
		return "debug items";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "debugitem";
	}

	@Override
	public String[] SubAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			player.closeInventory();
			player.openInventory(ItemsAndMenusUtils.INSTANCE.GetDebugItemMenu());
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return false;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || Rebug.hasAdminPerms(player))
				return false;
			
			return true;
		}
		
		return false;
	}

	@Override
	public Types getType() 
	{
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
