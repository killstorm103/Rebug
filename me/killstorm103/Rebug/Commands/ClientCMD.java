package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class ClientCMD extends Command
{

	@Override
	public String getName ()
	{
		return "client";
	}

	@Override
	public String getSyntax ()
	{
		return "client <nothing to target yourself or a player's name to target them>";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String getDescription ()
	{
		return "tell's you the client brand of the given player!";
	}

	@Override
	public String getPermission () 
	{
		return StartOfPermission() + "client";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		String brand;
		User user;
		Player player;
		if (args.length < 2)
		{
			if (sender instanceof Player)
			{
			    player = (Player) sender;
				user = Rebug.getUser(player);
				if (user == null)
				{
					PT.KickPlayer(player, PT.RebugsUserWasNullErrorMessage("in ClientCMD Line 57"));
					return;
				}
				
				brand = user.getBrand();
				if (brand == null || user.BrandSetCount < 1 || brand.length() <= 0)
				{
					if (Config.getClientInfoSetting().equalsIgnoreCase("warn"))
	    				user.getPlayer().sendMessage(Rebug.RebugMessage + "failed to load your client brand this may cause issues!");
	    			
	    			else if (Config.getClientInfoSetting().equalsIgnoreCase("kick")) 
	    				PT.KickPlayer(user.getPlayer(), Rebug.RebugMessage + "kicked you due to failing to load your client brand!");
	    			
	    			else if (Config.getClientInfoSetting().equalsIgnoreCase("ban"))
	    				PT.BanPlayer(user.getPlayer(), Rebug.RebugMessage + "banned you due to failing to load your client brand!");
					
					return;
				}
				sender.sendMessage(Rebug.RebugMessage + ChatColor.RED + "Your " + ChatColor.AQUA + "client " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + brand + " " + user.getVersion_Short() + " (" + user.getProtocol() + ")");
				sender.sendMessage(Rebug.RebugMessage + ChatColor.RED + "Your " + ChatColor.AQUA + "register " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + user.getRegister());
			}
			else
				sender.sendMessage(Rebug.RebugMessage + "Server cannot have client info!!");
			
			return;
		}
		player = Bukkit.getServer().getPlayer(args[1]);
		if (player == null)
		{
			sender.sendMessage(Rebug.RebugMessage + "Unknown Player!");
			return;
		}
		user = Rebug.getUser(player);
		if (user == null)
		{
			PT.KickPlayer(player, PT.RebugsUserWasNullErrorMessage("in ClientCMD Line 91"));
			return;
		}
		
		brand = user.ClientBrand();
		if (brand == null || user.BrandSetCount < 1 || brand.length() <= 0)
		{
			if (Config.getClientInfoSetting().equalsIgnoreCase("warn"))
			{
				sender.sendMessage(Rebug.RebugMessage + "warned " + user.getPlayer().getName() + " Rebug failed to get their client brand and this may cause issues!");
				user.getPlayer().sendMessage("Rebug failed to load your client brand this may cause issues!");
			}
			else if (Config.getClientInfoSetting().equalsIgnoreCase("kick")) 
			{
				sender.sendMessage(Rebug.RebugMessage + "kicked " + user.getPlayer().getName() + " due to failing to load their client brand!");
				PT.KickPlayer(user.getPlayer(), Rebug.RebugMessage + "kicked you due to failing to load your client brand!");
			}
			
			else if (Config.getClientInfoSetting().equalsIgnoreCase("ban"))
			{
				sender.sendMessage(Rebug.RebugMessage + "banned " + user.getPlayer().getName() + " due to failing to load their client brand!");
				PT.BanPlayer(user.getPlayer(), Rebug.RebugMessage + "banned you due to failing to load your client brand!");
			}
			
			return;
		}
		sender.sendMessage(Rebug.RebugMessage + ChatColor.RED + user.getPlayer().getName() + "'s " + ChatColor.AQUA + "client " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + brand + " " + user.getVersion_Short() + " (" + user.getProtocol() + ")");
		sender.sendMessage(Rebug.RebugMessage + ChatColor.RED + user.getPlayer().getName() + "'s " + ChatColor.AQUA + "register " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + user.getRegister());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args) 
	{
		return null;
	}

	@Override
	public boolean HasCustomTabComplete() 
	{
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

	@Override
	public String[] SubAliases() 
	{
		return new String[] {"/client"};
	}
}
