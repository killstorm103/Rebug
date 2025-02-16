package me.killstorm103.Rebug.Utils.Menu.Old;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import lombok.Getter;
import lombok.Setter;
import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Utils.FileUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.QuickConfig;
import me.killstorm103.Rebug.Utils.ServerVersionUtil;

@SuppressWarnings("deprecation")
public class ItemsAndMenusUtils
{
	public static final List<String> TabAntiCheats = new ArrayList<>();
	@Getter
	private static final String[] AntiCheatDepends = new String[] {"PacketEvents", "ProtocolLib", "Atlas", "ProtocolSupport", "PacketListenerApi", "HamsterAPI", "TinyProtocol", "PacketAPI", "GPacket"};
	
	public static Inventory getTopInventory (InventoryEvent event) 
	{
	    try {
	        Object view = event.getView();
	        Method getTopInventory = view.getClass().getMethod("getTopInventory");
	        getTopInventory.setAccessible(true);
	        return (Inventory) getTopInventory.invoke(view);
	    } 
	    catch (Exception e)
	    {
	        throw new RuntimeException(e);
	    }
	}
	public static Inventory getTopInventory (InventoryView e) 
	{
	    try {
	        Object view = e;
	        Method getTopInventory = view.getClass().getMethod("getTopInventory");
	        getTopInventory.setAccessible(true);
	        return (Inventory) getTopInventory.invoke(view);
	    } 
	    catch (Exception ee)
	    {
	        throw new RuntimeException(ee);
	    }
	}
	// Never used but there if i need it!
	public static Inventory getBottomInventory (InventoryEvent event) 
	{
	    try {
	        Object view = event.getView();
	        Method getBottomInventory = view.getClass().getMethod("getBottomInventory");
	        getBottomInventory.setAccessible(true);
	        return (Inventory) getBottomInventory.invoke(view);
	    } 
	    catch (Exception e)
	    {
	        throw new RuntimeException(e);
	    }
	}
	
	@Getter
	private static final ItemsAndMenusUtils INSTANCE = new ItemsAndMenusUtils();
	@Setter
	private static Inventory AntiCheatMenu, RebugSettingsMenu, ItemsMenu, DebugItemMenu;
	// Inventory Size can be: 9, 18, 27, 36, 45, 54
	public ItemStack Reset (Material material) 
	{
		this.item = (ItemStack) (this.itemMeta = null);
		this.lore.clear();
		this.item = new ItemStack(material);
		this.itemMeta = this.item.getItemMeta();
		this.item.setItemMeta(this.itemMeta);
			
		return this.item;
	}
	public ItemStack Reset (Material material, int amount)
	{
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = new ItemStack(material, amount);
		itemMeta = this.item.getItemMeta();
		this.item.setItemMeta(this.itemMeta);

		return this.item;
	}
	public ItemStack Reset (Material material, int amount, short damage)
	{
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = new ItemStack(material, amount, damage);
		itemMeta = this.item.getItemMeta();
		this.item.setItemMeta(this.itemMeta);

		return this.item;
	}
	@SuppressWarnings("removal")
	public ItemStack Reset (Material material, Integer amount, Short damage, Byte data) 
	{
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = new ItemStack(material, amount, damage, data);
		itemMeta = this.item.getItemMeta();
		this.item.setItemMeta(this.itemMeta);
		return this.item;
	}
	public ItemStack Reset (ItemStack item)
	{
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = item;
		itemMeta = this.item.getItemMeta();
		this.item.setItemMeta(this.itemMeta);
		return this.item;
	}
	public void UpdateMenuValueChangeLore (Inventory inventory, int slot, int loreslot, String ChangedTo) 
	{
		if (inventory == null || PT.isStringNull(ChangedTo))
			return;

		ItemMeta NewMeta = inventory.getItem(slot).getItemMeta();
		List<String> NewLore = NewMeta.getLore();
		NewLore.set(loreslot, ChangedTo);
		NewMeta.setLore(NewLore);
		inventory.getItem(slot).setItemMeta(NewMeta);
	}
	public String getValues (boolean SetValue) 
	{
		return ChatColor.AQUA + "Status: " + (SetValue ? ChatColor.GREEN : ChatColor.DARK_RED) + SetValue;
	}
	public ArrayList<String> lore = new ArrayList<>();
	public ItemMeta itemMeta = null;
	public ItemStack item = null;
	
