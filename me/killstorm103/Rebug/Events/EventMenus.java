package me.killstorm103.Rebug.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import me.killstorm103.Rebug.Main.Rebug;


public class EventMenus implements Listener
{
	public void Command (String command)
	{
		Bukkit.dispatchCommand(Rebug.getGetMain().getServer().getConsoleSender(), "rebug " + command);
	}
	@EventHandler
	public void onMenu (InventoryClickEvent e)
	{
		ItemMeta meta = null;
		String MenuName = ChatColor.stripColor(e.getView().getTitle());
		if (MenuName != null && e.getView().getPlayer() != null)
		{
			if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null)
			{
				meta = e.getCurrentItem().getItemMeta();
				String ItemName = ChatColor.stripColor(meta.getDisplayName());
				Player player = (Player) e.getView().getPlayer();
				if (MenuName.equalsIgnoreCase("Exploits"))
				{
					if (ItemName != null)
					{
						switch (ItemName.toLowerCase()) 
						{
						case "fake death":
							Command("exploit fakedeath " + player.getName());
							break;
							
						case "demo":
							Command("exploit demo " + player.getName());
							break;

						default:
							break;
						}
						e.setCancelled(true);
					}
				}
				if (MenuName.equalsIgnoreCase("Crashers"))
				{
					if (ItemName != null)
					{
						switch (ItemName.toLowerCase()) 
						{
						case "gamestate":
							Command("crash GameState " + player.getName());
							break;
							
						default:
							break;
						}
						e.setCancelled(true);
					}
				}
			}
		}
	}
}
