package me.killstorm103.Rebug.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
import me.killstorm103.Rebug.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.PTNormal;
import me.killstorm103.Rebug.Utils.User;


public class EventMenus implements Listener
{
	public void Command (Player sender, String command)
	{
		Bukkit.dispatchCommand(sender, "rebug " + command);
	}
	private void Refresh (Player player, String menu)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Rebug.getINSTANCE(), new Runnable()
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
	}
	@SuppressWarnings("deprecation")
	@EventHandler (priority =  EventPriority.HIGHEST)
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
			if (e.getClickedInventory() != e.getWhoClicked().getInventory() && item != null && MenuName.equalsIgnoreCase("Items") && item.getType() == Material.WOOL)
			{
				if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
				{
					if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("delete item!"))
					{
						if (getCursor != null && getCursor.getType() != Material.AIR)
							e.setCursor(null);
						
						e.setCancelled(true);
						return;
					}
					if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("Debug item!"))
					{
						if (getCursor != null && getCursor.getType() != Material.AIR)
						{
							player.sendMessage(Rebug.RebugMessage + "Debug= " + getCursor + " ID= " + getCursor.getType().getId());
							if (getCursor.getData() != null)
								player.sendMessage(Rebug.RebugMessage + "Data= " + getCursor.getData().getData());
						}
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
			User user = Rebug.getUser((Player) e.getView().getPlayer());
			if (user == null)
			{
				PTNormal.KickPlayer((Player) e.getView().getPlayer(), PTNormal.RebugsUserWasNullErrorMessage("in " + MenuName));
				return;
			}
			if (item != null && item.getItemMeta() != null)
			{
				meta = item.getItemMeta();
				String ItemName = meta.hasDisplayName() ? ChatColor.stripColor(meta.getDisplayName()) : null;
				if (MenuName.equalsIgnoreCase("Potions") && e.getClickedInventory() != user.getPlayer().getInventory())
				{
					e.setCancelled(true);
					Bukkit.getScheduler().runTask(Rebug.getINSTANCE(), new Runnable()
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
					if (item != null && item.getType() != Material.AIR && Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.debug-clicked-item"))
					{
						user.sendMessage("Clicked on= " + ItemName);
						user.sendMessage("Name= " + item.getType().name());
						user.sendMessage("ID= " + item.getType().getId());
						if (getCursor.getData() != null)
							player.sendMessage(Rebug.RebugMessage + "Data= " + getCursor.getData().getData());
						
						return;
					}
					if (item == null || !item.hasItemMeta() || item.getItemMeta().getLore() == null || item.getItemMeta().getLore().size() <= 1 || ItemName.equalsIgnoreCase("info"))
						return;
					
					if (ItemName.equalsIgnoreCase("Vanilla"))
					{
						if (clickType == ClickType.RIGHT)
						{
							user.getPlayer().closeInventory();
							Bukkit.dispatchCommand(user.getPlayer(), "rebug menu Vanilla%Fly%Checks");
							return;
						}
					}
					Rebug.getINSTANCE().UpdateAntiCheat(user, new String[] {ItemName}, item, null, false);
				}
				if (MenuName.equalsIgnoreCase("Exploits") && e.getClickedInventory() != user.getPlayer().getInventory() && ItemName != null)
				{
					e.setCancelled(true);
					if (!ItemName.equalsIgnoreCase("User " + user.getPlayer().getName()))
					{
						if (user.getCommandTarget(true) == null)
						{
							user.sendMessage("Your Command Target was null so they must of left the server!");
							user.getPlayer().closeInventory();
							return;
						}
						if (user.getCommandTarget(false) != user.getPlayer())
						{
							if (ItemName.toLowerCase().contains("crash") && !user.hasPermission("me.killstorm103.rebug.user.use_crashers.others") && !Rebug.hasAdminPerms(user) 
							|| !ItemName.toLowerCase().contains("crash") && !user.hasPermission("me.killstorm103.rebug.user.use_exploits.others") && !Rebug.hasAdminPerms(user))
							{
								user.sendMessage("You don't have permission to use this on other players!");
								return;
							}
						}
						Rebug.getINSTANCE().getNMS().ExploitSendPacket(user.getPlayer(), user.getCommandTarget(false), ItemName, ItemName.toLowerCase().contains("crash"));
					}
				}
				if (MenuName.equalsIgnoreCase("Vanilla Fly Checks") && e.getClickedInventory() != user.getPlayer().getInventory())
				{
					e.setCancelled(true);
					if (item != null && ItemName != null && item.getType() != Material.STAINED_GLASS_PANE)
					{
						if (ItemName.equalsIgnoreCase("Back"))
						{
							user.getPlayer().closeInventory();
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
						e.setCurrentItem(Rebug.getINSTANCE().getNMS().getMadeItems (user, MenuName, ItemName));
					}
				}
				if (MenuName.equalsIgnoreCase("Rebug Settings") && Rebug.hasAdminPerms(user.getPlayer()) && e.getClickedInventory() != user.getPlayer().getInventory())
				{
					e.setCancelled(true);
					
					if (item != null && ItemName != null && !ItemName.equalsIgnoreCase("Add more Settings as i think of them!"))
					{
						if (ItemName.equalsIgnoreCase("back"))
						{
							user.getPlayer().closeInventory();
							Refresh(user.getPlayer(), "menu settings");
							return;
						}
						if (ItemName.equalsIgnoreCase("Reset Scaffold Area"))
						{
							if (Rebug.getINSTANCE().ResetScaffoldTask != null)
							{
								Rebug.getINSTANCE().ResetScaffoldTask.cancel();
								ResetScaffoldTestArea.getMainTask().run();
							}
							else
							{
								user.sendMessage("Scaffold Area Task was null!, Restarting Scaffold Area Task!");
								Rebug.getINSTANCE().ResetScaffoldTask = Bukkit.getScheduler().runTaskTimer(Rebug.getINSTANCE(), ResetScaffoldTestArea.getMainTask(), 0, 10000); // 6000
								Rebug.getINSTANCE().ResetScaffoldTask.cancel();
								ResetScaffoldTestArea.getMainTask().run();
							}
							
							return;
						}
						if (ItemName.equalsIgnoreCase("Reload Config"))
						{
							Rebug.getINSTANCE().Reload_Configs(user.getPlayer());
							return;
						}
						
						ItemsAndMenusUtils.INSTANCE.UpdateItemInMenu(ItemsAndMenusUtils.getRebugSettingsMenu, slot, Rebug.getINSTANCE().getNMS().getMadeItems(MenuName, ItemName));
					}
				}
				if (MenuName.equalsIgnoreCase("Packet Selector") && e.getClickedInventory() != user.getPlayer().getInventory())
				{
					e.setCancelled(true);
					user.UpdateItemInMenu(user.PacketDebuggerMenu, slot, Rebug.getINSTANCE().getNMS().getMadeItems(user, MenuName, ItemName));
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
								if (Rebug.hasAdminPerms(user.getPlayer()))
								{
									user.getPlayer().closeInventory();
									Refresh(user.getPlayer(), "menu Rebug%Settings");
								}
								else
									user.sendMessage("You don't have Permission to access that!");
								
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
							if (ItemName.equalsIgnoreCase("Client Command Checker"))
								user.ClientCommandChecker =! user.ClientCommandChecker;
							
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
								if (user.getPlayer().hasPermission("me.killstorm103.rebug.user.infinite_blocks") || Rebug.hasAdminPerms(user.getPlayer()))
								{
									if (!Rebug.getINSTANCE().getConfig().getBoolean("infinite-blocks-enabled"))
									{
										user.sendMessage("Sorry but Infinite Blocks is Disabled!");
										return;
									}
									user.Infinite_Blocks =! user.Infinite_Blocks;
								}
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
							if (ItemName.equalsIgnoreCase("Allow Mentions"))
							{
								user.AllowMentions =! user.AllowMentions;
							}
							if (ItemName.equalsIgnoreCase("Flags"))
							{
								user.ShowFlags =! user.ShowFlags;
							}
							if (ItemName.equalsIgnoreCase("Punishes"))
							{
								user.ShowPunishes =! user.ShowPunishes;
							}
							if (ItemName.equalsIgnoreCase("Kick"))
							{
								if (!Rebug.hasAdminPerms(user) && !user.hasPermission("me.killstorm103.rebug.user.anticheat-kicks"))
								{
									user.sendMessage("You don't have Permission to use this!");
									return;
								}
								if (!Rebug.getINSTANCE().getConfig().getBoolean("are-users-allowed-to-disable-kicks"))
									user.sendMessage("Kicks is Disabled so changing this Setting won't do anything!");
								
								user.AntiCheatKick =! user.AntiCheatKick;
							}
							if (ItemName.equalsIgnoreCase("S08 Alerts"))
								user.ShowS08Alert =! user.ShowS08Alert;
							
							if (ItemName.equalsIgnoreCase("Hide All Online Players"))
							{
								user.HideOnlinePlayers =! user.HideOnlinePlayers;
							}
							if (ItemName.equalsIgnoreCase("Direct Messages"))
							{
								user.AllowDirectMessages =! user.AllowDirectMessages;
							}
							if (ItemName.equalsIgnoreCase("Setbacks"))
								user.ShowSetbacks =! user.ShowSetbacks;
								
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
						if (PTNormal.isInventoryFull(user.getPlayer()))
						{
							user.getPlayer().sendMessage(Rebug.RebugMessage + "Your Inventory's full make some space!");
							return;
						}
						if (item.getType() == Material.TNT && !Rebug.getINSTANCE().getConfig().getBoolean("special-tnt-enabled"))
						{
							user.sendMessage("Sorry but Special TNT is Disabled!");
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
					/*
					if (ItemName != null)
					{
						if (ItemName.equalsIgnoreCase("User " + user.CommandTarget.getName()))
						{
							e.setCancelled(true);
							return;
						}
						if (!Rebug.hasAdminPerms(user.getPlayer()) && ItemName.equalsIgnoreCase("illegal Effect"))
						{
							user.sendMessage("because this crashes the server to you have to be Opped to use it!");
							e.setCancelled(true);
							return;
						}
						if (ItemName.equalsIgnoreCase("spawn entity crashers"))
						{
							user.getPlayer().closeInventory();
							user.getPlayer().openInventory(user.getSpawnEntityCrashers());
							e.setCancelled(true);
							return;
						}
						Rebug.getINSTANCE().getNMS().CrashSendPacket(user.getPlayer(), user.CommandTarget, ItemName, null);
						e.setCancelled(true);
					}
					*/
				}
				if (MenuName.equalsIgnoreCase("Spawn Entity Crashers"))
				{
					/*
					if (ItemName != null && e.getClickedInventory() != user.getPlayer().getInventory())
					{
						if (ItemName.equalsIgnoreCase("User " + user.CommandTarget.getName()))
						{
							e.setCancelled(true);
							return;
						}
						if (ItemName.equalsIgnoreCase("Back"))
						{
							user.getPlayer().closeInventory();
							Bukkit.getScheduler().scheduleSyncDelayedTask(Rebug.getINSTANCE(), new Runnable()
				            {
				                @Override
				                public void run()
				                {
				                	Command(user.getPlayer(), "menu crashers " + user.CommandTarget.getName());
				                }
				                
				            }, 1L);
							e.setCancelled(true);
							return;
						}
						Rebug.getINSTANCE().getNMS().CrashSendPacket(user.getPlayer(), user.CommandTarget, "SpawnEntity", ItemName);
						e.setCancelled(true);
					}
					*/
				}
			}
		}
	}
}
