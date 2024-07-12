package me.killstorm103.Rebug.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class PacketDebugger extends Command
{

	@Override
	public String getName() 
	{
		return "packetdebugger";
	}

	@Override
	public String getSyntax() {
		return "packetdebugger <toggle, config>";
	}

	@Override
	public String getDescription() {
		return "debug packets";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "packetdebugger";
	}

	@Override
	public String[] SubAliases() {
		return new String[] {"/packetdebugger"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (!(sender instanceof Player))
			return;
		
		if (args.length < 2) 
		{
			sender.sendMessage(Rebug.RebugMessage + getSyntax());
			return;
		}
		
		User user = Rebug.getUser((Player) sender);
		if (user == null) return;
		
		if (args[1].equalsIgnoreCase("toggle"))
		{
			if (!user.AllowedToDebug)
			{
				user.sendMessage("This client doesn't allow you to debug it's modules");
				return;
			}
			user.DebugEnabled =! user.DebugEnabled;
			user.sendMessage("PacketDebugger " + (user.DebugEnabled ? ChatColor.GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled") + ChatColor.GRAY + "!");
		}
		else if (args[1].equalsIgnoreCase("config"))
		{
			user.getPlayer().openInventory(user.getPacketDebuggerMenu());
		}
		else
			user.sendMessage(getSyntax());
	}

	@Override
	public List<String> onTabComplete (CommandSender sender, org.bukkit.command.Command command, String[] args)
	{
		if (args.length == 2)
		{
			final List<String> s = new ArrayList<>();
			s.add("toggle");
			s.add("config");
			
			return s;
		}
		
		return null;
	}

	@Override
	public boolean HasCustomTabComplete() {
		return true;
	}

	@Override
	public boolean HideFromCommandsList() {
		return false;
	}

	@Override
	public boolean HasToBeConsole() {
		return false;
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	
}
