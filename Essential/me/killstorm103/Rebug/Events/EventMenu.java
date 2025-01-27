package me.killstorm103.Rebug.Events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.killstorm103.Rebug.Main.RebugPlugin;
import me.killstorm103.Rebug.Utils.FileUtils;
import me.killstorm103.Rebug.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

@SuppressWarnings("deprecation")
public class EventMenu implements Listener
{
	private boolean ShouldCancelAction (String MenuName, String Type)
	{
		if (PT.isStringNull(MenuName) || PT.isStringNull(Type)) return false;
		
		if ((Type.equals("Drag") || Type.equals("Click")) && (MenuName.equalsIgnoreCase("Player Settings") || MenuName.equalsIgnoreCase("Rebug Settings") || MenuName.equalsIgnoreCase("anticheats")))
			return true;
		
		
		return false;
	}
	private String ListedMenuNames (User user, Inventory inventory)
	{
		if (user == null || inventory == null) return null;
		
		try
		{
			String s = inventory.equals(user.getSettingsMenu()) ? "Player Settings" : inventory.equals(ItemsAndMenusUtils.getINSTANCE().getAntiCheats()) ? "AntiCheats" : inventory.equals(ItemsAndMenusUtils.getINSTANCE().getRebugSettings()) ? "Rebug Settings" : null;
			return s;
		}
		catch (Exception e) {}
		
		return null;
	}
	
