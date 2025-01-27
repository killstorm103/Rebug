package me.killstorm103.Rebug.Command.Commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Main.RebugPlugin;

public class Credits extends Command {
	private static final Map<String, String> others = new HashMap<>();
	static {
		others.clear();
		others.put("Retrooper", "Packet Events!");
		others.put("Matej (kangarko)", "a little help with Folia Support");
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
	public String[] SubAliases() {
		return new String[] { "/credits" };
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		for (Map.Entry<String, String> map : others.entrySet())
			sender.sendMessage(RebugPlugin.getRebugMessage() + map.getKey() + ": " + map.getValue());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return false;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) {
		return false;
	}

	@Override
	public Types getType() {
		return Types.AnySender;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
}
