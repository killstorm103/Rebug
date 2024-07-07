package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.User;

public class ResetUserMenus extends Command
{

	@Override
	public String getName() {
		return "resetusermenus";
	}

	@Override
	public String getSyntax() {
		return "resetusermenus <user>";
	}

	@Override
	public String getDescription() {
		return "reset the menus of rebug users";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String getPermission() {
		return StartOfPermission() + "resetusermenus";
	}

	@Override
	public String[] SubAliases() 
	{
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		if (args.length < 2)
		{
			sender.sendMessage(Rebug.RebugMessage + getSyntax());
			return;
		}
		User user = Rebug.getUser(Bukkit.getPlayer(args[1]));
		if (user == null)
		{
			sender.sendMessage(Rebug.RebugMessage + "Unknown User!");
			return;
		}
		user.OldInventory = user.CrashersMenu = user.ExploitsMenu = user.SettingsMenu = user.VanillaFlyChecksMenu = ItemsAndMenusUtils.INSTANCE.getRebugSettingsMenu = user.SpawnEntityCrashersMenu = null;
		sender.sendMessage(Rebug.RebugMessage + "Successfully Reset " + user.getPlayer().getName() + "'s Menus!");
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
