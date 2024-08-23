package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class SlashFly extends Command
{

	@Override
	public String getName() {
		return "fly";
	}

	@Override
	public String getSyntax() {
		return "fly";
	}

	@Override
	public String getDescription() {
		return "fly";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "flycmd";
	}

	@Override
	public String[] SubAliases() {
		return new String[] {"/fly"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage(Rebug.RebugMessage + "Only Players can run this command!");
			return;
		}
		Player player = (Player) sender;
		player.setAllowFlight(!player.getAllowFlight());
		player.setFallDistance(0);
		player.sendMessage(Rebug.RebugMessage + "You can " + (player.getAllowFlight() ? "now" : "no longer") + " fly!");
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
