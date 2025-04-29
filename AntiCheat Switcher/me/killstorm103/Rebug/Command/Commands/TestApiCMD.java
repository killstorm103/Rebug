package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;
import me.killstorm103.Rebug.Utils.API.ApiProvider;

public class TestApiCMD extends Command
{

	// TODO Add TapCo
	
	@Override
	public String getName() {
		return "testapi";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "testapi | testapi <player>");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "test api things");
	}

	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "testapi.cmd");
	}

	@Override
	public String[] SubAliases() {
		return new String[] {"testapi"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		int amount = 0, i = 0;
		String[] AntiCheats = null;
		if (sender instanceof Player) 
		{
			Player player = (Player) sender;
			User user = RebugsAntiCheatSwitcherPlugin.getUser(player);
			if (user == null) return;
			
			amount = user.getSelectedAntiCheats();
			if (amount < 1)
			{
				user.sendMessage("Vanilla isn't a AntiCheat it's normal MC there isn't a Api to test!");
				return;
			}
			if (amount > 1)
			{
				AntiCheats = PT.SplitString(user.getAntiCheat().replace(",", " ").replace(", ", " "));
				for (i = 0; i < AntiCheats.length; i ++)
				{
					user.sendMessage("Testing " + AntiCheats[i] + "'s Api!");
					ApiProvider.onApi(user.getPlayer(), AntiCheats[i], amount, 0);
				}
			}
			else
				ApiProvider.onApi(user.getPlayer(), user.getAntiCheat(), amount, 0);
		}
		else
		{
			if (args.length < 2)
			{
				Log(sender, getUsage());
				return;
			}
			User user = RebugsAntiCheatSwitcherPlugin.getUser(Bukkit.getPlayer(args[1]));
			if (user == null) 
			{
				Log(sender, "Unknown User!");
				return;
			}
			amount = user.getSelectedAntiCheats();
			if (amount < 1)
			{
				user.sendMessage("Vanilla isn't a AntiCheat it's normal MC there isn't a Api to test!");
				return;
			}
			if (amount > 1)
			{
				AntiCheats = PT.SplitString(user.getAntiCheat().replace(",", " ").replace(", ", " "));
				for (i = 0; i < AntiCheats.length; i ++)
				{
					Log(sender, "Testing " + user.getName() + "'s " + AntiCheats[i] + "'s Api!");
					ApiProvider.onApi(user.getPlayer(), AntiCheats[i], amount, 0);
				}
			}
			else
				ApiProvider.onApi(user.getPlayer(), user.getAntiCheat(), user.getSelectedAntiCheats(), 0);
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}
}
