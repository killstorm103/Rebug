package me.killstorm103.Rebug.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;

public class User 
{
	private final Player player;
    private static User user;
    private String brand, register;
    private final int protocol;
    public boolean SentUpdatedCommand, Infinite_Blocks, InvSeed, CancelInteract, AutoCloseAntiCheatMenu, hasLoadedBefore, BrandSet, Hunger, Fire_Resistance, Damage_Resistance, Exterranl_Damage, Vanilla1_8FlyCheck, Vanilla1_9FlyCheck, NotifyFlyingKick, PotionEffects, AutoRefillBlocks, AntiCheatKick, AllowAT, ProximityPlayerHider, HideOnlinePlayers, AllowDirectMessages, ShowFlags, FallDamage;
    public org.bukkit.Location death_location;
	public int UnReceivedBrand, ShouldTeleportByBow;
    public final Map<UUID, Long> joinTimeMap = new HashMap<UUID, Long>();
    public final Map<Location, Block> BlockPlaced = new HashMap<Location, Block>();
    public String AntiCheat;
    public Player CommandTarget; // TODO: Fix this not setting to null when it should or maybe should leave it!?
    public Map<String, Boolean> AlertsEnabled = new HashMap<>();
    public Player getPlayer ()
    {
    	return this.player;
    }
    
