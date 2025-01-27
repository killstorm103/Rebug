package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.RebugPlugin;
import me.killstorm103.Rebug.Utils.PT;

@SuppressWarnings("deprecation")
public class Discord extends Command {

	@Override
	public String getName() {
		return "discord";
	}

	@Override
	public String getSyntax() {
		return "discord";
	}

	@Override
	public String getDescription() {
		return "gives you the link to our discord server!";
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "discord";
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "/discord" };
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (RebugPlugin.getINSTANCE().getConfig().getBoolean("discord.disabled", false))
		{
			String reason = RebugPlugin.getINSTANCE().getConfig().getString("discord.reason", "disabled for no given reason!");
			sender.sendMessage(RebugPlugin.getRebugMessage() + (RebugPlugin.getINSTANCE().getConfig().getBoolean("discord.color-code-support", false) ? ChatColor.translateAlternateColorCodes('&', reason) : reason));
			return;
		}
		RebugPlugin.getINSTANCE().getConfig().getConfigurationSection("discord.messages").getKeys(false).forEach(key -> 
		{
			if (RebugPlugin.getINSTANCE().getConfig().getBoolean("discord.messages." + key + ".enabled", false))
			{
				String text = RebugPlugin.getINSTANCE().getConfig().getString("discord.messages." + key + ".text", "Put a .text: text").replace("%discord%", Config.DiscordInviteLink()).replace("%link%", Config.DiscordInviteLink());
				sender.sendMessage(RebugPlugin.getRebugMessage() + (RebugPlugin.getINSTANCE().getConfig().getBoolean("discord.messages." + key + ".color-code-support", false) ? ChatColor.translateAlternateColorCodes('&', text) : text));
			}
		});
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
			
			return true;
		} 

		return false;
	}

	@Override
	public Types getType() {
		return Types.AnySender;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
}
