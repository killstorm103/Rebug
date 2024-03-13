package me.killstorm103.Rebug.Commands;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class getInfo extends Command
{

	@Override
	public String getName() {
		return "getinfo";
	}

	@Override
	public String getSyntax() {
		return "getinfo <command or server>";
	}

	@Override
	public String getDescription() {
		return "get info about a command or the server";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "getinfo";
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) throws Exception 
	{
		if (args.length == 1)
		{
			Log(sender, getSyntax());
			return;
		}
		Server server = getRebug().getServer();
		if (args[1].equalsIgnoreCase("command"))
		{
			if (args.length < 4)
			{
				Log(sender, "/rebug getinfo command <command name> <info>");
				return;
			}
			Command cmd = Rebug.getGetMain().getCommandByName(args[2]);
			if (cmd == null)
			{
				Log(sender, "Unknown command!");
				return;
			}
			if (!args[3].equalsIgnoreCase("Syntax") && !args[3].equalsIgnoreCase("Description") && !args[3].equalsIgnoreCase("Permission"))
			{
				Log(sender, "<info= Description, Permission, Syntax>");
				return;
			}
			
			if (args[3].equalsIgnoreCase("Description"))
			{
				Log(sender, cmd.getName() + "'s Description: " + cmd.getDescription());
			}
			if (args[3].equalsIgnoreCase("Permission"))
			{
				Log(sender, cmd.getName() + "'s Permission: " + cmd.getPermission());
			}
			if (args[3].equalsIgnoreCase("Syntax"))
			{
				Log(sender, cmd.getName() + "'s Syntax: " + cmd.getSyntax());
			}
		}
		if (args[1].equalsIgnoreCase("server"))
		{
			Log(sender, "BukkitVersion: " + server.getBukkitVersion());
			Log(sender, "Version: " + server.getVersion());
		}
		if (args[1].equalsIgnoreCase("player"))
		{
			if (args.length < 2)
			{
				Log(sender, "/rebug getinfo player <player name>");
				return;
			}
			Player player = server.getPlayer(args[2]);
			if (player == null)
			{
				Log(sender, "Player not found!");
				return;
			}
			if (args.length < 3)
			{
				Log(sender, "/rebug getinfo player <player name> <info= op, allowedEdit, allowedFlight>");
				return;
			}
			if (args[3].equalsIgnoreCase("op"))
			{
				Log(sender, "is " + player.getName() + " op= " + player.isOp());
			}
			if (args[3].equalsIgnoreCase("allowedFlight"))
			{
				Log(sender, "is " + player.getName() + " AllowFlight= " + player.getAllowFlight());
			}
			if (args[3].equalsIgnoreCase("Location"))
			{
				Log(sender, "is " + player.getName() + " Location= " + player.getLocation());
			}
		}
	}
	
}
