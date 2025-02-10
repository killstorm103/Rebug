package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.PT;

public class SlashFly extends Command {

	@Override
	public String getName() {
		return "fly";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "fly");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "fly");
	}

	@Override
	public @NotNull String getPermission()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "flycmd");
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "fly" };
	}
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (!(sender instanceof Player)) 
		{
			sender.sendMessage(RebugPlugin.getRebugMessage() + "Only Players can run this command!");
			return;
		}
		Player player = (Player) sender;
		player.setAllowFlight(!player.getAllowFlight());
		player.setFallDistance(0);
		player.sendMessage(RebugPlugin.getRebugMessage() + "You can " + (player.getAllowFlight() ? "now" : "no longer") + " fly!");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,String alias) 
	{
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return false;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || PT.hasAdminPerms(player))
				return false;
			
			return true;
		}

		return true;
	}

	@Override
	public SenderType getType() {
		return SenderType.Player;
	}
}
