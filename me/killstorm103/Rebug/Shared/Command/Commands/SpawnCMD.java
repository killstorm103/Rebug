package me.killstorm103.Rebug.Shared.Command.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Shared.Command.Command;
import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.SoftWares.Spigot.Utils.PT_Spigot;

public class SpawnCMD extends Command {

	@Override
	public String getName() {
		return "spawn";
	}

	@Override
	public String getSyntax() {
		return "spawn | spawn <player> (if console)";
	}

	@Override
	public String getDescription() {
		return "Teleports you to spawn";
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "spawn";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.setNoDamageTicks(50);
			player.setFallDistance(0);
			player.teleport(PT_Spigot.INSTANCE.getSpawn());
		} else {
			if (args.length > 1) {
				Player player = Bukkit.getPlayer(args[1]);
				if (player == null) {
					Log(sender, "Unknown Player!");
					return;
				}
				player.setNoDamageTicks(50);
				player.setFallDistance(0);
				player.teleport(PT_Spigot.INSTANCE.getSpawn());
				return;
			}
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
			if (Rebug.hasAdminPerms(player) || player.hasPermission(getPermission()))
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
	public String[] SubAliases() {
		return new String[] { "/spawn" };
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}

}
