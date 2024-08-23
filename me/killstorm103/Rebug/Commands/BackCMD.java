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
	public boolean hasCommandCoolDown () 
	{
		return false;
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
			if (user == null) return;
			
			if (user.death_location != null)
			{
				user.getPlayer().teleport(user.death_location);
				user.death_location = null;
			}
			else
				user.getPlayer().sendMessage(Rebug.RebugMessage + ChatColor.GRAY + "You don't have a last death location!");
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias) 
	{
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
		return new String[] {"/back"};
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
}
