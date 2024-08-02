package me.killstorm103.Rebug.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import me.killstorm103.Rebug.Commands.Menu;
import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;

public class ItemsAndMenusUtils implements InventoryHolder
{
	public static final ItemsAndMenusUtils INSTANCE = new ItemsAndMenusUtils();
	public List<String> lore = new ArrayList<>();// clearing this as a test
	private ItemMeta itemMeta = null;
	private ItemStack item = null;
	public ItemStack getMadeItems(String menuName, String itemName)
	{
		switch (menuName.toLowerCase())
		{
		case "rebug settings":
			if (itemName.equalsIgnoreCase("Per Player Alerts"))
			{
				Rebug.PrivatePerPlayerAlerts =! Rebug.PrivatePerPlayerAlerts;
				item = Reset(Material.DROPPER);
				itemMeta.setDisplayName(ChatColor.ITALIC + (Rebug.PrivatePerPlayerAlerts ? ChatColor.GREEN : ChatColor.RED).toString() + "Per Player Alerts");
				item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("Debug"))
			{
				Rebug.debug =! Rebug.debug;
				item = Reset(Material.REDSTONE);
				itemMeta.setDisplayName(ChatColor.ITALIC + (Rebug.debug ? ChatColor.GREEN : ChatColor.RED).toString()  + "Debug");
				item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("Debug To Ops Only"))
			{
				Rebug.debugOpOnly =! Rebug.debugOpOnly;
				item = Reset(Material.PAPER);
				itemMeta.setDisplayName(ChatColor.ITALIC + (Rebug.debugOpOnly ? ChatColor.GREEN : ChatColor.RED).toString()+ "Debug To Ops Only");
				item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("Kick on reload config"))
			{
				Rebug.KickOnReloadConfig =! Rebug.KickOnReloadConfig;
				item = Reset(Material.PAPER);
				itemMeta.setDisplayName(ChatColor.ITALIC + (Rebug.KickOnReloadConfig ? ChatColor.GREEN : ChatColor.RED).toString() + "Kick on Reload Config");
				item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("Auto Clear scaffold after place"))
			{
				Rebug.AutoRefillBlocks =! Rebug.AutoRefillBlocks;
				item = Reset(Material.RECORD_12);
				itemMeta.setDisplayName(ChatColor.ITALIC.toString() + (Rebug.AutoRefillBlocks ? ChatColor.GREEN : ChatColor.DARK_RED) + "Auto Clear scaffold after place");
				item.setItemMeta(itemMeta);
				return item;
			}
			break;

		default:
			break;
		}
		return null;
	}
	
	public Inventory getRebugSettingsMenu ()
    {
    	if (getRebugSettingsMenu == null)
    	{
    		Inventory inventory = PT.createInventory(this, 18, ChatColor.ITALIC.toString() + ChatColor.BOLD + ChatColor.RED + "REBUG " + ChatColor.RESET + ChatColor.ITALIC + ChatColor.GRAY + "Settings");
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
			
			
			item = Reset(Material.DROPPER);
			itemMeta.setDisplayName(ChatColor.ITALIC + (Rebug.PrivatePerPlayerAlerts ? ChatColor.GREEN : ChatColor.RED).toString() + "Per Player Alerts");
			item.setItemMeta(itemMeta);
			inventory.setItem(5, item);
			
			
			item = Reset(Material.WOOL, 1, (short) 0, (byte) 14);
			itemMeta.setDisplayName(ChatColor.ITALIC + ChatColor.RED.toString() + "Reset Scaffold Area");
			item.setItemMeta(itemMeta);
			inventory.setItem(9, item);
			
			item = Reset(Material.RECORD_12);
			itemMeta.setDisplayName(ChatColor.ITALIC.toString() + (Rebug.AutoRefillBlocks ? ChatColor.GREEN : ChatColor.DARK_RED) + "Auto Clear scaffold after place");
			item.setItemMeta(itemMeta);
			inventory.setItem(10, item);
			
			getRebugSettingsMenu = inventory;
    	}
    	
    	return getRebugSettingsMenu;
    }
	
