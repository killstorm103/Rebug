package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.PT;

public class DamageTick extends Command
{

	@Override
	public @NotNull String getName() {
		return "damagetick";
	}

	@Override
	public @NotNull String getUsage() {
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "damagetick <amout>");
	}

	@Override
	public @NotNull String getDescription() {
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "change your damagetick");
	}

	@Override
	public @NotNull String getPermission() {
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "damagetick");
	}

	@Override
	public String[] SubAliases() 
	{
		return new String[] {"damagetick"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (args.length < 2) 
		{
			Log(sender, getUsage());
			return;
		}
		Player player = (Player) sender;
		int amount = Integer.parseInt(args[1]);
		amount = amount < 0 ? 0 : amount > player.getMaximumNoDamageTicks() ? player.getMaximumNoDamageTicks() : amount;
		player.setNoDamageTicks(amount);
		player.sendMessage(RebugPlugin.getRebugMessage() + "Your damage tick is now: " + amount);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) 
	{
		return false;
	}
	public @NotNull SenderType getType ()
	{
		return SenderType.Player;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (PT.hasPermission(player, getPermission(), PT.AllCommandsPermission()))
				return false;
		}
		
		return true;
	}
	
}
