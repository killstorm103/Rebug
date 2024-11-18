package me.killstorm103.Rebug.Shared.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.StringUtil;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.manager.server.ServerVersion;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.killstorm103.Rebug.Shared.Command.Command.Types;
import me.killstorm103.Rebug.Shared.Command.Commands.BackCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.BuilderCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.CheckAC;
import me.killstorm103.Rebug.Shared.Command.Commands.ClientCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.ClientCommandCheckerCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.Credits;
import me.killstorm103.Rebug.Shared.Command.Commands.DamageCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.DebugItemCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.DebugRebugCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.DebugSoundCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.Discord;
import me.killstorm103.Rebug.Shared.Command.Commands.FeedCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.GetUUID;
import me.killstorm103.Rebug.Shared.Command.Commands.HealAndFeedCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.HealCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.Help;
import me.killstorm103.Rebug.Shared.Command.Commands.Menu;
import me.killstorm103.Rebug.Shared.Command.Commands.PacketDebugger;
import me.killstorm103.Rebug.Shared.Command.Commands.PlayerInfoCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.PotionCommand;
import me.killstorm103.Rebug.Shared.Command.Commands.Reload;
import me.killstorm103.Rebug.Shared.Command.Commands.Repair;
import me.killstorm103.Rebug.Shared.Command.Commands.SetHealthCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.SetUserAntiCheat;
import me.killstorm103.Rebug.Shared.Command.Commands.ShowCommands;
import me.killstorm103.Rebug.Shared.Command.Commands.SlashFly;
import me.killstorm103.Rebug.Shared.Command.Commands.SpawnCMD;
import me.killstorm103.Rebug.Shared.Command.Commands.Test;
import me.killstorm103.Rebug.Shared.Command.Commands.Unblock;
import me.killstorm103.Rebug.Shared.Command.Commands.VClip;
import me.killstorm103.Rebug.Shared.Command.Commands.Version;
import me.killstorm103.Rebug.Shared.Command.Commands.getInfo;
import me.killstorm103.Rebug.Shared.Command.Commands.Handler.EventCommandPreProcess;
import me.killstorm103.Rebug.Shared.Command.Commands.ShortCut.ShortCutBasic;
import me.killstorm103.Rebug.Shared.Events.EventBlockHandling;
import me.killstorm103.Rebug.Shared.Events.EventHandlePlayerSpawn;
import me.killstorm103.Rebug.Shared.Events.EventPlayer;
import me.killstorm103.Rebug.Shared.Events.EventWeather;
import me.killstorm103.Rebug.Shared.Events.PluginMessage_Listener;
import me.killstorm103.Rebug.Shared.Interfaces.NMS;
import me.killstorm103.Rebug.Shared.Interfaces.PTs;
import me.killstorm103.Rebug.Shared.PacketEvents.EventPackets;
import me.killstorm103.Rebug.Shared.PluginHooks.ApiProvider;
import me.killstorm103.Rebug.Shared.PluginHooks.PlaceholderapiHook;
import me.killstorm103.Rebug.Shared.Server.ServerVersionUtil;
import me.killstorm103.Rebug.Shared.Server.ServerVersionUtil.SoftWare;
import me.killstorm103.Rebug.Shared.Server.ConsoleFilter.ConsoleFilter;
import me.killstorm103.Rebug.Shared.Tasks.OneSecondUpdater_Task;
import me.killstorm103.Rebug.Shared.Tasks.ResetScaffoldTestArea;
import me.killstorm103.Rebug.Shared.Utils.CustomChatColor;
import me.killstorm103.Rebug.Shared.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Shared.Utils.PT;
import me.killstorm103.Rebug.Shared.Utils.User;
import me.killstorm103.Rebug.SoftWares.Bukkit.Events.EventMenus_Bukkit;
import me.killstorm103.Rebug.SoftWares.Bukkit.Utils.PT_Bukkit;
import me.killstorm103.Rebug.SoftWares.Bukkit.Versions.*;
import me.killstorm103.Rebug.SoftWares.Spigot.Commands.NoBreak;
import me.killstorm103.Rebug.SoftWares.Spigot.Events.EventMenus_Spigot;
import me.killstorm103.Rebug.SoftWares.Spigot.Utils.PT_Spigot;
import me.killstorm103.Rebug.SoftWares.Spigot.Versions.*;

public class Rebug extends JavaPlugin implements Listener 
{
	// Interfaces
	private NMS theNMS;
	private PTs thePT;
	
	public NMS getNMS ()
	{
		if (theNMS == null)
			Log(Level.SEVERE, "NMS interface was Null, Server Type: " + ServerVersionUtil.Type.getName() + " (Version: " + ServerVersionUtil.Type.getVersion() + ")");
		
		return theNMS;
	}
	public PTs getPT ()
	{
		if (thePT == null)
			Log(Level.SEVERE, "PT interface was Null, Server Type: " + ServerVersionUtil.Type.getName() + " (Version: " + ServerVersionUtil.Type.getVersion() + ")");
		
		return thePT;
	}
	
	public static Map<UUID, Integer> PacketDebuggerPlayers = new HashMap<>();
	public static boolean debug = false, KickOnReloadConfig = false, debugOpOnly = true, PrivatePerPlayerAlerts = true,
			AutoRefillBlocks = true, DebugSound = false;
	public static final String AllCommands_Permission = "me.killstorm103.rebug.commands.*";
	private static Rebug INSTANCE;
	private final ArrayList<me.killstorm103.Rebug.Shared.Command.Command> commands = new ArrayList<me.killstorm103.Rebug.Shared.Command.Command>();
	public static final HashMap<UUID, User> USERS = new HashMap<>();
	public static final String RebugMessage = ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| "
			+ ChatColor.DARK_RED + "REBUG " + ChatColor.DARK_GRAY + ">> ";
	public static String MessageMaker (String middle)
	{
		return  ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| "
				+ ChatColor.DARK_RED + middle + ChatColor.DARK_GRAY + ">> ";
	}
	public final String[] AntiCheatDepends ()
	{
		return new String[] {"PacketEvents", "ProtocolLib", "Atlas", "ProtocolSupport", "PacketListenerApi", "HamsterAPI", "TinyProtocol", "PacketAPI", "GPacket"};
	}
	public String getServerVersion() {
		return ServerVersionUtil.Server_Version;
	}
	private boolean isServerVersionSupportExperimental (String version)
	{
		if (getNMS() != null && PT.isServerNewerOrEquals(ServerVersion.V_1_13))
			return true;
		
		return false;
	}

	private boolean isServerVersionSupportedCheck () 
	{
		String ServerVersion = "N/A", fullName = getServer().getClass().getPackage().getName();
		try {
			ServerVersion = fullName.split("\\.")[3];
		} catch (ArrayIndexOutOfBoundsException e) 
		{
			Log(Level.SEVERE, "Failed to get the Server's version!");
			e.printStackTrace();
			return false;
		}
		ServerVersionUtil.Server_Version = ServerVersion;
		Log(Level.INFO, "Checking if Server Version[" + ServerVersionUtil.Server_Version + "] " + "is supported!");
		boolean isSpigot = ServerVersionUtil.isRunningSpigot(true);
		thePT = isSpigot ? new PT_Spigot () : new PT_Bukkit();
		switch (ServerVersionUtil.Server_Version)
		{
		case "v1_8_R3":
			theNMS = isSpigot ? new Spigot_1_8_8_Interface() : new Bukkit_1_8_8_Interface();
			break;

		case "v1_9_R1":
			theNMS = isSpigot ? new Spigot_1_9_Interface() : new Bukkit_1_9_Interface();
			break;

		case "v1_9_R2":
			theNMS = isSpigot ? new Spigot_1_9_4_Interface() : new Bukkit_1_9_4_Interface ();
			break;

		case "v1_10_R1":
			theNMS = isSpigot ? new Spigot_1_10_X_Interface() : new Bukkit_1_10_X_Interface();
			break;

		case "v1_11_R1":
			theNMS = isSpigot ? new Spigot_1_11_X_Interface() : new Bukkit_1_11_X_Interface();
			break;

		case "v1_12_R1":
			theNMS = isSpigot ? new Spigot_1_12_X_Interface() : new Bukkit_1_12_X_Interface();
			break;
			
		case "v1_13_R1":
			theNMS = isSpigot ? new Spigot_1_13_Interface() : new Bukkit_1_13_Interface();
			break;
			
		case "v1_13_R2":
			theNMS = isSpigot ? new Spigot_1_13_2_Interface() : new Bukkit_1_13_2_Interface();
			break;
			
		case "v1_14_R1":
			theNMS = isSpigot ? new Spigot_1_14_X_Interface () : new Bukkit_1_14_X_Interface();
			break;
			
		case "v1_15_R1":
			theNMS = isSpigot ? new Spigot_1_15_X_Interface () : new Bukkit_1_15_X_Interface();
			break;
			
		case "v1_16_R1":
			theNMS = isSpigot ? new Spigot_1_16_Interface() : new Bukkit_1_16_Interface();
			break;
			
		case "v1_16_R2":
			theNMS = isSpigot ? new Spigot_1_16_3_Interface() : new Bukkit_1_16_3_Interface();
			break;
		
		case "v1_16_R3":
			theNMS = isSpigot ? new Spigot_1_16_5_Interface() : new Bukkit_1_16_5_Interface();
			break;
			
		case "v1_17_R1":
			//theNMS = isSpigot ? new Spigot_1_17_X_Interface () : new Bukkit_1_17_X_Interface ();
			break;

		default:
			break;
		}
		if (theNMS != null)
		{
			if (ServerVersionUtil.Type == SoftWare.Bukkit)
				Log(Level.WARNING, "Support for Bukkit is Experimental!");
			
			if (isServerVersionSupportExperimental(ServerVersionUtil.Server_Version))
				Log(Level.WARNING, "Support for " + ServerVersionUtil.Server_Version + " is Experimental!");
		}
		
		return theNMS != null;
	}

	public static User getUser (Player player) 
	{
		if (player == null)
			return null;

		for (User user : USERS.values())
		{
			if (user.getPlayer() != player && !user.getPlayer().getUniqueId().equals(player.getUniqueId()))
				continue;

			return user;
		}
		return null;
	}