	public ItemStack getMadeItems (String ItemName)
    {
    	ItemStack order = null;
    	if (PT.isStringNull(ItemName)) return order;
    	
    	ArrayList<String> lore = new ArrayList<>();
    	lore.clear();
    	ItemMeta itemMeta = null;
    	
    	switch (ItemName.toLowerCase())
    	{
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
			
		case "%dead_glass%":
			order = Reset(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 7);
			itemMeta = order.getItemMeta();
			itemMeta.setDisplayName(" ");
			order.setItemMeta(itemMeta);
			break;

		default:
			break;
		}
    	if (order != null)
    		order.setItemMeta(itemMeta);
    	
    	return order;
    }
	public void UpdateMenuValueChangeLore(Inventory inventory, int slot, int loreslot, String ChangedTo)
	{
		if (inventory == null || PT.isStringNull(ChangedTo)) return;
		
		
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
		if (inventory == null || PT.isStringNull(text) || inventory.getItem(itemslot) == null || !inventory.getItem(itemslot).hasItemMeta()) return;
		
		
		ItemMeta NewMeta = inventory.getItem(itemslot).getItemMeta();
		List<String> NewLore = NewMeta.getLore();
		NewLore.set(loreslot, text);
		NewMeta.setLore(NewLore);
		inventory.getItem(itemslot).setItemMeta(NewMeta);
	}
	public ItemStack Reset (ItemStack item)
	{
		this.item = (ItemStack) (this.itemMeta =  null);
		lore.clear();
		this.item = item;
		itemMeta = this.item.getItemMeta();
			
		return this.item;
	}
	@SuppressWarnings({ "deprecation" })
	public Inventory getAntiCheats ()
	{
		if (AntiCheatMenu == null)
		{
			Inventory inventory = PT.createInventory(this, Rebug.getINSTANCE().getLoadedAntiCheatsFile().getInt("loaded-anticheats.Inventory-size"), ChatColor.RED + "AntiCheats");
			final int size = Config.getLoadedAntiCheats().size();
			item = Reset(Material.DIRT);
			itemMeta.setDisplayName(ChatColor.WHITE + "Vanilla");
			lore.add("");
			lore.add(ChatColor.AQUA + "Version: " + ChatColor.WHITE + Bukkit.getServer().getBukkitVersion());
			lore.add(ChatColor.AQUA + "Author: " + ChatColor.WHITE + "Mojang");
			lore.add("");
			lore.add(ChatColor.AQUA + "Info: " + ChatColor.WHITE + "You can adjust vanilla");
			lore.add(ChatColor.WHITE + "fly checks by using /settings");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(0, item);
			
			if (size < 1)
				Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "Add the AntiCheats to: loaded-anticheats in the config file!");
			
			else
			{
				Menu.TabAntiCheats.clear();
				Menu.TabAntiCheats.add("Vanilla");
				for (Plugin AC : Bukkit.getPluginManager().getPlugins())
				{
					for (int i = 0; i < size; i ++)
					{
						String name = ChatColor.translateAlternateColorCodes('&', Config.getLoadedAntiCheats().get(i));
						name = ChatColor.stripColor(name);
						if (name != null && AC != null && AC.getName().equalsIgnoreCase(name) && !Rebug.anticheats.containsKey(AC))
						{
							if (Rebug.debug)
								Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "Adding AntiCheat to list= " + AC.getName());
							
							Menu.TabAntiCheats.add(name);
							Rebug.anticheats.put(AC, name);
						}
					}
				}
			}
			if (!Rebug.anticheats.isEmpty())
			{
				int adding = 1;
				for (Map.Entry<Plugin, String> map : Rebug.anticheats.entrySet())
				{
					Plugin AntiCheat = map.getKey();
					String author = "Unknown", name = map.getValue().toLowerCase(), description = AntiCheat.getDescription() != null && AntiCheat.getDescription().getDescription() != null ? AntiCheat.getDescription().getDescription() : null,  version = AntiCheat.getDescription() != null && AntiCheat.getDescription().getVersion() != null ? AntiCheat.getDescription().getVersion() : "Unknown";
					int author_size = 0;
					if (Rebug.debug)
						Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "Found AntiCheat: " + name + " " + version);
					
					if (AntiCheat.getDescription() != null && AntiCheat.getDescription().getAuthors() != null && AntiCheat.getDescription().getAuthors().size() > 0)
					{
						author_size = AntiCheat.getDescription().getAuthors().size();
						if (author_size > 1)
						{
							author = "[";
							for (int i = 0; i < AntiCheat.getDescription().getAuthors().size(); i ++)
							{
								author += AntiCheat.getDescription().getAuthors().get(i) + ", ";
							}
							author += "]";
							author = author.replace(", ]", "]").replace("[[", "[").replace("]]", "]").replace(",,", ",");
						}
						else
							author = AntiCheat.getDescription().getAuthors().get(0);
					}
					if (Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + name + ".has-data"))
						item = Reset(Material.getMaterial(Config.getAntiCheatItemID(name)), 1, (short) 0, (byte) Config.getItemData(name));
					else
						item = Reset(Material.getMaterial(Config.getAntiCheatItemID(name)));
					
