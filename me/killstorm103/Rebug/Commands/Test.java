package me.killstorm103.Rebug.Commands;

import org.bukkit.command.CommandSender;

import me.killstorm103.Rebug.Main.Command;

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
	public String getPermission() {
		return StartOfPermission() + "test";
	}
	@Override
	public void onCommand(CommandSender sender, String[] args) throws Exception
	{
	}
}
