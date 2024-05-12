package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;

public class Discord extends Command
{

	@Override
	public String getName() {
		return "discord";
	}

	@Override
	public String getSyntax() {
		return "discord";
	}

	@Override
	public String getDescription() {
		return "gives you the link to our discord server!";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "discord";
	}

	@Override
	public String[] SubAliases()
	{
		String[] s = {"/discord"};
		return s;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		sender.sendMessage(Rebug.RebugMessage + "Our discord server invite link:");
		sender.sendMessage(Rebug.RebugMessage + Config.DiscordInviteLink());
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