					if (Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + name + ".enable_enchantment"))
					{
						for (String enchant : Rebug.getINSTANCE().getLoadedAntiCheatsFile().getStringList("loaded-anticheats." + name + ".enchantments"))
						{
							int ID = Integer.valueOf(enchant.split(":")[0]), level = Integer.valueOf(enchant.split(":")[1]);
							Enchantment enchantment = Enchantment.getById(ID);
							if (enchantment == null)
								Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "Unknown enchantment in Loaded AntiCheats.yml (AntiCheat= " + name + ") (enchant ID= " + ID + " level= " + level + ")");
							
							else
							{
								item.addUnsafeEnchantment(enchantment, level);
								itemMeta.addEnchant(enchantment, level, true);
							}
						}
					}
					if (Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + name + ".enable_item_flag"))
					{
						for (String itemflag : Rebug.getINSTANCE().getLoadedAntiCheatsFile().getStringList("loaded-anticheats." + name + ".ItemFlags")) 
						{
							ItemFlag flag = ItemFlag.valueOf(itemflag);
							if (flag == null)
								Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "Unknown ItemFlag in Loaded AntiCheats.yml! (AntiCheat= " + name + " ItemFlag= " + itemflag + ")");
							else
								itemMeta.addItemFlags(flag);
						}
					}
					
					itemMeta.spigot().setUnbreakable(Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + name + ".unbreakable"));
					
					itemMeta.setDisplayName(ChatColor.WHITE + AntiCheat.getName());
					lore.add("");
					lore.add(ChatColor.AQUA + "Status: " + (AntiCheat.isEnabled() ? ChatColor.GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));
					lore.add("");
					lore.add(ChatColor.AQUA + "Version: "+ ChatColor.WHITE + version);
					if (author_size > 0)
						lore.add(ChatColor.AQUA + "Author" + (author_size > 1 ? "(s)" : "") + ": "+ ChatColor.WHITE + author);
					else
						lore.add(ChatColor.AQUA + "Author(s) " + ChatColor.WHITE + "Unknown!");
					
					if (Rebug.debug)
						Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "ac= " + name + " description length= " + (description == null ? 0 : description.length()));

					if (description != null)
					{
						if (Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + name + ".fix-description"))
						{
							int fix = Config.LoadDescriptionFix(name);
							if (fix > 0)
							{
								Iterable<String> result = Splitter.fixedLength(fix).split(description);
								String[] parts = Iterables.toArray(result, String.class);
								for (int i = 0; i < parts.length; i ++)
									lore.add((i == 0 ? ChatColor.AQUA + "Description: " : "") + ChatColor.WHITE + parts[i]);
							}
							else
								lore.add(ChatColor.AQUA + "Failed to fix Description!");
						}
						else
							lore.add(ChatColor.AQUA + "Description: "+ ChatColor.WHITE + description);
					}
					else
						lore.add(ChatColor.AQUA + "Description: "+ ChatColor.WHITE + "None");
					
					if (Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + name + ".has_extra_lore"))
					{
						for (String lores : Rebug.getINSTANCE().getLoadedAntiCheatsFile().getStringList("loaded-anticheats." + name + ".lore"))
							lore.add(ChatColor.translateAlternateColorCodes('&', lores));
					}
					
					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
					inventory.setItem(adding++, item);
				}
			}
			else
			{
				inventory = null;
				Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "Failed to create AntiCheats Menu(Rebug.anticheatsloaded was Empty)!");
			}
				
			AntiCheatMenu = inventory;
		}
		
		return AntiCheatMenu;
	}
	public Inventory ItemPickerMenu, AntiCheatMenu, getRebugSettingsMenu;
	@SuppressWarnings("deprecation")
	public Inventory getItemPickerMenu ()
	{
		final ItemStack deadglass = getMadeItems("%dead_glass%");
		if (ItemPickerMenu != null)
		{
			if (Config.getItemMenuDeleteItem())
			{
				item = getMadeItems("%delete_item%");
				ItemPickerMenu.setItem(Config.getItemMenuSize()-1, item);
			}
			else
				ItemPickerMenu.setItem(Config.getItemMenuSize() - 1, deadglass);
			
			if (Config.getItemMenuDebugItem())
			{
				item = getMadeItems("%debug_item%");
				ItemPickerMenu.setItem(Config.getItemMenuSize()-2, item);
			}
			else
				ItemPickerMenu.setItem(Config.getItemMenuSize() - 2, deadglass);
		}
		if (ItemPickerMenu == null)
		{
			Inventory inventory = PT.createInventory(this, Config.getItemMenuSize(), ChatColor.GREEN + "Items");
			Rebug.getINSTANCE().getLoadedItemsFile().getConfigurationSection("items").getKeys(false).forEach(key -> 
			{
				Material material = Material.getMaterial(key);
				material = material == null || material == Material.AIR ? Material.getMaterial(PT.SubString(key, 0, key.length() - Rebug.getINSTANCE().getLoadedItemsFile().getInt("items." + key + ".numbered"))) : material;
				
				boolean hasData = Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("items." + key + ".hasData");
				if (hasData)
					item = Reset(material, Rebug.getINSTANCE().getLoadedItemsFile().getInt("items." + key + ".amount"), (short) 0, (byte) Rebug.getINSTANCE().getLoadedItemsFile().getInt("items." + key + ".data"));
				else
					item = Reset(material, Rebug.getINSTANCE().getLoadedItemsFile().getInt("items." + key + ".amount"));
			
				if (Rebug.getINSTANCE().getLoadedItemsFile().getString("items." + key + ".display-name") != null)
					itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Rebug.getINSTANCE().getLoadedItemsFile().getString("items." + key + ".display-name")));

				boolean hasLore = Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("items." + key + ".hasLore"), isUnbreakable = Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("items." + key + ".unbreakable"), isEnchanted = Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("items." + key + ".enable_enchantment"), hasItemFlag = Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("items." + key + ".enable_item_flag");
				if (isEnchanted)
				{
					for (String enchant : Rebug.getINSTANCE().getLoadedItemsFile().getStringList("items." + key + ".enchantments"))
					{
						int ID = Integer.valueOf(enchant.split(":")[0]), level = Integer.valueOf(enchant.split(":")[1]);
						Enchantment enchantment = Enchantment.getById(ID);
						if (enchantment == null)
							Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "Unknown enchantment in items.yml (key= " + key + ") (enchant ID= " + ID + " level= " + level + ")");
						
						else
						{
							item.addUnsafeEnchantment(enchantment, level);
							itemMeta.addEnchant(enchantment, level, true);
						}
					}
				}
				itemMeta.spigot().setUnbreakable(isUnbreakable);
				
				if (hasItemFlag) 
				{
					for (String itemflag : Rebug.getINSTANCE().getLoadedItemsFile().getStringList("items." + key + ".ItemFlag")) 
					{
						ItemFlag flag = ItemFlag.valueOf(itemflag);
						if (flag == null)
							Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "Unknown ItemFlag! (key= " + key + " ItemFlag= " + itemflag + ")");
						else
							itemMeta.addItemFlags(flag);
					}
				}
				if (hasLore)
				{
					lore.clear();
					for (String lores : Rebug.getINSTANCE().getLoadedItemsFile().getStringList("items." + key + ".Lore"))
					{
						lore.add(ChatColor.translateAlternateColorCodes('&', lores));
					}
					itemMeta.setLore(lore);
				}
					
				item.setItemMeta(itemMeta);
				inventory.setItem(Rebug.getINSTANCE().getLoadedItemsFile().getInt("items." + key + ".slot"), item);
			});

			if (Config.getItemMenuDeleteItem())
			{
				item = getMadeItems("%delete_item%");
				inventory.setItem(Config.getItemMenuSize()-1, item);
			}
			if (Config.getItemMenuDebugItem())
			{
				item = getMadeItems("%debug_item%");
				inventory.setItem(Config.getItemMenuSize()-2, item);
			}
			
			for (int i = 0; i < inventory.getSize(); i ++)
			{
				if (inventory.getItem(i) == null)
					inventory.setItem(i, deadglass);
			}
			
			ItemPickerMenu = inventory;
		}
		
		return ItemPickerMenu;
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
	@SuppressWarnings({ "deprecation" })
	private ItemStack Reset (Material material, Integer amount, Short damage, Byte data)
	{
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = new ItemStack(material, amount, damage, data);
		itemMeta = this.item.getItemMeta();
			
		return this.item;
	}
	// don't ever get this
	@Override
	public Inventory getInventory() {
		return null;
	}
}
