package me.killstorm103.Rebug.Shared.Command.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Shared.Command.Command;
import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Utils.PT;

public class SetHealthCMD extends Command {

	@Override
	public String getName() {
		return "health";
	}

	@Override
	public String getSyntax() {
		return "health <0-20>";
	}

	@Override
	public String getDescription() {
		return "set your health (0-Max Player Health)";
	}

	@Override
	public boolean hasCommandCoolDown() {
		return true;
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "healthcmd";
	}

	@Override
	public String[] SubAliases() {
		return new String[] { "/health" };
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length < 2) {
				player.sendMessage(Rebug.RebugMessage + getSyntax());
				player.sendMessage(Rebug.RebugMessage + "Your Max Health is: " + player.getMaxHealth() + " BTW!");
				return;
			}
			if (PT.isNumber_Double(args[1])) {
				double health = Double.parseDouble(args[1]);
				health = health > player.getMaxHealth() ? player.getMaxHealth() : health < 0 ? 0 : health;
				player.setHealth(health);
				player.sendMessage(Rebug.RebugMessage + "Your health has been updated!");
			} else
				player.sendMessage(Rebug.RebugMessage + "put in a number <0-" + player.getMaxHealth() + ">!");
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
			if (player.hasPermission(getPermission()) || Rebug.hasAdminPerms(player))
				return false;
		}

		return true;
	}

	@Override
	public Types getType() {
		return Types.Player;
	}

	@Override
	public boolean RemoveSlash() {
		return false;
	}

}
