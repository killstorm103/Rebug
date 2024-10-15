package me.killstorm103.Rebug.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import me.killstorm103.Rebug.Main.Rebug;

public class User {
	private final Player player;
	private String brand = null, register = null;
	private int protocol;

	public boolean ShowS08Alert = true, Builder = false, ClientCommandChecker = false, SentUpdatedCommand = false,
			Infinite_Blocks, CancelInteract = false, AutoCloseAntiCheatMenu, Hunger, Fire_Resistance, Damage_Resistance,
			Exterranl_Damage, Vanilla1_8FlyCheck, Vanilla1_9FlyCheck, NotifyFlyingKick1_8, NotifyFlyingKick1_9,
			PotionEffects, AutoRefillBlocks, AntiCheatKick, AllowMentions, ProximityPlayerHider, HideOnlinePlayers,
			AllowDirectMessages, ShowFlags, ShowPunishes, ShowSetbacks, FallDamage, ResetColorAC, HideIP = true;

	public org.bukkit.Location death_location;
	public String AntiCheat, NumberIDs = "", Keycard, HackedUUID, ColoredAC;
	public int SelectedAntiCheats = 0;

	private Player PlayerTarget;
	public UUID Target;
	public Map<String, Boolean> AlertsEnabled = new HashMap<>();
	public int S08Pos = 0, UnReceivedBrand = 0, ShouldTeleportByBow = 0, preSend = 0, preReceive = 0,
			sendPacketCounts = 0, receivePacketCounts = 0, BrandSetCount = 0, ClicksPerSecondRight = 0,
			ClicksPerSecondLeft = 0, preCPSRight = 0, preCPSLeft = 0, Yapper_Message_Count = 0, potionlevel = 1,
			potion_effect_seconds = 240;
	public double lastTickPosX = 0, lastTickPosY = 0, lastTickPosZ = 0;
	public long timer_balance = 0, joined = 0;
	public float yaw, pitch;

	public Player getCommandTarget (boolean NullOfflineCheck) 
	{
		if (PlayerTarget != null && !NullOfflineCheck)
			return PlayerTarget;

		if (Target != null) 
		{
			Player target = Bukkit.getPlayer(Target);
			if (target != null)
			{
				PlayerTarget = target;
				target = null;
				return PlayerTarget;
			}
		}
		Target = null;
		return PlayerTarget = null;
	}

	public Player getPlayer() 
	{
		return this.player;
	}

	public final UUID getUUID() {
		return getPlayer().getUniqueId();
	}

	public final String getName() {
		return getPlayer().getName();
	}
	private Map<String, String> mods = new HashMap<>();
	public final Map<String, String> getMods()
	{
		return Collections.unmodifiableMap(mods);
	}
	public void setMods (Map<String, String> mods_map)
	{
		this.mods = mods_map;
	}
	
	public void getModData (byte[] data) 
	{
		Map<String, String> mods = new HashMap<>();
		boolean store = false;
		String tempName = null;
		
		for(int i = 2; i < data.length; store = !store) 
		{
			int end = i + data[i] + 1;
			byte[] range = Arrays.copyOfRange(data, i + 1, end);
			String string = new String(range);
			Rebug.getINSTANCE().Log(Level.SEVERE, "str= " + string);
			if (store) 
				mods.put(tempName, string);
			else
				tempName = string;
			
			i = end;
		}
		
		this.mods = mods;
	}
	
	public User(Player player) {
		this.HackedUUID = UUID.randomUUID().toString();
		this.Keycard = ".say " + HackedUUID;
		AlertsEnabled.clear();
		this.AntiCheat = Rebug.getINSTANCE().getLoadedAntiCheatsFile().getString("default-anticheat");
		this.AntiCheat = PTNormal.isStringNull(this.AntiCheat) ? "Vanilla" : this.AntiCheat;
		this.SelectedAntiCheats = this.AntiCheat.equalsIgnoreCase("Vanilla") ? 0 : 1;
		this.player = player;
		LoadDefault();
		FileConfiguration playerConfig = Rebug.getINSTANCE().getPlayerConfig(player);
		if (playerConfig == null)
			ResetColorAC = true;
		
		this.protocol = getNumber();
		this.joined = System.currentTimeMillis() + (5 * 1000);
	}

