package me.killstorm103.Rebug.Utils;

import java.util.List;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;

public class ItemsAndMenusUtils {
	public static final ItemsAndMenusUtils INSTANCE = new ItemsAndMenusUtils();

	public Inventory getRebugSettingsMenu() {
		if (getRebugSettingsMenu == null)
			Rebug.getINSTANCE().getNMS().SetUpMenu("rebug settings");

		return getRebugSettingsMenu;
	}

	public void UpdateMenuValueChangeLore(Inventory inventory, int slot, int loreslot, String ChangedTo) {
		if (inventory == null || PTNormal.isStringNull(ChangedTo))
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
		if (inventory == null || PTNormal.isStringNull(text) || inventory.getItem(itemslot) == null
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

	public static Inventory ItemPickerMenu, AntiCheatMenu, getRebugSettingsMenu;

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
}
