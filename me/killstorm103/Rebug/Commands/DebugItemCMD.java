package me.killstorm103.Rebug.Commands;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.retrooper.packetevents.manager.server.ServerVersion;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PTNormal;

public class DebugItemCMD extends Command
{

	@Override
	public String getName() {
		return "debugitem";
	}

	@Override
	public String getSyntax() {
		return "debugitem | debugitem <player>";
	}

	@Override
	public String getDescription() {
		return "debug items";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "debugitem";
	}

	@Override
	public String[] SubAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		Player player;
		ItemStack item;
		if (sender instanceof Player)
		{
			if (args.length < 2)
			{
				player = (Player) sender;
				item = player.getItemInHand();
				Info(sender, player, item);
				return;
			}
			player = Bukkit.getPlayer(args[1]);
			if (player == null)
			{
				Log(sender, "Unknown Player!");
				return;
			}
			item = player.getItemInHand();
			Info(sender, player, item);
		}
		else
		{
			if (args.length < 2)
			{
				Log(sender, getSyntax());
				return;
			}
			player = Bukkit.getPlayer(args[1]);
			if (player == null)
			{
				Log(sender, "Unknown Player!");
				return;
			}
			item = player.getItemInHand();
			Info(sender, player, item);
		}
	}
	@SuppressWarnings("deprecation")
	private void Info (CommandSender sender, Player player, ItemStack item)
	{
		if (player == null)
		{
			Log(sender, "Unknown Player!");
			return;
		}
		if (item == null)
		{
			Log(sender, player.getName() + " doesn't have a item in their hand!");
			return;
		}
		Log(sender, player.getName() + "'s Item is:");
		if (item.hasItemMeta())
		{
			if (item.getItemMeta().hasDisplayName())
				Log(sender, "Displayname: " + item.getItemMeta().getDisplayName());
			
			if (item.getItemMeta().hasEnchants())
			{
				Log(sender, "Enchants:");
				for (Map.Entry<Enchantment, Integer> map : item.getItemMeta().getEnchants().entrySet())
				{
					if (PTNormal.isServerNewerOrEquals(ServerVersion.V_1_13))
						Log(sender, "Name: " + map.getKey().getName() + " Level: " + map.getValue());
					else
						Log(sender, "Name: " + map.getKey().getName() + " ID: " + map.getKey().getId() + " Level: " + map.getValue());
					
				}
			}
		}
		Log(sender, "MaterialName: " + item.getType().name());
		Log(sender, "ID: " + item.getType().getId());
		if (item.getData() != null)
			Log(sender, "Data: " + item.getData().getData());
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
	public boolean HideFromCommandsList(CommandSender sender) 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (player.hasPermission(getPermission()) || Rebug.hasAdminPerms(player))
				return false;
			
			return true;
		}
		
		return false;
	}

	@Override
	public Types getType() 
	{
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
