package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class ClientCommandChecker extends Command
{

	@Override
	public @NotNull String getName() {
		return "clientcommandchecker";
	}

	@Override
	public @NotNull String getUsage() {
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "clientcommandchecker | clientcommandchecker <player> (console only!)");
	}

	@Override
	public @NotNull String getDescription() {
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "runs the client command checker on the player!");
	}

	@Override
	public @NotNull String getPermission() {
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "clientcommandchecker");
	}

	@Override
	public String[] SubAliases() {
		return new String[] {"clientcommandchecker"};
	}

	@Override
	public void onCommand (CommandSender sender, String command, String[] args) throws Exception
	{
		Player player = null;
		User user = null;
		if (sender instanceof Player)
		{
			player = (Player) sender;
			user = RebugPlugin.getUser(player);
			if (user == null)
			{
				Log(sender, "Unknown User!");
				return;
			}
			if (args.length >= 2)
			{
				player.chat(user.Keycard);
				return;
			}
			if (PT.lockedList.contains(user.getUUID()))
			{
				Log(sender, "Already checking " + user.getName() + "!");
				return;
			}
			PT.checkPlayer(user, false);
			return;
		}
		if (args.length < 2)
		{
			Log(sender, getUsage());
			return;
		}
		player = Bukkit.getPlayer(args[1]);
		if (player == null)
		{
			Log(sender, "Unknown Player!");
			return;
		}
		user = RebugPlugin.getUser(player);
		if (user == null)
		{
			Log(sender, "Unknown User!");
			return;
		}
		if (args.length >= 3)
		{
			player.chat(user.Keycard);
			return;
		}
		if (PT.lockedList.contains(user.getUUID()))
		{
			Log(sender, "Already checking " + user.getName() + "!");
			return;
		}
		PT.checkPlayer(user, true);
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
	public boolean HideFromCommandsList(CommandSender sender)
	{
		if (sender instanceof Player && !PT.hasPermission((Player) sender, getPermission()))
			return true;
		
		return false;
	}
	
}
