package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;

public class ConsoleMessage extends Command
{

	@Override
	public String getName() {
		return "consolemessage";
	}

	@Override
	public String getSyntax() {
		return getName() + " <player> <message>";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String getDescription() {
		return "Message a player as Console!";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "conesolemessage";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (args.length >= 2)
		{
			Player player = Rebug.getGetMain().getServer().getPlayer(args[1]);
			if (player == null)
			{
				sender.sendMessage(Rebug.RebugMessage + "Unknown Player!");
				return;
			}
			String msg = PT.SubString(command, player.getName().length() + 1, command.length());
			Bukkit.dispatchCommand(Rebug.getGetMain().getServer().getConsoleSender(), "tell " + player.getName() + " " + msg);
		}
		else
			sender.sendMessage(getSyntax());
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean HasToBeConsole() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] SubAliases() {
		// TODO Auto-generated method stub
		return null;
	}

}
