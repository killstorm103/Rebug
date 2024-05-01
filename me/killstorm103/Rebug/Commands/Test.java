package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.killstorm103.Rebug.Main.Command;

public class Test extends Command
{
	public static boolean Packets = false;

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
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		Packets =! Packets;
		sender.sendMessage("PacketEvents= " + Packets);
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
		return false;
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
