package me.killstorm103.Rebug.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class Menu extends Command
{
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args)
	{
		final List<String> t = new ArrayList<>();
		t.clear();
		if (args.length == 2)
		{
			t.add("crashers");
			t.add("exploits");
			t.add("items");
			t.add("settings");
			t.add("ac");
			
			return t;
		}
		if (args.length == 3)
			return PT.getPlayerNames ();
		
		return null;
	}

	@Override
	public boolean HasCustomTabComplete() {
		return true;
	}
	@Override
	public boolean HideFromCommandsList() {
		return false;
	}
	@Override
	public boolean HasToBeConsole() {
		return false;
	}

	@Override
	public String[] SubAliases() 
	{
		String[] s = {"/crashers", "/exploits", "/items", "/settings", "/ac"};
		
		return s;
	}
	
	@Override
	public String getName() {
		return "menu";
	}

	@Override
	public String getSyntax() {
		return "menu <menu name> <...args>";
	}

	@Override
	public String getDescription() {
		return "opens a menu";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String getPermission() {
		return StartOfPermission() + "menu";
	}
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (args.length < 2)
		{
			Log(sender, getSyntax());
			return;
		}
		if (sender instanceof Player)
		{
			if (args.length < 2)
			{
				sender.sendMessage(Rebug.RebugMessage + getSyntax());
				return;
			}
			User user = Rebug.getUser((Player) sender);
			if (user == null)
			{
				PT.KickPlayer((Player) sender, PT.RebugsUserWasNullErrorMessage("when trying to use the rebug menu command"));
				return;
			}
			String menu = args[1].replace("%", " ").replace("_", " ").replace("-", " ");
			
			if (menu.equalsIgnoreCase("ac") || menu.equalsIgnoreCase("items") || menu.equalsIgnoreCase("settings") || menu.equalsIgnoreCase("Rebug Settings") || menu.equalsIgnoreCase("Vanilla Fly Checks")) {}
			else
			{
				if (args.length >= 3)
				{
					if (user.CommandTarget == null)
					{
						user.CommandTarget = Bukkit.getPlayer(args[2]);
						if (user.CommandTarget == null)
						{
							Log(sender, Rebug.RebugMessage + "Unknown Player!");
							return;
						}
					}
				}
				else
					user.CommandTarget = user.getPlayer();
				
			}
			switch (menu.toLowerCase())
			{
			case "ac":
				if (user.getPlayer().hasPermission("me.killstorm103.rebug.user.select_anticheat") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin") || user.getPlayer().isOp())
					user.getPlayer().openInventory(ItemsAndMenusUtils.INSTANCE.getAntiCheats());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				break;
				
			case "crashers":
				if (user.getPlayer().hasPermission("me.killstorm103.rebug.user.use_crashers") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin") || user.getPlayer().isOp())
				{
					if (user.CommandTarget == user.getPlayer() || user.CommandTarget != user.getPlayer() && (user.hasPermission("me.killstorm103.rebug.user.use_crashers.others") || user.hasPermission("me.killstorm103.rebug.server_owner") || user.hasPermission("me.killstorm103.rebug.server_admin") || user.getPlayer().isOp()))
						user.getPlayer().openInventory(user.getCrashers());
					else
						user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use this on other players!");
				}
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				break;
				
			case "exploits":
				if (user.getPlayer().hasPermission("me.killstorm103.rebug.user.use_exploits") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin") || user.getPlayer().isOp())
				{
					if (user.CommandTarget == user.getPlayer() || user.CommandTarget != user.getPlayer() && (user.hasPermission("me.killstorm103.rebug.user.use_exploits.others") || user.hasPermission("me.killstorm103.rebug.server_owner") || user.hasPermission("me.killstorm103.rebug.server_admin") || user.getPlayer().isOp()))
						user.getPlayer().openInventory(user.getExploits());
					else
						user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use this on other players!");
				}
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				break;
				
			case "vanilla fly checks":
				if (user.getPlayer().hasPermission("me.killstorm103.rebug.user.player_settings") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin") || user.getPlayer().isOp())
					user.getPlayer().openInventory(user.getVanillaFlyChecks());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				break;
				
			case "settings":
				if (user.getPlayer().hasPermission("me.killstorm103.rebug.user.player_settings") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin") || user.getPlayer().isOp())
					user.getPlayer().openInventory(user.getSettings());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				break;
				
			case "items":
				if (user.getPlayer().hasPermission("me.killstorm103.rebug.user.menu.items") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin") || user.getPlayer().isOp())
					user.getPlayer().openInventory(ItemsAndMenusUtils.INSTANCE.getItemPickerMenu());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				break;
				
			case "rebug settings":
				if (user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin") || user.getPlayer().isOp())
					user.getPlayer().openInventory(user.getRebugSettingsMenu());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to do that!");
				break;

			default:
				break;
			}
			user.lore.clear();
		}
		else
			Log(sender, Rebug.RebugMessage + "Only player's can run this command!");
	}
}
