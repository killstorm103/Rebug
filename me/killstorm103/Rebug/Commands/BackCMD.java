package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class BackCMD extends Command
{

	@Override
	public String getName() {
		return "back";
	}

	@Override
	public String getSyntax() {
		return "back";
	}

	@Override
	public String getDescription() {
		return "Teleports you back to where you died!";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "back";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (sender instanceof Player)
		{
			User user = Rebug.getUser((Player) sender);
			if (user != null)
			{
				if (user.getDeath_location() != null)
					user.getPlayer().teleport(user.getDeath_location());
				else
					user.getPlayer().sendMessage(Rebug.RebugMessage + ChatColor.GRAY + "You don't have a last death location!");
			}
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args) 
	{
		return null;
	}

	@Override
	public boolean HasCustomTabComplete() {
		return false;
	}

	@Override
	public boolean HideFromCommandsList() {
		return true;
	}

	@Override
	public boolean HasToBeConsole() {
		return false;
	}

	@Override
	public String[] SubAliases() 
	{
		String[] s = {"/back"};
		
		return s;
	}

}
