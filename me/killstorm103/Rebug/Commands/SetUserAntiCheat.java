package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class SetUserAntiCheat extends Command
{

	@Override
	public String getName() {
		return "setuseranticheat";
	}

	@Override
	public String getSyntax() {
		return "setuseranticheat <player> <ac>";
	}

	@Override
	public String getDescription() {
		return "Manually set the AntiCheat of a user!";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "set-user-anticheat";
	}

	@Override
	public String[] SubAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			if (!Rebug.hasAdminPerms(p) && !p.hasPermission(getPermission()))
			{
				p.sendMessage(Rebug.RebugMessage + "You don't have Permission to do that!");
				return;
			}
		}
		if (args.length < 3)
		{
			sender.sendMessage (Rebug.RebugMessage + getSyntax());
			return;
		}
		
		Player player = Bukkit.getPlayer(args[1]);
		if (player == null)
		{
			sender.sendMessage(Rebug.RebugMessage + "Unknown Player!");
			return;
		}
		User user = Rebug.getUser(player);
		if (user == null)
		{
			sender.sendMessage(Rebug.RebugMessage + "Unknown User!");
			return;
		}
		Rebug.getINSTANCE().UpdateAntiCheat(user, args[2], null, sender);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) 
	{
		if (args.length > 2 && !Menu.TabAntiCheats.isEmpty())
			return Menu.TabAntiCheats;
		
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) 
	{
		if (args.length > 2 && !Menu.TabAntiCheats.isEmpty())
			return true;
		
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
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
	
}
