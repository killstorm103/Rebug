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
		return "help | help <'command'> <command name>";
	}

	@Override
	public String getDescription() {
		return "gets a list of commands and Syntax of commands";
	}
	@Override
	public String getPermission() {
		return StartOfPermission() + "help";
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
				if (sender instanceof Player)
				{
					if (((Player) sender).hasPermission(commands.getPermission()) || ((Player) sender).hasPermission(Rebug.AllCommands_Permission))
						Log(sender, "/rebug " + commands.getName());
				}
				else
					Log(sender, "/rebug " + commands.getName());
			}
			if (args.length == 3 && args[1].equalsIgnoreCase("command") && args[2].equalsIgnoreCase(commands.getName()))
				Log(sender, "/rebug " + commands.getSyntax());
		}
	}
	
}
