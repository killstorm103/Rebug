package me.killstorm103.Rebug.Shared.Command.Commands;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Shared.Command.Command;
import me.killstorm103.Rebug.Shared.Main.Config;
import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Utils.User;


public class ClientCMD extends Command {

	@Override
	public String getName() {
		return "client";
	}

	@Override
	public String getSyntax() {
		return "client <nothing to target yourself or a player's name to target them>";
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public String getDescription() {
		return "tell's you the client brand of the given player!";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "client";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		String brand;
		User commander_user, user_target;
		Player commander, target;
		if (sender instanceof Player)
		{
			commander = (Player) sender;
			if (args.length < 2)
			{
				commander_user = Rebug.getUser(commander);
				if (commander_user == null)
				{
					commander.sendMessage(Rebug.RebugMessage + "User was somehow Null!");
					return;
				}
				brand = commander_user.getBrand();
				if (brand == null || commander_user.BrandSetCount < 1 || brand.length() <= 0)
				{
					if (Config.getClientInfoSetting().equalsIgnoreCase("warn"))
						commander_user.sendMessage("failed to load your client brand this may cause issues!");
	    			
	    			else if (Config.getClientInfoSetting().equalsIgnoreCase("kick")) 
	    			{
	    				Rebug.getINSTANCE().getPT().KickPlayer(commander_user.getPlayer(), Rebug.RebugMessage + "kicked you due to failing to load your client brand!");
	    				return;
	    			}
	    			else if (Config.getClientInfoSetting().equalsIgnoreCase("ban"))
	    			{
	    				Rebug.getINSTANCE().getPT().KickPlayer(commander_user.getPlayer(), Rebug.RebugMessage + "banned you due to failing to load your client brand!");
	    				return;
	    			}
				}
				commander_user.Target = commander_user.getUUID();
				commander_user.getPlayer().closeInventory();
				commander_user.getPlayer().openInventory(commander_user.getClientInfoMenu());
				return;
			}
			if (commander.hasPermission(getPermission() + "_others") || Rebug.hasAdminPerms(commander))
			{
				commander_user = Rebug.getUser(commander);
				if (commander_user == null)
				{
					commander.sendMessage(Rebug.RebugMessage + "Sender's User was somehow Null!");
					return;
				}
				target = Bukkit.getPlayer(args[1]);
				if (target == null)
				{
					Log(sender, "Unknown Player!");
					return;
				}
				user_target = Rebug.getUser(target);
				if (user_target == null)
				{
					Log(sender, "Unknown User!");
					return;
				}
				brand = user_target.getBrand();
				if (brand == null || user_target.BrandSetCount < 1 || brand.length() <= 0)
				{
					if (Config.getClientInfoSetting().equalsIgnoreCase("warn"))
						commander_user.sendMessage("Failed to load " + user_target.getName() + "'s client brand this may cause issues!");
	    			
	    			if (Config.getClientInfoSetting().equalsIgnoreCase("kick")) 
	    			{
	    				Rebug.getINSTANCE().getPT().KickPlayer(user_target.getPlayer(), Rebug.RebugMessage + "kicked you due to failing to load your client brand!");
	    				return;
	    			}
	    			if (Config.getClientInfoSetting().equalsIgnoreCase("ban"))
	    			{
	    				Rebug.getINSTANCE().getPT().KickPlayer(user_target.getPlayer(), Rebug.RebugMessage + "banned you due to failing to load your client brand!");
	    				return;
	    			}
				}
				
				commander_user.Target = user_target.getUUID();
				commander_user.getPlayer().closeInventory();
				commander_user.getPlayer().openInventory(commander_user.getClientInfoMenu());
			}
			else
				Log(sender, "You don't have Permission to use this on other players!");
		}
		else
		{
			if (args.length < 2)
			{
				Log(sender, getSyntax());
				return;
			}
			target = Bukkit.getPlayer(args[1]);
			if (target == null)
			{
				Log(sender, Rebug.RebugMessage + "Unknow Player!");
				return;
			}
			user_target = Rebug.getUser(target);
			if (user_target == null)
			{
				Log(sender, Rebug.RebugMessage + "User was somehow Null!");
				return;
			}
			brand = user_target.getBrand();
			if (brand == null || user_target.BrandSetCount < 1 || brand.length() <= 0)
			{
				if (Config.getClientInfoSetting().equalsIgnoreCase("warn"))
					Rebug.getINSTANCE().Log(Level.WARNING, "Failed to load your client brand this may cause issues!");
    			
    			if (Config.getClientInfoSetting().equalsIgnoreCase("kick")) 
    			{
    				Rebug.getINSTANCE().getPT().KickPlayer(user_target.getPlayer(), Rebug.RebugMessage + "kicked you due to failing to load your client brand!");
    				return;
    			}
    			if (Config.getClientInfoSetting().equalsIgnoreCase("ban"))
    			{
    				Rebug.getINSTANCE().getPT().KickPlayer(user_target.getPlayer(), Rebug.RebugMessage + "banned you due to failing to load your client brand!");
    				return;
    			}
			}
			Log(sender, Rebug.RebugMessage + ChatColor.RED + user_target.getName() + "'s " + ChatColor.AQUA + "client " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + brand + " " + user_target.getVersion_Short() + " (" + user_target.getProtocol() + ")");
			Log(sender, Rebug.RebugMessage + ChatColor.RED + user_target.getName() + "'s " + ChatColor.AQUA + "register " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + user_target.getRegister());
			// TODO Add mods list when Made!
			
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
	public boolean HideFromCommandsList(CommandSender sender) {
		boolean s = true;
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (Rebug.hasAdminPerms(player) || player.hasPermission(getPermission()))
				s = false;
		} else
			s = false;

		return s;
	}

	@Override
	public Types getType() {
		return Types.AnySender;
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "/client" };
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}

}
