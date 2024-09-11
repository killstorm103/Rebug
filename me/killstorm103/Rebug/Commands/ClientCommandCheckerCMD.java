package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class ClientCommandCheckerCMD extends Command
{

	@Override
	public String getName() {
		return "clientcommandchecker";
	}

	@Override
	public String getSyntax() {
		return "clientcommandchecker <nothing or player name>";
	}

	@Override
	public String getDescription() {
		return "checks if the player has client commands";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "clientcommandchecker_cmd";
	}

	@Override
	public String[] SubAliases() {
		return new String[] {"/clientcommandchecker"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		if (sender instanceof Player)
		{
			User user = null;
			Player p = (Player) sender;
			if (args.length > 1)
			{
				if (!p.hasPermission("me.killstorm103.rebug.user.use_clientcommandchecker.others") && !Rebug.hasAdminPerms(p))
				{
					p.sendMessage(Rebug.RebugMessage + "You don't have permission to use this on other players!");
					return;
				}
				Player player = Bukkit.getPlayer(args[1]);
				if (player == null)
				{
					Log(sender, "Unknown Player!");
					return;
				}
				user = Rebug.getUser(player);
			}
			else
				user = Rebug.getUser(p);
			
			if (user == null)
			{
				Log(sender, "Unknown User!");
				return;
			}
			Rebug.getINSTANCE().checkPlayer(user, true);
			return;
		}
		if (args.length < 2)
		{
			Log(sender, "as the Server this as to be ran on a player!");
			return;
		}
		Player player = Bukkit.getPlayer(args[1]);
		if (player == null)
		{
			Log(sender, "Unknown Player!");
			return;
		}
		User user = Rebug.getUser(player);
		if (user == null)
		{
			Log(sender, "Unknown User!");
			return;
		}
		Rebug.getINSTANCE().checkPlayer(user, true);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
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
	public Types getType ()
	{
		return Types.AnySender;
	}

	@Override
	public boolean hasCommandCoolDown()
	{
		return false;
	}

	@Override
	public boolean RemoveSlash() 
	{
		return false;
	}
	
}
