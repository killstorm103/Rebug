package me.killstorm103.Rebug.Shared.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Shared.Command.Command;
import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Utils.PT;

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
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length > 1) {
				if (!PT.isNumber_Double(args[1])) {
					player.sendMessage(Rebug.RebugMessage + "You must put a number!");
					return;
				}
				double tp = Double.parseDouble(args[1]);
				if (!Rebug.hasAdminPerms(player)) {
					double max = Rebug.getINSTANCE().getConfig().getDouble("max-vclip-value");
					tp = max > 0 && tp > max ? max : tp;
				}

				player.setNoDamageTicks(35);
				player.setFallDistance(0);
				player.teleport(player.getLocation().add(0, tp, 0));
			} else
				player.sendMessage(Rebug.RebugMessage + getSyntax());
		} else {
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
		boolean s = true;
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (Rebug.hasAdminPerms(player) || player.hasPermission(getPermission()))
				s = false;
		} else
			s = false;

		return s;
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
