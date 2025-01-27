package me.killstorm103.Rebug.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Main.RebugPlugin;
import me.killstorm103.Rebug.Utils.PT;

public class VClip extends Command {

	@Override
	public String getName() {
		return "vclip";
	}

	@Override
	public String getSyntax() {
		return "vclip <if console: player> <tp number>";
	}

	@Override
	public String getDescription() {
		return "vclip";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "vclip";
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "/vclip" };
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (!RebugPlugin.getINSTANCE().getConfig().getBoolean("clip.vclip.enabled"))
		{
			sender.sendMessage(RebugPlugin.getRebugMessage() + "The VClip command is Disabled!");
			return;
		}
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length > 1) {
				if (!PT.isNumber_Double(args[1])) {
					player.sendMessage(RebugPlugin.getRebugMessage() + "You must put a number!");
					return;
				}
				double tp = Double.parseDouble(args[1]);
				if (!PT.hasAdminPerms(player)) 
				{
					double max = RebugPlugin.getINSTANCE().getConfig().getDouble("clip.vclip.max-value");
					tp = max > 0 && tp > max ? max : tp;
				}

				player.setNoDamageTicks(35);
				player.setFallDistance(0);
				player.teleport(player.getLocation().add(0, tp, 0));
			} 
			else
				player.sendMessage(RebugPlugin.getRebugMessage() + getSyntax());
		}
		else
		{
			if (args.length > 2) {
				Player player = Bukkit.getPlayer(args[1]);
				if (player == null) {
					Log(sender, "Unknown Player!");
					return;
				}
				if (!PT.isNumber_Double(args[2])) {
					Log(sender, "You must put a number!");
					return;
				}
				player.setNoDamageTicks(35);
				player.setFallDistance(0);
				player.teleport(player.getLocation().add(0, Double.parseDouble(args[1]), 0));
			} else
				Log(sender, getSyntax());
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
			
			return true;
		} 

		return false;
	}

	@Override
	public Types getType() {
		return Types.AnySender;
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}
}