	public User (Player player)
    {
        setUser(this);
        AlertsEnabled.clear();
        this.CommandTarget = null;
        this.OldInventory = this.CrashersMenu = this.ExploitsMenu = this.SettingsMenu = this.VanillaFlyChecksMenu = this.getRebugSettingsMenu = this.SpawnEntityCrashersMenu = null;
        this.AntiCheat = "Vanilla";
        this.player = player;
        this.BrandSet = this.HideOnlinePlayers = this.hasLoadedBefore = this.Vanilla1_9FlyCheck = this.Fire_Resistance = this.Damage_Resistance = this.CancelInteract = this.SentUpdatedCommand = false;
        this.brand = this.register = null;
        this.UnReceivedBrand = this.ShouldTeleportByBow = 0;
        this.protocol = getNumber (this.player);
        this.death_location = null;
        this.Hunger = this.Exterranl_Damage = this.NotifyFlyingKick = this.ProximityPlayerHider = this.AutoCloseAntiCheatMenu = this.AllowDirectMessages = this.AntiCheatKick = this.FallDamage = this.ShowFlags = this.PotionEffects = this.Vanilla1_8FlyCheck = this.AutoRefillBlocks = this.AllowAT = true;
    }
	public Location getLocation ()
	{
		return this.player.getLocation();
	}
	public String getDebugLocation ()
	{
		return "Player= " + getPlayer().getName() + " Loc= " + "(world: " + getWorld().getName() + ") " + 
				"(x: " + getLocation().getBlockX() + ") " + "(y: " + getLocation().getY() + ") " + "(z: " + getLocation().getBlockZ() + ") " + "(yaw: " + getLocation().getYaw() + ") " + "(pitch: " + getLocation().getPitch() + ")";
	}
	public void sendMessage (String message)
	{
		this.player.sendMessage(message);
	}
    public String getColoredAntiCheat () 
	{
    	if (AntiCheat.equalsIgnoreCase("Vanilla"))
    	{
    		AntiCheat = ChatColor.GREEN + "Vanilla";
    	}
    	else
    	{
    		int size = Config.getLoadedAntiCheats().size();
    		if (size > 0)
        	{
        		for (int i = 0; i < size; i ++)
            	{
        			String loaded = ChatColor.translateAlternateColorCodes('&', Config.getLoadedAntiCheats().get(i));
            		if (AntiCheat.equalsIgnoreCase(ChatColor.stripColor(loaded)))
            		{
            			AntiCheat = loaded;
            			break;
            		}
            	}
        	}
    	}
		return AntiCheat;
	}
    public String getValues (String menu, String SettingValue)
    {
    	String text = null;
    	if (!PT.INSTANCE.isStringNull(menu, SettingValue))
    	{
    		switch (menu.toLowerCase()) 
    		{
			case "player settings":
				switch (SettingValue.toLowerCase())
				{
				case "fall damage":
					text = ChatColor.AQUA + "Status: " + (FallDamage ? ChatColor.GREEN : ChatColor.DARK_RED) + FallDamage;
					break;
					
				case "exterranl damage":
					text = ChatColor.AQUA + "Status: " + (Exterranl_Damage ? ChatColor.GREEN : ChatColor.DARK_RED) + Exterranl_Damage;
					break;
					
				case "hunger":
					text = ChatColor.AQUA + "Status: " + (Hunger ? ChatColor.GREEN : ChatColor.DARK_RED) + Hunger;
					break;
					
				case "potions":
					text = ChatColor.AQUA + "Status: " + (PotionEffects ? ChatColor.GREEN : ChatColor.DARK_RED) + PotionEffects;
					break;
					
				case "auto refill blocks":
					text = ChatColor.AQUA + "Status: " + (AutoRefillBlocks ? ChatColor.GREEN : ChatColor.DARK_RED) + AutoRefillBlocks;
					break;
					
				case "hide all online players":
					text = ChatColor.AQUA + "Status: " + (HideOnlinePlayers ? ChatColor.GREEN : ChatColor.DARK_RED) + HideOnlinePlayers;
					break;
					
				case "proximity player hider":
					text = ChatColor.AQUA + "Status: " + (ProximityPlayerHider ? ChatColor.GREEN : ChatColor.DARK_RED) + ProximityPlayerHider;
					break;
					
				case "auto close anticheats menu":
					text = ChatColor.AQUA + "Status: " + (AutoCloseAntiCheatMenu ? ChatColor.GREEN : ChatColor.DARK_RED) + AutoCloseAntiCheatMenu;
					break;
					
				case "direct messages":
					text = ChatColor.AQUA + "Status: " + (AllowDirectMessages ? ChatColor.GREEN : ChatColor.DARK_RED) + AllowDirectMessages;
					break;
					
				case "allowat":
					text = ChatColor.AQUA + "Status: " + (AllowAT ? ChatColor.GREEN : ChatColor.DARK_RED) + AllowAT;
					break;
					
				case "flags":
					text = ChatColor.AQUA + "Status: " + (ShowFlags ? ChatColor.GREEN : ChatColor.DARK_RED) + ShowFlags;
					break;
					
				case "kick":
					text = ChatColor.AQUA + "Status: " + (AntiCheatKick ? ChatColor.GREEN : ChatColor.DARK_RED) + AntiCheatKick;
					break;
					
				case "notify on flying kick":
					text = ChatColor.AQUA + "Status: " + (NotifyFlyingKick ? ChatColor.GREEN : ChatColor.DARK_RED) + NotifyFlyingKick;
					break;
					
				case "infinite blocks":
					text = ChatColor.AQUA + "Status: " + (Infinite_Blocks ? ChatColor.GREEN : ChatColor.DARK_RED) + Infinite_Blocks;
					break;
					
				case "damage resistance":
					text = ChatColor.AQUA + "Status: " + (Damage_Resistance ? ChatColor.GREEN : ChatColor.DARK_RED) + Damage_Resistance;
					break;
					
				case "fire resistance":
					text = ChatColor.AQUA + "Status: " + (Fire_Resistance ? ChatColor.GREEN : ChatColor.DARK_RED) + Fire_Resistance;
					break;
					
				default:
					text = "Unknown " + menu + " Setting!";
					break;
				}
				break;

			default:
				text = "Unknown Menu!";
				break;
			}
    		
    	}
    	
    	return text;
    }
	public final int getVersion ()
    {
    	return Via.getAPI().getPlayerVersion(this.player.getUniqueId());
    }
    public final String getVersion_ ()
    {
    	return ProtocolVersion.getProtocol(this.protocol).getName();
    }
	public long getJoinTime(UUID uuid) {
        return joinTimeMap.getOrDefault(uuid, 0L);
    }
    public void setJoinTime(UUID uuid, long joinTime) {
        joinTimeMap.put(uuid, joinTime);
    }
	public String getRegister() {
		return register;
	}
	public void setRegister(String register) {
		this.register = register;
	}
	private int getNumber (Player player)
    {
    	int returning =- 1;
    	try
    	{
    		returning = PT.getPlayerVersion(player);
    	}
    	catch (Exception e) 
    	{
    		e.printStackTrace();
    		Bukkit.getServer().getScheduler().runTask(Rebug.getGetMain(), new Runnable()
    		{
				@Override
				public void run()
				{
					Bukkit.getServer().getConsoleSender().sendMessage(Rebug.RebugMessage + "Failed to get " + player.getName() + "'s protocol number so returning: -1");
				}
			});
    	}
    	
    	return returning;
    }
	public String getBrand() {
		return brand;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public int getProtocol() {
		return protocol;
	}

	public World getWorld ()
	{
		return this.player.getWorld();
	}
    public String ClientBrand ()
    {
    	return brand;
    }
    public static User getUser () 
    {
        return user;
    }
    public static void setUser(User userr) 
    {
        user = userr;
    }
	
	// Inventory
    public Inventory OldInventory, CrashersMenu, ExploitsMenu, SettingsMenu, VanillaFlyChecksMenu, getRebugSettingsMenu, SpawnEntityCrashersMenu;
 // Inventory Size can be: 9, 18, 27, 36, 45, 54
    
    
    public ArrayList<String> lore = new ArrayList<>();// clearing this as a test
	private ItemMeta itemMeta = null;
	private ItemStack item = null;
	public final Map<ItemStack, Integer> OldItems = new HashMap<>();
	public static final Map<ItemStack, Integer> TempItemsInvSee = new HashMap<>();
	static
	{
		@SuppressWarnings("deprecation")
		ItemStack tempstack =  new ItemStack(Material.WOOL, 1, (short) 0, (byte) 5);
		ItemMeta meta = tempstack.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Max Item");
		tempstack.setItemMeta(meta);
		TempItemsInvSee.put(tempstack, 16);
		@SuppressWarnings("deprecation")
		ItemStack tempstack2 = new ItemStack(Material.WOOL, 1, (short) 0, (byte) 14);
		ItemMeta meta2 = tempstack2.getItemMeta();
		meta2.setDisplayName(ChatColor.RED + "Delete Item");
		tempstack2.setItemMeta(meta2);
		TempItemsInvSee.put(tempstack2, 17);
	}
	
	public void UpdateMenuValueChangeLore(Inventory inventory, int slot, int loreslot, String ChangedTo)
	{
		if (inventory == null || PT.INSTANCE.isStringNull(ChangedTo)) return;
		
		
		ItemMeta NewMeta = inventory.getItem(slot).getItemMeta();
		List<String> NewLore = NewMeta.getLore();
		NewLore.set(loreslot, ChangedTo);
		NewMeta.setLore(NewLore);
		inventory.getItem(slot).setItemMeta(NewMeta);
	}
	public void UpdateItemInMenu (Inventory inventory, int itemslot, ItemStack NewItem)
	{
		if (inventory == null) return;
		
		inventory.setItem(itemslot, NewItem);
	}
	public void UpdateMenuLore (Inventory inventory, int itemslot, int loreslot, String text)
	{
		if (inventory == null || PT.INSTANCE.isStringNull(text)) return;
		
		
		ItemMeta NewMeta = inventory.getItem(itemslot).getItemMeta();
		List<String> NewLore = NewMeta.getLore();
		NewLore.set(loreslot, text);
		NewMeta.setLore(NewLore);
		inventory.getItem(itemslot).setItemMeta(NewMeta);
	}
	public void UpdateItemInHandLore (int loreslot, String text)
	{
		if (this.player.getItemInHand() == null || PT.INSTANCE.isStringNull(text)) return;
		
		
		ItemMeta NewMeta = this.player.getItemInHand().getItemMeta();
		List<String> NewLore = NewMeta.getLore();
		NewLore.set(loreslot, text);
		NewMeta.setLore(NewLore);
		this.player.getItemInHand().setItemMeta(NewMeta);
	}
	
	// Inventory Size can be: 9, 18, 27, 36, 45, 54
	private ItemStack Reset (Material material)
	{
		this.item = (ItemStack) (this.itemMeta = null);
		this.lore.clear();
		this.item = new ItemStack(material);
		this.itemMeta = this.item.getItemMeta();
		
		return this.item;
	}
	@SuppressWarnings("unused")
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
		this.item = new ItemStack(material, amount, damage, data);
		itemMeta = this.item.getItemMeta();
		
		return this.item;
	}
    public Inventory getCrashers ()
    {
    	if (CrashersMenu != null)
    	{
    		User user = Rebug.getUser(CommandTarget);
    		UpdateItemInMenu(CrashersMenu, 0, getMadeItems("%user-info%", user));
    	}
    	if (CrashersMenu == null)
    	{
    		User user = Rebug.getUser(CommandTarget);
    		Inventory inventory = OldInventory = PT.createInventory(getPlayer(), 18, ChatColor.DARK_RED + "Crashers");
    		item = getMadeItems("%user-info%", user);
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
			CrashersMenu = inventory;
    	}
    	
    	return OldInventory = CrashersMenu;
    }
    public Inventory getVanillaFlyChecks ()
    {
    	if (VanillaFlyChecksMenu == null)
    	{
    		Inventory inventory = PT.createInventory(player, 9, ChatColor.BLUE + "Vanilla Fly Checks");
			item = Reset(Material.COMMAND);
			itemMeta.setDisplayName(ChatColor.RED.toString() + ChatColor.ITALIC + "Back");
			lore.add(ChatColor.AQUA + "Go back!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(0, item);
			
			item = Reset(Vanilla1_8FlyCheck ? Material.BAKED_POTATO : Material.POTATO_ITEM);
			itemMeta.setDisplayName(ChatColor.ITALIC + "1.8.x");
			lore.add(ChatColor.AQUA + "Status: " + (Vanilla1_8FlyCheck ? ChatColor.GREEN : ChatColor.DARK_RED) + Vanilla1_8FlyCheck);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable 1.8.x Fly check");
			lore.add(ChatColor.AQUA + "In Development!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(1, item);
			
			item = Reset(Vanilla1_9FlyCheck ? Material.BAKED_POTATO : Material.POTATO_ITEM);
			itemMeta.setDisplayName(ChatColor.ITALIC + "1.9+");
			lore.add(ChatColor.AQUA + "Status: " + (Vanilla1_9FlyCheck ? ChatColor.GREEN : ChatColor.DARK_RED) + Vanilla1_9FlyCheck);
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
			VanillaFlyChecksMenu = inventory;
    	}
    	
    	return VanillaFlyChecksMenu;
    }
    public Inventory getSettings ()
    {
    	if (SettingsMenu != null)
    	{
    		if (player.hasPermission("me.killstorm103.rebug.server_owner") || player.hasPermission("me.killstorm103.rebug.server_admin") || player.isOp())
			{
				item = Reset(Material.TNT);
				itemMeta.setDisplayName(ChatColor.ITALIC.toString() + ChatColor.BOLD + ChatColor.RED + "REBUG " + ChatColor.RESET + ChatColor.ITALIC + ChatColor.GRAY + "Settings");
			}
    		else
    		{
    			item = Reset(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 7);
				itemMeta.setDisplayName(ChatColor.RED + "Add more Settings as i think of them!");
    		}
    		item.setItemMeta(itemMeta);
    		SettingsMenu.setItem(44, item);
    	}
    	if (SettingsMenu == null)
    	{
    		Inventory inventory = PT.createInventory(player, 45, ChatColor.BLUE + "Player Settings");
			item = Reset(Material.COOKED_BEEF);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Hunger");
			lore.add(ChatColor.AQUA + "Status: " + (Hunger ? ChatColor.GREEN : ChatColor.DARK_RED) + Hunger);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Hunger");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(0, item);
			
			item = Reset(Material.FEATHER);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Fall Damage");
			lore.add(ChatColor.AQUA + "Status: " + (FallDamage ? ChatColor.GREEN : ChatColor.DARK_RED) + FallDamage);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Fall Damage");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(1, item);
			
			item = Reset(Material.ARROW);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Exterranl Damage");
			lore.add(ChatColor.AQUA + "Status: " + (Exterranl_Damage ? ChatColor.GREEN : ChatColor.DARK_RED) + Exterranl_Damage);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Exterranl Damage");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(2, item);
			
			
			item = Reset(Material.ARMOR_STAND);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Damage Resistance");
			lore.add(ChatColor.AQUA + "Status: " + (Damage_Resistance ? ChatColor.GREEN : ChatColor.DARK_RED) + Damage_Resistance);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Damage Resistance");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(3, item);
			
			
			item = Reset(Material.FIREBALL);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Fire Resistance");
			lore.add(ChatColor.AQUA + "Status: " + (Fire_Resistance ? ChatColor.GREEN : ChatColor.DARK_RED) + Fire_Resistance);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Fire Resistance");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(4, item);
			
			item = Reset(Material.COMMAND);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Vanilla Fly Checks");
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Vanilla Fly Checks");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(5, item);
			
			item = Reset(Material.BOOK_AND_QUILL);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Notify On Flying Kick");
			lore.add(ChatColor.AQUA + "Status: " + (NotifyFlyingKick ? ChatColor.GREEN : ChatColor.DARK_RED) + NotifyFlyingKick);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable alerts");
			lore.add(ChatColor.RESET + "for you being picked up for");
			lore.add(ChatColor.RESET + "flying by vanilla");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(6, item);
			
			item = Reset(Material.POTION);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Potions");
			lore.add(ChatColor.AQUA + "Status: " + (PotionEffects ? ChatColor.GREEN : ChatColor.DARK_RED) + PotionEffects);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Potion Effects");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(7, item);
			
			item = Reset(Material.STONE);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Auto Refill Blocks");
			lore.add(ChatColor.AQUA + "Status: " + (AutoRefillBlocks ? ChatColor.GREEN : ChatColor.DARK_RED) + AutoRefillBlocks);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Auto Block Refill");
			lore.add(ChatColor.RESET + "for Scaffold Test Area");
			lore.add(ChatColor.AQUA + "In Development!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(8, item);
			
			item = Reset(Material.DISPENSER);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Infinite Blocks");
			lore.add(ChatColor.AQUA + "Status: " + (Infinite_Blocks ? ChatColor.GREEN : ChatColor.DARK_RED) + Infinite_Blocks);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable blocks you place being Infinite!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(9, item);
			
			item = Reset(Material.PAPER);
			itemMeta.setDisplayName(ChatColor.ITALIC + "AllowAT");
			lore.add(ChatColor.AQUA + "Status: " + (AllowAT ? ChatColor.GREEN : ChatColor.DARK_RED) + AllowAT);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Allow other players to mention you in chat");
			lore.add(ChatColor.AQUA + "In Development!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(10, item);
			
			item = Reset(Material.BANNER);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Flags");
			lore.add(ChatColor.AQUA + "Status: " + (ShowFlags ? ChatColor.GREEN : ChatColor.DARK_RED) + ShowFlags);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Show anticheat flags");
			lore.add(ChatColor.AQUA + "In Development!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(11, item);
			
			
			item = Reset(Material.ANVIL);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Kick");
			lore.add(ChatColor.AQUA + "Status: " + (AntiCheatKick ? ChatColor.GREEN : ChatColor.DARK_RED) + AntiCheatKick);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable anticheat kicks");
			lore.add(ChatColor.AQUA + "In Development!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(12, item);
			
			item = Reset(Material.EXP_BOTTLE);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Hide All Online Players");
			lore.add(ChatColor.AQUA + "Status: " + (HideOnlinePlayers ? ChatColor.GREEN : ChatColor.DARK_RED) + HideOnlinePlayers);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Hide/Unhide All Online Players");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(13, item);
			
			item = Reset(Material.IRON_DOOR);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Proximity Player Hider");
			lore.add(ChatColor.AQUA + "Status: " + (ProximityPlayerHider ? ChatColor.GREEN : ChatColor.DARK_RED) + ProximityPlayerHider);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Hide players that are near you!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(14, item);
			
			item = Reset(Material.BOOK_AND_QUILL);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Direct Messages");
			lore.add(ChatColor.AQUA + "Status: " + (AllowDirectMessages ? ChatColor.GREEN : ChatColor.DARK_RED) + AllowDirectMessages);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Allow/Disallow Direct Messages");
			lore.add(ChatColor.AQUA + "In Development!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(15, item);


			item = Reset(Material.BED);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Auto Close AntiCheats Menu");
			lore.add(ChatColor.AQUA + "Status: " + (AutoCloseAntiCheatMenu ? ChatColor.GREEN : ChatColor.DARK_RED) + AutoCloseAntiCheatMenu);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Auto Closing AntiCheat Menu");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(16, item);
			
			
			if (player.hasPermission("me.killstorm103.rebug.server_owner") || player.hasPermission("me.killstorm103.rebug.server_admin") || player.isOp())
			{
				item = Reset(Material.TNT);
				itemMeta.setDisplayName(ChatColor.ITALIC.toString() + ChatColor.BOLD + ChatColor.RED + "REBUG " + ChatColor.RESET + ChatColor.ITALIC + ChatColor.GRAY + "Settings");
				item.setItemMeta(itemMeta);
				inventory.setItem(44, item);
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
			SettingsMenu = inventory;
    	}
    	
    	return OldInventory = SettingsMenu;
    }
    
    public Inventory getExploits ()
    {
    	if (ExploitsMenu != null)
    	{
    		User user = Rebug.getUser(CommandTarget);
    		item = getMadeItems("%user-info%", user);
    	}
    	if (ExploitsMenu == null)
    	{
    		User user = Rebug.getUser(CommandTarget);
    		Inventory inventory = PT.createInventory(player, 9, ChatColor.DARK_RED + "Exploits");
    		item = getMadeItems("%user-info%", user);
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
			
			item = Reset(Material.ARMOR_STAND);
			itemMeta.setDisplayName(ChatColor.RED + "Spawn Player");
			lore.add("Spawn Player Exploit!");
			lore.add("Freezes the player and stops");
			lore.add("them from being able");
			lore.add("to move!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(6, item);
			
			
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
			
			ExploitsMenu = inventory;
    	}
    	
    	return ExploitsMenu;
    }
    private ItemStack Reset (ItemStack item)
	{
		this.item = (ItemStack) (this.itemMeta =  null);
		lore.clear();
		this.item = item;
		itemMeta = this.item.getItemMeta();
		
		return this.item;
	}
    
    public Inventory getSpawnEntityCrashers ()
    {
    	if (CrashersMenu == null || this.player.getOpenInventory() != CrashersMenu)
    		return null;
    	
    	
    	this.player.closeInventory();
    	if (SpawnEntityCrashersMenu == null)
    	{
    		Inventory inventory = PT.createInventory(this.player, 9, ChatColor.RED + "Spawn Entity Crashers");
			item = Reset(new ItemStack(Material.WOOL, 1, (short) 14));
			itemMeta.setDisplayName(ChatColor.BLUE + "Back");
			lore.add("takes you back to the");
			lore.add("crashers menu.");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(0, item);
			
			item = Reset(new ItemStack(Material.SKULL_ITEM, 1, (short) 4));
			itemMeta.setDisplayName(ChatColor.RED + "Creeper");
			lore.add("Creeper Crash Exploit!");
			lore.add("");
			lore.add("Works on " + ChatColor.DARK_RED + "1.8.x");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(1, item);
			
			item = Reset(new ItemStack(Material.MOB_SPAWNER));
			itemMeta.setDisplayName(ChatColor.RED + "Test");
			lore.add("Test Crash Exploit!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(2, item);
			
			SpawnEntityCrashersMenu = inventory;
    	}
    	
    	return SpawnEntityCrashersMenu;
    }
    
    public Inventory getRebugSettingsMenu ()
    {
    	if (getRebugSettingsMenu == null)
    	{
    		Inventory inventory = PT.createInventory(player, 18, ChatColor.ITALIC.toString() + ChatColor.BOLD + ChatColor.RED + "REBUG " + ChatColor.RESET + ChatColor.ITALIC + ChatColor.GRAY + "Settings");
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
			
			item = Reset(Material.PAPER);
			itemMeta.setDisplayName(ChatColor.ITALIC + (Rebug.KickOnReloadConfig ? ChatColor.GREEN : ChatColor.RED).toString() + "Kick on Reload Config");
			item.setItemMeta(itemMeta);
			inventory.setItem(4, item);
			
			item = Reset(Material.WOOL, 1, (short) 0, (byte) 14);
			itemMeta.setDisplayName(ChatColor.ITALIC + ChatColor.RED.toString() + "Reset Scaffold Area");
			item.setItemMeta(itemMeta);
			inventory.setItem(9, item);

			item = Reset(Material.WOOL, 1, (short) 0, (byte) 14);
			itemMeta.setDisplayName(ChatColor.ITALIC + ChatColor.RED.toString() + "Update AntiCheats");
			item.setItemMeta(itemMeta);
			inventory.setItem(10, item);
			
			getRebugSettingsMenu = inventory;
    	}
    	
    	return getRebugSettingsMenu;
    }
    public ItemStack getMadeItems (String ItemName, User user)
    {
    	ItemStack order = null;
    	if (user == null || PT.INSTANCE.isStringNull(ItemName)) return order;
    	
    	ArrayList<String> lore = new ArrayList<>();
    	lore.clear();
    	ItemMeta itemMeta = null;
    	
    	switch (ItemName.toLowerCase())
    	{
		case "%user-info%":
			order = Reset(Material.BOOK_AND_QUILL);
			itemMeta = order.getItemMeta();
			itemMeta.setDisplayName(ChatColor.DARK_PURPLE + "User " + ChatColor.GRAY + user.getPlayer().getName());
			lore.add("AntiCheat " + user.getColoredAntiCheat());
			lore.add("Client Brand " + ChatColor.GRAY + user.getBrand());
			lore.add("Version " + ChatColor.GRAY + PT.getPlayerVersion(user.getProtocol()) + " (" + PT.getPlayerVersion(user.getPlayer()) + ")");
			String world = user.getWorld().getName();
			world = world.length() > 5 ? world = PT.SubString(world, 5, world.length()).replace("_", "").replace("theend", "The End") : world;
			world = world.equalsIgnoreCase("the end") ? world : world.replace(PT.SubString(world, 0, 1), PT.SubString(world, 0, 1).toUpperCase());
			lore.add("World " + ChatColor.GRAY + world);
			itemMeta.setLore(lore);
			break;
			
		case "%delete_item%":
			order = Reset(Material.WOOL, 1, (short) 0, (byte) 14);
			itemMeta = order.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RED + "Delete Item!");
			break;
			
		case "%debug_item%":
			order = Reset(Material.WOOL, 1, (short) 0, (byte) 1);
			itemMeta = order.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RED + "Debug Item!");
			break;

		default:
			break;
		}
    	if (order != null)
    		order.setItemMeta(itemMeta);
    	
    	return order;
    }
	public ItemStack getMadeItems(String menuName, String itemName)
	{
		switch (menuName.toLowerCase())
		{
		case "vanilla fly checks":
			if (itemName.equalsIgnoreCase("1.8.x"))
			{
				item = Reset(Vanilla1_8FlyCheck ? Material.BAKED_POTATO : Material.POTATO_ITEM);
				itemMeta.setDisplayName(ChatColor.ITALIC + "1.8.x");
				lore.add(ChatColor.AQUA + "Status: " + (Vanilla1_8FlyCheck ? ChatColor.GREEN : ChatColor.DARK_RED) + Vanilla1_8FlyCheck);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable 1.8.x Fly check");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("1.9+"))
			{
				item = Reset(Vanilla1_9FlyCheck ? Material.BAKED_POTATO : Material.POTATO_ITEM);
				itemMeta.setDisplayName(ChatColor.ITALIC + "1.9+");
				lore.add(ChatColor.AQUA + "Status: " + (Vanilla1_9FlyCheck ? ChatColor.GREEN : ChatColor.DARK_RED) + Vanilla1_9FlyCheck);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable 1.9+ Fly check");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				return item;
			}
			break;
			
		case "rebug settings":
			if (itemName.equalsIgnoreCase("Debug"))
			{
				item = Reset(Material.REDSTONE);
				itemMeta.setDisplayName(ChatColor.ITALIC + (Rebug.debug ? ChatColor.GREEN : ChatColor.RED).toString()  + "Debug");
				item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("Debug To Ops Only"))
			{
				item = Reset(Material.PAPER);
				itemMeta.setDisplayName(ChatColor.ITALIC + (Rebug.debugOpOnly ? ChatColor.GREEN : ChatColor.RED).toString()+ "Debug To Ops Only");
				item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("Kick on reload config"))
			{
				item = Reset(Material.PAPER);
				itemMeta.setDisplayName(ChatColor.ITALIC + (Rebug.KickOnReloadConfig ? ChatColor.GREEN : ChatColor.RED).toString() + "Kick on Reload Config");
				item.setItemMeta(itemMeta);
				return item;
			}
			break;

		default:
			break;
		}
		return null;
	}
	public boolean hasPermission (String perm) 
	{
		return getPlayer().hasPermission(perm);
	}
}
