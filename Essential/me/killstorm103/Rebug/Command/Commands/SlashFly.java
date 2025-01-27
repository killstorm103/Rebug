package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Main.RebugPlugin;
import me.killstorm103.Rebug.Utils.PT;

public class SlashFly extends Command {

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
		return new String[] { "/fly" };
	}
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (!(sender instanceof Player)) 
		{
			sender.sendMessage(RebugPlugin.getRebugMessage() + "Only Players can run this command!");
			return;
		}
		Player player = (Player) sender;
		player.setAllowFlight(!player.getAllowFlight());
		player.setFallDistance(0);
		player.sendMessage(RebugPlugin.getRebugMessage() + "You can " + (player.getAllowFlight() ? "now" : "no longer") + " fly!");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,String alias) 
	{
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return false;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || PT.hasAdminPerms(player))
				return false;
			
			return true;
		}

		return true;
	}

	@Override
	public Types getType() {
		return Types.Player;
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
