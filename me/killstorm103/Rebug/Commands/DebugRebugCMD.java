package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class DebugRebugCMD extends Command
{

	@Override
	public String getName() {
		return "debug";
	}

	@Override
	public String getSyntax() {
		return "debug";
	}

	@Override
	public String getDescription() {
		return "debug things, helpful for the dev";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String getPermission() {
		return StartOfPermission() + "debug";
	}

	@Override
	public String[] SubAliases() 
	{
		return null;
	}
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		Rebug.debug =! Rebug.debug;
		sender.sendMessage(Rebug.RebugMessage + "" + Rebug.debug);
	}
	

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias) {
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

	@Override
	public boolean RemoveSlash() {
		return false;
	}
}
