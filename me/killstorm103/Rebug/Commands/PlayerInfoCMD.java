package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PTNormal;
import me.killstorm103.Rebug.Utils.User;

public class PlayerInfoCMD extends Command
{

	@Override
	public String getName() {
		return "player";
	}

	@Override
	public String getSyntax() {
		return "player | player <player name>";
	}

	@Override
	public String getDescription() {
		return "get info about a player";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "playerinfocmd";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String[] SubAliases() 
	{
		return new String[] {"/player"};
	}
	// TODO Improve how much info you get

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		User user;
		if (sender instanceof Player)
		{
			if (args.length < 2)
			{
				user = Rebug.getUser((Player) sender);
				user.sendMessage(Rebug.RebugMessage + "" + user.getPlayer().getName());
				user.sendMessage(Rebug.RebugMessage + "Your client is: " + user.ClientBrand() + " " + PTNormal.getPlayerVersion(user.getProtocol()) + " (" + PTNormal.getPlayerVersion(user.getPlayer()) + ")");
				user.sendMessage(Rebug.RebugMessage + "Selected AntiCheat: " + user.AntiCheat);
			}
			else
			{
				user = Rebug.getUser(Bukkit.getPlayer(args[1]));
				if (user == null)
				{
					sender.sendMessage(Rebug.RebugMessage + "Unknown Player!");
					return;
				}
				sender.sendMessage(Rebug.RebugMessage + user.getPlayer().getName() + "'s");
				sender.sendMessage(Rebug.RebugMessage + "Client is: " + user.ClientBrand() + " " + PTNormal.getPlayerVersion(user.getProtocol()) + " (" + PTNormal.getPlayerVersion(user.getPlayer()) + ")");
				sender.sendMessage(Rebug.RebugMessage + "Selected AntiCheat: " + user.AntiCheat);
			}
		}
		else
		{
			if (args.length < 2)
			{
				sender.sendMessage(Rebug.RebugMessage + "player <player name>");
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if (player == null)
			{
				Log(sender, "Unknown Player!");
				return;
			}
			
			user = Rebug.getUser(player);
			if (user == null)
			{
				Log(sender, "Unknown User!");
				return;
			}
			sender.sendMessage(Rebug.RebugMessage + "" + user.getPlayer().getName() + "'s");
			sender.sendMessage(Rebug.RebugMessage + "Client is: " + user.ClientBrand() + " " + PTNormal.getPlayerVersion(user.getProtocol()) + " (" + PTNormal.getPlayerVersion(user.getPlayer()) + ")");
			sender.sendMessage(Rebug.RebugMessage + "Selected AntiCheat: " + user.AntiCheat);
		}
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
	public boolean RemoveSlash() {
		return false;
	}
}
