package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class SpawnCMD extends Command
{

	@Override
	public String getName() {
		return "spawn";
	}

	@Override
	public String getSyntax() {
		return "spawn";
	}

	@Override
	public String getDescription()
	{
		return "Teleports you to spawn";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String getPermission() {
		return StartOfPermission() + "spawn";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			player.setNoDamageTicks(50);
			player.setFallDistance(0);
			player.teleport(new Location(Bukkit.getServer().getWorld("world"), 41, 58, 319, -91.200165F, -0.5999501F));
		}
		else
			sender.sendMessage(Rebug.RebugMessage + "Only Players can use this command!");
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
		
		return s;
	}


	@Override
	public boolean HasToBeConsole()
	{
		return false;
	}

	@Override
	public String[] SubAliases() 
	{
		return new String[] {"/spawn"};
	}

	@Override
	public boolean RemoveSlash() 
	{
		return false;
	}

}
