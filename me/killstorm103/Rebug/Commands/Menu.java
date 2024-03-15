package me.killstorm103.Rebug.Commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Utils.PT;

public class Menu extends Command
{

	@Override
	public String getName() {
		return "menu";
	}

	@Override
	public String getSyntax() {
		return "menu <menu name>";
	}

	@Override
	public String getDescription() {
		return "opens a menu";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "menu";
	}
	private ArrayList<String> lore = new ArrayList<>();
	private ItemMeta itemMeta = null;
	private ItemStack item = null;
	
	// Inventory Size can be: 9, 18, 27, 36, 45, 54
	private ItemStack Reset (ItemStack item)
	{
		this.item = null;
		lore.clear();
		this.item = item;
		itemMeta = null;
		itemMeta = this.item.getItemMeta();
		
		return this.item;
	}
	public static Inventory OldInventory = null;
	
	@Override
	public void onCommand(CommandSender sender, String[] args) throws Exception 
	{
		if (args.length < 2)
		{
			Log(sender, getSyntax());
			return;
		}
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			String menu = args[1];
			Inventory inventory = OldInventory = null;
			if (menu.equalsIgnoreCase("crashers"))
			{
				OldInventory = inventory = PT.createInventory(player, 18, ChatColor.DARK_RED + "Crashers");
				item = Reset(new ItemStack(Material.CLAY));
				itemMeta.setDisplayName(ChatColor.RED + "GameState");
				lore.add("GameState Exploit!");
				lore.add("Crashes the client");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(0, item);
				
				item = Reset(new ItemStack(Material.TNT));
				itemMeta.setDisplayName(ChatColor.RED + "Explosion");
				lore.add("Explosion Exploit!");
				lore.add("Crashes the client");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(1, item);
				
				item = Reset(new ItemStack(Material.COMMAND));
				itemMeta.setDisplayName(ChatColor.RED + "NumbWare");
				lore.add("NumbWare Client");
				lore.add("Crasher");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(2, item);
				
				item = Reset(new ItemStack(Material.FIREWORK));
				itemMeta.setDisplayName(ChatColor.RED + "Particle");
				lore.add("Particle Exploit!");
				lore.add("crashes the client");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(3, item);
				
				item = Reset(new ItemStack(Material.COMMAND));
				itemMeta.setDisplayName(ChatColor.RED + "Log4j");
				lore.add("Log4j Exploit");
				lore.add("crashes the client");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(4, item);
				
				item = Reset(new ItemStack(Material.PISTON_BASE));
				itemMeta.setDisplayName(ChatColor.RED + "illegal Position");
				lore.add("illegal Position Exploit!");
				lore.add("Crashes the client");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(5, item);
				
				// Paged Items
				item = Reset(new ItemStack(Material.DARK_OAK_DOOR_ITEM));
				itemMeta.setDisplayName(ChatColor.BLUE + "Spawn Entity Crashers");
				lore.add("Opens the menu");
				lore.add("Entity crashers!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(6, item);
				
			}
			if (menu.equalsIgnoreCase("exploits"))
			{
				OldInventory = inventory = PT.createInventory(player, 9, ChatColor.DARK_RED + "Crashers");
				item = Reset(new ItemStack(Material.DEAD_BUSH));
				itemMeta.setDisplayName(ChatColor.RED + "Fake Death");
				lore.add("Fake Death Exploit!");
				lore.add("Makes the client");
				lore.add("think the player");
				lore.add("died but didn't");
				lore.add("and is now bugged");
				lore.add("out and can't respawn");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(0, item);
				
				item = Reset(new ItemStack(Material.ANVIL));
				itemMeta.setDisplayName(ChatColor.RED + "Demo");
				lore.add("Demo Exploit!");
				lore.add("Makes the players game");
				lore.add("think it's a demo!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(1, item);
			}
			
			if (inventory != null)
				player.openInventory(inventory);
		}
		else
			Log(sender, "Only player's can run this command!");
	}
	
}
