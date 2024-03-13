package me.killstorm103.Rebug.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class GetIP extends Command
{

	@Override
	public String getName() {
		return "getip";
	}

	@Override
	public String getSyntax() {
		return "getip <player>";
	}

	@Override
	public String getDescription() {
		return "get's the ip of the player";
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) throws Exception {
		if (args.length < 2)
		{
			sender.sendMessage(getSyntax());
			return;
		}
		Player player = Rebug.getGetMain().getServer().getPlayer(args[1]);
		sender.sendMessage(sender.getName() + " " + player.getName() + "'s ip is: " + player.getAddress().getHostName() + ":" + player.getAddress().getPort());
	}
	@Override
	public String getPermission() {
		return "me.killstorm103.rebug.commands.getip";
	}
	
}
