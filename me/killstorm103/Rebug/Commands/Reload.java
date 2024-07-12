package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class Reload extends Command
{

	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public String getSyntax() {
		return "reload <config or server>";
	}

	@Override
	public String getDescription() {
		return "reload";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "reload_command";
	}

	@Override
	public String[] SubAliases() 
	{
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		if (args.length < 2)
		{
			sender.sendMessage(Rebug.RebugMessage + getSyntax());
			return;
		}
		
		if (args[1].equalsIgnoreCase("config"))
		{
			Rebug.getINSTANCE().Reload_Configs(sender instanceof Player ? Rebug.getUser((Player) sender) : null);
		}
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
	public boolean hasCommandCoolDown() {
		return false;
	}
	
}