	private void LoadDefault() {
		// Player Settings
		this.HideOnlinePlayers = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile()
				.getBoolean("Hide Online Players");
		this.ProximityPlayerHider = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile()
				.getBoolean("Proximity Player Hider");
		this.NotifyFlyingKick1_8 = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile()
				.getBoolean("Notify Flying Kick 1.8.x");
		this.NotifyFlyingKick1_9 = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile()
				.getBoolean("Notify Flying Kick 1.9");
		this.Vanilla1_8FlyCheck = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile()
				.getBoolean("Vanilla 1.8.x fly Check");
		this.Vanilla1_9FlyCheck = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile()
				.getBoolean("Vanilla 1.9 fly Check");
		this.Fire_Resistance = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Fire Resistance");
		this.Damage_Resistance = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile()
				.getBoolean("Damage Resistance");
		this.Hunger = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Hunger");
		this.Exterranl_Damage = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Exterranl Damage");
		this.FallDamage = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Fall Damage");
		this.AutoCloseAntiCheatMenu = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile()
				.getBoolean("Auto Close AntiCheat Menu");
		this.AntiCheatKick = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("AntiCheat Kick");
		this.ShowFlags = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Show Flags");
		this.ShowPunishes = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Show Punishes");
		this.ShowSetbacks = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Show Setbacks");
		this.AllowDirectMessages = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile()
				.getBoolean("Allow Direct Messages");
		this.PotionEffects = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile()
				.getBoolean("Allow Potion Effects");
		this.AutoRefillBlocks = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile()
				.getBoolean("Auto Refill Blocks Placed");
		this.AllowMentions = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Allow Mentions");
		this.Infinite_Blocks = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Infinite Blocks");
		this.Yapper_Message_Count = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getInt("Yapper");
		this.ShowS08Alert = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("S08 Alerts");
		this.ClientCommandChecker = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile()
				.getBoolean("Client Command Checker");

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
		this.CustomPayLoadPacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile()
				.getBoolean("Custom PayLoad");
		this.TabCompletePacket = Rebug.getINSTANCE().getDefaultPlayerSettingsConfigFile().getBoolean("Tab Complete");
	}

	public Inventory getPotionsMenu() {
		if (PotionsMenu == null)
			Rebug.getINSTANCE().getNMS().SetUpMenu(this, "potions menu");

		return PotionsMenu;
	}

	public Location getLocation() {
		return this.player.getLocation();
	}

	public String getDebugLocation() {
		return "Player= " + getPlayer().getName() + " Loc= " + "(world: " + getWorld().getName() + ") " + "(x: "
				+ getLocation().getBlockX() + ") " + "(y: " + getLocation().getY() + ") " + "(z: "
				+ getLocation().getBlockZ() + ") " + "(yaw: " + getLocation().getYaw() + ") " + "(pitch: "
				+ getLocation().getPitch() + ")";
	}

	public void sendMessage(String message) {
		message = message.replace(Rebug.RebugMessage, "");
		this.player.sendMessage(Rebug.RebugMessage + message);
	}

