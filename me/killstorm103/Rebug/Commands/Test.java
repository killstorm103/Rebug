package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class Test extends Command
{
	@Override
	public String getName() {
		return "test";
	}

	@Override
	public String getSyntax() {
		return "test";
	}

	@Override
	public String getDescription() {
		return "command for testing new commands";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String getPermission() {
		return StartOfPermission() + "test";
	}
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		if (!(sender instanceof Player))
			return;
		
		Player player = (Player) sender;
		player.sendMessage(Rebug.RebugMessage + player.getWorld().getName());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args) {
		return null;
	}

	@Override
	public boolean HasCustomTabComplete() {
		return false;
	}

	@Override
	public boolean HideFromCommandsList() {
		return true;
	}

	@Override
	public boolean HasToBeConsole() {
		return false;
	}

	@Override
	public String[] SubAliases() {
		return null;
	}

}
