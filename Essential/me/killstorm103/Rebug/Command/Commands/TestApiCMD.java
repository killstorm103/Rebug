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
import me.killstorm103.Rebug.Utils.API.ApiProvider;

public class TestApiCMD extends Command
{

	@Override
	public String getName() {
		return "testapi";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "testapi");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "test api things");
	}

	@Override
	public @NotNull String getPermission()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "testapi.cmd");
	}

	@Override
	public String[] SubAliases() {
		return new String[] {"testapi"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (sender instanceof Player) 
		{
			Player player = (Player) sender;
			User user = RebugPlugin.getUser(player);
			if (user == null) return;
			
			user.sendMessage("Testing " + user.getAntiCheat() + "'s Api!");
			ApiProvider.onApi(user.getPlayer(), user.getAntiCheat(), user.getSelectedAntiCheats(), 0);
		}
		else
		{
			if (args.length < 2)
			{
				Log(sender, getUsage());
				return;
			}
			User user = RebugPlugin.getUser(Bukkit.getPlayer(args[1]));
			if (user == null) 
			{
				Log(sender, "Unknown User!");
				return;
			}
			Log(sender, "Testing " + user.getName() + "'s " + user.getAntiCheat() + "'s Api!");
			ApiProvider.onApi(user.getPlayer(), user.getAntiCheat(), user.getSelectedAntiCheats(), 0);
		}
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
