package me.killstorm103.Rebug.Commands;

import org.bukkit.command.CommandSender;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class Help extends Command
{

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getSyntax() {
		return "help | help <command>";
	}

	@Override
	public String getDescription() {
		return "gets a list of commands";
	}
	@Override
	public String getPermission() {
		return "me.killstorm103.rebug.commands.help";
	}
	

	@Override
	public void onCommand(CommandSender sender, String[] args) throws Exception 
	{
		if (args.length == 1)
			Log(sender, "commands:");
		
		for (Command commands : Rebug.getGetMain().getCommands())
		{
			if (args.length == 1)
			{
				Log(sender, commands.getName());
			}
			if (args.length == 2 && args[1].equalsIgnoreCase(commands.getName()))
			{
				Log(sender, commands.getSyntax());
			}
		}
	}
	
}
