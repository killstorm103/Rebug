package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PTNormal;

public class Unblock extends Command {

	@Override
	public String getName() {
		return "unblock";
	}

	@Override
	public String getSyntax() {
		return "unblock <player>";
	}

	@Override
	public String getDescription() {
		return "unblocks the packetdebugger for numbware";
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "unblock";
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (args.length == 1) {
			Log(sender, getSyntax());
			return;
		}
		Player player = null;
		player = Bukkit.getPlayer(args[1]);
		if (player == null) {
			Log(sender, "Player not found!");
			return;
		}
		PTNormal.INSTANCE.SendPacketUnBlock(player);
		Log(sender, Rebug.RebugMessage + "Tried to unlock packet debugger for NumbWare");
		Log(sender,
				Rebug.RebugMessage + "if this didn't work it's cause your not using Developer Edition of NumbWare!");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return null;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) {
		boolean s = true;
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || Rebug.hasAdminPerms(player))
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
	public String[] SubAliases() {
		return null;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) {
		return false;
	}

}
