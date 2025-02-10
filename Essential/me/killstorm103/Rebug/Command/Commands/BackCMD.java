package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

@SuppressWarnings("deprecation")
public class BackCMD extends Command {

	@Override
	public @NotNull String getName () 
	{
		return "back";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "back");
	}

	@Override
	public @NotNull String getDescription () 
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "Teleports you back to where you died!");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "back");
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (sender instanceof Player) 
		{
			User user = RebugPlugin.getUser((Player) sender);
			if (user == null)
				return;

			if (user.getDeath_location() != null) {
				user.getPlayer().teleport(user.getDeath_location());
				user.setDeath_location(null);
			} else
				user.getPlayer()
						.sendMessage(RebugPlugin.getRebugMessage() + ChatColor.GRAY + "You don't have a last death location!");
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
	public boolean HideFromCommandsList(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || PT.hasAdminPerms(player))
				return false;
		}

		return true;
	}

	@Override
	public @NotNull SenderType getType() {
		return SenderType.Player;
	}

	@Override
	public String[] SubAliases() {
		return new String[] {"back"};
	}
}
