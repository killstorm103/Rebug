package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
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
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (args.length < 2)
		{
			sender.sendMessage(Rebug.RebugMessage + getSyntax());
			return;
		}
		Player player = Bukkit.getServer().getPlayer(args[1]);
		if (player == null)
		{
			sender.sendMessage(Rebug.RebugMessage + " player was not found!");
			return;
		}
		sender.sendMessage(Rebug.RebugMessage + player.getName() + "'s ip is: " + player.getAddress().getHostName() + ":" + player.getAddress().getPort());
	}
	@Override
	public String getPermission() {
		return "me.killstorm103.rebug.commands.getip";
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

	@Override
	public String[] SubAliases() {
		return null;
	}

}
