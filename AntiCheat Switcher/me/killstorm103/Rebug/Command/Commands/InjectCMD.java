package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Events.EventJoinAndLeave;
import me.killstorm103.Rebug.Utils.User;

public class InjectCMD extends Command
{
	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "inject <register/unregister> <user/player>");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "inject things");
	}

	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "inject.cmd");
	}
	@Override
	public @NotNull String getName() 
	{
		return "inject";
	}
	@Override
	public String[] SubAliases() 
	{
		return new String[] {"inject"};
	}
	public boolean BypassUserNullCheck ()
	{
		return true;
	}
	@Override
	public void onCommand (CommandSender sender, String command, String[] args) throws Exception 
	{
		if (args.length < 2) 
		{
			Log(sender, getUsage());
			return;
		}
		User user = null;
		Player player = null;
		switch (args[1].toLowerCase())
		{
		case "register":
			if (args.length < 3)
			{
				Log(sender, getUsage());
				return;
			}
			player = Bukkit.getPlayer(args[2]);
			if (player == null)
			{
				Log(sender, "Unknown Player!");
				return;
			}
			user = RebugsAntiCheatSwitcherPlugin.getUser(player);
			if (user != null)
			{
				Log(sender, "User is already Registered!");
				return;
			}
			Log(sender, "Registering/UnRegistering using the command may cause issues!");
			EventJoinAndLeave.onHandle(true, player);
			break;
			
		case "unregister":
			if (args.length < 3)
			{
				Log(sender, getUsage());
				return;
			}
			player = Bukkit.getPlayer(args[2]);
			if (player == null)
			{
				Log(sender, "Unknown Player!");
				return;
			}
			user = RebugsAntiCheatSwitcherPlugin.getUser(player);
			if (user == null)
			{
				Log(sender, "User is already Unregistered!");
				return;
			}
			Log(sender, "Registering/UnRegistering using the command may cause issues!");
			EventJoinAndLeave.onHandle(false, player);
			break;

		default:
			break;
		}
		
	}
	@Override
	public List<String> onTabComplete (CommandSender sender, org.bukkit.command.Command command, String[] args, String alias) 
	{
		return null;
	}
}