	@EventHandler
	public void onDrag (InventoryDragEvent e)
	{
		boolean isInterface = e.getView().getClass().isArray();
		String MenuName = isInterface ? ChatColor.stripColor(e.getView().getTitle()) : null;
		ItemStack item = e.getCursor();
		if (item == null || item.getType() == Material.AIR) return;
		
		ItemMeta meta = null;
		meta = item != null && item.hasItemMeta() ? item.getItemMeta() : null;
		String ItemName = meta != null && meta.hasDisplayName() ? ChatColor.stripColor(meta.getDisplayName()) : null;
		if (PT.isStringNull(ItemName)) return;
		
		User user = RebugPlugin.getUser((Player) e.getWhoClicked());
		if (user == null) return;
		
		if (PT.isStringNull(MenuName))
			MenuName = ListedMenuNames(user, e.getInventory());

		if (PT.isStringNull(MenuName) || !ShouldCancelAction(MenuName, "Drag")) return;
		
		e.setCancelled(true);
	}
	@SuppressWarnings("null")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMenu (InventoryClickEvent e) 
	{
		boolean isInterface = e.getView().getClass().isArray();
		String MenuName = isInterface ? ChatColor.stripColor(e.getView().getTitle()) : null;
		ItemStack item = e.getCurrentItem();
		if (item == null || item.getType() == Material.AIR) return;
		
		ItemMeta meta = item.getItemMeta();
		if (meta == null || !meta.hasDisplayName()) return;
		
		String ItemName = ChatColor.stripColor(meta.getDisplayName());
		if (PT.isStringNull(ItemName)) return;
		
		User user = RebugPlugin.getUser((Player) e.getWhoClicked());
		if (user == null) return;
		
		if (PT.isStringNull(MenuName))
			MenuName = ListedMenuNames(user, e.getClickedInventory());

		if (PT.isStringNull(MenuName) || !ShouldCancelAction(MenuName, "Click")) return;
		
		
		ClickType clickType = e.getClick();
		int slot = e.getSlot();
		try
		{
			if (!isInterface) 
			{
				if (e.getClickedInventory() != ItemsAndMenusUtils.getTopInventory(e)) return;
				
				e.setCancelled(true);
			}
			else
				e.setCancelled(true);
			
			if (MenuName.equalsIgnoreCase("AntiCheats"))
			{
				if (item == null || !item.hasItemMeta() || item.getItemMeta().getLore() == null
						|| item.getItemMeta().getLore().size() <= 1 || ItemName.equalsIgnoreCase("info"))
					return;
				
				PT.UpdateAntiCheat(user, new String[] {ItemName}, item, null, false);
			}
			
			if (MenuName.equalsIgnoreCase("Rebug Settings"))
			{
				e.setCancelled(true);
				if (item != null && ItemName != null
						&& !ItemName.equalsIgnoreCase("Add more Settings as i think of them!")) 
				{
					if (!RebugPlugin.getSettingsList().containsKey(ItemName)) return;
					
					switch (RebugPlugin.getSettingsList().get(ItemName).toLowerCase())
					{
					case "boolean":
						RebugPlugin.getSettingsBooleans().put(ItemName, !RebugPlugin.getSettingsBooleans().get(ItemName));
						ItemsAndMenusUtils.getINSTANCE().UpdateMenuValueChangeLore(ItemsAndMenusUtils.getINSTANCE().getRebugSettings(), slot, 0, ItemsAndMenusUtils.getINSTANCE().getValues(RebugPlugin.getSettingsBooleans().get(ItemName)));
						break;
						
					case "action":
						switch (ItemName.toLowerCase())
						{
						case "op/deop":
							user.getPlayer().setOp(!user.getPlayer().isOp());
							user.sendMessage("you are " + (user.getPlayer().isOp() ? "now " + ChatColor.GREEN + "Opped" : "no longer " + ChatColor.DARK_RED + "Op"));
							break;
							
						case "reload config":
							RebugPlugin.getINSTANCE().Reload_Configs(user.getPlayer());
							break;
							
						case "back":
							user.getPlayer().closeInventory();
							user.getPlayer().openInventory(user.getSettingsMenu());
							break;

						default:
							break;
						}
						break;
						
					case "double":
						if (clickType != ClickType.RIGHT && clickType != ClickType.LEFT) break;
						
						double value = RebugPlugin.getSettingsDoubles().get(ItemName), 
						max = FileUtils.getMenusConfig().getConfigurationSection(MenuName).getDouble(ItemName + ".max-value", 5), min = FileUtils.getMenusConfig().getConfigurationSection(MenuName).getDouble(ItemName + ".min-value", 1),
						change_by_value = FileUtils.getMenusConfig().getConfigurationSection(MenuName).getDouble(ItemName + ".change-value", .5);
						value = clickType == ClickType.RIGHT ? value - change_by_value : value + change_by_value;
						value = value > max ? min : value < min ? max : value;
						RebugPlugin.getSettingsDoubles().put(ItemName, value);
						ItemsAndMenusUtils.getINSTANCE().UpdateMenuValueChangeLore(ItemsAndMenusUtils.getINSTANCE().getRebugSettings(), slot, 0, ChatColor.AQUA + "Value" + ChatColor.WHITE + ": " + RebugPlugin.getSettingsDoubles().get(ItemName));
						break;
						
					case "integer":
						if (clickType != ClickType.RIGHT && clickType != ClickType.LEFT) break;
						
						int value_int = RebugPlugin.getSettingsIntegers().get(ItemName), max_int = FileUtils.getMenusConfig().getConfigurationSection(MenuName).getInt(ItemName + ".max-value", 5), min_int = FileUtils.getMenusConfig().getConfigurationSection(MenuName).getInt(ItemName + ".min-value", 1),
					    change_by_value_int = FileUtils.getMenusConfig().getConfigurationSection(MenuName).getInt(ItemName + ".change-value", 1);
						value_int = clickType == ClickType.RIGHT ? value_int - change_by_value_int : value_int + change_by_value_int;
						value_int = value_int > max_int ? min_int : value_int < min_int ? max_int : value_int;
						RebugPlugin.getSettingsIntegers().put(ItemName, value_int);
						ItemsAndMenusUtils.getINSTANCE().UpdateMenuValueChangeLore(ItemsAndMenusUtils.getINSTANCE().getRebugSettings(), slot, 0, ChatColor.AQUA + "Value" + ChatColor.WHITE + ": " + RebugPlugin.getSettingsIntegers().get(ItemName));
						break;

					default:
						break;
					}
				}
			}
			if (MenuName.equalsIgnoreCase("Player Settings")) 
			{
				e.setCancelled(true);
				if (ItemName != null && !ItemName.equalsIgnoreCase("Add more Settings as i think of them!")) 
				{
					if (ItemName.equalsIgnoreCase("Rebug Settings"))
					{
						if (PT.hasAdminPerms(user.getPlayer())) 
						{
							user.getPlayer().closeInventory();
							user.getPlayer().openInventory(ItemsAndMenusUtils.getINSTANCE().getRebugSettings());
						} 
						else
							user.sendMessage("You don't have Permission to access that!");
						
						return;
					}
					if (!user.getSettingsList().containsKey(ItemName) || FileUtils.getPlayerSettingsConfig().getString(ItemName + ".permission") != null && !PT.isStringNull(FileUtils.getPlayerSettingsConfig().getString(ItemName + ".permission")) && !PT.hasPermission (user, FileUtils.getPlayerSettingsConfig().getString(ItemName + ".permission"))) 
						return;
					
					switch (user.getSettingsList().get(ItemName).toLowerCase())
					{
					case "boolean":
						user.getSettingsBooleans().put(ItemName, !user.getSettingsBooleans().get(ItemName));
						ItemsAndMenusUtils.getINSTANCE().UpdateMenuValueChangeLore(user.getSettingsMenu(), slot, 0, ItemsAndMenusUtils.getINSTANCE().getValues(user.getSettingsBooleans().get(ItemName)));
						switch (ItemName.toLowerCase())
						{
						case "damage resistance":
							if (user.getSettingsBooleans().get(ItemName))
								user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 3, true, false));
							else
								user.getPlayer().removePotionEffect(PotionEffectType.RESISTANCE);// DAMAGE_RESISTANCE
							break;
							
						case "fire resistance":
							if (user.getSettingsBooleans().get(ItemName))
								user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
							else
								user.getPlayer().removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
							break;
							
						case "night vision":
							if (user.getSettingsBooleans().get(ItemName))
								user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
							else
								user.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
							break;

						default:
							break;
						}
						break;
						
					case "double":
						if (clickType != ClickType.RIGHT && clickType != ClickType.LEFT) break;
						
						double value = user.getSettingsDoubles().get(ItemName), max = FileUtils.getPlayerSettingsConfig().getDouble(ItemName + ".max-value", 5), min = FileUtils.getPlayerSettingsConfig().getDouble(ItemName + ".min-value", 1),
						change_by_value = FileUtils.getPlayerSettingsConfig().getDouble(ItemName + ".change-value", .5);
						value = clickType == ClickType.RIGHT ? value - change_by_value : value + change_by_value;
						value = value > max ? min : value < min ? max : value;
						user.getSettingsDoubles().put(ItemName, value);
						ItemsAndMenusUtils.getINSTANCE().UpdateMenuValueChangeLore(user.getSettingsMenu(), slot, 0, ChatColor.AQUA + "Value" + ChatColor.WHITE + ": " + user.getSettingsDoubles().get(ItemName));
						break;
						
					case "integer":
						if (clickType != ClickType.RIGHT && clickType != ClickType.LEFT) break;
						
						int value_int = user.getSettingsIntegers().get(ItemName),
						max_int = FileUtils.getPlayerSettingsConfig().getInt(ItemName + ".max-value", 5), min_int = FileUtils.getPlayerSettingsConfig().getInt(ItemName + ".min-value", 1),
						change_value_int = FileUtils.getPlayerSettingsConfig().getInt(ItemName + ".change-value", 1);
						value_int = clickType == ClickType.RIGHT ? value_int - change_value_int : value_int + change_value_int;
						value_int = value_int > max_int ? min_int : value_int < min_int ? max_int : value_int;
						user.getSettingsIntegers().put(ItemName, value_int);
						ItemsAndMenusUtils.getINSTANCE().UpdateMenuValueChangeLore(user.getSettingsMenu(), slot, 0, ChatColor.AQUA + "Value" + ChatColor.WHITE + ": " + user.getSettingsIntegers().get(ItemName));	
						break;

					default:
						break;
					}
				}
			}
		}
		catch (Exception pp) 
		{
			if (RebugPlugin.getSettingsList().isEmpty()) ItemsAndMenusUtils.getINSTANCE().getRebugSettings();
			
			if (RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false))
				pp.printStackTrace();
		}
	}
}
