package me.killstorm103.Rebug.Command.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.QuickConfig;
import me.killstorm103.Rebug.Utils.User;

public class ClientCommandChecker extends Command
{

	@Override
	public @NotNull String getName() {
		return "clientcommandchecker";
	}

	@Override
	public @NotNull String getUsage() {
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "clientcommandchecker check | clientcommandchecker <getprefix or setprefix <prefix>> | clientcommandchecker <player> (console only!)");
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
				if (args[1].equalsIgnoreCase("check"))
				{
					if (PT.lockedList.contains(user.getUUID()))
					{
						Log(sender, "Already checking " + user.getName() + "!");
						return;
					}
					PT.checkPlayer(user, false);
					return;
				}
				if (args[1].equalsIgnoreCase("sendMessage"))
				{
					PT.sendChatMessage(player, user.Keycard, false);
					return;
				}
				if (args[1].equalsIgnoreCase("getprefix"))
				{
					user.sendMessage("Your client command prefix is: " + user.ClientCommandPreFix);
					return;
				}
				if (args[1].equalsIgnoreCase("setprefix"))
				{
					if (args.length < 3)
						Log(sender, getUsage());
					else
					{
						if (args.length > 4)
						{
							String str = "";
							for (int i = 2; i < args.length; i ++)
							{
								if (PT.isStringNull(args[i])) 
								{
									Log(sender, getUsage());
									break;
								}
								str += args[i];
								if (i == args.length - 1)
								{
									user.ClientCommandPreFix = str;
									break;
								}
							}
							if (PT.isStringNull(str)) 
							{
								Log(sender, getUsage());
								return;
							}
						}
						else
						{
							if (PT.isStringNull(args[2])) 
							{
								Log(sender, getUsage());
								return;
							}
							user.ClientCommandPreFix = args[2];
						}
						
						user.sendMessage("Set your client command prefix to:");
						user.sendMessage("" + user.ClientCommandPreFix);
					}
					return;
				}
				Log(sender, getUsage());
				return;
			}
			Log(sender, getUsage());
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
			PT.sendChatMessage(player, user.Keycard, false);
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
			String alias) 
	{
		if (args.length == (alias.equalsIgnoreCase(QuickConfig.MainLabel()) ? 2 : 1))
		{
			List<String> list = new ArrayList<>(), list2 = new ArrayList<>();
			list2.add("check");
			list2.add("getprefix");
			list2.add("setprefix");
			StringUtil.copyPartialMatches(args[args.length - 1], list2, list);
			return list;
		}
			
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias) 
	{
		return sender instanceof Player;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender)
	{
		if (sender instanceof Player && !PT.hasPermission((Player) sender, getPermission()))
			return true;
		
		return false;
	}
	
}
