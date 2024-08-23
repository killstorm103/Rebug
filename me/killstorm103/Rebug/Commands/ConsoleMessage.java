package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

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
			Player player = Rebug.getINSTANCE().getServer().getPlayer(args[1]);
			if (player == null)
			{
				sender.sendMessage(Rebug.RebugMessage + "Unknown Player!");
				return;
			}
			//Bukkit.dispatchCommand(Rebug.getINSTANCE().getServer().getConsoleSender(), "tell " + player.getName() + " " + );
		}
		else
			sender.sendMessage(getSyntax());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias) {
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return false;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) 
	{
		boolean s = true;
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (Rebug.hasAdminPerms(player) || player.hasPermission(getPermission()))
				s = false;
		}
		else
			s = false;
		
		return s;
	}

	@Override
	public boolean HasToBeConsole() {
		return false;
	}

	@Override
	public String[] SubAliases() 
	{
		return null;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
}
