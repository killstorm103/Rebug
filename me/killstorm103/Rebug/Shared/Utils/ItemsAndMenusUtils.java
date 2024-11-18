package me.killstorm103.Rebug.Shared.Utils;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.retrooper.packetevents.manager.server.ServerVersion;

import me.killstorm103.Rebug.Shared.Main.Config;
import me.killstorm103.Rebug.Shared.Main.Rebug;

public class ItemsAndMenusUtils
{
	public static final ItemsAndMenusUtils INSTANCE = new ItemsAndMenusUtils();

	public Inventory getRebugSettingsMenu() {
		if (getRebugSettingsMenu == null)
			Rebug.getINSTANCE().getNMS().SetUpMenu("rebug settings");

		return getRebugSettingsMenu;
	}
	public ItemStack DebugItem = null, DeleteItem = null, DeadGlassItem = null;
	@SuppressWarnings("deprecation")
	public ItemStack getDebugItem ()
	{
		if (DebugItem == null)
		{
			final Material Safe = Material.DIRT;
			boolean foundSV = Rebug.getINSTANCE().getLoadedItemsFile().get ("special-item.debug." + Rebug.getINSTANCE().getServerVersion()) != null, useID = Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("special-item.debug." + (foundSV ? Rebug.getINSTANCE().getServerVersion() : "default") + ".use-material-id");
			String path = foundSV ? "special-item.debug." + Rebug.getINSTANCE().getServerVersion() : "special-item.debug.default", grabobject = path + "." + (useID ? "id" : "name");
			if (!foundSV)
			{
				Rebug.getINSTANCE().Log(Level.WARNING, "in Items.yml");
				Rebug.getINSTANCE().Log(Level.WARNING, "special-item.debug." + Rebug.getINSTANCE().getServerVersion() + " wasn't found");
				Rebug.getINSTANCE().Log(Level.WARNING, "So using .default");
			}
			Material theItem = null;
			if (useID)
			{
				if (PT.isServerNewerOrEquals(ServerVersion.V_1_13))
					theItem = PT.getMaterialByID(Rebug.getINSTANCE().getLoadedItemsFile().getInt(grabobject));
				else
					theItem = Material.getMaterial(Rebug.getINSTANCE().getLoadedItemsFile().getInt(grabobject));
			}
			else
				theItem = Material.getMaterial(Rebug.getINSTANCE().getLoadedItemsFile().getString(grabobject));
			
			if (theItem == null || theItem == Material.AIR)
			{
				Rebug.getINSTANCE().Log(Level.SEVERE, "Failed to create special item Delete!");
				theItem = Safe;
			}
			ItemStack item = null;
			try
			{
				if (Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("special-item.debug." + (foundSV ? Rebug.getINSTANCE().getServerVersion() : "default") + ".has-data"))
					item = new ItemStack(theItem, 1, (short) 0, (byte) Rebug.getINSTANCE().getLoadedItemsFile().getInt("special-item.debug." + (foundSV ? Rebug.getINSTANCE().getServerVersion() : "default") + ".data"));
				else
					item = new ItemStack(theItem);
			}
			catch (Exception e) 
			{
				item = new ItemStack(theItem);
			}
			
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RED + "Delete Item!");
			item.setItemMeta(itemMeta);
			DebugItem = item.clone();
		}
		return DebugItem;
	}
	@SuppressWarnings("deprecation")
	public ItemStack getDeadGlassItem ()
	{
		if (DeadGlassItem == null)
		{
			final Material Safe = Material.DIRT;
			boolean foundSV = Rebug.getINSTANCE().getLoadedItemsFile().get ("special-item.deadglass." + Rebug.getINSTANCE().getServerVersion()) != null, useID = Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("special-item.deadglass." + (foundSV ? Rebug.getINSTANCE().getServerVersion() : "default") + ".use-material-id");
			String path = foundSV ? "special-item.deadglass." + Rebug.getINSTANCE().getServerVersion() : "special-item.deadglass.default", grabobject = path + "." + (useID ? "id" : "name");
			if (!foundSV)
			{
				Rebug.getINSTANCE().Log(Level.WARNING, "in Items.yml");
				Rebug.getINSTANCE().Log(Level.WARNING, "special-item.deadglass." + Rebug.getINSTANCE().getServerVersion() + " wasn't found");
				Rebug.getINSTANCE().Log(Level.WARNING, "So using .default");
			}
			Material theItem = null;
			if (useID)
			{
				if (PT.isServerNewerOrEquals(ServerVersion.V_1_13))
					theItem = PT.getMaterialByID(Rebug.getINSTANCE().getLoadedItemsFile().getInt(grabobject));
				else
					theItem = Material.getMaterial(Rebug.getINSTANCE().getLoadedItemsFile().getInt(grabobject));
			}
			else
				theItem = Material.getMaterial(Rebug.getINSTANCE().getLoadedItemsFile().getString(grabobject));
			
			if (theItem == null || theItem == Material.AIR)
			{
				Rebug.getINSTANCE().Log(Level.SEVERE, "Failed to create special item Delete!");
				theItem = Safe;
			}
			ItemStack item = null;
			try
			{
				if (Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("special-item.deadglass." + (foundSV ? Rebug.getINSTANCE().getServerVersion() : "default") + ".has-data"))
					item = new ItemStack(theItem, 1, (short) 0, (byte) Rebug.getINSTANCE().getLoadedItemsFile().getInt("special-item.deadglass." + (foundSV ? Rebug.getINSTANCE().getServerVersion() : "default") + ".data"));
				else
					item = new ItemStack(theItem);
			}
			catch (Exception e) 
			{
				item = new ItemStack(theItem);
			}
			
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(" ");
			item.setItemMeta(itemMeta);
			DeadGlassItem = item.clone();
		}
		return DeadGlassItem;
	}
	@SuppressWarnings("deprecation")
	public ItemStack getDeleteItem ()
	{
		if (DeleteItem == null)
		{
			final Material Safe = Material.DIRT;
			boolean foundSV = Rebug.getINSTANCE().getLoadedItemsFile().get ("special-item.delete." + Rebug.getINSTANCE().getServerVersion()) != null, useID = Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("special-item.delete." + (foundSV ? Rebug.getINSTANCE().getServerVersion() : "default") + ".use-material-id");
			String path = foundSV ? "special-item.delete." + Rebug.getINSTANCE().getServerVersion() : "special-item.delete.default", grabobject = path + "." + (useID ? "id" : "name");
			if (!foundSV)
			{
				Rebug.getINSTANCE().Log(Level.WARNING, "in Items.yml");
				Rebug.getINSTANCE().Log(Level.WARNING, "special-item.delete." + Rebug.getINSTANCE().getServerVersion() + " wasn't found");
				Rebug.getINSTANCE().Log(Level.WARNING, "So using .default");
			}
			Material theItem = null;
			if (useID)
			{
				if (PT.isServerNewerOrEquals(ServerVersion.V_1_13))
					theItem = PT.getMaterialByID(Rebug.getINSTANCE().getLoadedItemsFile().getInt(grabobject));
				else
					theItem = Material.getMaterial(Rebug.getINSTANCE().getLoadedItemsFile().getInt(grabobject));
			}
			else
				theItem = Material.getMaterial(Rebug.getINSTANCE().getLoadedItemsFile().getString(grabobject));
			
			if (theItem == null || theItem == Material.AIR)
			{
				Rebug.getINSTANCE().Log(Level.SEVERE, "Failed to create special item Delete!");
				theItem = Safe;
			}
			ItemStack item = null;
			try
			{
				if (Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("special-item.delete." + (foundSV ? Rebug.getINSTANCE().getServerVersion() : "default") + ".has-data"))
					item = new ItemStack(theItem, 1, (short) 0, (byte) Rebug.getINSTANCE().getLoadedItemsFile().getInt("special-item.delete." + (foundSV ? Rebug.getINSTANCE().getServerVersion() : "default") + ".data"));
				else
					item = new ItemStack(theItem);
			}
			catch (Exception e) 
			{
				item = new ItemStack(theItem);
			}
			
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RED + "Delete Item!");
			item.setItemMeta(itemMeta);
			DeleteItem = item.clone();
		}
		return DeleteItem;
	}

	public void UpdateMenuValueChangeLore(Inventory inventory, int slot, int loreslot, String ChangedTo) {
		if (inventory == null || PT.isStringNull(ChangedTo))
			return;

		ItemMeta NewMeta = inventory.getItem(slot).getItemMeta();
		List<String> NewLore = NewMeta.getLore();
		NewLore.set(loreslot, ChangedTo);
		NewMeta.setLore(NewLore);
		inventory.getItem(slot).setItemMeta(NewMeta);
	}

	public void UpdateItemInMenu(Inventory inventory, int itemslot, ItemStack NewItem) {
		if (inventory == null)
			return;

		inventory.setItem(itemslot, NewItem);
	}

	public void UpdateMenuLore(Inventory inventory, int itemslot, int loreslot, String text) {
		if (inventory == null || PT.isStringNull(text) || inventory.getItem(itemslot) == null
				|| !inventory.getItem(itemslot).hasItemMeta())
			return;

		ItemMeta NewMeta = inventory.getItem(itemslot).getItemMeta();
		List<String> NewLore = NewMeta.getLore();
		NewLore.set(loreslot, text);
		NewMeta.setLore(NewLore);
		inventory.getItem(itemslot).setItemMeta(NewMeta);
	}

	public Inventory getAntiCheats() {
		if (AntiCheatMenu == null)
			Rebug.getINSTANCE().getNMS().SetUpMenu("anticheats menu");

		return AntiCheatMenu;
	}

	public static Inventory ItemPickerMenu, AntiCheatMenu, getRebugSettingsMenu, DebugItemMenu;

	public Inventory getItemPickerMenu() {
		final ItemStack deadglass = Rebug.getINSTANCE().getNMS().getMadeItems("%dead_glass%");
		if (ItemPickerMenu != null) {
			if (Config.getItemMenuDeleteItem())
			{
				ItemPickerMenu.setItem(Config.getItemMenuSize() - 1,
						Rebug.getINSTANCE().getNMS().getMadeItems("%delete_item%"));
			} else
				ItemPickerMenu.setItem(Config.getItemMenuSize() - 1, deadglass);

			if (Config.getItemMenuDebugItem())
				ItemPickerMenu.setItem(Config.getItemMenuSize() - 2,
						Rebug.getINSTANCE().getNMS().getMadeItems("%debug_item%"));
			else
				ItemPickerMenu.setItem(Config.getItemMenuSize() - 2, deadglass);
		}
		if (ItemPickerMenu == null)
			Rebug.getINSTANCE().getNMS().SetUpMenu("items menu");

		return ItemPickerMenu;
	}
	public Inventory GetDebugItemMenu ()
	{
		if (DebugItemMenu == null)
			Rebug.getINSTANCE().getNMS().SetUpMenu("Rebug Debug Item Menu");
			
		
		return DebugItemMenu;
	}
}
