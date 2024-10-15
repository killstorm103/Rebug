package me.killstorm103.Rebug.Commands.ShortCuts;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import me.killstorm103.Rebug.Main.Rebug;

public class ShortCutBasic implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String arg2, String[] args) {
		me.killstorm103.Rebug.Main.Command c = Rebug.getINSTANCE().getCommandByName(command.getName());
		if (c != null)
			return c.HasCustomTabComplete(sender, command, args, arg2) ? c.onTabComplete(sender, command, args, arg2)
					: null;

		for (me.killstorm103.Rebug.Main.Command cmd : Rebug.getINSTANCE().getCommands()) {
			if (cmd.SubAliases() != null && cmd.SubAliases().length > 0) {
				for (int i = 0; i < cmd.SubAliases().length; i++) {
					if (command.getName().equalsIgnoreCase(cmd.SubAliases()[i].replace("/", "").replace("//", ""))
							&& cmd.HasCustomTabComplete(sender, command, args, arg2))
						return cmd.onTabComplete(sender, command, args, arg2);
				}
			}
		}

		return null;
	}
}