	public static void Debug (Object object, String msg)
	{
		if (!debug || PT.isStringNull(msg))
			return;
		
		getINSTANCE().Log(Level.INFO, msg);
		
		if (object == null) return; 
		
		if (object instanceof Player)
		{
			Player player = (Player) object;
			if (!debugOpOnly || hasAdminPerms(player))
				player.sendMessage(RebugMessage + msg);
			
			return;
		}
		if (object instanceof User)
		{
			User user = (User) object;
			if (!debugOpOnly || hasAdminPerms(user))
				user.sendMessage(msg);
			
			return;
		}
	}
	public static final String PluginName() 
	{
		return "Rebug";
	}

	public static final String getAuthor() {
		return "killstorm103";
	}

	public static final String PluginVersion() {
		return getINSTANCE().getDescription().getVersion();
	}
	public BukkitTask ResetScaffoldTask, EverySecondUpdaterTask;

	public File getConfigFile() {
		return ConfigFile;
	}

	public FileConfiguration getDefaultPlayerSettingsConfigFile() {
		return DefaultPlayerSettingsConfig;
	}

	private FileConfiguration anticheatConfig, ItemsConfig, DefaultPlayerSettingsConfig, serverranksConfig;
	private File folder, LoadedAntiCheatsConfigFile, LoadedItemsConfigFile, ConfigFile, DefaultPlayerSettingsFile,
			ServerRanksFile;

	public void initFolder() {
		folder = new File(this.getDataFolder(), "player-data");
		if (!folder.exists())
			folder.mkdirs();
	}

	private File getPlayerFile(Player player) {
		return new File(folder, player.getUniqueId().toString() + ".yml");
	}