	public Inventory getItemsMenu ()
	{
		if (ItemsMenu == null)
		{
			Inventory inventory = PT.createInventory(null, FileUtils.getMenusConfig().getConfigurationSection("Items.menu").getInt("size", 54), "Items");
			try
			{
				FileUtils.getMenusConfig().getConfigurationSection("Items").getKeys(false).forEach(key -> 
				{
					if (key.equalsIgnoreCase("menu")) return;
					
					boolean foundSV = FileUtils.getMenusConfig().getConfigurationSection("Items").get(ServerVersionUtil.getServer_Version()) != null;
					String path = foundSV ? key + "." + ServerVersionUtil.getServer_Version() : key + ".default", displayname = 
					FileUtils.getMenusConfig().getConfigurationSection("Items." + key).getString(path + ".display-name", "");
					//Unbreakable = PT.isServerOlderThan(ServerVersion.V_1_15) && FileUtils.getMenusConfig().getConfigurationSection("Items." + path)
					if (!foundSV && RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false))
					{
						RebugPlugin.getINSTANCE().Log(Level.WARNING, key + ".item." + ServerVersionUtil.getServer_Version());
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "wasn't found trying to use .default");
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "This is a warning for Items inside Menus.yml!");
					}
					Material items_item = Material.getMaterial(path + ".material");
					if (items_item == null || items_item == Material.AIR)
					{
						items_item = Material.DIRT;
						if (RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false))
							RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Failed to create items_item: " + key + " switching items_item to DIRT");
					}
					try
					{
						if (FileUtils.getMenusConfig().getConfigurationSection("Items").get (path + ".has-data") != null && FileUtils.getMenusConfig().getConfigurationSection("Items").get (path + ".data") != null && FileUtils.getMenusConfig().getConfigurationSection("Items").getBoolean (path + ".has-data"))
							item = Reset(items_item, 1, (short) 0, (byte) FileUtils.getMenusConfig().getConfigurationSection("Items").getInt(path + ".data"));
						else
							item = Reset(items_item);
					}
					catch (Exception e) 
					{
						item = Reset(items_item);
					}
					if (!PT.isStringNull(displayname))
						itemMeta.setDisplayName(displayname);
					
					item.setItemMeta(itemMeta);
					int slot = FileUtils.getMenusConfig().getConfigurationSection("Items." + path).getInt("slot", -1);
					if (slot <- 1)
						RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Slot for Item: " + key + " is less than 0, go set the Slot number!");
					else
						inventory.setItem(slot, item);
				});
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			ItemsMenu = inventory;
		}
		
		return ItemsMenu;
	}
	
	public Inventory getRebugSettings ()
	{
		if (RebugSettingsMenu == null)
		{
			Inventory inventory = PT.createInventory(null, FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings.menu").getInt("size", 18), ChatColor.RED + "Rebug Settings");
			RebugPlugin.getSettingsBooleans().clear();
			RebugPlugin.getSettingsDoubles().clear();
			RebugPlugin.getSettingsIntegers().clear();
			RebugPlugin.getSettingsActions().clear();
			RebugPlugin.getSettingsList().clear();
			try
			{
				FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getKeys(false).forEach(key -> 
				{
					if (!key.equalsIgnoreCase("menu"))
					{
						String type = FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getString(key + ".type", "unknown");
						switch (type.toLowerCase())
						{
						case "boolean":
							RebugPlugin.getSettingsBooleans().put(key, FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getBoolean(key + ".default-setting-value"));
							RebugPlugin.getSettingsList().put(key, type);
							break;
							
						case "action":
							RebugPlugin.getSettingsActions().put(key, "Click me to do a Action");
							RebugPlugin.getSettingsList().put(key, type);
							break;
							
						case "double":
							RebugPlugin.getSettingsDoubles().put(key, FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getDouble(key + ".default-setting-value"));
							RebugPlugin.getSettingsList().put(key, type);
							break;
					   
						case "integer":
							RebugPlugin.getSettingsIntegers().put(key, FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getInt(key + ".default-setting-value"));
							RebugPlugin.getSettingsList().put(key, type);
							break;

						default:
							RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Unknown Rebug Setting Type Setting Name: " + key + " Type: " + type);
							RebugPlugin.getINSTANCE().Log(Level.INFO, "Valid Types are: Boolean, Double, Integer");
							break;
						}
					}
				});
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			try
			{
				for (Map.Entry<String, String> list : RebugPlugin.getSettingsList().entrySet())
				{
					String name = list.getKey();
					boolean foundSV = FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").get (name + ".item." + ServerVersionUtil.getServer_Version()) != null;
					String path = foundSV ? name + ".item." + ServerVersionUtil.getServer_Version() : name + ".item.default";
					int slot = FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getInt(name + ".item" + ".slot");
					if (!foundSV && RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false))
					{
						RebugPlugin.getINSTANCE().Log(Level.WARNING, name + ".item." + ServerVersionUtil.getServer_Version());
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "wasn't found trying to use .default");
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "This is a warning for Rebug Setting inside Player Settings.yml!");
					}
					Material RebugSetting = Material.getMaterial(FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getString(path + ".material", Material.DIRT.name()));
					if (RebugSetting == null || RebugSetting == Material.AIR)
					{
						RebugSetting = Material.DIRT;
						if (RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false))
							RebugPlugin.getINSTANCE().Log(Level.WARNING, "Failed to create RebugSetting(Item) so changing it to DIRT");
					}
					try
					{
						if (FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").get (path + ".has-data") != null && FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").get (path + ".data") != null && FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getBoolean (path + ".has-data"))
							item = Reset(RebugSetting, 1, (short) 0, (byte) FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getInt(path + ".data"));
						else
							item = Reset(RebugSetting);
					}
					catch (Exception e) 
					{
						item = Reset(RebugSetting);
					}
					itemMeta.setDisplayName(ChatColor.WHITE + name);
					switch (list.getValue().toLowerCase())
					{
					case "boolean":
						lore.add(ItemsAndMenusUtils.getINSTANCE().getValues(RebugPlugin.getSettingsBooleans().get(name)));
						break;
						
					case "action":
						lore.add(ChatColor.AQUA + "Info" + ChatColor.WHITE + ": " + RebugPlugin.getSettingsActions().get(name));
						break;
						
					case "double":
						lore.add(ChatColor.AQUA + "Value" + ChatColor.WHITE + ": " + RebugPlugin.getSettingsDoubles().get(name));
						break;
						
					case "int":
					case "integer":
						lore.add(ChatColor.AQUA + "Value" + ChatColor.WHITE + ": " + RebugPlugin.getSettingsIntegers().get(name));
						break;

					default:
						break;
					}
					for (int i = 0 ; i < FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getStringList(name + ".item.description").size(); i ++)
					{
						if (i == 0)
							lore.add(ChatColor.AQUA + "Description" + ChatColor.WHITE + ": " + FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getStringList(name + ".item.description").get(0));
						else
							lore.add(ChatColor.WHITE + FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getStringList(name + ".item.description").get(i));
					}
					if (FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").get (name + ".item.state") != null && !PT.isStringNull(FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getString(name + ".item.state")))
						lore.add(ChatColor.AQUA + "State" + ChatColor.WHITE + ": " + FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getString(name + ".item.state"));
				
					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
					if (slot < 0)
					{
						if (RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false))
							RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Couldn't add Rebug Setting item to Menu due to slot < 0, setting: " + name);
					}
					else
						inventory.setItem(slot, item);
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			RebugSettingsMenu = inventory;
		}
		
		return RebugSettingsMenu;
	}
	public Inventory getAntiCheats ()
	{
		if (AntiCheatMenu == null)
		{
			Inventory inventory = PT.createInventory(null, FileUtils.getLoadedAntiCheatsFile().getInt("loaded-anticheats.Inventory.size", 54), ChatColor.RED + "AntiCheats");
			int size = QuickConfig.getLoadedAntiCheats().size(), manualsize = FileUtils.getLoadedAntiCheatsFile().getStringList("manually-added-anticheats.ac-names").size();
			
			RebugPlugin.End_Game_AntiCheats.clear();
			RebugPlugin.anticheats.clear();
			RebugPlugin.manual_anticheats.clear();
			ItemsAndMenusUtils.TabAntiCheats.clear();
			if (size < 1 && manualsize < 1)
				RebugPlugin.getINSTANCE().Log(Level.WARNING, "Add the AntiCheats to: loaded-anticheats in the config file!");
			
			else
				ItemsAndMenusUtils.TabAntiCheats.add("Vanilla");
			
			if (size >= 1)
			{
				for (Plugin AC : Bukkit.getPluginManager().getPlugins())
				{
					for (int line = 0; line < size; line ++) 
					{
						String name = QuickConfig.getLoadedAntiCheats().get(line);
						if (name != null && AC != null && AC.getName().equalsIgnoreCase(name) && !RebugPlugin.anticheats.containsKey(AC)) 
						{
							if (RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false))
								RebugPlugin.getINSTANCE().Log(Level.INFO, "Adding AntiCheat to list= " + AC.getName());

							ItemsAndMenusUtils.TabAntiCheats.add(name);
							RebugPlugin.anticheats.put(AC, name);
						}
					}
				}
			}
			if (manualsize >= 1)
			{
				for (String ac : FileUtils.getLoadedAntiCheatsFile().getStringList("manually-added-anticheats.ac-names"))
				{
					ItemsAndMenusUtils.TabAntiCheats.add(ac);
					RebugPlugin.manual_anticheats.put(ac, false);
				}
			}
			final Material SafeGuard = Material.DIRT;
			if (!RebugPlugin.anticheats.isEmpty() || !RebugPlugin.manual_anticheats.isEmpty())
			{
				boolean foundSV = FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats.multiple-anticheat.item." + ServerVersionUtil.getServer_Version()) != null;
				String path = null;
				if (!foundSV)
				{
					RebugPlugin.getINSTANCE().Log(Level.WARNING, "Warning for Loaded AntiCheats.yml!");
					RebugPlugin.getINSTANCE().Log(Level.WARNING, "loaded-anticheats.multiple-anticheat.item." + ServerVersionUtil.getServer_Version() + " Wasn't Found!");
					RebugPlugin.getINSTANCE().Log(Level.INFO, "You are using .default");
				}
				path = foundSV ? "loaded-anticheats.multiple-anticheat.item." + ServerVersionUtil.getServer_Version() + ".name" : "loaded-anticheats.multiple-anticheat.item.default" + ".name";

				Material multiple_anticheats = Material.getMaterial(FileUtils.getLoadedAntiCheatsFile().getString(path));
				if (multiple_anticheats == null || multiple_anticheats == Material.AIR)
				{
					RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Error for Loaded AntiCheats.yml!");
					RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Failed to create Material multiple_anticheats!");
					multiple_anticheats = SafeGuard;
				}
				item = Reset(multiple_anticheats);
				itemMeta.setDisplayName(ChatColor.WHITE + "Info");
				lore.add("");
				lore.add(ChatColor.AQUA + "To select Multiple AntiCheats");
				lore.add(ChatColor.AQUA + "do /ac <anticheat> <anticheat> (....)");
				lore.add("");
				lore.add(ChatColor.AQUA + "Multiple AntiCheats: " + (FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.multiple-anticheat.enabled") ? ChatColor.GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));
				final int limit = FileUtils.getLoadedAntiCheatsFile().getInt("loaded-anticheats.multiple-anticheat.max-anticheats");
				lore.add(ChatColor.AQUA + "Limiter: " + (limit < 2 || !FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.multiple-anticheat.limiter-enabled") ? ChatColor.DARK_RED + "Disabled" : ChatColor.GREEN + "Enabled"));
				lore.add(ChatColor.AQUA + "Max AntiCheats: " + ChatColor.WHITE + limit);
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(FileUtils.getLoadedAntiCheatsFile().getBoolean("custom-info-menu-slot")
				? FileUtils.getLoadedAntiCheatsFile().getInt("info-item-slot")
				: 0, item);
				
				
				foundSV = FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats.vanilla.item." + ServerVersionUtil.getServer_Version()) != null;
				path = foundSV ? "loaded-anticheats.vanilla.item." + ServerVersionUtil.getServer_Version() + ".name" : "loaded-anticheats.vanilla.item.default" + ".name";
				
				Material vanilla_anticheat = Material.getMaterial(FileUtils.getLoadedAntiCheatsFile().getString(path));
				if (vanilla_anticheat == null || vanilla_anticheat == Material.AIR)
				{
					RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Error for Loaded AntiCheats.yml!");
					RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Failed to create Material vanilla_anticheat!");
					vanilla_anticheat = SafeGuard;
				}
				item = Reset(vanilla_anticheat);
				itemMeta.setDisplayName(ChatColor.WHITE + "Vanilla");
				lore.add("");
				lore.add(ChatColor.AQUA + "Status: " + (FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.vanilla.enabled") ? ChatColor.GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));
				lore.add("");
				boolean g_type2 = FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.vanilla.server-type-version.enabled", false), g_type1 = FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.vanilla.server-type.enabled", false);
				if (g_type1)
				{
					String server_type = ServerVersionUtil.getSoftWareType();
					if (!FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.vanilla.server-type.detection") && !PT.isStringNull(FileUtils.getLoadedAntiCheatsFile().getString("loaded-anticheats.vanilla.server-type.custom")))
						server_type = FileUtils.getLoadedAntiCheatsFile().getString("loaded-anticheats.vanilla.server-type.custom");
					
					if (g_type2)
					{
						lore.add(ChatColor.AQUA + "SoftWare" + ChatColor.WHITE + ":");
						lore.add(ChatColor.AQUA + "Type: " + ChatColor.WHITE + server_type);
					}
					else
						lore.add(ChatColor.AQUA + "SoftWare Type: " + ChatColor.WHITE + server_type);
					
					if (g_type2)
					{
						String type_version = ServerVersionUtil.getVersionOfSoftWare();
						if (!FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.vanilla.server-type-version.detection") && !PT.isStringNull(FileUtils.getLoadedAntiCheatsFile().getString("loaded-anticheats.vanilla.server-type-version.custom")))
							server_type = FileUtils.getLoadedAntiCheatsFile().getString("loaded-anticheats.vanilla.server-type-version.custom");
						
						lore.add(ChatColor.AQUA + "Version" + ChatColor.WHITE + ": " + type_version);
						lore.add(" ");
					}
				}
				if (g_type1 && g_type2)
					lore.add(ChatColor.AQUA + "Minecraft" + ChatColor.WHITE + ":");
					
				lore.add(ChatColor.AQUA + "Version: " + ChatColor.WHITE + ServerVersionUtil.getServer_Version() + " (" + ServerVersionUtil.getVersionName() + ")");
				lore.add(ChatColor.AQUA + "NetworkID: " + ChatColor.WHITE + ServerVersionUtil.getNetworkId());
				lore.add(ChatColor.AQUA + "Author: " + ChatColor.WHITE + "Mojang");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.vanilla.custom-slot") ? FileUtils.getLoadedAntiCheatsFile().getInt("loaded-anticheats.vanilla.slot"): 1, item);
			}
			int adding = 2;
			final boolean CustomSlots = FileUtils.getLoadedAntiCheatsFile().getBoolean("Custom Slots");
			if (!RebugPlugin.manual_anticheats.isEmpty())
			{
				for (Map.Entry<String, Boolean> mapped : RebugPlugin.manual_anticheats.entrySet())
				{
					String name = mapped.getKey(), config_name = name.toLowerCase(), path = null, path_data = null;
					boolean foundSV = FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats." + config_name + ".item." + ServerVersionUtil.getServer_Version()) != null, 
					useID = FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + config_name + ".item.use-material-id");
					if (foundSV)
					{
						path = "loaded-anticheats." + config_name + ".item." + ServerVersionUtil.getServer_Version() + "." + (useID ? "id" : "name");
						path_data = "loaded-anticheats." + config_name + ".item." + ServerVersionUtil.getServer_Version() + ".";
					}
					else
					{
						path = "loaded-anticheats." + config_name + ".item.default" + "." + (useID ? "id" : "name");
						path_data = "loaded-anticheats." + config_name + ".item.default" + ".";
					}
					
					Material AntiCheatToItem = Material.getMaterial(FileUtils.getLoadedAntiCheatsFile().getString(path));
					if (AntiCheatToItem == null || AntiCheatToItem == Material.AIR)
					{
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "Failed to create AC (" + name + ") ItemType was AIR/Null");
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "Changing " + name + "'s AntiCheatToItem to SafeGuard (" + SafeGuard.name() + " : " + SafeGuard + ")");
						AntiCheatToItem = SafeGuard;
					}
					try
					{
						if (FileUtils.getLoadedAntiCheatsFile().getBoolean(path_data + "has-data", false))
						{
							int data = FileUtils.getLoadedAntiCheatsFile().getInt(path_data + "data", 0);
							item = Reset(AntiCheatToItem, 1, (short) 0, (byte) data);
						}
						else
							item = Reset(AntiCheatToItem);
					}
					catch (Exception ee) 
					{
						item = Reset(AntiCheatToItem);
					}
					
					if (FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + config_name + ".enable_enchantment", false)) 
					{
						if (FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats." + config_name + ".enchantments") != null)
						{
							for (String enchant : FileUtils.getLoadedAntiCheatsFile().getStringList("loaded-anticheats." + config_name + ".enchantments")) 
							{
								try
								{
									int level = Integer.valueOf(enchant.split(":")[1]);
									Enchantment enchantment = Enchantment.getByName(enchant.split(":")[0]);
									if (enchantment == null)
										RebugPlugin.getINSTANCE().Log(Level.WARNING, "Unknown enchantment in Loaded AntiCheats.yml (AntiCheat= " + config_name + ") (enchant ID= " + enchant.split(":")[0] + " level= " + level + ")");

									else 
									{
										item.addUnsafeEnchantment(enchantment, level);
										itemMeta.addEnchant(enchantment, level, true);
									}
								}
								catch (Exception e) {}
							}
						}
						else
							RebugPlugin.getINSTANCE().Log(Level.SEVERE, "loaded-anticheats." + config_name + ".enchantments Was NULL!");
					}
					if (FileUtils.getLoadedAntiCheatsFile()
							.get("loaded-anticheats." + config_name + ".enable_item_flag") != null
							&& FileUtils.getLoadedAntiCheatsFile()
									.getBoolean("loaded-anticheats." + config_name + ".enable_item_flag")) {
						for (String itemflag : FileUtils.getLoadedAntiCheatsFile()
								.getStringList("loaded-anticheats." + config_name + ".ItemFlags")) {
							ItemFlag flag = ItemFlag.valueOf(itemflag);
							if (flag == null)
								RebugPlugin.getINSTANCE().Log(Level.WARNING, "Unknown ItemFlag in Loaded AntiCheats.yml! (AntiCheat= " + config_name
												+ " ItemFlag= " + itemflag + ")");
							else
								itemMeta.addItemFlags(flag);
						}
					}
					
				//	if (FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats." + config_name + ".unbreakable") != null && PT.isServerOlderThan(ServerVersion.V_1_15))
					//	itemMeta.spigot().setUnbreakable(FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + config_name + ".unbreakable"));
					
					itemMeta.setDisplayName(ChatColor.WHITE + name);
					lore.add("");
					if (FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats." + config_name + ".enabled") == null)
						lore.add(ChatColor.AQUA + "Status: " + ChatColor.DARK_RED + "Disabled");
					else
						lore.add(ChatColor.AQUA + "Status: " + (FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + config_name + ".enabled") ? ChatColor.GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));
					
					lore.add("");
					if (FileUtils.getLoadedAntiCheatsFile().get("manually-added-anticheats." + config_name + ".version") == null)
						lore.add(ChatColor.AQUA + "Version: " + ChatColor.WHITE + "Unknown!");
					else
						lore.add(ChatColor.AQUA + "Version: " + ChatColor.WHITE + FileUtils.getLoadedAntiCheatsFile().getString("manually-added-anticheats." + config_name + ".version"));
					
					
					if (FileUtils.getLoadedAntiCheatsFile().get ("manually-added-anticheats." + config_name + ".authors") != null)
						lore.add(ChatColor.AQUA + "Author(s): " + ChatColor.WHITE + FileUtils.getLoadedAntiCheatsFile().getString ("manually-added-anticheats." + config_name + ".authors"));
					else
						lore.add(ChatColor.AQUA + "Author(s): " + ChatColor.WHITE + "Unknown!");
					
					if (FileUtils.getLoadedAntiCheatsFile().get ("manually-added-anticheats." + config_name + ".description") != null)
					{
						int start = 0;
						for (String s : FileUtils.getLoadedAntiCheatsFile().getStringList("manually-added-anticheats." + config_name + ".description"))
						{
							lore.add(start == 0 ? ChatColor.AQUA + "Description: " + ChatColor.WHITE + s : ChatColor.WHITE + s);
							start ++;
						}
					}
					else
						lore.add(ChatColor.AQUA + "Description: " + ChatColor.WHITE + "None");
					
					if (FileUtils.getLoadedAntiCheatsFile().get ("manually-added-anticheats." + config_name + ".soft_depend") != null && FileUtils.getLoadedAntiCheatsFile().getStringList ("manually-added-anticheats." + config_name + ".soft_depend").size () > 0)
					{
						int start = 0;
						if (FileUtils.getLoadedAntiCheatsFile().getStringList ("manually-added-anticheats." + config_name + ".soft_depend").size() == 1)
							lore.add(ChatColor.AQUA + "Soft Depend: " + ChatColor.WHITE + FileUtils.getLoadedAntiCheatsFile().getStringList ("manually-added-anticheats." + config_name + ".soft_depend").get(0));
						
						else
						{
							for (String s : FileUtils.getLoadedAntiCheatsFile().getStringList ("manually-added-anticheats." + config_name + ".soft_depend"))
							{
								lore.add(start == 0 ? ChatColor.AQUA + "Soft Depend: " + ChatColor.WHITE + s : ChatColor.WHITE + s);
								start ++;
							}
						}
					}
					if (FileUtils.getLoadedAntiCheatsFile().get ("manually-added-anticheats." + config_name + ".hard_depend") != null && FileUtils.getLoadedAntiCheatsFile().getStringList ("manually-added-anticheats." + config_name + ".hard_depend").size() > 0)
					{
						int start = 0;
						if (FileUtils.getLoadedAntiCheatsFile().getStringList ("manually-added-anticheats." + config_name + ".hard_depend").size() == 1)
							lore.add(ChatColor.AQUA + "Hard Depend: " + ChatColor.WHITE + FileUtils.getLoadedAntiCheatsFile().getStringList ("manually-added-anticheats." + config_name + ".hard_depend").get(0));
						else
						{
							for (String s : FileUtils.getLoadedAntiCheatsFile().getStringList ("manually-added-anticheats." + config_name + ".hard_depend"))
							{
								lore.add(start == 0 ? ChatColor.AQUA + "Hard Depend: " + ChatColor.WHITE + s : ChatColor.WHITE + s);
								start ++;
							}
						}
					}
					if ((FileUtils.getLoadedAntiCheatsFile().get ("manually-added-anticheats." + config_name + ".soft_depend") == null || FileUtils.getLoadedAntiCheatsFile().getStringList ("manually-added-anticheats." + config_name + ".soft_depend").size() < 1) && (FileUtils.getLoadedAntiCheatsFile().get ("manually-added-anticheats." + config_name + ".hard_depend") == null || FileUtils.getLoadedAntiCheatsFile().getStringList ("manually-added-anticheats." + config_name + ".hard_depend").size () < 1))
						lore.add(ChatColor.AQUA + "Dependencies: " + ChatColor.WHITE + "None");
					

					if (FileUtils.getLoadedAntiCheatsFile()
							.get("loaded-anticheats." + config_name + ".has_extra_lore") != null
							&& FileUtils.getLoadedAntiCheatsFile()
									.getBoolean("loaded-anticheats." + config_name + ".has_extra_lore")) {
						for (String lores : FileUtils.getLoadedAntiCheatsFile()
								.getStringList("loaded-anticheats." + config_name + ".lore"))
							lore.add(ChatColor.translateAlternateColorCodes('&', lores));
					}

					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
					int slot = 2;
					if (CustomSlots)
					{
						boolean hasSlotVersion = FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats." + config_name + ".menu-slot." + ServerVersionUtil.getServer_Version()) != null;
						slot = FileUtils.getLoadedAntiCheatsFile().getInt("loaded-anticheats." + config_name + ".menu-slot." + (hasSlotVersion ? ServerVersionUtil.getServer_Version() : "default"), adding ++);
					}
					else
						slot = adding ++;
						
					inventory.setItem(slot, item);
				}
			}
			if (!RebugPlugin.anticheats.isEmpty()) 
			{
				for (Map.Entry<Plugin, String> map : RebugPlugin.anticheats.entrySet()) 
				{
					Plugin AntiCheat = map.getKey();
					String author = "Unknown", name = map.getValue().toLowerCase(), description = AntiCheat.getDescription() != null && AntiCheat.getDescription().getDescription() != null
					? AntiCheat.getDescription().getDescription() : null, version = AntiCheat.getDescription() != null && AntiCheat.getDescription().getVersion() != null
					? AntiCheat.getDescription().getVersion() : "Unknown", path = null, path_data = null;
					boolean foundSV = FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats." + name + ".item." + ServerVersionUtil.getServer_Version()) != null;
					if (foundSV)
					{
						path = "loaded-anticheats." + name + ".item." + ServerVersionUtil.getServer_Version() + ".name";
						path_data = "loaded-anticheats." + name + ".item." + ServerVersionUtil.getServer_Version() + ".";
					}
					else
					{
						path = "loaded-anticheats." + name + ".item.default" + ".name";
						path_data = "loaded-anticheats." + name + ".item.default" + ".";
					}
					
					int author_size = 0;
					if (RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false))
						RebugPlugin.getINSTANCE().Log(Level.INFO, "Found AntiCheat: " + name + " " + version);

					if (AntiCheat.getDescription() != null && AntiCheat.getDescription().getAuthors() != null && AntiCheat.getDescription().getAuthors().size() > 0) 
					{
						author_size = AntiCheat.getDescription().getAuthors().size();
						if (author_size > 1) 
						{
							author = "[";
							for (int i = 0; i < AntiCheat.getDescription().getAuthors().size(); i++) 
							{
								author += AntiCheat.getDescription().getAuthors().get(i) + ", ";
							}
							author += "]";
							author = author.replace(", ]", "]").replace("[[", "[").replace("]]", "]").replace(",,", ",");
						}
						else
							author = AntiCheat.getDescription().getAuthors().get(0);
					}
					Material AntiCheatToItem = Material.getMaterial(FileUtils.getLoadedAntiCheatsFile().getString(path));
					if (AntiCheatToItem == null || AntiCheatToItem == Material.AIR)
					{
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "Failed to create AC (" + name + ") ItemType was AIR/Null");
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "Changing " + name + "'s AntiCheatToItem to SafeGuard (" + SafeGuard.name() + " : " + SafeGuard + ")");
						AntiCheatToItem = SafeGuard;
					}
					try
					{
						if (FileUtils.getLoadedAntiCheatsFile().getBoolean(path_data + "has-data", false))
						{
							int data = FileUtils.getLoadedAntiCheatsFile().getInt(path_data + "data", 0);
							item = Reset(AntiCheatToItem, 1, (short) 0, (byte) data);
						}
						else
							item = Reset(AntiCheatToItem);
					}
					catch (Exception ee) 
					{
						item = Reset(AntiCheatToItem);
					}

					if (FileUtils.getLoadedAntiCheatsFile()
							.getBoolean("loaded-anticheats." + name + ".enable_enchantment")) {
						for (String enchant : FileUtils.getLoadedAntiCheatsFile()
								.getStringList("loaded-anticheats." + name + ".enchantments"))
						{
							try
							{
								int level = Integer.valueOf(enchant.split(":")[1]);
								Enchantment enchantment = Enchantment.getByName(enchant.split(":")[0]);
								if (enchantment == null)
									RebugPlugin.getINSTANCE().Log(Level.WARNING, "Unknown enchantment in Loaded AntiCheats.yml (AntiCheat= " + name
											+ ") (enchant ID= " + enchant.split(":")[0] + " level= " + level + ")");

								else {
									item.addUnsafeEnchantment(enchantment, level);
									itemMeta.addEnchant(enchantment, level, true);
								}
							}
							catch (Exception e) {}
						}
					}
					if (FileUtils.getLoadedAntiCheatsFile()
							.get("loaded-anticheats." + name + ".enable_item_flag") != null
							&& FileUtils.getLoadedAntiCheatsFile()
									.getBoolean("loaded-anticheats." + name + ".enable_item_flag")) {
						for (String itemflag : FileUtils.getLoadedAntiCheatsFile()
								.getStringList("loaded-anticheats." + name + ".ItemFlags")) {
							ItemFlag flag = ItemFlag.valueOf(itemflag);
							if (flag == null)
								RebugPlugin.getINSTANCE().Log(Level.WARNING, "Unknown ItemFlag in Loaded AntiCheats.yml! (AntiCheat= " + name
										+ " ItemFlag= " + itemflag + ")");
							else
								itemMeta.addItemFlags(flag);
						}
					}
					//if (FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats." + name + ".unbreakable") != null && PT.isServerOlderThan(ServerVersion.V_1_15)) 
					//	itemMeta.spigot().setUnbreakable(FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + name + ".unbreakable"));

					itemMeta.setDisplayName(ChatColor.WHITE + AntiCheat.getName());
					lore.add("");
					lore.add(ChatColor.AQUA + "Status: "
							+ (AntiCheat.isEnabled() && FileUtils.getLoadedAntiCheatsFile()
									.getBoolean("loaded-anticheats." + name + ".enabled")
											? ChatColor.GREEN + "Enabled"
											: ChatColor.DARK_RED + "Disabled"));
					lore.add("");
					lore.add(ChatColor.AQUA + "Version: " + ChatColor.WHITE + version);

					if (author_size > 0)
						lore.add(ChatColor.AQUA + "Author" + (author_size > 1 ? "(s)" : "") + ": " + ChatColor.WHITE
								+ author);
					else
						lore.add(ChatColor.AQUA + "Author(s) " + ChatColor.WHITE + "Unknown!");

					if (RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false))
						RebugPlugin.getINSTANCE().Log(Level.INFO, "ac= " + name
								+ " description length= " + (description == null ? 0 : description.length()));

					if (description != null) {
						if (FileUtils.getLoadedAntiCheatsFile()
								.getBoolean("loaded-anticheats." + name + ".fix-description")) {
							int fix = QuickConfig.LoadDescriptionFix(name);
							if (fix > 0) {
								Iterable<String> result = Splitter.fixedLength(fix).split(description);
								String[] parts = Iterables.toArray(result, String.class);
								for (int i = 0; i < parts.length; i++)
									lore.add((i == 0 ? ChatColor.AQUA + "Description: " : "") + ChatColor.WHITE
											+ parts[i]);
							} else
								lore.add(ChatColor.AQUA + "Failed to fix Description!");
						} else
							lore.add(ChatColor.AQUA + "Description: " + ChatColor.WHITE + description);
					} else
						lore.add(ChatColor.AQUA + "Description: " + ChatColor.WHITE + "None");

					if (AntiCheat.getDescription() != null) {
						String soft_depend = "", hard_depend = "";
						int soft = AntiCheat.getDescription().getSoftDepend().size(),
								hard = AntiCheat.getDescription().getDepend().size();
						if (soft > 0) {
							if (soft > 1) {
								for (int i = 0; i < soft; i++) {
									for (int o = 0; o < ItemsAndMenusUtils.getAntiCheatDepends().length; o++) {
										if (AntiCheat.getDescription().getSoftDepend().get(i)
												.equalsIgnoreCase(ItemsAndMenusUtils.getAntiCheatDepends()[o]))
											soft_depend += AntiCheat.getDescription().getSoftDepend().get(i) + " ";
									}
								}
							}
							else
							{
								for (int o = 0; o < ItemsAndMenusUtils.getAntiCheatDepends().length; o++) 
								{
									if (AntiCheat.getDescription().getSoftDepend().get(0).equalsIgnoreCase(ItemsAndMenusUtils.getAntiCheatDepends()[o]))
										soft_depend += AntiCheat.getDescription().getSoftDepend().get(0);
								}
							}
							soft_depend = soft_depend.replace(", ]", "").replace("[[", "").replace("]]", "")
									.replace(",,", "").replace(",", "");
						}
						if (hard > 0) {
							if (hard > 1) 
							{
								for (int i = 0; i < hard; i++)
								{
									for (int o = 0; o < ItemsAndMenusUtils.getAntiCheatDepends().length; o++) 
									{
										if (AntiCheat.getDescription().getDepend().get(i)
												.equalsIgnoreCase(ItemsAndMenusUtils.getAntiCheatDepends()[o]))
											hard_depend += AntiCheat.getDescription().getDepend().get(i) + " ";
									}
								}
							} else {
								for (int o = 0; o < ItemsAndMenusUtils.getAntiCheatDepends().length; o++) {
									if (AntiCheat.getDescription().getDepend().get(0).equalsIgnoreCase(ItemsAndMenusUtils.getAntiCheatDepends()[o]))
										hard_depend += AntiCheat.getDescription().getDepend().get(0);
								}
							}
							hard_depend = hard_depend.replace(", ]", "").replace("[[", "").replace("]]", "")
									.replace(",,", "").replace(",", "");
						}
						if (!PT.isStringNull(hard_depend)) {
							String NewHD = "";
							String[] Split = PT.SplitString(hard_depend);
							if (Split.length > 1) {
								for (int i = 0; i < Split.length; i++) {
									NewHD += Split[i] + (i < Split.length - 1 ? ", " : "");
								}
							}

							lore.add(ChatColor.AQUA + "Hard Depend: " + ChatColor.WHITE
									+ (PT.isStringNull(NewHD) ? hard_depend : NewHD));
						}
						if (!PT.isStringNull(soft_depend)) {
							String NewSD = "";
							String[] Split = PT.SplitString(soft_depend);
							if (Split.length > 1) {
								for (int i = 0; i < Split.length; i++) {
									NewSD += Split[i] + (i < Split.length - 1 ? ", " : "");
								}
							}
							lore.add(ChatColor.AQUA + "Soft Depend: " + ChatColor.WHITE
									+ (PT.isStringNull(NewSD) ? soft_depend : NewSD));
						}
						if (PT.isStringNull(soft_depend) && PT.isStringNull(hard_depend))
							lore.add(ChatColor.AQUA + "Dependencies: " + ChatColor.WHITE + "None");
					}

					if (FileUtils.getLoadedAntiCheatsFile()
							.get("loaded-anticheats." + name + ".has_extra_lore") != null
							&& FileUtils.getLoadedAntiCheatsFile()
									.getBoolean("loaded-anticheats." + name + ".has_extra_lore")) {
						for (String lores : FileUtils.getLoadedAntiCheatsFile()
								.getStringList("loaded-anticheats." + name + ".lore"))
							lore.add(ChatColor.translateAlternateColorCodes('&', lores));
					}

					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
					int slot = 2;
					if (CustomSlots)
					{
						boolean hasSlotVersion = FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats." + name + ".menu-slot." + ServerVersionUtil.getServer_Version()) != null;
						slot = FileUtils.getLoadedAntiCheatsFile().getInt("loaded-anticheats." + name + ".menu-slot." + (hasSlotVersion ? ServerVersionUtil.getServer_Version() : "default"), adding ++);
					}
					else
						slot = adding ++;
						
					inventory.setItem(slot, item);
				}
			}
			if (RebugPlugin.manual_anticheats.isEmpty() && RebugPlugin.anticheats.isEmpty())
			{
				inventory = null;
				RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Failed to create AntiCheats Menu(Rebug.anticheatsloaded was Empty)!");
			}
			if (!RebugPlugin.manual_anticheats.isEmpty())
			{
				for (Map.Entry<String, Boolean> toMap : RebugPlugin.manual_anticheats.entrySet())
				{
					RebugPlugin.End_Game_AntiCheats.put(toMap.getKey(), toMap.getKey());
				}
			}
			if (!RebugPlugin.anticheats.isEmpty())
			{
				for (Map.Entry<Plugin, String> toMap : RebugPlugin.anticheats.entrySet())
				{
					RebugPlugin.End_Game_AntiCheats.put(toMap.getValue(), toMap.getValue());
				}
			}
			AntiCheatMenu = inventory;
		}
		return AntiCheatMenu;
	}
}
