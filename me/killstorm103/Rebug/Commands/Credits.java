package me.killstorm103.Rebug.Commands;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class Credits extends Command
{
	private static final Map<String, String> others = new HashMap<>();
	static 
	{
		others.clear();
		others.put("Kody Simpson", "Teleport Bow and TeleportUtils!");
		others.put("MinusKube", "Netherboard!");
		others.put("Retrooper", "Packet Events!");
	}
	@Override
	public String getName() {
		return "credits";
	}
	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	@Override
	public String getSyntax() {
		return "credits";
	}

	@Override
	public String getDescription() {
		return "gives credit to other devs who helped";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "creditscmd";
	}

	@Override
	public String[] SubAliases()
	{
		String[] s = {"/credits"};
		return s;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		for (Map.Entry<String, String> map : others.entrySet())
			sender.sendMessage(Rebug.RebugMessage + map.getKey() + ": " + map.getValue());
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