	public FileConfiguration getPlayerConfig(Player p) {
		File playerFile = getPlayerFile(p);
		if (!playerFile.exists())
			return null;

		YamlConfiguration playerConfig = new YamlConfiguration();
		try {
			playerConfig.load(playerFile);
			return playerConfig;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	private void save(Player p, FileConfiguration config) {
		File playerFile = getPlayerFile(p);
		try {
			config.save(playerFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeConfigs(Player p, int mode) 
	{
		File playerFile = getPlayerFile(p);
		if (mode == 0 && playerFile.exists() && Config.ShouldDeletePlayerConfigAfterLoading())
			playerFile.delete();
	}
	public FileConfiguration getPlayerConfigFile(Player p) {
		if (Bukkit.getPlayerExact(p.getName()) == null)
			return null;

		FileConfiguration playerConfig = getPlayerConfig(p);
		if (playerConfig != null)
			return playerConfig;

		return null;
	}

	public void restorePlayer(Player p)
	{
		if (Bukkit.getPlayerExact(p.getName()) == null)
			return;

		FileConfiguration playerConfig = getPlayerConfig(p);
		if (playerConfig == null)
			return;

		User user = getUser(p);
		if (user == null)
			return;

		// Player Settings
		user.FallDamage = playerConfig.getConfigurationSection("Player Settings").getBoolean("Fall Damage");
		user.Exterranl_Damage = playerConfig.getConfigurationSection("Player Settings").getBoolean("Exterranl Damage");
		user.Damage_Resistance = playerConfig.getConfigurationSection("Player Settings")
				.getBoolean("Damage Resistance");
		user.Fire_Resistance = playerConfig.getConfigurationSection("Player Settings").getBoolean("Fire Resistance");
		user.Hunger = playerConfig.getConfigurationSection("Player Settings").getBoolean("Hunger");
		user.PotionEffects = playerConfig.getConfigurationSection("Player Settings").getBoolean("Potion Effects");
		user.AutoRefillBlocks = playerConfig.getConfigurationSection("Player Settings")
				.getBoolean("Auto Refill Blocks");
		user.Infinite_Blocks = playerConfig.getConfigurationSection("Player Settings").getBoolean("Infinite Blocks");
		user.HideOnlinePlayers = playerConfig.getConfigurationSection("Player Settings")
				.getBoolean("HideOnlinePlayers");
		user.ProximityPlayerHider = playerConfig.getConfigurationSection("Player Settings")
				.getBoolean("Proximity Player Hider");
		user.AutoCloseAntiCheatMenu = playerConfig.getConfigurationSection("Player Settings")
				.getBoolean("Auto Close AntiCheat Menu");
		user.AllowDirectMessages = playerConfig.getConfigurationSection("Player Settings")
				.getBoolean("Allow Direct Messages");
		user.AllowMentions = playerConfig.getConfigurationSection("Player Settings").getBoolean("Allow Mentions");

		user.NumberIDs = playerConfig.getConfigurationSection("Player Settings").getString("AntiCheat Number IDs");
		user.SelectedAntiCheats = playerConfig.getConfigurationSection("Player Settings")
				.getInt("Amount Of Selected AntiCheats");
		
		if (user.SelectedAntiCheats < 2) 
		{
			user.NumberIDs = "";
			user.AntiCheat = hasAdminPerms(user) || getLoadedAntiCheatsFile().getBoolean("bypass-permission-enabled")
					&& user.hasPermission("me.killstorm103.rebug.user.bypass_force_default_anticheat")
							? playerConfig.getConfigurationSection("Player Settings").getString("AntiCheat")
							: getLoadedAntiCheatsFile().getBoolean("force-default-anticheat") ? user.AntiCheat
									: playerConfig.getConfigurationSection("Player Settings").getString("AntiCheat");

			if (!user.AntiCheat.equalsIgnoreCase("Vanilla"))
			{
				Plugin plugin = Bukkit.getPluginManager().getPlugin(user.AntiCheat);
				if (getLoadedAntiCheatsFile().get("loaded-anticheats." + user.AntiCheat.toLowerCase()) == null
				|| !getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + user.AntiCheat.toLowerCase() + ".enabled")
				|| plugin == null || !plugin.isEnabled()) 
				{
					user.AntiCheat = Rebug.getINSTANCE().getLoadedAntiCheatsFile().getString("default-anticheat");
					user.AntiCheat = PT.isStringNull(user.AntiCheat) ? "Vanilla" : user.AntiCheat;
				}
			}
		}
		else
			user.AntiCheat = playerConfig.getConfigurationSection("Player Settings").getString("AntiCheat");
		
		user.SelectedAntiCheats = user.AntiCheat.equalsIgnoreCase("Vanilla") ? 0 : user.SelectedAntiCheats;
		
		user.ResetColorAC = true;
		user.ShowFlags = playerConfig.getConfigurationSection("Player Settings").getBoolean("Show Flags");
		user.ShowPunishes = playerConfig.getConfigurationSection("Player Settings").getBoolean("Show Punishes");
		user.ShowSetbacks = playerConfig.getConfigurationSection("Player Settings").getBoolean("Show Setbacks");
		user.AntiCheatKick = playerConfig.getConfigurationSection("Player Settings").getBoolean("AC Kick");
		user.Vanilla1_8FlyCheck = playerConfig.getConfigurationSection("Player Settings")
				.getBoolean("Vanilla Fly 1_8_Plus");
		user.Vanilla1_9FlyCheck = playerConfig.getConfigurationSection("Player Settings")
				.getBoolean("Vanilla Fly 1_9Plus");
		user.NotifyFlyingKick1_8 = playerConfig.getConfigurationSection("Player Settings")
				.getBoolean("Notify Vanilla Fly Kick 1_8");
		user.NotifyFlyingKick1_9 = playerConfig.getConfigurationSection("Player Settings")
				.getBoolean("Notify Vanilla Fly Kick 1_9");
		user.potionlevel = playerConfig.getConfigurationSection("Player Settings").getInt("Potion Setting Level");
		user.potion_effect_seconds = playerConfig.getConfigurationSection("Player Settings")
				.getInt("Potion Setting Timer");
		int max_timer = getConfig().getInt("potion-settings-max-timer"),
				max_level = getConfig().getInt("potion-settings-max-level");
		user.potion_effect_seconds = user.potion_effect_seconds < 1 ? 1
				: user.potion_effect_seconds > max_timer ? max_timer : user.potion_effect_seconds;
		user.potionlevel = user.potionlevel < 1 ? 1 : user.potionlevel > max_level ? max_level : user.potionlevel;
		user.Yapper_Message_Count = playerConfig.getConfigurationSection("Player Settings").getInt("Yapper");
		user.ShowS08Alert = playerConfig.getConfigurationSection("Player Settings").getBoolean("S08 Alerts");
		user.ClientCommandChecker = playerConfig.getConfigurationSection("Player Settings")
				.getBoolean("Client Command Checker");

		// PacketDebugger Settings
		user.FlyingPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Flying");
		user.PositionPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Position");
		user.PositionLookPacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("PositionLook");
		user.LookPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Look");
		user.ArmAnimationPacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("Arm Animation");
		user.HeldItemSlotPacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("Held Item Slot");
		user.DiggingPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Digging");
		user.BlockPlacePacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("Block Place");
		user.EntityActionPacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("Entity Action");
		user.CloseWindowPacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("Close Window");
		user.ClickWindowPacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("Click Window");
		user.SettingsPacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("Client Settings");
		user.StatusPacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("Client Status");
		user.AbilitiesPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Abilities");
		user.KeepAlivePacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("Keep Alive");
		user.TransactionPacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("Transaction");
		user.SpectatePacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Spectate");
		user.SteerVehiclePacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("Steer Vehicle");
		user.CustomPayLoadPacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("Custom PayLoad");
		user.TabCompletePacket = playerConfig.getConfigurationSection("Packet Debugger Settings")
				.getBoolean("Tab Complete");
		
		removeConfigs(p, 0);
	}

	public void savePlayer(Player p) {
		YamlConfiguration playerConfig = new YamlConfiguration();
		User user = getUser(p);
		if (user == null)
			return;
		playerConfig.set("Player Name", p.getName());
		playerConfig.createSection("Player Settings");

		playerConfig.getConfigurationSection("Player Settings").set("Fall Damage", user.FallDamage);
		playerConfig.getConfigurationSection("Player Settings").set("Exterranl Damage", user.Exterranl_Damage);
		playerConfig.getConfigurationSection("Player Settings").set("Damage Resistance", user.Damage_Resistance);
		playerConfig.getConfigurationSection("Player Settings").set("Fire Resistance", user.Fire_Resistance);
		playerConfig.getConfigurationSection("Player Settings").set("Hunger", user.Hunger);
		playerConfig.getConfigurationSection("Player Settings").set("Potion Effects", user.PotionEffects);
		playerConfig.getConfigurationSection("Player Settings").set("Auto Refill Blocks", user.AutoRefillBlocks);
		playerConfig.getConfigurationSection("Player Settings").set("Infinite Blocks", user.Infinite_Blocks);
		playerConfig.getConfigurationSection("Player Settings").set("HideOnlinePlayers", user.HideOnlinePlayers);
		playerConfig.getConfigurationSection("Player Settings").set("Proximity Player Hider",
				user.ProximityPlayerHider);
		playerConfig.getConfigurationSection("Player Settings").set("Auto Close AntiCheat Menu",
				user.AutoCloseAntiCheatMenu);
		playerConfig.getConfigurationSection("Player Settings").set("Allow Direct Messages", user.AllowDirectMessages);
		playerConfig.getConfigurationSection("Player Settings").set("Allow Mentions", user.AllowMentions);
		String AntiCheat = ChatColor.translateAlternateColorCodes('&', user.AntiCheat);
		playerConfig.getConfigurationSection("Player Settings").set("AntiCheat", ChatColor.stripColor(AntiCheat));
		playerConfig.getConfigurationSection("Player Settings").set("Amount Of Selected AntiCheats",
				user.SelectedAntiCheats);
		playerConfig.getConfigurationSection("Player Settings").set("AntiCheat Number IDs", user.NumberIDs);

		playerConfig.getConfigurationSection("Player Settings").set("Show Flags", user.ShowFlags);
		playerConfig.getConfigurationSection("Player Settings").set("Show Punishes", user.ShowPunishes);
		playerConfig.getConfigurationSection("Player Settings").set("Show Setbacks", user.ShowSetbacks);
		playerConfig.getConfigurationSection("Player Settings").set("AC Kick", user.AntiCheatKick);
		playerConfig.getConfigurationSection("Player Settings").set("Vanilla Fly 1_8_Plus", user.Vanilla1_8FlyCheck);
		playerConfig.getConfigurationSection("Player Settings").set("Vanilla Fly 1_9Plus", user.Vanilla1_9FlyCheck);
		playerConfig.getConfigurationSection("Player Settings").set("Notify Vanilla Fly Kick 1_8",
				user.NotifyFlyingKick1_8);
		playerConfig.getConfigurationSection("Player Settings").set("Notify Vanilla Fly Kick 1_9",
				user.NotifyFlyingKick1_9);
		playerConfig.getConfigurationSection("Player Settings").set("Potion Setting Level", user.potionlevel);
		playerConfig.getConfigurationSection("Player Settings").set("Potion Setting Timer", user.potion_effect_seconds);
		playerConfig.getConfigurationSection("Player Settings").set("Yapper", user.Yapper_Message_Count);
		playerConfig.getConfigurationSection("Player Settings").set("S08 Alerts", user.ShowS08Alert);
		playerConfig.getConfigurationSection("Player Settings").set("Client Command Checker",
				user.ClientCommandChecker);

		playerConfig.createSection("Packet Debugger Settings");

		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Flying", user.FlyingPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Position", user.PositionPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("PositionLook", user.PositionLookPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Look", user.LookPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Arm Animation", user.ArmAnimationPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Held Item Slot", user.HeldItemSlotPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Digging", user.DiggingPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Block Place", user.BlockPlacePacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Entity Action", user.EntityActionPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Close Window", user.CloseWindowPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Click Window", user.ClickWindowPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Client Settings", user.SettingsPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Client Status", user.StatusPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Abilities", user.AbilitiesPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Keep Alive", user.KeepAlivePacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Transaction", user.TransactionPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Spectate", user.SpectatePacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Steer Vehicle", user.SteerVehiclePacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Custom PayLoad",
				user.CustomPayLoadPacket);
		playerConfig.getConfigurationSection("Packet Debugger Settings").set("Tab Complete", user.TabCompletePacket);

		save(p, playerConfig);
	}

	private final static List<String> list_configs = new ArrayList<>();
	static {
		list_configs.clear();
		list_configs.add("config");
		list_configs.add("loaded anticheats");
		list_configs.add("loaded items");
		list_configs.add("default player settings");
		list_configs.add("server ranks");
	}

	public FileConfiguration getLoadedAntiCheatsFile() {
		return anticheatConfig;
	}

	public FileConfiguration getServerRanksConfig() {
		return serverranksConfig;
	}

	public FileConfiguration getLoadedItemsFile() {
		return ItemsConfig;
	}
	public static final ConcurrentHashMap  <UUID, Long> timedout = new ConcurrentHashMap <>();
	public void Reload_Configs (CommandSender sender)
	{
		if (KickOnReloadConfig) 
		{
			for (Player players : Bukkit.getOnlinePlayers())
				getPT().KickPlayer(players, ChatColor.DARK_RED + "Rejoin reloading Rebug's Config!");
		}
		boolean noerror;
		try {
			getConfig().load(getConfigFile());
			getConfig().save(getConfigFile());
			anticheatConfig = YamlConfiguration.loadConfiguration(LoadedAntiCheatsConfigFile);
			ItemsConfig = YamlConfiguration.loadConfiguration(LoadedItemsConfigFile);
			DefaultPlayerSettingsConfig = YamlConfiguration.loadConfiguration(DefaultPlayerSettingsFile);
			serverranksConfig = YamlConfiguration.loadConfiguration(ServerRanksFile);
			anticheats.clear();
			manual_anticheats.clear();
			ItemsAndMenusUtils.AntiCheatMenu = ItemsAndMenusUtils.ItemPickerMenu = null;
			timedout.clear();
			ResetScaffoldTask.cancel();
			ItemsAndMenusUtils.INSTANCE.DebugItem = ItemsAndMenusUtils.INSTANCE.DeadGlassItem = ItemsAndMenusUtils.INSTANCE.DeleteItem = null;
			ResetScaffoldTask = null;
			ResetScaffoldTask = getServer().getScheduler().runTaskTimer(this, ResetScaffoldTestArea.getMainTask(), 0, 20 * getConfig().getInt("scaffold-test-area.every")); 
			for (Player p : Bukkit.getOnlinePlayers())
			{
				User used = getUser(p);
				if (used != null)
				{
					used.SettingsMenu = null;
				}
			}
			
			
			if (sender instanceof Player)
				sender.sendMessage(RebugMessage + "Successfully Reloaded Config!");

			Bukkit.getConsoleSender().sendMessage(RebugMessage + "Successfully Reloaded Config!");
			noerror = true;
		} catch (Exception e) {
			noerror = false;
			if (sender instanceof Player)
				sender.sendMessage(RebugMessage + "Failed to Reload Config!");

			Bukkit.getConsoleSender().sendMessage(RebugMessage + "Failed to Reload Config!");
			e.printStackTrace();
		}
		if (noerror) {
			if (getLoadedAntiCheatsFile().getBoolean("Disabled-AntiCheat.disabled")) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.closeInventory();
					User used = getUser(p);
					if (used != null && !used.AntiCheat.equalsIgnoreCase("Vanilla")
							&& (Rebug.getINSTANCE().getLoadedAntiCheatsFile()
									.get("loaded-anticheats." + used.AntiCheat.toLowerCase()) == null
									|| !Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean(
											"loaded-anticheats." + used.AntiCheat.toLowerCase() + ".enabled"))) {
						if (getLoadedAntiCheatsFile().getBoolean("Disabled-AntiCheat.kick-on-Invalid-anticheat"))
							getPT().KickPlayer(used.getPlayer(), RebugMessage + used.AntiCheat
									+ " was Disabled! - Invalid AntiCheat upon Config Reload");

						else {
							used.sendMessage(used.AntiCheat + " was Disabled! - Invalid AntiCheat upon Config Reload");
							used.AntiCheat = getLoadedAntiCheatsFile().getString("default-anticheat");
							used.AntiCheat = PT.isStringNull(used.AntiCheat) ? "Vanilla" : used.AntiCheat;
							String NewAC = used.getColoredAntiCheat(),
									striped = ChatColor.stripColor(used.AntiCheat).toLowerCase();
							if (getLoadedAntiCheatsFile()
									.getBoolean("loaded-anticheats." + striped + ".has-short-name"))
								NewAC = NewAC.replace(NewAC,
										ChatColor.translateAlternateColorCodes('&',
												getLoadedAntiCheatsFile()
														.getString("loaded-anticheats." + striped + ".short-name"))
												+ ChatColor.RESET);

							UpdateUserPerms(used.getPlayer(), used.AntiCheat);
							UpdateScoreBoard(used, ChatColor.DARK_RED + "AC " + NewAC, 10, "anticheat");
						}
					}
				}
				getLoadedAntiCheatsFile().set("Disabled-AntiCheat.disabled", false);
				try {
					getLoadedAntiCheatsFile().save(LoadedAntiCheatsConfigFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void createCustomConfig(String con) {
		if (PT.isStringNull(con))
			return;

		switch (con.toLowerCase()) {
		case "server ranks":
			ServerRanksFile = new File(getDataFolder(), "Server Ranks.yml");
			if (!ServerRanksFile.exists()) {
				ServerRanksFile.getParentFile().mkdirs();
				saveResource("Server Ranks.yml", false);
			}
			serverranksConfig = YamlConfiguration.loadConfiguration(ServerRanksFile);
			break;

		case "config":
			ConfigFile = new File(getDataFolder(), "config.yml");
			if (!ConfigFile.exists()) {
				ConfigFile.getParentFile().mkdirs();
				saveResource("config.yml", false);
			}
			YamlConfiguration.loadConfiguration(ConfigFile);
			break;

		case "default player settings":
			DefaultPlayerSettingsFile = new File(getDataFolder(), "Default Player Settings.yml");
			if (!DefaultPlayerSettingsFile.exists()) {
				DefaultPlayerSettingsFile.getParentFile().mkdirs();
				saveResource("Default Player Settings.yml", false);
			}

			DefaultPlayerSettingsConfig = YamlConfiguration.loadConfiguration(DefaultPlayerSettingsFile);
			break;

		case "loaded items":
			LoadedItemsConfigFile = new File(getDataFolder(), "Items.yml");
			if (!LoadedItemsConfigFile.exists()) {
				LoadedItemsConfigFile.getParentFile().mkdirs();
				saveResource("Items.yml", false);
			}
			ItemsConfig = YamlConfiguration.loadConfiguration(LoadedItemsConfigFile);
			break;

		case "loaded anticheats":
			LoadedAntiCheatsConfigFile = new File(getDataFolder(), "Loaded AntiCheats.yml");
			if (!LoadedAntiCheatsConfigFile.exists()) {
				LoadedAntiCheatsConfigFile.getParentFile().mkdirs();
				saveResource("Loaded AntiCheats.yml", false);
			}
			anticheatConfig = YamlConfiguration.loadConfiguration(LoadedAntiCheatsConfigFile);
			break;

		default:
			Log(Level.SEVERE, "Unknown Config File!");
			break;
		}
	}

	public static final Map<Plugin, String> anticheats = new HashMap<>();
	public static final Map<String, Boolean> manual_anticheats = new HashMap<>();
	private final String TickMark = "✔️";

	public final String getTickMark() 
	{
		final String New = TickMark.substring(0, 1);
		return New;
	}
	public final String getXMark ()
	{
		try
		{
			return ChatColor.translateAlternateColorCodes('&', getConfig().getString("scoreboard.marks.x"));
		}
		catch (Exception e) {}
		
		return getConfig().getString("scoreboard.marks.x");
	}

	@SuppressWarnings("deprecation")
	public void addToScoreBoard(Player player) 
	{
		if (!Config.RebugScoreBoard())
			return;
		
		User user = getUser(player);
		if (user == null || Netherboard.instance().hasBoard(player))
			return;
		
		user.HasBoard = false;

		final String color = ChatColor.DARK_RED.toString();
		String AC = user.getColoredAntiCheat(), strip = ChatColor.stripColor(AC).toLowerCase();
		if (getLoadedAntiCheatsFile().get("loaded-anticheats." + strip + ".has-short-name") != null && getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + strip + ".has-short-name"))
			AC = AC.replace(AC, ChatColor.translateAlternateColorCodes('&', getLoadedAntiCheatsFile().getString("loaded-anticheats." + strip + ".short-name")) + ChatColor.RESET);
		
		
		Object board = null;
		final String Title = ChatColor.translateAlternateColorCodes('&', Config.ScoreboardTitle());
		final boolean usePreset = getConfig().getBoolean("scoreboard.use-preset");
		boolean defaulted = false;
		switch (getConfig().getInt("scoreboard.plugin"))
		{
		case 1:
			board = Netherboard.instance().createBoard(user.getPlayer(), Title);
			if (usePreset)
			{
				((BPlayerBoard) board).set("  " + ChatColor.DARK_GRAY + "| §cTest §2Server   " + ChatColor.DARK_GRAY + "|", 12);
				((BPlayerBoard) board).set(ChatColor.RESET + " ", 11);
				((BPlayerBoard) board).set(color + "AC " + AC, 10);
				((BPlayerBoard) board).set(color + "Client " + ChatColor.WHITE + user.getVersion_Short(), 9);
				((BPlayerBoard) board).set(color + "CPS " + ChatColor.WHITE + user.ClicksPerSecondLeft + " : " + user.ClicksPerSecondRight, 8);
				((BPlayerBoard) board).set(color + "BPS (XZ) " + ChatColor.WHITE + "0", 7);
				((BPlayerBoard) board).set(color + "BPS (Y) " + ChatColor.WHITE + "0", 6);
				((BPlayerBoard) board).set(color + "PPS " + ChatColor.WHITE + user.sendPacketCounts + "/in " + user.receivePacketCounts + "/out", 5);
				((BPlayerBoard) board).set(color + "TB InDev", 4);
				((BPlayerBoard) board).set(color + "Blocking " + getXMark(), 3);
				((BPlayerBoard) board).set(color + "Sprinting " + getXMark(), 2);
				((BPlayerBoard) board).set(color + "Sneaking " + getXMark(), 1);
				((BPlayerBoard) board).set(color + "OnGround " + getXMark(), 0);
			}
			else
			{
				int start = 0;
				for (String string : getConfig().getStringList("scoreboard.custom.list"))
				{
					string = string.replace("%user_onground%", user.getPlayer().isOnGround() ? getTickMark() : getXMark()).replace("%user_anticheat%", AC).replace("%user_client%", user.getVersion_Short()).
					replace("%user_cps%", user.ClicksPerSecondLeft + " : " + user.ClicksPerSecondRight).replace("%user_timer_balance%", user.timer_balance + "").replace("%user_isblocking%", user.getPlayer().isBlocking() ? getTickMark() : getXMark())
					.replace("%user_bpsxz%", "0").replace("%user_bpsy%", "0").replace("%user_pps%", user.sendPacketCounts + "/in " + user.receivePacketCounts + "/out").replace("%user_issprinting%", user.getPlayer().isSprinting() ? getTickMark() : getXMark())
					.replace("%user_issneaking%", user.getPlayer().isSneaking() ? getTickMark() : getXMark());
					((BPlayerBoard) board).set(ChatColor.translateAlternateColorCodes('&', string), start ++);
				}
			}
			user.getPlayer().setScoreboard(((BPlayerBoard) board).getScoreboard());
			break;
			
		default:
			defaulted = true;
			Log(Level.WARNING, "Unsupported Board! (addToScoreBoard)");
			break;
		}
		user.HasBoard =! defaulted;
	}
	public void UpdateScoreBoard (User user, String text, int score, String WhatItIs)
	{
		if (!Config.RebugScoreBoard() || user == null || !user.getPlayer().isOnline() || !user.HasBoard)
			return;
		
		if (!getConfig().getBoolean("scoreboard.use-preset"))
		{
			if (getConfig().get ("scoreboard.custom.slots-that-change." + WhatItIs.toLowerCase()) == null || !getConfig().getBoolean("scoreboard.custom.slots-that-change." + WhatItIs.toLowerCase() + ".enabled"))
				return;
			
			if (getConfig().get ("scoreboard.custom.slots-that-change." + WhatItIs.toLowerCase() + ".replace") != null && getConfig().getBoolean("scoreboard.custom.slots-that-change." + WhatItIs.toLowerCase() + ".replace") && getConfig().get("scoreboard.custom.slots-that-change." + WhatItIs.toLowerCase() + ".text") != null && !PT.isStringNull(getConfig().getString("scoreboard.custom.slots-that-change." + WhatItIs.toLowerCase() + ".text")))
			{
				String AC = user.getColoredAntiCheat(), striped = ChatColor.stripColor(user.AntiCheat).toLowerCase();
				if (getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + striped + ".has-short-name"))
					AC = AC.replace(AC, ChatColor.translateAlternateColorCodes('&', getLoadedAntiCheatsFile().getString("loaded-anticheats." + striped + ".short-name")) + ChatColor.RESET);
				
				text = getConfig().getString("scoreboard.custom.slots-that-change." + WhatItIs.toLowerCase() + ".text").replace("%user_onground%", user.onGround ? getTickMark() : getXMark()).
				replace("%user_issneaking%", user.isSneaking ? getTickMark() : getXMark())
				.replace("%user_issprinting%", user.isSprinting ? getTickMark() : getXMark()).replace("%user_isblocking%", user.isBlocking ? getTickMark() : getXMark()).replace("%user_timer_balance%", user.timer_balance + "").
				replace("%user_pps%", ChatColor.WHITE.toString() + user.sendPacketCounts + "/in " + user.receivePacketCounts + "/out")
			    .replace("%user_bpsy%", user.BPSY).replace("%user_bpsxz%", user.BPSXZ).
			    replace("%user_cps%", ChatColor.WHITE.toString() + user.ClicksPerSecondLeft + " : " + user.ClicksPerSecondRight).replace("%user_client%", user.getVersion_Short()).replace("%user_anticheat%", AC);
				text = ChatColor.translateAlternateColorCodes('&', text);
			}
			score = getConfig().getInt("scoreboard.custom.slots-that-change." + WhatItIs.toLowerCase() + ".slot");
		}
		Object board = null;
		switch (getConfig().getInt("scoreboard.plugin"))
		{
		case 1:
			board = Netherboard.instance().getBoard(user.getPlayer());
			if (board == null)
				return;

			if (!PT.isStringNull(((BPlayerBoard) board).get(score)))
				((BPlayerBoard) board).remove(score);

			((BPlayerBoard) board).set(text, score);
			break;
			
		default:
			Log(Level.WARNING, "Unsupported Board! (UpdateScoreBoard)");
			break;
		}
	}
	private void DeleteScoreBoard (Player player)
	{
		if (!Config.RebugScoreBoard() || player == null) return;
		
		Object board = null;
		switch (getConfig().getInt("scoreboard.plugin"))
		{
		case 1:
			board = Netherboard.instance().getBoard(player);
			if (board != null) 
			{
				((BPlayerBoard) board).clear();
				((BPlayerBoard) board).delete();
			}
			break;
			
		default:
			Log(Level.WARNING, "Unsupported Board! (DeleteScoreBoard)");
			break;
		}
	}

	private final ArrayList<UUID> isAlertsEnabledEveryTime = new ArrayList<>(), isAlertsEnabledOnce = new ArrayList<>();
	public final ArrayList<UUID> isAllowedToDebugPackets = new ArrayList<>();
	public static final ArrayList<UUID> KickList = new ArrayList<>();
	public final Map<UUID, String> ClientBranded = new HashMap<>(), ClientRegistered = new HashMap<>();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onKick(PlayerKickEvent e) 
	{
		if (!Rebug.getINSTANCE().getConfig().getBoolean("events.bukkit.PlayerKickEvent")) return;
		
		String reason = e.getReason();
		Player player = e.getPlayer();
		if (!reason.equalsIgnoreCase("flying is not enabled on this server") && !getConfig().getBoolean("are-users-allowed-to-disable-kicks")) 
		{
			KickList.clear();
			return;
		}
		if (reason.equalsIgnoreCase("flying is not enabled on this server"))
		{
			User user = getUser(player);
			if (user != null)
			{
				if (ServerVersionUtil.Server_Version.equalsIgnoreCase("v1_8_R3"))
				{
					if (user.NotifyFlyingKick1_8)
						getNMS().sendTitle(player, "You would be kicked for", "Vanilla Fly 1.8.x Check", 5, 50, 5);
					
					if (!user.Vanilla1_8FlyCheck)
						e.setCancelled(true);
					else
						e.setReason("Vanilla Fly 1.8.x Check");
				}
				else
				{
					if (user.NotifyFlyingKick1_9)
						getNMS().sendTitle(player, "You would be kicked for", "Vanilla Fly 1.9+ Check", 5, 50, 5);

					if (!user.Vanilla1_9FlyCheck)
						e.setCancelled(true);
					else
						e.setReason("Vanilla Fly 1.9+ Check");
				}
			}
			return;
		}
		
		if (reason.equalsIgnoreCase("Kicked by an operator.") && !KickList.contains(player.getUniqueId()))
			KickList.add(player.getUniqueId());

		if (!KickList.isEmpty()) {
			if (KickList.contains(player.getUniqueId())) {
				isAlertsEnabledEveryTime.remove(player.getUniqueId());
				KickList.remove(player.getUniqueId());
				return;
			}

			User user = getUser(player);
			if (user == null)
				return;

			if (!KickList.contains(user.getPlayer().getUniqueId()) && !user.AntiCheatKick)
				e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) 
	{
		if (!Rebug.getINSTANCE().getConfig().getBoolean("events.bukkit.PlayerJoinEvent")) return;
		
		Player player = e.getPlayer();
		Rebug.USERS.put(player.getUniqueId(), new User(player));
		User user = Rebug.getUser(player);
		restorePlayer(user.getPlayer());
		UpdateUserPerms(user.getPlayer(), user.SelectedAntiCheats > 1 ? user.NumberIDs : user.AntiCheat);
		addToScoreBoard(user.getPlayer());
		if (user.Fire_Resistance)
			user.getPlayer().addPotionEffect(
					new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
		else
			user.getPlayer().removePotionEffect(PotionEffectType.FIRE_RESISTANCE);

		if (user.Damage_Resistance)
			user.getPlayer().addPotionEffect(
					new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3, true, false));
		else
			user.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

		if (Config.ShouldForceGameMode() && !hasAdminPerms(user))
			user.getPlayer().setGameMode(GameMode.SURVIVAL);

		final String message = getConfig().getString("join-message");
		if (message != null && message.length() > 0 && hasAdminPerms(user))
			user.sendMessage(ChatColor.translateAlternateColorCodes('&',
					message.replace("%user%", user.getName()).replace("%player%", user.getName())));

		Location loc = user.getLocation();
		if ((!user.getPlayer().hasPlayedBefore() || loc.getBlockX() >= 18 && loc.getBlockY() >= 57
				&& loc.getBlockZ() >= 306 && loc.getBlockX() <= 61 && loc.getBlockY() <= 88 && loc.getBlockZ() <= 330)
				&& getConfig().getBoolean("world-spawn.use-this")) {
			Bukkit.getScheduler().runTask(this, new Runnable() {
				@Override
				public void run() {
					user.getPlayer().teleport(PT_Spigot.INSTANCE.getSpawn());
				}
			});
		}

		e.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + user.getName());
		if (!isAlertsEnabledOnce.contains(user.getUUID())) {
			for (String s : Rebug.getINSTANCE().getConfig().getStringList("on-join-commands.once"))
				Bukkit.dispatchCommand(user.getPlayer(), s);

			isAlertsEnabledOnce.add(user.getUUID());

		}
		if (!isAlertsEnabledEveryTime.contains(user.getUUID())) {
			for (String s : Rebug.getINSTANCE().getConfig().getStringList("on-join-commands.everytime"))
				Bukkit.dispatchCommand(user.getPlayer(), s);

			isAlertsEnabledEveryTime.add(user.getUUID());
		}

		checkPlayer(user, false);
		/*
		Bukkit.getScheduler().runTaskLater(this, () ->
		{
			user.getPlayer().sendPluginMessage(this, "FML|HS", new byte[] { -2, 0 });
			user.getPlayer().sendPluginMessage(this, "FML|HS", new byte[] { 0, 2, 0, 0, 0, 0 });
			user.getPlayer().sendPluginMessage(this, "FML|HS", new byte[] { 2, 0, 0, 0, 0 });
		}, 5);
		*/
		
	}
	public final static Map<UUID, Map<String, String>> ForgeModUsers = new HashMap<>();

	public void run_server_command(String cmd) {
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
	}

	public static List<UUID> lockedList = new ArrayList<>();

	public void checkPlayer(User user, boolean sudo) {
		if (user == null || !user.ClientCommandChecker && !sudo)
			return;

		if (!lockedList.contains(user.getUUID()))
			lockedList.add(user.getUUID());

		String code = user.Keycard;
		if (getConfig().getBoolean("client-command-checker.sendOnce"))
		{
			int secondLeft = 60;
			user.sendMessage(ChatColor.translateAlternateColorCodes('&',
					getConfig().getString("client-command-checker.topString")));
			run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage
					+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
					+ code + "\"}}]");
			run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage
					+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
					+ code + "\"}}]");
			run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage
					+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
					+ code + "\"}}]");
			run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage
					+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
					+ code + "\"}},{\"text\":\" " + "Remaining" + ": \",\"color\":\"red\",\"bold\":false},{\"text\":\""
					+ secondLeft + (secondLeft < 2 ? " second" : " seconds")
					+ "\",\"color\":\"yellow\",\"bold\":true}]");
			run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage
					+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
					+ code + "\"}}]");
			run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage
					+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
					+ code + "\"}}]");
			run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage
					+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
					+ code + "\"}}]");
			user.sendMessage(ChatColor.translateAlternateColorCodes('&',
					getConfig().getString("client-command-checker.bottomString")));
		} 
		else
		{
			new BukkitRunnable()
			{
				final Player localPlayer = user.getPlayer();
				final User user1 = user;
				int secondLeft = 60;
				
				public void run() 
				{
					if (!user1.ClientCommandChecker && !sudo) 
					{
						cancel();
						lockedList.remove(user1.getUUID());
					}
					
					this.secondLeft--;
					if (lockedList.contains(this.localPlayer.getUniqueId())) 
					{
						if (this.localPlayer.isOnline()) {
							this.localPlayer.sendMessage(RebugMessage + ChatColor.translateAlternateColorCodes('&',
									getConfig().getString("client-command-checker.topString")));
							run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\""
									+ RebugMessage
									+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
									+ code + "\"}}]");
							run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\""
									+ RebugMessage
									+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
									+ code + "\"}}]");
							run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\""
									+ RebugMessage
									+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
									+ code + "\"}}]");
							run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\""
									+ RebugMessage
									+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
									+ code + "\"}},{\"text\":\" " + "Remaining"
									+ ": \",\"color\":\"red\",\"bold\":false},{\"text\":\"" + this.secondLeft
									+ (this.secondLeft < 2 ? " second" : " seconds")
									+ "\",\"color\":\"yellow\",\"bold\":true}]");
							run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\""
									+ RebugMessage
									+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
									+ code + "\"}}]");
							run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\""
									+ RebugMessage
									+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
									+ code + "\"}}]");
							run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\""
									+ RebugMessage
									+ "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
									+ code + "\"}}]");
							this.localPlayer.sendMessage(RebugMessage + ChatColor.translateAlternateColorCodes('&',
									getConfig().getString("client-command-checker.bottomString")));
						} else {
							this.cancel();
							lockedList.remove(user1.getUUID());
						}
					}
					else 
					{
						this.cancel();
					}
				}
			}.runTaskTimer(this, 0L, 20L);
		}
		new BukkitRunnable() {
			final Player localPlayer = user.getPlayer();
			final User user1 = user;
			public void run() 
			{
				if (!user1.ClientCommandChecker && !sudo) 
				{
					cancel();
					lockedList.remove(user1.getUUID());
				}
				
				Player player = Bukkit.getPlayer(this.localPlayer.getUniqueId());
				if (player == null || !player.isOnline()) {
					cancel();
					lockedList.remove(player != null ? player.getUniqueId() : localPlayer.getUniqueId());
				}
				if (!lockedList.contains(player != null ? player.getUniqueId() : localPlayer.getUniqueId())) {
					cancel();
					return;
				}

				if (lockedList.contains(player != null ? player.getUniqueId() : localPlayer.getUniqueId())) {
					this.localPlayer.sendMessage(RebugMessage + "You Failed the Client Command Checker!");
					this.localPlayer.sendMessage(RebugMessage + "To Disable this check use /settings");
					cancel();
					lockedList.remove(player != null ? player.getUniqueId() : localPlayer.getUniqueId());
				}
			}
		}.runTaskLater(this, 20 * 60);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e) 
	{
		if (!Rebug.getINSTANCE().getConfig().getBoolean("events.bukkit.PlayerQuitEvent")) return;
		
		Player player = e.getPlayer();
		if (isAlertsEnabledEveryTime.contains(player.getUniqueId()))
			isAlertsEnabledEveryTime.remove(player.getUniqueId());
		
		if (ForgeModUsers.containsKey(player.getUniqueId()))
			ForgeModUsers.remove(player.getUniqueId());
		
		DeleteScoreBoard(player);
		User user = Rebug.getUser(player);
		if (user != null)
		{
			user.Builder = false;
			savePlayer(player);
			Rebug.USERS.remove(user.getPlayer().getUniqueId(), user);
		}
		if (PacketDebuggerPlayers.containsKey(player.getUniqueId()) && !hasAdminPerms(player)
				&& (!player.hasPermission("me.killstorm103.rebug.user.packet_debugger.save")
						|| !player.hasPermission("me.killstorm103.rebug.user.packet_debugger.use")))

			PacketDebuggerPlayers.remove(player.getUniqueId(), PacketDebuggerPlayers.get(player.getUniqueId()));

		e.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "] " + e.getPlayer().getName());
		
	}

	public void Log(Level JavaUtilLog, String message)
	{
		if (PT.isStringNull(message)) 
		{
			getLogger().log(Level.SEVERE, "message was null, Rebug.java: public void Log");
			message = "N/A";
		}
		if (JavaUtilLog == null) {
			getLogger().log(Level.SEVERE, "JavaUtilLog was null, Rebug.java: public void Log");
			JavaUtilLog = Level.INFO;
		}
		getLogger().log(JavaUtilLog, message);
	}

	@Override
	public void onEnable ()
	{
		INSTANCE = this;
		PacketDebuggerPlayers.clear();
		isAlertsEnabledEveryTime.clear();
		isAlertsEnabledOnce.clear();
		isAllowedToDebugPackets.clear();
		ClientBranded.clear();
		lockedList.clear();
		ClientRegistered.clear();
		initFolder();
		getConfig().options().copyDefaults(true).copyHeader(true);
		for (int i = 0; i < list_configs.size(); i++)
			createCustomConfig(list_configs.get(i));

		Log(Level.INFO, "Enabling commands");
		// ShortCuts
		getCommand("client").setExecutor(new ShortCutBasic());
		getCommand("exploits").setExecutor(new ShortCutBasic());
		getCommand("items").setExecutor(new ShortCutBasic());
		getCommand("settings").setExecutor(new ShortCutBasic());
		getCommand("heal").setExecutor(new ShortCutBasic());
		getCommand("feed").setExecutor(new ShortCutBasic());
		getCommand("getuuid").setExecutor(new ShortCutBasic());
		getCommand("healandfeed").setExecutor(new ShortCutBasic());
		if (!ServerVersionUtil.isBukkit())
			getCommand("nobreak").setExecutor(new ShortCutBasic());
		
		getCommand("ac").setExecutor(new ShortCutBasic());
		getCommand("ac").setTabCompleter(new ShortCutBasic());
		getCommand("health").setExecutor(new ShortCutBasic());
		getCommand("credits").setExecutor(new ShortCutBasic());
		getCommand("player").setExecutor(new ShortCutBasic());
		getCommand("player").setTabCompleter(new ShortCutBasic());
		getCommand("potions").setExecutor(new ShortCutBasic());
		getCommand("potions").setTabCompleter(new ShortCutBasic());
		getCommand("potion").setExecutor(new ShortCutBasic());
		getCommand("potion").setTabCompleter(new ShortCutBasic());
		getCommand("packetdebugger").setExecutor(new ShortCutBasic());
		getCommand("packetdebugger").setTabCompleter(new ShortCutBasic());
		getCommand("vclip").setExecutor(new ShortCutBasic());
		getCommand("fly").setExecutor(new ShortCutBasic());
		getCommand("clientcommandchecker").setExecutor(new ShortCutBasic());
		getCommand("checkac").setExecutor(new ShortCutBasic());
		

		cmd.clear();
		commands.clear();
		commands.add(new Unblock());
		commands.add(new Version());
		commands.add(new getInfo());
		commands.add(new GetUUID());
		commands.add(new Test());
		commands.add(new SpawnCMD());
		commands.add(new DamageCMD());
		commands.add(new Menu());
		commands.add(new ClientCMD());
		commands.add(new BackCMD());
		commands.add(new SetHealthCMD());
		commands.add(new HealCMD());
		commands.add(new Repair());
		commands.add(new DebugSoundCMD());
		commands.add(new FeedCMD());
		commands.add(new Credits());
		commands.add(new Discord());
		commands.add(new HealAndFeedCMD());
		commands.add(new PlayerInfoCMD());
		commands.add(new PotionCommand());
		commands.add(new DebugItemCMD());
		commands.add(new PacketDebugger());
		commands.add(new Reload());
		commands.add(new VClip());
		commands.add(new Help());
		commands.add(new DebugRebugCMD());
		commands.add(new SlashFly());
		commands.add(new SetUserAntiCheat());
		commands.add(new ShowCommands());
		commands.add(new ClientCommandCheckerCMD());
		commands.add(new BuilderCMD());
		commands.add(new CheckAC());
		for (me.killstorm103.Rebug.Shared.Command.Command cmds : commands)
			cmd.add(cmds.getName());

		Log(Level.INFO, "Enabling Events/Listeners");
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new EventBlockHandling(), this);
		pm.registerEvents(this, this);
		if (ServerVersionUtil.isBukkit())
			pm.registerEvents(new EventMenus_Bukkit(), this);
		else
		{
			if (ServerVersionUtil.isRunningSpigot(false))
			{
				pm.registerEvents(new EventMenus_Spigot(), this);
				commands.add(new NoBreak());
			}
		}
		pm.registerEvents(new EventWeather(), this);
		pm.registerEvents(new EventHandlePlayerSpawn(), this);
		pm.registerEvents(new EventPlayer(), this);
		pm.registerEvents(new EventCommandPreProcess(), this);
		pm.registerEvents(new PluginMessage_Listener(), this);
		
		Log(Level.INFO, "Rebug reload to change how ofter scaffold area gets reset!");
		ResetScaffoldTask = ResetScaffoldTask == null ? getServer().getScheduler().runTaskTimer(this, ResetScaffoldTestArea.getMainTask(), 0, 10000) : ResetScaffoldTask; 
		EverySecondUpdaterTask = EverySecondUpdaterTask == null ? getServer().getScheduler().runTaskTimerAsynchronously(this, OneSecondUpdater_Task.getMainTask(), 0, 20) : EverySecondUpdaterTask;

		Log(Level.INFO, "Enabling Packet Events");
		PacketEvents.getAPI().getEventManager().registerListener(new EventPackets(), PacketListenerPriority.HIGHEST);
		PacketEvents.getAPI().init();

		Log(Level.INFO, "Checking Hooks/Depends");
		Plugin placeholder = pm.getPlugin("PlaceholderAPI");
		if (placeholder != null && placeholder.isEnabled()) 
		{
			Log(Level.INFO, "Hooking PlaceholderAPI!");
			PlaceholderapiHook.registerHook();
		}
		else
			Log(Level.SEVERE, "The Plugin PlaceholderAPI wasn't Found this will cause issues!");
		
		ConsoleFilter.INSTANCE.HideConsoleMessages();
		
		ForgeModUsers.clear();
		
		// Test
		boolean is1_13 = PT.isServerNewerOrEquals(ServerVersion.V_1_13);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, is1_13 ? "fml:hs" : "FML|HS");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, is1_13 ? "fml:hs" : "FML|HS", new PluginMessage_Listener());
	}
	@Override
	public void onDisable () 
	{
		Bukkit.getOnlinePlayers().forEach(this::savePlayer);
		if (ResetScaffoldTask != null)
			ResetScaffoldTask.cancel();

		if (EverySecondUpdaterTask != null)
			EverySecondUpdaterTask.cancel();

		PacketEvents.getAPI().terminate();
		if (getConfig().getBoolean("ConsoleFilter.enabled") && !getConfig().getStringList("ConsoleFilter.blocked").isEmpty())
			Log(Level.INFO, "ConsoleFilter: " + ConsoleFilter.INSTANCE.getEngine().getHiddenMessagesCount() + " Messages were hidden thanks to Rebug!");
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onLoad()
	{
		anticheats.clear();
		manual_anticheats.clear();
		if (!isServerVersionSupportedCheck()) 
		{
			Log(Level.SEVERE, "Unsupported Server Version!");
			Log(Level.INFO, "Rebug supports: 1.8.8-1.16.x");
			Log(Level.INFO, "1.13+ support is Experimental!");
			Log(Level.SEVERE, "You should stop the server and remove Rebug!");
			return;
		}
		PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
		PacketEvents.getAPI().getSettings().reEncodeByDefault(false).checkForUpdates(true).bStats(false);
		PacketEvents.getAPI().load();
	}
	public ArrayList<me.killstorm103.Rebug.Shared.Command.Command> getCommands() {
		return commands;
	}

	public me.killstorm103.Rebug.Shared.Command.Command getCommandByName(String name) {
		Iterator<me.killstorm103.Rebug.Shared.Command.Command> iter = commands.iterator();
		while (iter.hasNext()) {
			me.killstorm103.Rebug.Shared.Command.Command mod = iter.next();
			if (mod.getName().equalsIgnoreCase(name))
				return mod;
		}
		return null;
	}

	public me.killstorm103.Rebug.Shared.Command.Command getCommandBySubName (String name)
	{
		Iterator<me.killstorm103.Rebug.Shared.Command.Command> iter = commands.iterator();
		while (iter.hasNext()) 
		{
			me.killstorm103.Rebug.Shared.Command.Command mod = iter.next();
			if (mod.SubAliases() != null && mod.SubAliases().length > 0)
			{
				for (int o = 0; o < mod.SubAliases().length; o++) 
				{
					if (mod.SubAliases()[o].equalsIgnoreCase(name))
						return mod;
				}
			}
		}
		return null;
	}

	private final List<String> cmd = new ArrayList<>();
	public final List<String> getCMDList ()
	{
		return cmd;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) 
	{
		if (alias.equalsIgnoreCase("rebug")) 
		{
			if (args.length == 1)
			{
				final List<String> list = new ArrayList<>();
				list.clear();
				StringUtil.copyPartialMatches(args[args.length - 1], cmd, list);
				return list;
			}

			if (args.length > 1)
			{
				me.killstorm103.Rebug.Shared.Command.Command c = getCommandByName(args[0]);
				if (c != null && c.HasCustomTabComplete(sender, command, args, alias))
					return c.onTabComplete(sender, command, args, alias);
			}
		}

		return super.onTabComplete(sender, command, alias, args);
	}

	public static boolean hasAdminPerms (Object object)
	{
		if (object == null)
			return false;
		
		if (object instanceof Player)
		{
			Player player = (Player) object;
			return player.isOp() || player.hasPermission("me.killstorm103.rebug.server_owner") || player.hasPermission("me.killstorm103.rebug.server_admin");
		}
		if (object instanceof User)
		{
			User user = (User) object;
			return user.getPlayer().isOp() || user.hasPermission("me.killstorm103.rebug.server_owner") || user.hasPermission("me.killstorm103.rebug.server_admin");
		}
		
		return false;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		Debug(sender, "args.length= " + args.length + " command= " + command);

		if (command.toLowerCase().equals(PluginName().toLowerCase())) {
			if (args.length == 0) {
				Bukkit.dispatchCommand(sender, "rebug help");
				return true;
			}
			me.killstorm103.Rebug.Shared.Command.Command commands = getCommandByName(args[0]);
			if (commands != null) {
				try {
					Types type = commands.getType();
					if (type == null)
					{
						if (sender instanceof Player && hasAdminPerms((Player) sender))
							sender.sendMessage(RebugMessage + commands.getName() + "'s Command Type was somehow Null!");
							
						Log(Level.WARNING, commands.getName() + "'s Command Type was somehow Null!");
						type = Types.AnySender;
					}
					if (type.equals(Types.Player) && !(sender instanceof Player)) {
						Log(Level.INFO, "Only Players can use this Command!");
						return true;
					}
					if (type.equals(Types.Console) && sender instanceof Player) {
						sender.sendMessage(RebugMessage + "Only Console can use this Command!");
						return true;
					}
					User user = null;
					if (sender instanceof Player) 
					{
						user = getUser((Player) sender);
						if (user == null) 
						{
							sender.sendMessage(RebugMessage + "\"User\" was somehow null when executing command!");
							return true;
						}
					}
					if (user == null) {
						commands.onCommand(sender, command, args);
						return true;
					}
					if ((user.hasPermission(commands.getPermission()) || user.hasPermission(AllCommands_Permission)
							|| hasAdminPerms(user))) {
						if (commands.hasCommandCoolDown()
								&& !user.hasPermission("me.killstorm103.rebug.command_bypass_delay")
								&& !hasAdminPerms(user)) {
							if (commands.CoolDown.containsKey(user.getUUID())) {
								if (commands.CoolDown.get(user.getUUID()) > System.currentTimeMillis()) {
									long time = (commands.CoolDown.get(user.getUUID()) - System.currentTimeMillis())
											/ 1000;
									user.sendMessage("Command is on CoolDown for " + time + " second"
											+ (time > 1 ? "s" : "") + "!");
									return true;
								} else {
									commands.onCommand(sender, command, args);
									commands.CoolDown.put(user.getUUID(), System.currentTimeMillis() + (5 * 1000));
									return true;
								}
							} else {
								commands.onCommand(sender, command, args);
								commands.CoolDown.put(user.getUUID(), System.currentTimeMillis() + (5 * 1000));
								return true;
							}
						} else {
							commands.onCommand(sender, command, args);
							return true;
						}
					} else {
						user.sendMessage("You don't have Permission to use this command!");
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				sender.sendMessage(RebugMessage + "Unknown Command!");
				return true;
			}
		}

		return true;
	}

	private void UpdateUserPerms(Player user, String itemName) 
	{
		if (user == null || PT.isStringNull(itemName))
			return;

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getConfig().getString("user-update-perms-command").replace("%player%", user.getName())
		.replace("%user%", user.getName()).replace("%ac%", itemName).replace("%anticheat%", itemName));
		if (getConfig().getBoolean("use-ranks")) 
		{
			for (String section : getServerRanksConfig().getKeys(true))
			{
				if (!PT.isStringNull(section))
				{
					String perm = getServerRanksConfig().getString(section + ".permission");
					if (PT.isStringNull(perm))
						Log(Level.SEVERE, "user: " + user.getName() + " section<" + section + ">'s permission was Null! (Rebug.class, line: 1318)");

					else
					{
						if (user.hasPermission(perm))
						{
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getConfig().getString("user-staff-group-command").replace("%user%", user.getName())
							.replace("%player%", user.getName()) + " " + section.toLowerCase());

							if (getConfig().getBoolean("use-one-rank"))
								break;
						}
					}
				}
			}
		}
	}

	public static Rebug getINSTANCE ()
	{
		return INSTANCE;
	}

	public void UpdateAntiCheat (User user, String[] ItemName, ItemStack item, CommandSender Sudo, boolean Multi) 
	{
		if (user == null) 
		{
			if (Sudo != null)
				Sudo.sendMessage(RebugMessage + "User was null!");

			return;
		}
		if (Multi) 
		{
			final int size = ItemName.length - 2;
			if (!getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.multiple-anticheat.enabled")) 
			{
				if (Sudo == null) 
				{
					user.sendMessage("Sorry but Multiple AntiCheats is Disabled!");
					return;
				} 
				else
					Sudo.sendMessage(RebugMessage + ChatColor.YELLOW + "Warning " + ChatColor.DARK_GRAY + "You making "
							+ user.getName()
							+ " Select Multiple AntiCheats while it's Disabled in the Loaded AntiCheats.yml config!");
			}
			final int maxACs = getLoadedAntiCheatsFile().getInt("loaded-anticheats.multiple-anticheat.max-anticheats");
			if (getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.multiple-anticheat.limiter-enabled") && maxACs > 1 && size > maxACs) 
			{
				if (Sudo != null)
					Sudo.sendMessage(RebugMessage + "Sorry but your trying to Choose more AntiCheats than allowed!");
				else
					user.sendMessage("Sorry but your trying to Choose more AntiCheats than allowed!");

				return;
			}

			String ID_Name = "", Carded = "", Kickables = "", AntiCheatName = "";
			for (int i = 2; i < ItemName.length; i++) 
			{
				if (PT.isStringNull(ItemName[i])) 
				{
					if (Sudo != null)
						Sudo.sendMessage(
								RebugMessage + "Failed to change user's AntiCheat, the given anticheat name string ["
										+ i + "] was null!");
					else
						user.sendMessage("Failed to change AntiCheats due to the given anticheat name string [" + i
								+ "] was null!");

					return;
				}
				if (ItemName[i].equalsIgnoreCase("Vanilla")) 
				{
					if (Sudo == null)
						user.sendMessage("Dumbass you can't Select Vanilla when trying to Select Multiple AntiCheats!");
					else
						Sudo.sendMessage(RebugMessage + "Dumbass you can't Choose Vanilla for Multiple AntiCheats!");

					return;
				}
				
				if (getLoadedAntiCheatsFile().get("loaded-anticheats." + ItemName[i].toLowerCase()) == null) 
				{
					if (Sudo != null)
						Sudo.sendMessage(RebugMessage + "AntiCheat " + "\"" + ItemName[i] + "\" was not found!");
					else
						user.sendMessage("AntiCheat " + "\"" + ItemName[i] + "\" was not found!");

					return;
				}
				AntiCheatName = CustomChatColor
						.stripColor(CustomChatColor.translateAlternateColorCodes('&', getLoadedAntiCheatsFile()
								.getString("loaded-anticheats." + ItemName[i].toLowerCase() + ".main-name"), true, ItemName[i]));
				
				if (!getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + AntiCheatName.toLowerCase() + ".enabled")) 
				{
					if (Sudo != null)
						Sudo.sendMessage(RebugMessage + ChatColor.YELLOW + "Warning " + ChatColor.DARK_GRAY
								+ "You are making " + user.getName() + " Select " + AntiCheatName
								+ " which is a Disabled AntiCheat");

					else
					{
						user.sendMessage("AntiCheat: " + AntiCheatName + " is Disabled!");
						if (user.AutoCloseAntiCheatMenu)
							user.getPlayer().closeInventory();

						return;
					}
				}
				if (Sudo == null
						&& getLoadedAntiCheatsFile().get("loaded-anticheats." + AntiCheatName.toLowerCase() + ".permission-to-use") != null && getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + AntiCheatName.toLowerCase() + ".permission-to-use")
						&& !user.hasPermission(
								"me.killstorm103.rebug.user.select_anticheat_" + AntiCheatName.toLowerCase())) 
				{
					user.sendMessage("Sorry but permission-to-use is Enabled for this AntiCheat!");
					user.sendMessage("You need the permission:");
					user.sendMessage("me.killstorm103.rebug.user.select_anticheat_" + AntiCheatName.toLowerCase());
					if (user.AutoCloseAntiCheatMenu)
						user.getPlayer().closeInventory();

					return;
				}

				if (getLoadedAntiCheatsFile().get("loaded-anticheats." + AntiCheatName.toLowerCase() + ".is-multi-enabled") == null || !getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + AntiCheatName.toLowerCase() + ".is-multi-enabled")) 
				{
					if (Sudo != null)
						Sudo.sendMessage(
								RebugMessage + "Sorry but multi AntiCheat is not Enabled for " + AntiCheatName + "!");
					else
						user.sendMessage("Sorry but multi AntiCheat is not Enabled for " + AntiCheatName + "!");

					return;
				}

				String m_id = AntiCheatName.toLowerCase();
				if (ID_Name.toLowerCase().contains(m_id))
				{
					if (Sudo == null)
						user.sendMessage("Failed to Add Muti AC " + AntiCheatName
								+ " Due to ID already containing this AntiCheat!");
					else
						Sudo.sendMessage(RebugMessage + "Failed to Add Muti AC " + AntiCheatName
								+ " Due to ID already containing this AntiCheat!");

					return;
				}
				if (getLoadedAntiCheatsFile().get("loaded-anticheats." + AntiCheatName.toLowerCase() + ".requires-reconnect") != null && getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + AntiCheatName.toLowerCase() + ".requires-reconnect"))
					Kickables += AntiCheatName + (i < ItemName.length - 1 ? " " : "");

				ID_Name += AntiCheatName + (i < ItemName.length - 1 ? " " : "");
			}
			if (PT.isStringNull(ID_Name)) 
			{
				if (Sudo != null)
					Sudo.sendMessage(RebugMessage + "Failed to set Multi AC due to ID being null!");
				else
					user.sendMessage("Failed to set Multi AC due to ID being null!");

				return;
			}
			Carded = ID_Name.replace(" ", "_").toLowerCase();
			if (getLoadedAntiCheatsFile().get("loaded-anticheats.multiple-anticheats." + Carded) == null) 
			{
				if (size == 2)
					Carded = PT.ReverseString(Carded.replace("_", " ")).replace(" ", "_");

				else 
				{
					Carded = Carded.replace("_", " ");
					for (String AC : getLoadedAntiCheatsFile().getConfigurationSection("loaded-anticheats.multiple-anticheats").getKeys(true)) 
					{
						if (PT.isSameStringButDiffOrder(Carded, AC.replace("_", " "))) 
						{
							Carded = AC;
							break;
						}
					}
				}
				Carded = Carded.replace(" ", "_").toLowerCase();
				if (getLoadedAntiCheatsFile().get("loaded-anticheats.multiple-anticheats." + Carded) == null) 
				{
					if (Sudo != null)
						Sudo.sendMessage(RebugMessage + Carded + " was not Found in the Loaded AntiCheats.yml file!");
					else
						user.sendMessage(
								Carded + " was not Found in the Loaded AntiCheats.yml file! tell a Owner or Admin!");

					return;
				}
				if (PT.isSameStringButDiffOrder(user.AntiCheat, Carded))
				{
					if (getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.use-custom-sound"))
						getNMS().PlaySound(user.getPlayer(), getLoadedAntiCheatsFile().getString("loaded-anticheats.custom-sound"), 1, 1);
					else
						getNMS().PlayAntiCheatSelectedSound(user.getPlayer());
					
					return;
				}
			}
			Debug(Sudo == null ? user : Sudo, "Incoming Card= " + Carded + "!");
			
			user.AntiCheat = Carded.replace("_", " ");
			user.SelectedAntiCheats = size;
			user.NumberIDs = getLoadedAntiCheatsFile().getInt("loaded-anticheats.multiple-anticheats." + Carded) + "";
			UpdateUserPerms(user.getPlayer(), user.NumberIDs);
			user.ResetColorAC = true;
			if (Sudo != null) 
			{
				Sudo.sendMessage(RebugMessage + "Successfully Changed " + user.getName() + "'s AntiCheats to: "
						+ user.AntiCheat);
				user.sendMessage((Sudo instanceof Player ? ((Player) Sudo).getName() : "a Owner or Admin")
						+ " Manually Set Your AntiCheats to: " + user.AntiCheat + "!");
			}
			else
				user.sendMessage("You selected: " + user.AntiCheat);

			
			UpdateScoreBoard(user, ChatColor.DARK_RED + "AC " + ChatColor.AQUA + "Multi", 10, "anticheat");
			if (getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.use-custom-sound"))
				getNMS().PlaySound(user.getPlayer(), getLoadedAntiCheatsFile().getString("loaded-anticheats.custom-sound"), 1, 1);
			else
				getNMS().PlayAntiCheatSelectedSound(user.getPlayer());
			
			if (getLoadedAntiCheatsFile().get ("log-anticheat-switches-to-console") != null && getLoadedAntiCheatsFile().getBoolean("log-anticheat-switches-to-console"))
				Log(Level.INFO, "" + getLoadedAntiCheatsFile().getString("switched-anticheat-message").replace("%player%", user.getName()).replace("%user%", user.getName()).replace("%ac%", user.AntiCheat).replace("%anticheat%", user.AntiCheat));
				
			if (!PT.isStringNull(Kickables))
			{
				String[] Split = StringUtils.split(Kickables);
				if (Split.length > 1)
				{
					Kickables = "";
					for (int i = 0; i < Split.length; i++) 
					{
						Kickables += CustomChatColor
								.stripColor(CustomChatColor.translateAlternateColorCodes('&',
										getLoadedAntiCheatsFile().getString(
												"loaded-anticheats." + Split[i].toLowerCase() + ".main-name"), true, Split[i]))
								+ (i < Split.length - 1 ? " & " : "");
					}
				}
				else
					Kickables = CustomChatColor
							.stripColor(CustomChatColor.translateAlternateColorCodes('&', getLoadedAntiCheatsFile()
									.getString("loaded-anticheats." + Split[0].toLowerCase() + ".main-name"), true, Split[0]));

				getPT().KickPlayer(user.getPlayer(), RebugMessage + "Reconnect required in order to make sure "
						+ Kickables + ChatColor.DARK_GRAY + " work properly!");
				return;
			}
			return;
		}

		if (PT.isStringNull(ItemName[0])) 
		{
			if (Sudo != null)
				Sudo.sendMessage(
						RebugMessage + "Failed to change user's AntiCheat, the given anticheat name string was null!");

			user.sendMessage("Failed to change AntiCheat due to the given AntiCheat name string was null!");
			return;
		}

		if (user.AntiCheat.equalsIgnoreCase(ItemName[0])) 
		{
			if (Sudo != null)
				Sudo.sendMessage(RebugMessage + "Unable to change " + user.getName() + "'s AntiCheat due to "
						+ user.getName() + " already having this AntiCheat Selected!");
			
			if (getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.use-custom-sound"))
				getNMS().PlaySound(user.getPlayer(), getLoadedAntiCheatsFile().getString("loaded-anticheats.custom-sound"), 1, 1);
			else
				getNMS().PlayAntiCheatSelectedSound(user.getPlayer());
			
			if (user.AutoCloseAntiCheatMenu)
				user.getPlayer().closeInventory();

			return;
		}
		if (!ItemName[0].equalsIgnoreCase("vanilla") && getLoadedAntiCheatsFile().get("loaded-anticheats." + ItemName[0].toLowerCase()) == null)
		{
			if (Sudo != null)
				Sudo.sendMessage(RebugMessage + "AntiCheat " + "\"" + ItemName[0] + "\" was not found!");
			else
				user.sendMessage("AntiCheat " + "\"" + ItemName[0] + "\" was not found!");

			return;
		}
		if (ItemName[0].equalsIgnoreCase("Vanilla"))
		{
			if (Sudo == null && !user.hasPermission("me.killstorm103.rebug.user.select_vanilla") && !hasAdminPerms(user)) 
			{
				user.sendMessage("You don't have permission to use Vanilla!");
				if (user.AutoCloseAntiCheatMenu)
					user.getPlayer().closeInventory();

				return;
			}
			if (!getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.vanilla.enabled")) 
			{
				if (Sudo != null)
					Sudo.sendMessage(RebugMessage + ChatColor.YELLOW + "Warning " + ChatColor.DARK_GRAY + "You are making " + user.getName() + " Select Vanilla as their AntiCheat which is Disabled!");
				else 
				{
					user.sendMessage("Vanilla is Disabled!");
					if (user.AutoCloseAntiCheatMenu)
						user.getPlayer().closeInventory();

					return;
				}
			}
		}
		if (!ItemName[0].equalsIgnoreCase("Vanilla"))
		{
			if (!getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + ItemName[0].toLowerCase() + ".enabled"))
			{
				if (Sudo != null)
					Sudo.sendMessage(RebugMessage + ChatColor.YELLOW + "Warning " + ChatColor.DARK_GRAY
							+ "You are making " + user.getName() + "'s Select "
							+ CustomChatColor.stripColor(CustomChatColor.translateAlternateColorCodes('&',
									Rebug.getINSTANCE().getLoadedAntiCheatsFile().getString(
											"loaded-anticheats." + ItemName[0].toLowerCase() + ".main-name"), true, ItemName[0]))
							+ " which is a Disabled AntiCheat");

				else 
				{
					user.sendMessage("AntiCheat: " + ItemName[0] + " is Disabled!");
					if (user.AutoCloseAntiCheatMenu)
						user.getPlayer().closeInventory();

					return;
				}
			}
			if (Sudo == null
					&& getLoadedAntiCheatsFile()
							.getBoolean("loaded-anticheats." + ItemName[0].toLowerCase() + ".permission-to-use")
					&& !user.hasPermission(
							"me.killstorm103.rebug.user.select_anticheat_" + ItemName[0].toLowerCase())) {
				user.sendMessage("Sorry but permission-to-use is Enabled for this AntiCheat!");
				user.sendMessage("You need the permission:");
				user.sendMessage("me.killstorm103.rebug.user.select_anticheat_" + ItemName[0].toLowerCase());
				if (user.AutoCloseAntiCheatMenu)
					user.getPlayer().closeInventory();

				return;
			}
			if (Sudo == null && getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + ItemName[0].toLowerCase() + ".requires-combining"))
			{
				if (!getLoadedAntiCheatsFile().getBoolean("bypass-requires-combining-permission") || !user.hasPermission("me.killstorm103.rebug.user.bypass_anticheatcombine") && !hasAdminPerms(user))
				{
					user.sendMessage("Sorry but you have to combine this AntiCheat With another AntiCheat!");
					user.sendMessage("do /ac <anticheat> <anticheat> (....)");
					return;
				}
			}
		}
		user.AntiCheat = ItemName[0].equalsIgnoreCase("vanilla") ? "Vanilla" :
		ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', getLoadedAntiCheatsFile().getString("loaded-anticheats." + ItemName[0].toLowerCase() + ".main-name")));
		user.SelectedAntiCheats = user.AntiCheat.equalsIgnoreCase("Vanilla") ? 0 : 1;
		user.NumberIDs = "";
		UpdateUserPerms(user.getPlayer(), user.AntiCheat);
		user.ResetColorAC = true;
		if (user.SelectedAntiCheats > 0 && getLoadedAntiCheatsFile().get ("loaded-anticheats." + user.AntiCheat.toLowerCase() + ".api") != null &&
		getLoadedAntiCheatsFile().getBoolean ("loaded-anticheats." + user.AntiCheat.toLowerCase() + ".api"))
		{
			try
			{
				ApiProvider.onApi (user.getPlayer(), user.AntiCheat);
			}
			catch (Exception e) 
			{
				if (debug)
					e.printStackTrace();
			}
		}
		String NewAC = user.getColoredAntiCheat(), striped = ChatColor.stripColor(user.AntiCheat).toLowerCase();
		if (getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + striped + ".has-short-name"))
			NewAC = NewAC.replace(NewAC, ChatColor.translateAlternateColorCodes('&', getLoadedAntiCheatsFile().getString("loaded-anticheats." + striped + ".short-name")) + ChatColor.RESET);

		UpdateScoreBoard(user, ChatColor.DARK_RED + "AC " + NewAC, 10, "anticheat");
		String AC = ChatColor.stripColor(NewAC);
		if (Sudo != null) {
			Sudo.sendMessage(RebugMessage + "Successfully Changed " + user.getName() + "'s AntiCheat to: "
					+ ChatColor.stripColor(user.getColoredAntiCheat()));
			user.sendMessage((Sudo instanceof Player ? ((Player) Sudo).getName() : "a Owner or Admin")
					+ " Manually Set Your AntiCheat to: " + ChatColor.stripColor(user.getColoredAntiCheat()) + "!");
		} 
		else 
		{
			if (item != null)
				user.sendMessage("You selected: " + AC
						+ (AC.equalsIgnoreCase("vanilla") || AC.equalsIgnoreCase("nocheatplus")
								|| AC.equalsIgnoreCase("NCP")
										? ""
										: " " + ChatColor.stripColor(PT
												.SubString(item.getItemMeta().getLore().get(3), 10,
														item.getItemMeta().getLore().get(3).length())
												.replace(" ", ""))));
			else
				user.sendMessage("You selected: " + AC);
		}
		if (getLoadedAntiCheatsFile().get ("loaded-anticheats.use-custom-sound") != null && getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.use-custom-sound"))
			getNMS().PlaySound(user.getPlayer(), getLoadedAntiCheatsFile().getString("loaded-anticheats.custom-sound"), 1, 1);
		else
			getNMS().PlayAntiCheatSelectedSound(user.getPlayer());
		
		if (getLoadedAntiCheatsFile().get ("log-anticheat-switches-to-console") != null && getLoadedAntiCheatsFile().getBoolean("log-anticheat-switches-to-console"))
			Log(Level.INFO, "" + getLoadedAntiCheatsFile().getString("switched-anticheat-message").replace("%player%", user.getName()).replace("%user%", user.getName()).replace("%ac%", user.AntiCheat).replace("%anticheat%", user.AntiCheat));
		
		if (user.AutoCloseAntiCheatMenu)
			user.getPlayer().closeInventory();
		
		if (getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + user.AntiCheat.toLowerCase() + ".requires-reconnect"))
			getPT().KickPlayer(user.getPlayer(), RebugMessage + "Reconnect required in order to make sure " + user.getColoredAntiCheat() + ChatColor.DARK_GRAY + " works properly!");
	}

	public int getAccessToCommandsNumber (CommandSender sender)
	{
		int access = getCommands().size();
		if (sender instanceof Player) 
		{
			Player player = (Player) sender;
			if (!player.hasPermission(AllCommands_Permission) && !hasAdminPerms(player)) 
			{
				for (me.killstorm103.Rebug.Shared.Command.Command c : getCommands())
				{
					if (!player.hasPermission(c.getPermission()))
						access --;
				}
			}
			return access;
		}
		for (me.killstorm103.Rebug.Shared.Command.Command c : getCommands())
		{
			if (c.getType() == Types.Player)
				access --;
		}
		return access;
	}
}
