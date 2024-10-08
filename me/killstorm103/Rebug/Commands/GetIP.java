package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;


public class GetIP extends Command
{

	@Override
	public String getName() {
		return "getip";
	}

	@Override
	public String getSyntax() {
		return "getip <player>";
	}

	@Override
	public String getDescription() {
		return "get's the ip of the player";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (args.length < 2)
		{
			sender.sendMessage(Rebug.RebugMessage + getSyntax());
			return;
		}
		Player player = Bukkit.getServer().getPlayer(args[1]);
		if (player == null)
		{
			sender.sendMessage(Rebug.RebugMessage + " player was not found!");
			return;
		}
		sender.sendMessage(Rebug.RebugMessage + player.getName() + "'s ip is: " + player.getAddress().getHostName() + ":" + player.getAddress().getPort());
	}
	@Override
	public String getPermission() {
		return "me.killstorm103.rebug.commands.getip";
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
	public Types getType ()
	{
		return Types.AnySender;
	}
	@Override
	public String[] SubAliases() {
		return null;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
}
