package me.killstorm103.Rebug.Commands;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
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
	public void onCommand(CommandSender sender, String[] args) throws Exception 
	{
		String brand;
		User user;
		if (args.length < 2)
		{
			if (sender instanceof Player)
			{
				Player player = (Player) sender;
				user = Rebug.getUser(player);
				brand = user.getBrand();
				if (brand == null || brand.equalsIgnoreCase("%kick%"))
				{
					player.kickPlayer("Rebug kicked you due to failing to load your client brand!");
					return;
				}
				sender.sendMessage(ChatColor.RED + "Your " + ChatColor.AQUA + "client " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + brand + " " + PT.getPlayerVersion(Rebug.getUser(player).getProtocol()) + " (" + PT.getPlayerVersion(Rebug.getUser(player).getPlayer()) + ")");
			}
			else
				sender.sendMessage("Server cannot have client info!!");
			
			return;
		}
		Player player = Rebug.getGetMain().getServer().getPlayer(args[1]);
		if (player == null)
		{
			sender.sendMessage("Unknown Player!");
			return;
		}
		user = Rebug.getUser(player);
		brand = user.ClientBrand();
		if (brand == null || brand.equalsIgnoreCase("%kick%"))
		{
			sender.sendMessage("Rebug kicked " + player.getName() + " due to failing to load their client brand!");
			player.kickPlayer("Rebug kicked you due to failing to load your client brand!");
			return;
		}
		sender.sendMessage(ChatColor.RED + player.getName() + "'s " + ChatColor.AQUA + "client brand " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + brand + " " + PT.getPlayerVersion(Rebug.getUser(player).getProtocol()) + " (" + PT.getPlayerVersion(Rebug.getUser(player).getPlayer()) + ")");
	}
	
}
