package me.killstorm103.Rebug.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;

public class SetHealthCMD extends Command
{

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
		return "set your health (0-20)";
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
	public String[] SubAliases() 
	{
		String[] s = {"/health"};
		return s;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (args.length < 2)
			{
				player.sendMessage(Rebug.RebugMessage + getSyntax());
				player.sendMessage(Rebug.RebugMessage + "Your Max Health is: " + player.getMaxHealth() + " BTW!");
				return;
			}
			if (PT.isNumber_Double(args[1]))
			{
				double health = Double.parseDouble(args[1]);
				health = health > player.getMaxHealth() ? player.getMaxHealth() : health < 0 ? 0 : health;
				player.setHealth(health);
				player.sendMessage(Rebug.RebugMessage + "Your health has been updated!");
			}
			else
				player.sendMessage(Rebug.RebugMessage + "put in a number <0-20>!");
		}
		else
			sender.sendMessage(Rebug.RebugMessage + "Only players can run this command!");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean HasCustomTabComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean HideFromCommandsList() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean HasToBeConsole() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
