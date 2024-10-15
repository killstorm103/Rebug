package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class DebugSoundCMD extends Command
{

	@Override
	public String getName ()
	{
		return "debugsound";
	}

	@Override
	public String getSyntax() {
		return "debugsound";
	}

	@Override
	public String getDescription() {
		return "debug sound";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "debugsound";
	}

	@Override
	public String[] SubAliases()
	{
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		Log(sender, "Sounds:");
		Log(Bukkit.getConsoleSender(), "Sounds:");
		for (Sound s : Sound.values())
		{
			Log(sender, "Name: " + s.name());
			Log(Bukkit.getConsoleSender(), "Name: " + s.name());
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
	public boolean HideFromCommandsList(CommandSender sender) 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || Rebug.hasAdminPerms(player))
				return false;
			
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
