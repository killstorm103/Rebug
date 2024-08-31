package me.killstorm103.Rebug.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.PTNormal;
import me.killstorm103.Rebug.Utils.User;

public class Menu extends Command
{
	public static final List<String> TabAntiCheats = new ArrayList<>();
	
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias)
	{
		List<String> t = new ArrayList<>();
		t.clear();
		boolean rebug = alias.equalsIgnoreCase("rebug");
		if (alias.equalsIgnoreCase("ac") || rebug && args.length > 1 && args[1].equalsIgnoreCase("ac"))
		{
			if (TabAntiCheats.isEmpty()) return null;
			
			return TabAntiCheats;
		}
		
		if (args.length == 2 && rebug)
		{
			t.add("crashers");
			t.add("exploits");
			t.add("items");
			t.add("potions");
			t.add("settings");
			t.add("ac");
			
			return t;
		}
		
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) 
	{
		boolean rebug = alias.equalsIgnoreCase("rebug");
		if (alias.equalsIgnoreCase("ac") || rebug && args.length > 1 && args[1].equalsIgnoreCase("ac")) return true;
		
		if (args.length == 2 && rebug)
			return true;
		
		return false;
	}
	@Override
	public boolean HideFromCommandsList (CommandSender sender)
	{
		return false;
	}
	@Override
	public boolean HasToBeConsole() {
		return false;
	}

	@Override
	public String[] SubAliases() 
	{
		return new String[] {"/crashers", "/exploits", "/items", "/settings", "/ac", "/potions"};
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
				PTNormal.KickPlayer((Player) sender, PTNormal.RebugsUserWasNullErrorMessage("when trying to use the rebug menu command"));
				return;
			}
			String menu = args[1].replace("%", " ").replace("_", " ").replace("-", " ");
			
			if (menu.equalsIgnoreCase("ac") || menu.equalsIgnoreCase("potions") || menu.equalsIgnoreCase("items") || menu.equalsIgnoreCase("settings") || menu.equalsIgnoreCase("Rebug Settings") || menu.equalsIgnoreCase("Vanilla Fly Checks")) {}
			else
			{
				if (args.length >= 3)
				{
					if (user.CommandTarget == null || !user.CommandTarget.getName().equals(args[2]))
					{
						user.CommandTarget = null;
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
			case "potions":
				if (Rebug.hasAdminPerms(user.getPlayer()) || user.hasPermission("me.killstorm103.rebug.user.select_potions"))
					user.getPlayer().openInventory(user.getPotionsMenu());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
					
				break;
				
			case "ac":
				if (user.hasPermission("me.killstorm103.rebug.user.select_anticheat") || Rebug.hasAdminPerms(user.getPlayer()))
				{
					if (args.length >= 3)
					{
						Rebug.getINSTANCE().UpdateAntiCheat(user, args.length > 3 ? args : new String[] {args[2]}, null, null, args.length > 3);
						return;
					}
					user.getPlayer().openInventory(ItemsAndMenusUtils.INSTANCE.getAntiCheats());
				}
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				
				break;
				
			case "crashers":
				if (user.hasPermission("me.killstorm103.rebug.user.use_crashers") || Rebug.hasAdminPerms(user.getPlayer()))
				{
					if (user.CommandTarget == user.getPlayer() || user.CommandTarget != user.getPlayer() && (user.hasPermission("me.killstorm103.rebug.user.use_crashers.others") || Rebug.hasAdminPerms(user.getPlayer())))
						user.getPlayer().openInventory(user.getCrashers());
					else
						user.sendMessage("You don't have permission to use this on other players!");
				}
				else
					user.sendMessage("You don't have permission to use that!");
				break;
				
			case "exploits":
				if (user.hasPermission("me.killstorm103.rebug.user.use_exploits") || Rebug.hasAdminPerms(user.getPlayer()))
				{
					if (user.CommandTarget == user.getPlayer() || user.CommandTarget != user.getPlayer() && (user.hasPermission("me.killstorm103.rebug.user.use_exploits.others") || Rebug.hasAdminPerms(user.getPlayer())))
						user.getPlayer().openInventory(user.getExploits());
					else
						user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use this on other players!");
				}
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				break;
				
			case "vanilla fly checks":
				if (user.hasPermission("me.killstorm103.rebug.user.player_settings") || Rebug.hasAdminPerms(user.getPlayer()))
					user.getPlayer().openInventory(user.getVanillaFlyChecks());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				break;
				
			case "settings":
				if (user.getPlayer().hasPermission("me.killstorm103.rebug.user.player_settings") || Rebug.hasAdminPerms(user.getPlayer()))
					user.getPlayer().openInventory(user.getSettings());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				break;
				
			case "items":
				if (user.getPlayer().hasPermission("me.killstorm103.rebug.user.menu.items") || Rebug.hasAdminPerms(user.getPlayer()))
					user.getPlayer().openInventory(ItemsAndMenusUtils.INSTANCE.getItemPickerMenu());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to use that!");
				break;
				
			case "rebug settings":
				if (Rebug.hasAdminPerms(user.getPlayer()))
					user.getPlayer().openInventory(ItemsAndMenusUtils.INSTANCE.getRebugSettingsMenu());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have permission to do that!");
				break;

			default:
				break;
			}
		}
		else
			Log(sender, Rebug.RebugMessage + "Only player's can run this command!");
	}

	@Override
	public boolean RemoveSlash() {
		return true;
	}

}
