package me.killstorm103.Rebug.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class Menu extends Command
{
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args)
	{
		List<String> t = new ArrayList<>();
		t.clear();
		if (args.length == 2)
		{
			t.add("crashers");
			t.add("exploits");
			t.add("items");
			return t;
		}
		if (args.length == 3)
		{
			return t = PT.getPlayerNames ();
		}
		return null;
	}

	@Override
	public boolean HasCustomTabComplete() {
		return true;
	}
	
	
	
	public static Player player = null;
	public static CommandSender sender = null;

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
	private ItemStack Reset (Material material)
	{
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = new ItemStack(material);
		itemMeta = this.item.getItemMeta();
		
		return this.item;
	}
	private ItemStack Reset (Material material, int amount)
	{
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = new ItemStack(material, amount);
		itemMeta = this.item.getItemMeta();
		
		return this.item;
	}
	@SuppressWarnings("unused")
	private ItemStack Reset (Material material, int amount, short damage)
	{
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = new ItemStack(material, amount, damage);
		itemMeta = this.item.getItemMeta();
		
		return this.item;
	}
	@SuppressWarnings({ "unused", "deprecation" })
	private ItemStack Reset (Material material, int amount, short damage, byte data)
	{
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = new ItemStack(material, amount, damage, data);
		itemMeta = this.item.getItemMeta();
		
		return this.item;
	}
	public static Inventory OldInventory = null;
	
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		if (args.length < 2)
		{
			Log(sender, getSyntax());
			return;
		}
		Menu.sender = sender;
		Menu.player = null;
		if (sender instanceof Player)
		{
			if (args.length >= 3)
			{
				Menu.player = getRebug().getServer().getPlayer(args[2]);
				if (Menu.player == null)
				{
					Log(Menu.sender, Rebug.RebugMessage + "Unknown Player!");
					return;
				}
			}
			else
				Menu.player = (Player) sender;
			
			User user = Rebug.getUser(Menu.player);
			String menu = args[1];
			Inventory inventory = OldInventory = null;
			if (menu.equalsIgnoreCase("crashers"))
			{
				inventory = OldInventory = PT.createInventory(player, 18, ChatColor.DARK_RED + "Crashers");
				item = Reset(Material.BOOK_AND_QUILL);
				itemMeta.setDisplayName(ChatColor.RED + "Info");
				lore.add("player " + ChatColor.GRAY + Menu.player.getName());
				lore.add("client brand " + ChatColor.GRAY + user.getBrand());
				lore.add("version " + ChatColor.GRAY + PT.getPlayerVersion(user.getProtocol()) + " (" + PT.getPlayerVersion(user.getPlayer()) + ")");
				String world = Menu.player.getWorld().getName();
				if (world.length() > 5)
				{
					world = PT.SubString(world, 5, world.length()).replace("_", "").replace("theend", "The End");
				}
				world = world.equalsIgnoreCase("the end") ? world : world.replace(PT.SubString(world, 0, 1), PT.SubString(world, 0, 1).toUpperCase());
				lore.add("world " + ChatColor.GRAY + world);
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(0, item);
				
				
				item = Reset(Material.CLAY);
				itemMeta.setDisplayName(ChatColor.RED + "GameState");
				lore.add("GameState Exploit!");
				lore.add("Crashes the client");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(1, item);
				
				item = Reset(Material.TNT);
				itemMeta.setDisplayName(ChatColor.RED + "Explosion");
				lore.add("Explosion Exploit!");
				lore.add("Crashes the client");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(2, item);
				
				item = Reset(Material.COMMAND);
				itemMeta.setDisplayName(ChatColor.RED + "NumbWare");
				lore.add("NumbWare Client");
				lore.add("Crasher");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(3, item);
				
				item = Reset(Material.FIREWORK);
				itemMeta.setDisplayName(ChatColor.RED + "Particle");
				lore.add("Particle Exploit!");
				lore.add("crashes the client");
				lore.add("");
				lore.add("Works on " + ChatColor.DARK_RED + "1.8.x-1.20.4");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(4, item);
				
				item = Reset(Material.COMMAND);
				itemMeta.setDisplayName(ChatColor.RED + "Log4j");
				lore.add("Log4j Exploit");
				lore.add("crashes the client");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(5, item);
				
				item = Reset(Material.PISTON_BASE);
				itemMeta.setDisplayName(ChatColor.RED + "illegal Position");
				lore.add("illegal Position Exploit!");
				lore.add("Crashes the client");
				lore.add("");
				lore.add("Works on " + ChatColor.DARK_RED + "1.8.x-1.20.4");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(6, item);
				
				item = Reset(Material.POTION);
				itemMeta.setDisplayName(ChatColor.RED + "illegal Effect");
				lore.add("illegal Effect Exploit!");
				lore.add("Crashes the client and server if there's no anticrasher!");
				lore.add("rebug will have a anticrasher at some point!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(7, item);
				
				item = Reset(Material.DROPPER);
				itemMeta.setDisplayName(ChatColor.RED + "ResourcePack");
				lore.add("ResourcePack Exploit!");
				lore.add("Crashes the client");
				lore.add("");
				lore.add("Works on " + ChatColor.DARK_RED + "1.8.x");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(8, item);
				
				
				item = Reset(Material.BEDROCK);
				itemMeta.setDisplayName(ChatColor.RED + "Test");
				lore.add("Test Exploit");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(9, item);
				
				
				// Paged Items
				item = Reset(Material.DARK_OAK_DOOR_ITEM);
				itemMeta.setDisplayName(ChatColor.BLUE + "Spawn Entity Crashers");
				lore.add("Opens the menu");
				lore.add("Entity crashers!");
				lore.add("entity crasher is the same");
				lore.add("as illegal Data Watcher");
				lore.add("But it adds the illegal Data");
				lore.add("to a entity other than");
				lore.add("the player");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(10, item);
				
				
				((Player) Menu.sender).openInventory(inventory);
			}
			if (menu.equalsIgnoreCase("exploits"))
			{
				inventory = OldInventory = PT.createInventory(player, 9, ChatColor.DARK_RED + "Exploits");
				item = Reset(Material.BOOK_AND_QUILL);
				itemMeta.setDisplayName(ChatColor.RED + "Info");
				lore.add("player " + ChatColor.GRAY + Menu.player.getName());
				lore.add("client brand " + ChatColor.GRAY + user.getBrand());
				lore.add("version " + ChatColor.GRAY + PT.getPlayerVersion(user.getProtocol()) + " (" + PT.getPlayerVersion(user.getPlayer()) + ")");
				String world = Menu.player.getWorld().getName();
				if (world.length() > 5)
				{
					world = PT.SubString(world, 5, world.length()).replace("_", "").replace("theend", "The End");
				}
				world = world.equalsIgnoreCase("the end") ? world : world.replace(PT.SubString(world, 0, 1), PT.SubString(world, 0, 1).toUpperCase());
				lore.add("world " + ChatColor.GRAY + world);
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(0, item);
				
				
				
				item = Reset(Material.DEAD_BUSH);
				itemMeta.setDisplayName(ChatColor.RED + "Fake Death");
				lore.add("Fake Death Exploit!");
				lore.add("Makes the client");
				lore.add("think the player");
				lore.add("died but didn't");
				lore.add("and is now bugged");
				lore.add("out and can't respawn");
				lore.add("");
				lore.add("Works on " + ChatColor.DARK_RED + "1.8.x-1.20.4");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(1, item);
				
				item = Reset(Material.BED);
				itemMeta.setDisplayName(ChatColor.RED + "Force Sleep");
				lore.add("Force Sleep Exploit!");
				lore.add("works like the fake death exploit but with bed");
				lore.add("you can't get out of the bed and do anything!");
				lore.add("");
				lore.add("Works on " + ChatColor.DARK_RED + "1.8.x-1.20.4");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(2, item);
				
				item = Reset(Material.ANVIL);
				itemMeta.setDisplayName(ChatColor.RED + "Demo");
				lore.add("Demo Exploit!");
				lore.add("Makes the players game");
				lore.add("think it's a demo!");
				lore.add("");
				lore.add("Works on " + ChatColor.DARK_RED + "1.8.x-1.20.4");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(3, item);
				
				item = Reset(Material.DROPPER);
				itemMeta.setDisplayName(ChatColor.RED + "ResourcePack");
				lore.add("Test Exploit!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(4, item);
				
				item = Reset(Material.CHEST);
				itemMeta.setDisplayName(ChatColor.RED + "Test");
				lore.add("Test Exploit!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(5, item);
				
				((Player) Menu.sender).openInventory(inventory);
			}
			if (menu.equalsIgnoreCase("items"))
			{
				inventory = OldInventory = PT.createInventory(player, 36, ChatColor.GREEN + "Items");
				item = Reset(Material.STONE, 64);
				item.setItemMeta(itemMeta);
				inventory.setItem(0, item);

				item = Reset(Material.COOKED_BEEF, 64);
				item.setItemMeta(itemMeta);
				inventory.setItem(1, item);
				
				item = Reset(Material.DIAMOND_AXE);
				item.setItemMeta(itemMeta);
				inventory.setItem(2, item);
				
				item = Reset(Material.DIAMOND_SWORD);
				item.setItemMeta(itemMeta);
				inventory.setItem(3, item);
				
				((Player) Menu.sender).openInventory(inventory);
			}
		}
		else
			Log(sender, "Only player's can run this command!");
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
	public String[] SubAliases() {
		return null;
	}
}
