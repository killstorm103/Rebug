package me.killstorm103.Rebug.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
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
			t.add("settings");
			
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
	@SuppressWarnings("deprecation")
	private ItemStack Reset (Material material, Integer amount, Short damage, Byte data)
	{
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = new ItemStack(material, amount, damage,data);
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
				
				for (int i = 0; i < inventory.getSize(); i ++)
				{
					if (inventory.getItem(i) == null)
					{
						item = Reset(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 7);
						itemMeta.setDisplayName(" ");
						item.setItemMeta(itemMeta);
						inventory.setItem(i, item);
					}
				}
				
				
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
				
				for (int i = 0; i < inventory.getSize(); i ++)
				{
					if (inventory.getItem(i) == null)
					{
						item = Reset(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 7);
						itemMeta.setDisplayName(" ");
						item.setItemMeta(itemMeta);
						inventory.setItem(i, item);
					}
				}
				
				((Player) Menu.sender).openInventory(inventory);
			}
			if (menu.equalsIgnoreCase("VanillaFlyChecks"))
			{
				inventory = OldInventory = PT.createInventory(player, 9, ChatColor.BLUE + "Vanilla Fly Checks");
				item = Reset(Material.COMMAND);
				itemMeta.setDisplayName(ChatColor.RED.toString() + ChatColor.ITALIC + "Back");
				lore.add(ChatColor.AQUA + "Go back!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(0, item);
				
				item = Reset(user.isVanilla1_8FlyCheck() ? Material.BAKED_POTATO : Material.POTATO_ITEM);
				itemMeta.setDisplayName(ChatColor.ITALIC + "1.8.x");
				lore.add(ChatColor.AQUA + "Status: " + (user.isVanilla1_8FlyCheck() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.isVanilla1_8FlyCheck());
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable 1.8.x Fly check");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(1, item);
				
				item = Reset(user.isVanilla1_9FlyCheck() ? Material.BAKED_POTATO : Material.POTATO_ITEM);
				itemMeta.setDisplayName(ChatColor.ITALIC + "1.9+");
				lore.add(ChatColor.AQUA + "Status: " + (user.isVanilla1_9FlyCheck() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.isVanilla1_9FlyCheck());
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable 1.9+ Fly check");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(2, item);
				
				
				for (int i = 0; i < inventory.getSize(); i ++)
				{
					if (inventory.getItem(i) == null)
					{
						item = Reset(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 1);
						itemMeta.setDisplayName(ChatColor.RED + "Add more Checks as they come!");
						item.setItemMeta(itemMeta);
						inventory.setItem(i, item);
					}
				}
				
				((Player) Menu.sender).openInventory(inventory);
			}
			if (menu.equalsIgnoreCase("settings"))
			{
				inventory = OldInventory = PT.createInventory(player, 36, ChatColor.BLUE + "Player Settings");
				item = Reset(Material.COOKED_BEEF);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Hunger");
				lore.add(ChatColor.AQUA + "Status: " + (user.isEats() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.isEats());
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Hunger");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(0, item);
				
				item = Reset(Material.FEATHER);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Fall Damage");
				lore.add(ChatColor.AQUA + "Status: " + (user.FallDamage() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.FallDamage());
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Fall Damage");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(1, item);
				
				item = Reset(Material.ARROW);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Exterranl Damage");
				lore.add(ChatColor.AQUA + "Status: " + (user.isDamageable() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.isDamageable());
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Exterranl Damage");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(2, item);
				
				item = Reset(Material.COMMAND);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Vanilla Fly Checks");
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Vanilla Fly Checks");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(3, item);
				
				item = Reset(Material.BOOK_AND_QUILL);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Notify On Flying Kick");
				lore.add(ChatColor.AQUA + "Status: " + (user.isNotifyFlyingKick() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.isNotifyFlyingKick());
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable alerts");
				lore.add(ChatColor.RESET + "for you being picked up for flying");
				lore.add(ChatColor.RESET + "by vanilla");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(4, item);
				
				item = Reset(Material.POTION);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Potions");
				lore.add(ChatColor.AQUA + "Status: " + (user.isPotionEffects() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.isPotionEffects());
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Potion Effects");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(5, item);
				
				item = Reset(Material.STONE);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Auto Refill Blocks");
				lore.add(ChatColor.AQUA + "Status: " + (user.isAutoRefillBlocks() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.isAutoRefillBlocks());
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Auto Block Refill");
				lore.add(ChatColor.RESET + "for Scaffold Test Area");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(6, item);
				
				item = Reset(Material.PAPER);
				itemMeta.setDisplayName(ChatColor.ITALIC + "AllowAT");
				lore.add(ChatColor.AQUA + "Status: " + (user.Mentions() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.Mentions());
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Allow other players to mention you in chat");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(7, item);
				
				item = Reset(Material.BANNER);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Flags");
				lore.add(ChatColor.AQUA + "Status: " + (user.isShowFlags() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.isShowFlags());
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Show anticheat flags");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(8, item);
				
				
				item = Reset(Material.ANVIL);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Kick");
				lore.add(ChatColor.AQUA + "Status: " + (user.isKickable() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.isKickable());
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable anticheat kicks");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(9, item);
				
				item = Reset(Material.EXP_BOTTLE);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Hide All Online Players");
				lore.add(ChatColor.AQUA + "Status: " + (user.HideOnlinePlayers() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.HideOnlinePlayers());
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Hide/Unhide All Online Players");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(10, item);
				
				item = Reset(Material.BOOK_AND_QUILL);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Direct Messages");
				lore.add(ChatColor.AQUA + "Status: " + (user.AllowDirectMessages() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.AllowDirectMessages());
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Allow/Disallow Direct Messages");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(11, item);
				
				if (player.hasPermission("me.killstorm103.rebug.server_owner") || player.hasPermission("me.killstorm103.rebug.server_admin"))
				{
					item = Reset(Material.TNT);
					itemMeta.setDisplayName(ChatColor.ITALIC.toString() + ChatColor.BOLD + ChatColor.RED + "REBUG " + ChatColor.RESET + ChatColor.ITALIC + ChatColor.GRAY + "Settings");
					item.setItemMeta(itemMeta);
					inventory.setItem(35, item);
				}
				
				
				for (int i = 0; i < inventory.getSize(); i ++)
				{
					if (inventory.getItem(i) == null)
					{
						item = Reset(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 7);
						itemMeta.setDisplayName(ChatColor.RED + "Add more Settings as i think of them!");
						item.setItemMeta(itemMeta);
						inventory.setItem(i, item);
					}
				}
				
				((Player) Menu.sender).openInventory(inventory);
			}
			if (menu.equalsIgnoreCase("Rebug_Settings") && (player.hasPermission("me.killstorm103.rebug.server_owner") || player.hasPermission("me.killstorm103.rebug.server_admin")))
			{
				inventory = OldInventory = PT.createInventory(player, 18, ChatColor.ITALIC.toString() + ChatColor.BOLD + ChatColor.RED + "REBUG " + ChatColor.RESET + ChatColor.ITALIC + ChatColor.GRAY + "Settings");
				item = Reset(Material.COMMAND);
				itemMeta.setDisplayName(ChatColor.ITALIC + ChatColor.RED.toString() + "Back");
				lore.add(ChatColor.AQUA + "Go back!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(0, item);
				
				item = Reset(Material.REDSTONE);
				itemMeta.setDisplayName(ChatColor.ITALIC + (Rebug.debug ? ChatColor.GREEN : ChatColor.RED).toString()  + "Debug");
				item.setItemMeta(itemMeta);
				inventory.setItem(1, item);
				
				item = Reset(Material.PAPER);
				itemMeta.setDisplayName(ChatColor.ITALIC + (Rebug.debugOpOnly ? ChatColor.GREEN : ChatColor.RED).toString()+ "Debug To Ops Only");
				item.setItemMeta(itemMeta);
				inventory.setItem(2, item);
				
				item = Reset(Material.PAPER);
				itemMeta.setDisplayName(ChatColor.ITALIC + ChatColor.RED.toString() + "Reload Config");
				item.setItemMeta(itemMeta);
				inventory.setItem(3, item);
				
				
				((Player) Menu.sender).openInventory(inventory);
			}
			
			if (menu.equalsIgnoreCase("items"))
			{
				inventory = OldInventory = PT.createInventory(player, 54, ChatColor.GREEN + "Items");
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
				
				item = Reset(Material.GOLDEN_APPLE, 64);
				item.setItemMeta(itemMeta);
				inventory.setItem(4, item);
				
				item = Reset(Material.GOLDEN_APPLE, 64, (short) 0, (byte) 1);
				item.setItemMeta(itemMeta);
				inventory.setItem(5, item);
				
				item = Reset(Material.WOOD_STEP, 64);
				item.setItemMeta(itemMeta);
				inventory.setItem(9, item);
				
				item = Reset(Material.BOW);
				item.addEnchantment(Enchantment.ARROW_INFINITE, Enchantment.ARROW_INFINITE.getMaxLevel());
				itemMeta.addEnchant(Enchantment.ARROW_INFINITE, Enchantment.ARROW_INFINITE.getMaxLevel(), true);
				item.setItemMeta(itemMeta);
				inventory.setItem(12, item);
				
				item = Reset(Material.ARROW, 64);
				item.setItemMeta(itemMeta);
				inventory.setItem(13, item);
				
				
				item = Reset(Material.WOOL, 1, (short) 0, (byte) 14);
				itemMeta.setDisplayName(ChatColor.RED + "Delete Item!");
				item.setItemMeta(itemMeta);
				inventory.setItem(53, item);
				
				for (int i = 0; i < inventory.getSize(); i ++)
				{
					if (inventory.getItem(i) == null)
					{
						item = Reset(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 7);
						itemMeta.setDisplayName(" ");
						item.setItemMeta(itemMeta);
						inventory.setItem(i, item);
					}
				}
				
				((Player) Menu.sender).openInventory(inventory);
			}
		}
		else
			Log(sender, Rebug.RebugMessage + "Only player's can run this command!");
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
