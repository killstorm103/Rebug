package me.killstorm103.Rebug.Commands;

import org.bukkit.command.CommandSender;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class Version extends Command
{

	@Override
	public String getName() {
		return "version";
	}

	@Override
	public String getSyntax() {
		return "version";
	}

	@Override
	public String getDescription() {
		return "gets the version of rebug";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "version";
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) throws Exception 
	{
		Log(sender, "Rebug v" + Rebug.PluginVersion() + " made by " + Rebug.getAuthor());
	}
	
}
