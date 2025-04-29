package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.QuickConfig;

@SuppressWarnings("deprecation")
public class Discord extends Command {

	@Override
	public String getName() {
		return "discord";
	}
	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "discord");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "gives you the link to our discord server!");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "discord");
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "discord" };
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("discord.disabled", false))
		{
			String reason = RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("discord.reason", "disabled for no given reason!");
			sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("discord.color-code-support", false) ? ChatColor.translateAlternateColorCodes('&', reason) : reason));
			return;
		}
		RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getConfigurationSection("discord.messages").getKeys(false).forEach(key -> 
		{
			if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("discord.messages." + key + ".enabled", false))
			{
				String text = RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("discord.messages." + key + ".text", "Put a .text: text").replace("%discord%", QuickConfig.DiscordInviteLink()).replace("%link%", QuickConfig.DiscordInviteLink());
				sender.sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("discord.messages." + key + ".color-code-support", false) ? ChatColor.translateAlternateColorCodes('&', text) : text));
			}
		});
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}
}
