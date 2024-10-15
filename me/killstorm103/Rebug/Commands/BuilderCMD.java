package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class BuilderCMD extends Command
{

	@Override
	public String getName() {
		return "builder";
	}

	@Override
	public String getSyntax() {
		return "builder | builder <user>";
	}

	@Override
	public String getDescription() {
		return "used for when building";
	}

	@Override
	public String getPermission() 
	{
		return StartOfPermission() + "builder_cmd";
	}

	@Override
	public String[] SubAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		Player player = null;
		User user = null;
		if (sender instanceof Player)
		{
			if (args.length < 2)
			{
				player = (Player) sender;
				user = Rebug.getUser(player);
				if (user == null)
				{
					Log(sender, "Unknown User!");
					return;
				}
				user.Builder =! user.Builder;
				user.sendMessage("You are " + (user.Builder ? "now a Builder!" : "no longer a Builder!"));
				return;
			}
			player = Bukkit.getPlayer(args[1]);
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
			user.Builder =! user.Builder;
			user.sendMessage("You are " + (user.Builder ? "now a Builder!" : "no longer a Builder!"));
			Log(sender, user.getName() + " is " + (user.Builder ? "now a Builder!" : "no longer a Builder!"));
		}
		else
		{
			if (args.length < 2)
			{
				Log(sender, getSyntax());
				return;
			}
			player = Bukkit.getPlayer(args[1]);
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
			user.Builder =! user.Builder;
			user.sendMessage("You are " + (user.Builder ? "now a Builder!" : "no longer a Builder!"));
			Log(sender, user.getName() + " is " + (user.Builder ? "now a Builder!" : "no longer a Builder!"));
		}
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
	public boolean HideFromCommandsList(CommandSender sender) {
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