	public String getColoredAntiCheat ()
	{
		if (this.SelectedAntiCheats == 0)
		{
			ResetColorAC = false;
			return ChatColor.GREEN + "Vanilla";
		}
		if (this.SelectedAntiCheats > 1)
		{
			ResetColorAC = false;
			return ChatColor.AQUA + "Multi";
		}
		if (ResetColorAC)
		{
			String strip = ChatColor.stripColor(AntiCheat).toLowerCase();
			try
			{
				ColoredAC = ChatColor.translateAlternateColorCodes('&', Rebug.getINSTANCE().getLoadedAntiCheatsFile()
				.getString("loaded-anticheats." + strip + ".main-name"));
				ResetColorAC = false;
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return ColoredAC;
	}

	public String getValues(String menu, String SettingValue) {
		String text = null;
		if (!PTNormal.isStringNull(menu) && !PTNormal.isStringNull(SettingValue))
		{
			switch (menu.toLowerCase()) 
			{
			case "player settings":
				switch (SettingValue.toLowerCase()) {
				case "fall damage":
					text = ChatColor.AQUA + "Status: " + (FallDamage ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ FallDamage;
					break;

				case "exterranl damage":
					text = ChatColor.AQUA + "Status: " + (Exterranl_Damage ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ Exterranl_Damage;
					break;

				case "setbacks":
					text = ChatColor.AQUA + "Status: " + (ShowSetbacks ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ ShowSetbacks;
					break;

				case "hunger":
					text = ChatColor.AQUA + "Status: " + (Hunger ? ChatColor.GREEN : ChatColor.DARK_RED) + Hunger;
					break;

				case "potions":
					text = ChatColor.AQUA + "Status: " + (PotionEffects ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ PotionEffects;
					break;

				case "auto refill blocks":
					text = ChatColor.AQUA + "Status: " + (AutoRefillBlocks ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ AutoRefillBlocks;
					break;

				case "hide all online players":
					text = ChatColor.AQUA + "Status: " + (HideOnlinePlayers ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ HideOnlinePlayers;
					break;

				case "proximity player hider":
					text = ChatColor.AQUA + "Status: " + (ProximityPlayerHider ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ ProximityPlayerHider;
					break;

				case "auto close anticheats menu":
					text = ChatColor.AQUA + "Status: " + (AutoCloseAntiCheatMenu ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ AutoCloseAntiCheatMenu;
					break;

				case "direct messages":
					text = ChatColor.AQUA + "Status: " + (AllowDirectMessages ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ AllowDirectMessages;
					break;

				case "allow mentions":
					text = ChatColor.AQUA + "Status: " + (AllowMentions ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ AllowMentions;
					break;

				case "flags":
					text = ChatColor.AQUA + "Status: " + (ShowFlags ? ChatColor.GREEN : ChatColor.DARK_RED) + ShowFlags;
					break;

				case "punishes":
					text = ChatColor.AQUA + "Status: " + (ShowPunishes ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ ShowPunishes;
					break;

				case "kick":
					text = ChatColor.AQUA + "Status: " + (AntiCheatKick ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ AntiCheatKick;
					break;

				case "infinite blocks":
					text = ChatColor.AQUA + "Status: " + (Infinite_Blocks ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ Infinite_Blocks;
					break;

				case "damage resistance":
					text = ChatColor.AQUA + "Status: " + (Damage_Resistance ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ Damage_Resistance;
					break;

				case "fire resistance":
					text = ChatColor.AQUA + "Status: " + (Fire_Resistance ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ Fire_Resistance;
					break;

				case "s08 alerts":
					text = ChatColor.AQUA + "Status: " + (ShowS08Alert ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ ShowS08Alert;
					break;

				case "client command checker":
					text = ChatColor.AQUA + "Status: " + (ClientCommandChecker ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ ClientCommandChecker;
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
					text = ChatColor.AQUA + "Status: " + (NotifyFlyingKick1_8 ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ NotifyFlyingKick1_8;
					break;

				case "notify on flying kick 1.9+":
					text = ChatColor.AQUA + "Status: " + (NotifyFlyingKick1_9 ? ChatColor.GREEN : ChatColor.DARK_RED)
							+ NotifyFlyingKick1_9;
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

	public final int getVersion() {
		return Via.getAPI().getPlayerVersion(this.player.getUniqueId());
	}

	public final String getVersion_() {
		return ProtocolVersion.getProtocol(this.protocol).getName();
	}

	public final String getVersion_Short() {
		String version = getVersion_();
		int find = 0;
		if (version.contains("-")) {
			while (PTNormal.SubString(version, find, version.length()).contains("-")) 
			{
				find++;
			}

			version = PTNormal.SubString(version, find, version.length());
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
		int game_version = -1;
		try {
			game_version = Via.getAPI().getPlayerVersion(this.player.getUniqueId());
		} catch (Exception e) {
			e.printStackTrace();
		}

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

	public World getWorld() {
		return this.player.getWorld();
	}

	public String ClientBrand() {
		return brand;
	}

	// Inventory
	public Inventory OldInventory, ExploitsMenu, SettingsMenu, VanillaFlyChecksMenu, PotionsMenu, PacketDebuggerMenu, ClientInfoMenu;
	// Inventory Size can be: 9, 18, 27, 36, 45, 54

	// Packet Debugger
	public boolean AllowedToDebug = true;

	public boolean isPacketDebuggerEnabled() {
		return AllowedToDebug && Rebug.PacketDebuggerPlayers.containsKey(getPlayer().getUniqueId());
	}

	// Packets
	public boolean FlyingPacket = true, PositionPacket = true, PositionLookPacket = true, LookPacket = true,
			ArmAnimationPacket = true, HeldItemSlotPacket = true, DiggingPacket = true, BlockPlacePacket = true,
			EntityActionPacket = true, CloseWindowPacket = true, ClickWindowPacket = true, SettingsPacket = true,
			AbilitiesPacket = true, KeepAlivePacket = true, TransactionPacket = true, StatusPacket = true,
			SpectatePacket = true, SteerVehiclePacket = true, CustomPayLoadPacket = true, TabCompletePacket = true;

	public Inventory getPacketDebuggerMenu () 
	{
		if (PacketDebuggerMenu == null)
			Rebug.getINSTANCE().getNMS().SetUpMenu(this, "packetdebugger menu");

		return PacketDebuggerMenu;
	}
	
	public Inventory getClientInfoMenu () 
	{
		Rebug.getINSTANCE().getNMS().SetUpMenu(this, "clientinfo menu");
		return ClientInfoMenu;
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
		if (inventory == null || PTNormal.isStringNull(text))
			return;

		ItemMeta NewMeta = inventory.getItem(itemslot).getItemMeta();
		List<String> NewLore = NewMeta.getLore();
		NewLore.set(loreslot, text);
		NewMeta.setLore(NewLore);
		inventory.getItem(itemslot).setItemMeta(NewMeta);
	}

	public void UpdateItemInHandLore(int loreslot, String text) {
		if (this.player.getItemInHand() == null || PTNormal.isStringNull(text))
			return;

		ItemMeta NewMeta = this.player.getItemInHand().getItemMeta();
		List<String> NewLore = NewMeta.getLore();
		NewLore.set(loreslot, text);
		NewMeta.setLore(NewLore);
		this.player.getItemInHand().setItemMeta(NewMeta);
	}

	public Inventory getVanillaFlyChecks() {
		if (VanillaFlyChecksMenu == null)
			Rebug.getINSTANCE().getNMS().SetUpMenu(this, "vanilla fly checks menu");

		return VanillaFlyChecksMenu;
	}

	public Inventory getSettings() {
		if (SettingsMenu == null)
			Rebug.getINSTANCE().getNMS().SetUpMenu(this, "player settings");

		return OldInventory = SettingsMenu;
	}

	public Inventory getExploits() {
		if (ExploitsMenu != null) {
			if (getCommandTarget(true) == null) {
				getPlayer().closeInventory();
				sendMessage("Your Command Target was null so they must of left the server!");
				return ExploitsMenu = null;
			}
			User user = Rebug.getUser(getCommandTarget(false));
			UpdateItemInMenu(ExploitsMenu, 0, Rebug.getINSTANCE().getNMS().getMadeItems("%user-info%", user));
		}
		if (ExploitsMenu == null)
			Rebug.getINSTANCE().getNMS().SetUpMenu(this, "exploits menu");

		return ExploitsMenu;
	}

	public boolean hasPermission(String perm) {
		return getPlayer().hasPermission(perm);
	}

	public boolean isOnLadder() {
		return getLocation().getBlock().getType() == Material.LADDER
				|| getLocation().getBlock().getType() == Material.VINE;
	}

	public void DebuggerChatMessage(PacketReceiveEvent e, String toDebug) {
		if (PTNormal.isStringNull(toDebug))
			toDebug = "N/A";

		Rebug.getINSTANCE().getNMS().DebuggerChatMessage(getPlayer(), e, toDebug);
	}
}
