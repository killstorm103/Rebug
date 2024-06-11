package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;

public class DamageCMD extends Command
{

	@Override
	public String getName() {
		return "damage";
	}

	@Override
	public String getSyntax () 
	{
		return "damage | damage <amount> | damage <amount> <player>";
	}

	@Override
	public String getDescription() {
		return "damages you or a given player if you have op!";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return true;
	}
	@Override
	public String getPermission() {
		return StartOfPermission() + "damage";
	}

	@Override
	public String[] SubAliases()
	{
		String[] s = {"/damage"};
		return s;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		Player player;
		double damage = 1;
		if (args.length <= 2)
		{
			if (sender instanceof Player)
			{
				player = (Player) sender;
				if (args.length == 2)
				{
					if (PT.isNumber_Double(args[1]))
					{
						damage = Double.parseDouble(args[1]);
					}
					else
					{
						Log(sender, Rebug.RebugMessage + "you have to put in a number!");
						return;
					}
				}
				player.damage(damage);
				player.sendMessage(Rebug.RebugMessage + "dealt " + damage + " damage to " + player.getName());
			}
			else
				sender.sendMessage(Rebug.RebugMessage + " your servers not able to damage itself!");
		}
		else if (args.length == 3)
		{
			if (PT.isNumber_Double(args[1]))
			{
				damage = Double.parseDouble(args[1]);
			}
			else
			{
				Log(sender, Rebug.RebugMessage + "you have to put in a number!");
				return;
			}
			player = Bukkit.getServer().getPlayer(args[2]);
			if (player == null)
			{
				sender.sendMessage(Rebug.RebugMessage + "Unknown Player!");
				return;
			}
			player.damage(damage);
			sender.sendMessage(Rebug.RebugMessage + "dealt " + damage + " damage to " + player.getName());
		}
		else
			sender.sendMessage(Rebug.RebugMessage + getSyntax());
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
		return false;
	}

	@Override
	public boolean HasToBeConsole() {
		return false;
	}
	
}
