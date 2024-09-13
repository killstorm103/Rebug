package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class CheckAC extends Command
{

	@Override
	public String getName() {
		return "checkac";
	}

	@Override
	public String getSyntax() {
		return "checkac | checkac <user>";
	}

	@Override
	public String getDescription() {
		return "check the ac of a user";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "checkac";
	}

	@Override
	public String[] SubAliases() {
		return new String[] {"/checkac"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		if (sender instanceof Player)
		{
			User user = null;
			if (args.length > 1)
			{
				Player player = Bukkit.getPlayer(args[1]);
				if (player == null)
				{
					Log(sender, "Unknown Player!");
					return;
				}
				user = Rebug.getUser(player);
			}
			else
				user = Rebug.getUser((Player) sender);
			
			if (user == null)
			{
				Log(sender, "Unknown User!");
				return;
			}
			Log(sender, user.getName() + "'s AC" + (user.SelectedAntiCheats > 1 ? "s" : "") + ":");
			Log(sender, "AC" + (user.SelectedAntiCheats > 1 ? "s" : "") + ": " + user.AntiCheat.replace(" ", ", "));
			Log(sender, "Amount of AntiCheats: " + user.SelectedAntiCheats);
			Log(sender, "NumberIDs: " + (user.SelectedAntiCheats > 1 ? user.NumberIDs : "null cause this is used for multi acs"));
		}
		else
		{
			if (args.length < 2)
			{
				Log(sender, getSyntax());
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
			Log(sender, user.getName() + "'s AC" + (user.SelectedAntiCheats > 1 ? "s" : "") + ":");
			Log(sender,  "AC" + (user.SelectedAntiCheats > 1 ? "s" : "") + ": " + user.AntiCheat.replace(" ", ", "));
			Log(sender, "Amount of AntiCheats: " + user.SelectedAntiCheats);
			Log(sender, "NumberIDs: " + (user.SelectedAntiCheats > 1 ? user.NumberIDs : "null cause this is used for multi acs"));
			
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) 
	{
		return false;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (!player.hasPermission(getPermission()) && !Rebug.hasAdminPerms(player))
				return true;
		}
		
		return false;
	}

	@Override
	public Types getType() {
		return Types.AnySender;
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
