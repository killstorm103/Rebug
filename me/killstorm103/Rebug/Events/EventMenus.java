package me.killstorm103.Rebug.Events;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Tasks.ResetScaffoldTestArea;
import me.killstorm103.Rebug.Utils.CrashersAndOtherExploits;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;


public class EventMenus implements Listener
{
	public void Command (Player sender, String command)
	{
		Bukkit.dispatchCommand(sender, "rebug " + command);
	}
	private void Refresh (Player player, String menu)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Rebug.GetMain(), new Runnable()
        {
            @Override
            public void run()
            {
            	Command(player, menu);
            }
        }, 0L);
	}
	@EventHandler
	public void onInventoryClose (InventoryCloseEvent e)
	{
		User user = Rebug.getUser((Player) e.getPlayer());
		if (user == null || user.CommandTarget == null) return;
		
		if (user.InvSeed)
		{
			for (Map.Entry<ItemStack, Integer> map : user.OldItems.entrySet())
			{
				if (map == null || map.getKey() == null) return;
				
				if (map.getKey().getType() == Material.AIR)
					user.getPlayer().getInventory().getItem(map.getValue()).setType(Material.AIR);
				else
					user.getPlayer().getInventory().setItem(map.getValue(), map.getKey());
			}
			user.getPlayer().updateInventory();
			user.OldItems.clear();
			user.InvSeed = false;
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMenu (InventoryClickEvent e)
	{
		ItemMeta meta = null;
		String MenuName = ChatColor.stripColor(e.getView().getTitle());
		ItemStack item = e.getCurrentItem(), getCursor = e.getCursor();
		Player player = (Player) e.getView().getPlayer();
		int slot = e.getSlot();
		ClickType clickType = e.getClick();
		if (Rebug.debug)
		{
			Rebug.Debug(player, "slot= " + slot + " item= " + item + " ItemName= " +  ChatColor.stripColor(item.getItemMeta().getDisplayName()) + " Menu= " + MenuName);
			Rebug.Debug(player, "ClickType= " + e.getClick().name());
		}
		if ((e.getInventory().getType() == InventoryType.ENCHANTING || e.getInventory().getType() == InventoryType.ANVIL) && !player.isOp() && !player.hasPermission("me.killstorm103.rebug.server_owner") && !player.hasPermission("me.killstorm103.rebug.server_admin"))
		{
			e.setCancelled(true);
			player.sendMessage(Rebug.RebugMessage + "You can't do that you don't have perms!");
			return;
		}
		if (MenuName != null)
		{
			if (e.getClickedInventory() != e.getWhoClicked().getInventory() && item != null && MenuName.equalsIgnoreCase("Items") && item.getType() == Material.WOOL && getCursor != null)
			{
				if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
				{
					if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("delete item!") && getCursor.getType() != Material.AIR)
					{
						e.setCursor(null);
						e.setCancelled(true);
						return;
					}
					if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("Debug item!") && getCursor.getType() != Material.AIR)
					{
						player.sendMessage(Rebug.RebugMessage + "Debug= " + getCursor + " ID= " + getCursor.getType().getId());
						if (getCursor.getData() != null)
							player.sendMessage(Rebug.RebugMessage + "Data= " + getCursor.getData().getData());
						
						e.setCancelled(true);
						return;
					}
				}
			}
			if (e.getClickedInventory() != e.getWhoClicked().getInventory() && item != null && (MenuName.equalsIgnoreCase("Items") || MenuName.equalsIgnoreCase("Crashers") || MenuName.equalsIgnoreCase("Exploits")) && item.getType() == Material.STAINED_GLASS_PANE)
			{
				e.setCancelled(true);
				return;
			}
		}
		if (MenuName != null)
		{
			User user = Rebug.getUser((Player) e.getView().getPlayer()), BackUser;
			if (user == null)
			{
				PT.KickPlayer((Player) e.getView().getPlayer(), PT.RebugsUserWasNullErrorMessage("in " + MenuName));
				return;
			}
			BackUser = user;
			if (item != null && item.getItemMeta() != null)
			{
				meta = item.getItemMeta();
				String ItemName = meta.hasDisplayName() ? ChatColor.stripColor(meta.getDisplayName()) : null;
				if (MenuName.equalsIgnoreCase("Inventory") && user.InvSeed && e.getClickedInventory() == user.getPlayer().getInventory())
				{
					if (e.getClickedInventory() != user.CommandTarget.getInventory())
					{
						if (ItemName.equalsIgnoreCase("Max Item"))
						{
							e.setCancelled(true);
							e.getCursor().setAmount(64);
							e.setCursor(e.getCursor());
						}
						if (ItemName.equalsIgnoreCase("Delete Item"))
						{
							e.setCancelled(true);
							e.setCursor(null);
						}
					}
				}
				if (MenuName.equalsIgnoreCase("Potions") && e.getClickedInventory() != user.getPlayer().getInventory())
				{
					e.setCancelled(true);
					Bukkit.getScheduler().runTask(Rebug.GetMain(), new Runnable()
					{
						@Override
						public void run()
						{
							if (item.getType() == Material.MILK_BUCKET)
							{
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "effect " + user.getPlayer().getName() + " clear");
								return;
							}
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "effect " + user.getPlayer().getName() + " " + ItemName.toLowerCase() + " " + user.potion_effect_seconds + " " + user.potionlevel);		
						}
					});
				}
				if (MenuName.equalsIgnoreCase("AntiCheats") && e.getClickedInventory() != user.getPlayer().getInventory())
				{
					e.setCancelled(true);
					if (item == null || !item.hasItemMeta() || item.getItemMeta().getLore() == null || item.getItemMeta().getLore().size() <= 1)
						return;
					
					if (ItemName.equalsIgnoreCase("Vanilla"))
					{
						if (clickType == ClickType.RIGHT)
						{
							Bukkit.dispatchCommand(user.getPlayer(), "rebug menu Vanilla%Fly%Checks");
							return;
						}
						if (!Rebug.hasAdminPerms(user.getPlayer()) && !user.hasPermission("me.killstorm103.rebug.user.select_vanilla"))
						{
							user.sendMessage(Rebug.RebugMessage + "You don't have permission to use Vanilla!");
							if (user.AutoCloseAntiCheatMenu)
								user.getPlayer().closeInventory();
							
							return;
						}
					}
				
					Rebug.UpdateUserPerms (user, ItemName);
					if (user.AutoCloseAntiCheatMenu)
						user.getPlayer().closeInventory();
					
					
					Bukkit.getScheduler().runTaskLater(Rebug.GetMain(), new Runnable()
					{
						
						@Override
						public void run()
						{
							user.AntiCheat = ItemName;
							//ShowFlags (user);
							String NewAC = user.getColoredAntiCheat(), striped = ChatColor.stripColor(user.AntiCheat).toLowerCase();
					   		if (Rebug.GetMain().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + striped + ".has-short-name"))
							{
					   			NewAC = NewAC.replace(NewAC, ChatColor.translateAlternateColorCodes('&', Rebug.GetMain().getLoadedAntiCheatsFile().getString("loaded-anticheats." + striped + ".short-name")) + ChatColor.RESET);
							}
					   		Rebug.getINSTANCE().UpdateScoreBoard(user, 9, ChatColor.DARK_RED + "AC " + NewAC);
							PT.PlaySound(user.getPlayer(), Sound.ANVIL_USE, 1, 1);
							String AC = ChatColor.stripColor(NewAC);
							user.getPlayer().sendMessage(Rebug.RebugMessage + "You selected: " + AC + (AC.equalsIgnoreCase("vanilla") ||
							AC.equalsIgnoreCase("nocheatplus") || AC.equalsIgnoreCase("NCP") ? "" : " " + ChatColor.stripColor(PT.SubString(item.getItemMeta().getLore().get(3), 10,
							item.getItemMeta().getLore().get(3).length()).replace(" ", ""))));
						}
					}, 10); // 15
				}
				if (MenuName.equalsIgnoreCase("Exploits") && e.getClickedInventory() != user.getPlayer().getInventory() && ItemName != null)
				{
					e.setCancelled(true);
					if (!ItemName.equalsIgnoreCase("User " + user.getPlayer().getName()))
						CrashersAndOtherExploits.INSTANCE.ExploitSendPacket(user.getPlayer(), user.CommandTarget, ItemName);
				}
				if (MenuName.equalsIgnoreCase("Vanilla Fly Checks") && e.getClickedInventory() != user.getPlayer().getInventory())
				{
					e.setCancelled(true);
					if (item != null && ItemName != null && item.getType() != Material.STAINED_GLASS_PANE)
					{
						if (ItemName.equalsIgnoreCase("Back"))
						{
							Refresh(user.getPlayer(), "menu settings");
							return;
						}
						if (ItemName.equalsIgnoreCase("Notify On Flying Kick 1.8.x"))
						{
							user.NotifyFlyingKick1_8 =! user.NotifyFlyingKick1_8;
							user.UpdateMenuValueChangeLore(user.VanillaFlyChecksMenu, slot, 0, user.getValues ("Vanilla Fly Checks", ItemName));
							return;
						}
						if (ItemName.equalsIgnoreCase("Notify On Flying Kick 1.9+"))
						{
							user.NotifyFlyingKick1_9 =! user.NotifyFlyingKick1_9;
							user.UpdateMenuValueChangeLore(user.VanillaFlyChecksMenu, slot, 0, user.getValues ("Vanilla Fly Checks", ItemName));
							return;
						}
						if (ItemName.equalsIgnoreCase("1.8.x"))
							user.Vanilla1_8FlyCheck =! user.Vanilla1_8FlyCheck;
						
						if (ItemName.equalsIgnoreCase("1.9+"))
							user.Vanilla1_9FlyCheck =! user.Vanilla1_9FlyCheck;
						
						e.setCurrentItem(user.getMadeItems (MenuName, ItemName));
					}
				}
				if (MenuName.equalsIgnoreCase("Rebug Settings") && (Rebug.hasAdminPerms(user.getPlayer())) && e.getClickedInventory() != user.getPlayer().getInventory())
				{
					e.setCancelled(true);
					
					if (item != null && ItemName != null && !ItemName.equalsIgnoreCase("Add more Settings as i think of them!"))
					{
						if (ItemName.equalsIgnoreCase("back"))
						{
							Refresh(user.getPlayer(), "menu settings");
							return;
						}
						if (ItemName.equalsIgnoreCase("Debug"))
							Rebug.debug =! Rebug.debug;
						
						if (ItemName.equalsIgnoreCase("Per Player Alerts"))
							Rebug.PrivatePerPlayerAlerts =! Rebug.PrivatePerPlayerAlerts;
						
						if (ItemName.equalsIgnoreCase("Debug To Ops Only"))
						{
							Rebug.debugOpOnly =! Rebug.debugOpOnly;
						}
						if (ItemName.equalsIgnoreCase("Reset Scaffold Area"))
						{
							Rebug.GetMain().RestScaffoldTask.cancel();
							ResetScaffoldTestArea.getMainTask().run();
							return;
						}
						if (ItemName.equalsIgnoreCase("Kick on reload config"))
						{
							Rebug.KickOnReloadConfig =! Rebug.KickOnReloadConfig;
						}
						if (ItemName.equalsIgnoreCase("Reload Config"))
						{
							Rebug.GetMain().Reload_Configs(user);
							if (Rebug.KickOnReloadConfig)
							{
								for (Player players : Bukkit.getOnlinePlayers())
								{
									PT.KickPlayer(players, ChatColor.DARK_RED + "Rejoin reloading Rebug's Config!");
								}
							}
						}
						else
							user.UpdateItemInMenu(user.getRebugSettingsMenu, slot, user.getMadeItems(MenuName, ItemName));
					}
				}
				if (MenuName.equalsIgnoreCase("Player Settings"))
				{
					if (e.getClickedInventory() != user.getPlayer().getInventory())
					{
						e.setCancelled(true);
						if (ItemName != null && !ItemName.equalsIgnoreCase("Add more Settings as i think of them!"))
						{
							if (ItemName.equalsIgnoreCase("Rebug Settings"))
							{
								Refresh(user.getPlayer(), "menu Rebug%Settings");
								return;
							}
							if (ItemName.equalsIgnoreCase("Hunger"))
							{
								user.Hunger =! user.Hunger;
							}
							if (ItemName.equalsIgnoreCase("Fall Damage"))
							{
								user.FallDamage =! user.FallDamage;
							}
							if (ItemName.equalsIgnoreCase("Exterranl Damage"))
							{
								user.Exterranl_Damage =! user.Exterranl_Damage;
							}
							if (ItemName.equalsIgnoreCase("Damage Resistance"))
							{
								user.Damage_Resistance =! user.Damage_Resistance;
								if (user.Damage_Resistance)
									user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3, true, false));
								else
									user.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
							}
							if (ItemName.equalsIgnoreCase("Fire Resistance"))
							{
								user.Fire_Resistance =! user.Fire_Resistance;
								if (user.Fire_Resistance)
									user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
								else
									user.getPlayer().removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
							}
							if (ItemName.equalsIgnoreCase("Infinite Blocks"))
							{
								if (user.getPlayer().hasPermission("me.killstorm103.rebug.user.infinite_blocks") || user.getPlayer().isOp() || user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin"))
									user.Infinite_Blocks =! user.Infinite_Blocks;
								else
								{
									user.getPlayer().sendMessage(Rebug.RebugMessage + "You don't have Permission to use this!");
									return;
								}
							}
							if (ItemName.equalsIgnoreCase("Vanilla Fly Checks"))
							{
								Command(user.getPlayer(), "menu Vanilla%Fly%Checks");
								return;
							}
							if (ItemName.equalsIgnoreCase("Potions"))
							{
								user.PotionEffects =! user.PotionEffects;
							}
							if (ItemName.equalsIgnoreCase("Auto Refill Blocks"))
							{
								user.AutoRefillBlocks =! user.AutoRefillBlocks;
							}
							if (ItemName.equalsIgnoreCase("Proximity Player Hider"))
							{
								user.ProximityPlayerHider =! user.ProximityPlayerHider;
							}
							if (ItemName.equalsIgnoreCase("AllowAT"))
							{
								user.AllowAT =! user.AllowAT;
							}
							if (ItemName.equalsIgnoreCase("Flags"))
							{
								user.ShowFlags =! user.ShowFlags;
								//ShowFlags(user);
							}
							if (ItemName.equalsIgnoreCase("Kick"))
							{
								user.AntiCheatKick =! user.AntiCheatKick;
							}
							if (ItemName.equalsIgnoreCase("Hide All Online Players"))
							{
								user.HideOnlinePlayers =! user.HideOnlinePlayers;
							}
							if (ItemName.equalsIgnoreCase("Direct Messages"))
							{
								user.AllowDirectMessages =! user.AllowDirectMessages;
							}
							if (ItemName.equalsIgnoreCase("Auto Close AntiCheats Menu"))
							{
								user.AutoCloseAntiCheatMenu =! user.AutoCloseAntiCheatMenu;
							}
							user.UpdateMenuValueChangeLore(user.SettingsMenu, slot, 0, user.getValues ("Player Settings", ItemName));
						}
					}
				}
				if (MenuName.equalsIgnoreCase("Items"))
				{
					if (e.getClickedInventory() != user.getPlayer().getInventory()) 
					{
						e.setCancelled(true);
						if (PT.isInventoryFull(user.getPlayer()))
						{
							user.getPlayer().sendMessage(Rebug.RebugMessage + "Your Inventory's full make some space!");
							return;
						}
						for (int i = 0; i < user.getPlayer().getInventory().getSize(); i ++)
						{
							ItemStack InventoryItem = user.getPlayer().getInventory().getItem(i);
							if (InventoryItem == null)
							{
								user.getPlayer().getInventory().setItem(i, item);
								break;
							}
						}
					}
				}
				if (MenuName.equalsIgnoreCase("Crashers") && e.getClickedInventory() != user.getPlayer().getInventory())
				{
					if (ItemName != null)
					{
						if (ItemName.equalsIgnoreCase("User " + user.getPlayer().getName()))
						{
							e.setCancelled(true);
							return;
						}
						if (!Rebug.hasAdminPerms(user.getPlayer()) && ItemName.equalsIgnoreCase("illegal Effect"))
						{
							user.getPlayer().sendMessage("because this crashes the server to you have to be Opped to use it!");
							e.setCancelled(true);
							return;
						}
						// TODO Move this
						if (ItemName.equalsIgnoreCase("spawn entity crashers"))
						{
							user.getPlayer().openInventory(user.getSpawnEntityCrashers());
							e.setCancelled(true);
							return;
						}
						CrashersAndOtherExploits.INSTANCE.CrashSendPacket(user.getPlayer(), user.CommandTarget, ItemName, null);
						e.setCancelled(true);
					}
				}
				if (MenuName.equalsIgnoreCase("Spawn Entity Crashers"))
				{
					if (ItemName != null && e.getClickedInventory() != user.getPlayer().getInventory())
					{
						if (ItemName.equalsIgnoreCase("Back"))
						{
							e.setCancelled(true);
							user.getPlayer().closeInventory();
							Bukkit.getScheduler().scheduleSyncDelayedTask(Rebug.GetMain(), new Runnable()
				            {
				                @Override
				                public void run()
				                {
				                	Command(BackUser.getPlayer(), "menu crashers " + BackUser.CommandTarget);
				                }
				                
				            }, 1L);
							
							return;
						}
						CrashersAndOtherExploits.INSTANCE.CrashSendPacket(user.getPlayer(), user.CommandTarget, "SpawnEntity", ItemName);
						e.setCancelled(true);
					}
				}
			}
		}
	}
	// This is a TEST!
	/*
	public static void ShowFlags (User user)
	{
		if (user == null) return;
		
		String AntiCheat = ChatColor.stripColor(user.AntiCheat).toLowerCase();
		String message = null;
		switch (AntiCheat)
		{
		case "grimac":
			Bukkit.dispatchCommand(user.getPlayer(), "grim verbose");
			//user.AlertsEnabled.put(AntiCheat, true);
			break;
			
		case "horizon":
			message = "horizon verbose";
			break;
		
		default:
			break;
		}
		if (message != null)
			user.sendMessage(Rebug.RebugMessage + "use /" + message + " to toggle alerts");
	}
	*/
}
