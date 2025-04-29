package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;

public class SlashFly extends Command {

	@Override
	public String getName() {
		return "fly";
	}

	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "fly");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "fly");
	}

	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "flycmd");
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "fly" };
	}
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		Player player = (Player) sender;
		player.setAllowFlight(!player.getAllowFlight());
		player.setFallDistance(0);
		player.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "You can " + (player.getAllowFlight() ? "now" : "no longer") + " fly!");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,String alias) 
	{
		return null;
	}
	@Override
	public SenderType getType() {
		return SenderType.Player;
	}
}
