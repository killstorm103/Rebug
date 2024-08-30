package me.killstorm103.Rebug.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.killstorm103.Rebug.Commands.BackCMD;
import me.killstorm103.Rebug.Commands.ClientCMD;
import me.killstorm103.Rebug.Commands.ConsoleMessage;
import me.killstorm103.Rebug.Commands.Credits;
import me.killstorm103.Rebug.Commands.DamageCMD;
import me.killstorm103.Rebug.Commands.DebugRebugCMD;
import me.killstorm103.Rebug.Commands.Discord;
import me.killstorm103.Rebug.Commands.Enchant;
import me.killstorm103.Rebug.Commands.FeedCMD;
import me.killstorm103.Rebug.Commands.GetIP;
import me.killstorm103.Rebug.Commands.GetUUID;
import me.killstorm103.Rebug.Commands.HealAndFeedCMD;
import me.killstorm103.Rebug.Commands.HealCMD;
import me.killstorm103.Rebug.Commands.Help;
import me.killstorm103.Rebug.Commands.InvSeeCMD;
import me.killstorm103.Rebug.Commands.Menu;
import me.killstorm103.Rebug.Commands.NoBreak;
import me.killstorm103.Rebug.Commands.PacketDebugger;
import me.killstorm103.Rebug.Commands.PlayerInfoCMD;
import me.killstorm103.Rebug.Commands.PotionCommand;
import me.killstorm103.Rebug.Commands.Reload;
import me.killstorm103.Rebug.Commands.Repair;
import me.killstorm103.Rebug.Commands.SetHealthCMD;
import me.killstorm103.Rebug.Commands.SetUserAntiCheat;
import me.killstorm103.Rebug.Commands.SlashFly;
import me.killstorm103.Rebug.Commands.SpawnCMD;
import me.killstorm103.Rebug.Commands.Test;
import me.killstorm103.Rebug.Commands.Unblock;
import me.killstorm103.Rebug.Commands.VClip;
import me.killstorm103.Rebug.Commands.Version;
import me.killstorm103.Rebug.Commands.getInfo;
import me.killstorm103.Rebug.Commands.Handler.EventCommandPreProcess;
import me.killstorm103.Rebug.Commands.ShortCuts.ShortCutBasic;
import me.killstorm103.Rebug.Events.EventBlockHandling;
import me.killstorm103.Rebug.Events.EventHandlePlayerSpawn;
import me.killstorm103.Rebug.Events.EventMenus;
import me.killstorm103.Rebug.Events.EventPlayer;
import me.killstorm103.Rebug.Events.EventWeather;
import me.killstorm103.Rebug.PacketEvents.EventPackets;
import me.killstorm103.Rebug.PacketEvents.EventTestPacket;
import me.killstorm103.Rebug.PluginHooks.PlaceholderapiHook;
import me.killstorm103.Rebug.Tasks.OneSecondUpdater_Task;
import me.killstorm103.Rebug.Tasks.ResetScaffoldTestArea;
import me.killstorm103.Rebug.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class Rebug extends JavaPlugin implements Listener
{
	public static Map<UUID, Integer> PacketDebuggerPlayers = new HashMap<>();
	public static boolean debug = false, KickOnReloadConfig = false, debugOpOnly = true, PrivatePerPlayerAlerts = true, AutoRefillBlocks = true, Reloading = false;
	public static final String AllCommands_Permission = "me.killstorm103.rebug.commands.*";
	private static Rebug INSTANCE;
	private final ArrayList<me.killstorm103.Rebug.Main.Command> commands = new ArrayList<me.killstorm103.Rebug.Main.Command>();
	public static final HashMap<UUID, User> USERS = new HashMap<>();
	public static final String RebugMessage = ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + "REBUG " + ChatColor.DARK_GRAY + ">> ";
	public static String APIVersion = "1.8";
	
	public static User getUser(Player player) 
	{
		if (player == null) return null;
		
        for (User user : USERS.values()) 
        {
        	if (user.getPlayer() != player && !user.getPlayer().getUniqueId().equals(player.getUniqueId())) continue;
        	
        	return user;
        }
        return null;
    }
    public static void Debug (CommandSender sender, String msg)
    {
    	if (!debug || PT.isStringNull(msg)) return;
    	
    	if (sender != null && sender instanceof Player)
    	{
    		Player player = (Player) sender;
    		if (hasAdminPerms(player) || !debugOpOnly)
        		player.sendMessage(RebugMessage + msg); 
    	}
    	Bukkit.getServer().getConsoleSender().sendMessage(RebugMessage + msg);
    }
    public static void Debug (User user, String msg)
    {
    	Debug (user == null ? null : user.getPlayer(), msg);
    }
	public static String StartOfPermission ()
	{
		return "me.killstorm103.rebug.";
	}
	public static final String PluginName ()
	{
		return "Rebug";
	}
	public static final String getAuthor ()
	{
		return "killstorm103";
	}
	public static final String PluginEdition ()
	{
		return getINSTANCE().getDescription().getDepend().get(0) + " Edition";
	}
	public static final String PluginVersion ()
	{
		return getINSTANCE().getDescription().getVersion();
	}
	public boolean DevMode ()
	{
		return true;
	}
	public BukkitTask ResetScaffoldTask, EverySecondUpdaterTask;

    public File getConfigFile()
    {
		return ConfigFile;
	}
    public FileConfiguration getDefaultPlayerSettingsConfigFile ()
    {
    	return DefaultPlayerSettingsConfig;
    }

	private FileConfiguration anticheatConfig, ItemsConfig, DefaultPlayerSettingsConfig;
	private File folder, LoadedAntiCheatsConfigFile, LoadedItemsConfigFile, ConfigFile, DefaultPlayerSettingsFile;

	public void initFolder() 
    {
        folder = new File(this.getDataFolder(), "player-data");
        if (!folder.exists())
            folder.mkdirs();
    }
    private File getPlayerFile(Player player) 
    {
        return new File(folder, player.getUniqueId().toString() + ".yml");
    }
    
    private FileConfiguration getPlayerConfig(Player p) 
    {
        File playerFile = getPlayerFile(p);
        if (!playerFile.exists())
            return null;

        YamlConfiguration playerConfig = new YamlConfiguration();
        try 
        {
            playerConfig.load(playerFile);
            return playerConfig;
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private void save(Player p, FileConfiguration config) 
    {
        File playerFile = getPlayerFile(p);
        try {
            config.save(playerFile);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
	public void removeConfigs(Player p, int mode)
    {
        File playerFile = getPlayerFile(p);
        if (mode == 0 && playerFile.exists() && Config.ShouldDeletePlayerConfigAfterLoading())
            playerFile.delete();
    }
	public FileConfiguration getPlayerConfigFile (Player p)
	{
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
        user.Damage_Resistance = playerConfig.getConfigurationSection("Player Settings").getBoolean("Damage Resistance");
        user.Fire_Resistance = playerConfig.getConfigurationSection("Player Settings").getBoolean("Fire Resistance");
        user.Hunger = playerConfig.getConfigurationSection("Player Settings").getBoolean("Hunger");
        user.PotionEffects = playerConfig.getConfigurationSection("Player Settings").getBoolean("Potion Effects");
        user.AutoRefillBlocks = playerConfig.getConfigurationSection("Player Settings").getBoolean("Auto Refill Blocks");
        user.Infinite_Blocks = playerConfig.getConfigurationSection("Player Settings").getBoolean("Infinite Blocks");
        user.HideOnlinePlayers = playerConfig.getConfigurationSection("Player Settings").getBoolean("HideOnlinePlayers");
        user.ProximityPlayerHider = playerConfig.getConfigurationSection("Player Settings").getBoolean("Proximity Player Hider");
        user.AutoCloseAntiCheatMenu = playerConfig.getConfigurationSection("Player Settings").getBoolean("Auto Close AntiCheat Menu");
        user.AllowDirectMessages = playerConfig.getConfigurationSection("Player Settings").getBoolean("Allow Direct Messages");
        user.AllowMentions = playerConfig.getConfigurationSection("Player Settings").getBoolean("Allow Mentions");
        
        
        user.NumberIDs = playerConfig.getConfigurationSection("Player Settings").getString("AntiCheat Number IDs");
        user.SelectedAntiCheats = playerConfig.getConfigurationSection("Player Settings").getInt("Amount Of Selected AntiCheats");
        if (user.SelectedAntiCheats < 2)
        {
        	user.AntiCheat = hasAdminPerms(user) || getLoadedAntiCheatsFile().getBoolean("bypass-permission-enabled") && user.hasPermission("me.killstorm103.rebug.user.bypass_force_default_anticheat") ?
        	    	playerConfig.getConfigurationSection("Player Settings").getString("AntiCheat") : 
        	    	getLoadedAntiCheatsFile().getBoolean("force-default-anticheat") ? user.AntiCheat : 
        	    	playerConfig.getConfigurationSection("Player Settings").getString("AntiCheat");
        	    	
        	    	if (!user.AntiCheat.equalsIgnoreCase("Vanilla"))
        	    	{
        	    		Plugin plugin = Bukkit.getPluginManager().getPlugin(user.AntiCheat);
        	    		if (getLoadedAntiCheatsFile().get ("loaded-anticheats." + user.AntiCheat.toLowerCase()) == null || !getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + user.AntiCheat.toLowerCase() + ".enabled") 
        	    		|| plugin == null || !plugin.isEnabled())
        	    		{
        	    			user.AntiCheat = Rebug.getINSTANCE().getLoadedAntiCheatsFile().getString("default-anticheat");
        	        		user.AntiCheat = PT.isStringNull(user.AntiCheat) ? "Vanilla" : user.AntiCheat;
        	    		}
        	    	}
        }
        else
        	user.AntiCheat = playerConfig.getConfigurationSection("Player Settings").getString("AntiCheat");
        	
    	user.ShowFlags = playerConfig.getConfigurationSection("Player Settings").getBoolean("Show Flags");
    	user.ShowPunishes = playerConfig.getConfigurationSection("Player Settings").getBoolean("Show Punishes");
    	user.ShowSetbacks = playerConfig.getConfigurationSection("Player Settings").getBoolean("Show Setbacks");
    	user.AntiCheatKick = playerConfig.getConfigurationSection("Player Settings").getBoolean("AC Kick");
    	user.Vanilla1_8FlyCheck = playerConfig.getConfigurationSection("Player Settings").getBoolean("Vanilla Fly 1_8_Plus");
    	user.Vanilla1_9FlyCheck = playerConfig.getConfigurationSection("Player Settings").getBoolean("Vanilla Fly 1_9Plus");
    	user.NotifyFlyingKick1_8 = playerConfig.getConfigurationSection("Player Settings").getBoolean("Notify Vanilla Fly Kick 1_8");
    	user.NotifyFlyingKick1_9 = playerConfig.getConfigurationSection("Player Settings").getBoolean("Notify Vanilla Fly Kick 1_9");
    	user.potionlevel = playerConfig.getConfigurationSection("Player Settings").getInt("Potion Setting Level");
    	user.potion_effect_seconds = playerConfig.getConfigurationSection("Player Settings").getInt("Potion Setting Timer");
    	int max_timer = getConfig().getInt("potion-settings-max-timer"), max_level = getConfig().getInt("potion-settings-max-level");
    	user.potion_effect_seconds = user.potion_effect_seconds < 1 ? 1 : user.potion_effect_seconds > max_timer ? max_timer : user.potion_effect_seconds;
    	user.potionlevel = user.potionlevel < 1 ? 1 : user.potionlevel > max_level ? max_level : user.potionlevel;
    	user.Yapper_Message_Count = playerConfig.getConfigurationSection("Player Settings").getInt("Yapper");
    	user.ShowS08Alert = playerConfig.getConfigurationSection("Player Settings").getBoolean("S08 Alerts");
    	user.ClientCommandChecker = playerConfig.getConfigurationSection("Player Settings").getBoolean("Client Command Checker");
    	
    	// PacketDebugger Settings
    	user.FlyingPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Flying");
        user.PositionPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Position");
        user.PositionLookPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("PositionLook");
        user.LookPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Look");
        user.ArmAnimationPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Arm Animation");
        user.HeldItemSlotPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Held Item Slot");
        user.DiggingPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Digging");
        user.BlockPlacePacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Block Place");
        user.EntityActionPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Entity Action");
        user.CloseWindowPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Close Window");
        user.ClickWindowPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Click Window");
        user.SettingsPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Client Settings");
        user.StatusPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Client Status");
        user.AbilitiesPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Abilities");
        user.KeepAlivePacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Keep Alive");
        user.TransactionPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Transaction");
        user.SpectatePacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Spectate");
        user.SteerVehiclePacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Steer Vehicle");
        user.CustomPayLoadPacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Custom PayLoad");
        user.TabCompletePacket = playerConfig.getConfigurationSection("Packet Debugger Settings").getBoolean("Tab Complete");
    	removeConfigs(p, 0);
    }
    public void savePlayer (Player p) 
    {
    	YamlConfiguration playerConfig = new YamlConfiguration();
        User user = getUser(p);
        if (user == null) return;
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
		playerConfig.getConfigurationSection("Player Settings").set("Proximity Player Hider", user.ProximityPlayerHider);
		playerConfig.getConfigurationSection("Player Settings").set("Auto Close AntiCheat Menu", user.AutoCloseAntiCheatMenu);
		playerConfig.getConfigurationSection("Player Settings").set("Allow Direct Messages", user.AllowDirectMessages);
		playerConfig.getConfigurationSection("Player Settings").set("Allow Mentions", user.AllowMentions);
		String AntiCheat = ChatColor.translateAlternateColorCodes('&', user.AntiCheat);
		playerConfig.getConfigurationSection("Player Settings").set("AntiCheat", ChatColor.stripColor(AntiCheat));
		playerConfig.getConfigurationSection("Player Settings").set("Amount Of Selected AntiCheats", user.SelectedAntiCheats);
		playerConfig.getConfigurationSection("Player Settings").set("AntiCheat Number IDs", user.NumberIDs);
		
		playerConfig.getConfigurationSection("Player Settings").set("Show Flags", user.ShowFlags);
		playerConfig.getConfigurationSection("Player Settings").set("Show Punishes", user.ShowPunishes);
		playerConfig.getConfigurationSection("Player Settings").set("Show Setbacks", user.ShowSetbacks);
		playerConfig.getConfigurationSection("Player Settings").set("AC Kick", user.AntiCheatKick);
		playerConfig.getConfigurationSection("Player Settings").set("Vanilla Fly 1_8_Plus", user.Vanilla1_8FlyCheck);
		playerConfig.getConfigurationSection("Player Settings").set("Vanilla Fly 1_9Plus", user.Vanilla1_9FlyCheck);
		playerConfig.getConfigurationSection("Player Settings").set("Notify Vanilla Fly Kick 1_8", user.NotifyFlyingKick1_8);
		playerConfig.getConfigurationSection("Player Settings").set("Notify Vanilla Fly Kick 1_9", user.NotifyFlyingKick1_9);
		playerConfig.getConfigurationSection("Player Settings").set("Potion Setting Level", user.potionlevel);
		playerConfig.getConfigurationSection("Player Settings").set("Potion Setting Timer", user.potion_effect_seconds);
		playerConfig.getConfigurationSection("Player Settings").set("Yapper", user.Yapper_Message_Count);
		playerConfig.getConfigurationSection("Player Settings").set("S08 Alerts", user.ShowS08Alert);
		playerConfig.getConfigurationSection("Player Settings").set("Client Command Checker", user.ClientCommandChecker);
		
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
        playerConfig.getConfigurationSection("Packet Debugger Settings").set("Custom PayLoad", user.CustomPayLoadPacket);
        playerConfig.getConfigurationSection("Packet Debugger Settings").set("Tab Complete", user.TabCompletePacket);
		
        save(p, playerConfig);
    }
    private final static List<String> list_configs = new ArrayList<>();
    static 
    {
    	list_configs.clear();
    	list_configs.add("config");
    	list_configs.add("loaded anticheats");
    	list_configs.add("loaded items");
    	list_configs.add("default player settings");
    }
    public FileConfiguration getLoadedAntiCheatsFile ()
    {
    	return anticheatConfig;
    }
    public FileConfiguration getLoadedItemsFile ()
    {
    	return ItemsConfig;
    }
    public void Reload_Configs (CommandSender sender)
    {
    	Reloading = true;
    	if (KickOnReloadConfig)
		{
			for (Player players : Bukkit.getOnlinePlayers())
				PT.KickPlayer(players, ChatColor.DARK_RED + "Rejoin reloading Rebug's Config!");
		}
    	boolean noerror;
    	try
		{
    		getConfig().load(getConfigFile());
    		getConfig().save(getConfigFile());
    		anticheatConfig = YamlConfiguration.loadConfiguration(LoadedAntiCheatsConfigFile);
        	ItemsConfig = YamlConfiguration.loadConfiguration(LoadedItemsConfigFile);
        	DefaultPlayerSettingsConfig = YamlConfiguration.loadConfiguration(DefaultPlayerSettingsFile);
        	anticheats.clear();
        	ItemsAndMenusUtils.INSTANCE.lore.clear();
        	ItemsAndMenusUtils.INSTANCE.AntiCheatMenu = ItemsAndMenusUtils.INSTANCE.ItemPickerMenu = null;
    		if (sender instanceof Player)
    			sender.sendMessage(RebugMessage + "Successfully Reloaded Config!");
    		
    		Bukkit.getConsoleSender().sendMessage(RebugMessage + "Successfully Reloaded Config!");
    		noerror = true;
		}
		catch (Exception e) 
		{
			noerror = false;
			if (sender instanceof Player)
				sender.sendMessage(RebugMessage + "Failed to Reload Config!");
			
    		Bukkit.getConsoleSender().sendMessage(RebugMessage + "Failed to Reload Config!");
			e.printStackTrace();
		}
    	if (noerror)
    	{
    		if (getLoadedAntiCheatsFile().getBoolean("Disabled-AntiCheat.disabled"))
			{
				for (Player p : Bukkit.getOnlinePlayers())
				{
					p.closeInventory ();
					User used = getUser(p);
					if (used != null && !used.AntiCheat.equalsIgnoreCase("Vanilla") && (Rebug.getINSTANCE().getLoadedAntiCheatsFile().get ("loaded-anticheats." + used.AntiCheat.toLowerCase()) == null || !Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + used.AntiCheat.toLowerCase() + ".enabled")))
					{
						if (getLoadedAntiCheatsFile().getBoolean("Disabled-AntiCheat.kick-on-Invalid-anticheat"))
							PT.KickPlayer(used.getPlayer(), RebugMessage + used.AntiCheat + " was Disabled! - Invalid AntiCheat upon Config Reload");
						
						else
						{
							used.sendMessage(used.AntiCheat + " was Disabled! - Invalid AntiCheat upon Config Reload");
							used.AntiCheat = getLoadedAntiCheatsFile().getString("default-anticheat");
							used.AntiCheat = PT.isStringNull(used.AntiCheat) ? "Vanilla" : used.AntiCheat;
							String NewAC = used.getColoredAntiCheat(), striped = ChatColor.stripColor(used.AntiCheat).toLowerCase();
					   		if (getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + striped + ".has-short-name"))
					   			NewAC = NewAC.replace(NewAC, ChatColor.translateAlternateColorCodes('&', getLoadedAntiCheatsFile().getString("loaded-anticheats." + striped + ".short-name")) + ChatColor.RESET);
					
					   		UpdateUserPerms (used, used.AntiCheat);
					   		UpdateScoreBoard(used, ChatColor.DARK_RED + "AC " + NewAC, 10);
						}
					}
				}
				getLoadedAntiCheatsFile().set("Disabled-AntiCheat.disabled", false);
				try
				{
					getLoadedAntiCheatsFile().save(LoadedAntiCheatsConfigFile);
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
    	}
    	Reloading = false;
    }
    private void createCustomConfig (String con) 
    {
    	if (PT.isStringNull(con)) return;
    	
    	if (con.equalsIgnoreCase("config"))
    	{
    		ConfigFile = new File(getDataFolder(), "config.yml");
            if (!ConfigFile.exists())
            {
            	ConfigFile.getParentFile().mkdirs();
                saveResource("config.yml", false);
             }
            YamlConfiguration.loadConfiguration(ConfigFile);
    	}
    	if (con.equalsIgnoreCase("default player settings"))
    	{
    		DefaultPlayerSettingsFile = new File(getDataFolder(), "Default Player Settings.yml");
    		if (!DefaultPlayerSettingsFile.exists())
    		{
    			DefaultPlayerSettingsFile.getParentFile().mkdirs();
    			saveResource("Default Player Settings.yml", false);
    		}
    		
    		DefaultPlayerSettingsConfig = YamlConfiguration.loadConfiguration(DefaultPlayerSettingsFile);
    	}
    	if (con.equalsIgnoreCase("loaded items"))
    	{
    		LoadedItemsConfigFile = new File(getDataFolder(), "Items.yml");
            if (!LoadedItemsConfigFile.exists())
            {
            	LoadedItemsConfigFile.getParentFile().mkdirs();
                saveResource("Items.yml", false);
            }
            ItemsConfig = YamlConfiguration.loadConfiguration(LoadedItemsConfigFile);
    	}
    	if (con.equalsIgnoreCase("loaded anticheats"))
    	{
    		LoadedAntiCheatsConfigFile = new File(getDataFolder(), "Loaded AntiCheats.yml");
            if (!LoadedAntiCheatsConfigFile.exists())
            {
            	LoadedAntiCheatsConfigFile.getParentFile().mkdirs();
                saveResource("Loaded AntiCheats.yml", false);
            }
            anticheatConfig = YamlConfiguration.loadConfiguration(LoadedAntiCheatsConfigFile);
    	}
    }
    public static final Map<Plugin, String> anticheats = new HashMap<>();
    private final String TickMark = "✔️";
    public final String getTickMark ()
    {
    	final String New = TickMark.substring(0, 1);
    	return New;
    }
   	public void addToScoreBoard (Player player)
   	{
   		if (!Config.RebugScoreBoard()) return;
   		
   		
   		User user = getUser(player);
   		if (user == null || Netherboard.instance().getBoard(player) != null) return;
   		
   		final String color = ChatColor.DARK_RED.toString();
   		String AC = user.getColoredAntiCheat(), strip = ChatColor.stripColor(AC).toLowerCase();
   		if (getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + strip + ".has-short-name"))
			AC = AC.replace(AC, ChatColor.translateAlternateColorCodes('&', getLoadedAntiCheatsFile().getString("loaded-anticheats." + strip + ".short-name")) + ChatColor.RESET);
   		
   		BPlayerBoard board = Netherboard.instance().createBoard(user.getPlayer(), ChatColor.translateAlternateColorCodes('&', ChatColor.DARK_GRAY + "| " + Config.ScoreboardTitle() + " " + ChatColor.DARK_GRAY + "|"));
   		board.set("  " + ChatColor.DARK_GRAY + "| §cTest §2Server   " + ChatColor.DARK_GRAY + "|", 12);
   		board.set(ChatColor.RESET + " ", 11);
   		board.set(color + "AC " + AC, 10);
   		board.set(color + "Client " + ChatColor.WHITE + user.getVersion_Short(), 9);
   		board.set(color + "CPS " + ChatColor.WHITE + user.ClicksPerSecond, 8);
   		board.set(color + "BPS (XZ) " + ChatColor.WHITE + "0", 7);
   		board.set(color + "BPS (Y) " + ChatColor.WHITE + "0", 6);
   		board.set(color + "PPS " + ChatColor.WHITE + user.sendPacketCounts + "/in " + user.receivePacketCounts + "/out", 5);
   		board.set(color + "TB InDev", 4);
   		board.set(color + "Blocking " + ChatColor.RED + ChatColor.BOLD.toString() + "X", 3);
   		board.set(color + "Sprinting " + ChatColor.RED + ChatColor.BOLD.toString() + "X", 2);
   		board.set(color + "Sneaking " + ChatColor.RED + ChatColor.BOLD.toString() + "X", 1);
   		board.set(color + "OnGround " + ChatColor.RED + ChatColor.BOLD.toString() + "X", 0);
   		user.getPlayer().setScoreboard(board.getScoreboard());
   	}
   	public void UpdateScoreBoard (User user, String text, int score)
   	{
   		if (!Config.RebugScoreBoard() || user == null || !user.getPlayer().isOnline() || Reloading) return;
   		
   		BPlayerBoard board = Netherboard.instance().getBoard(user.getPlayer());
   		if (board == null) return;
   		
   		if (!PT.isStringNull(board.get(score)))
   			board.remove(score);
   		
   		board.set(text, score);
   	}
   	private final ArrayList<UUID> isAlertsEnabled = new ArrayList<>();
   	public final ArrayList<UUID> isAllowedToDebugPackets = new ArrayList<>();
   	public static final ArrayList<UUID> KickList = new ArrayList<>();
   	public final Map<UUID, String> ClientBranded = new HashMap<>(), ClientRegistered = new HashMap<>();
   	
   	@EventHandler (priority = EventPriority.HIGHEST)
   	public void onKick (PlayerKickEvent e)
   	{
   		if (!getConfig().getBoolean("are-users-allowed-to-disable-kicks")) 
   		{
   			KickList.clear();
   			return;
   		}
   		String reason = e.getReason();
   		Player player = e.getPlayer();
   		
   		if (reason.equalsIgnoreCase("Kicked by an operator.") && !KickList.contains(player.getUniqueId()))
   			KickList.add(player.getUniqueId());
	   		
   		if (!KickList.isEmpty())
   		{
   			if (KickList.contains(player.getUniqueId()))
   			{
   				isAlertsEnabled.remove(player.getUniqueId());
   				KickList.remove(player.getUniqueId());
   				return;
   			}
	   		
   			User user = getUser(player);
   			if (user == null) return;
		   		
   			if (!KickList.contains(user.getPlayer().getUniqueId()) && !user.AntiCheatKick)
   				e.setCancelled(true);
   		}
   	}
   	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin (PlayerJoinEvent e) 
	{
   		Player player = e.getPlayer();
		Rebug.USERS.put(player.getUniqueId(), new User(player));
		User user = Rebug.getUser(player);
		restorePlayer(user.getPlayer());
		UpdateUserPerms(user, user.SelectedAntiCheats > 1 ? user.NumberIDs : user.AntiCheat);
		addToScoreBoard(user.getPlayer());
		if (user.Fire_Resistance)
			user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
		else
			user.getPlayer().removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
		
		if (user.Damage_Resistance)
			user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3, true, false));
		else
			user.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		
		if (Config.ShouldForceGameMode() && !hasAdminPerms(user))
			user.getPlayer().setGameMode(GameMode.SURVIVAL);
		
		final String message = getConfig().getString("join-message");
		if (message != null && message.length() > 0 && hasAdminPerms(user))
			user.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%user%", user.getName()).replace("%player%", user.getName())));
		
		Location loc = user.getLocation();
        if ((!user.getPlayer().hasPlayedBefore() || loc.getBlockX() >= 18 && loc.getBlockY() >= 57 && loc.getBlockZ() >= 306 && loc.getBlockX() <= 61 && loc.getBlockY() <= 88 && loc.getBlockZ() <= 330) && getConfig().getBoolean("world-spawn.use-this"))
        {
        	Bukkit.getScheduler().runTask(this, new Runnable()
        	{
				@Override
				public void run() 
				{
					user.getPlayer().teleport(PT.INSTANCE.getSpawn());
				}
			});
        }
        
   		e.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + user.getName());
   		if (!isAlertsEnabled.contains(player.getUniqueId())) 
   		{
   			Bukkit.getScheduler().runTaskLater(this, new Runnable() 
   			{
   				@Override
   				public void run() 
   				{
   					for (String s : Config.getOnJoinCommands())
   					{
   						Bukkit.dispatchCommand (player, s);
   					}
   					isAlertsEnabled.add(player.getUniqueId());
   				}
   			}, 100);
   		}
   		checkPlayer(user);
	} 
   	public void run_server_command (String cmd) 
   	{
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
    }
   	public static List<UUID> lockedList = new ArrayList<>();
   	
   	public void checkPlayer (User user)
   	{
   		if (user == null || !user.ClientCommandChecker) return;
   		
        if (!lockedList.contains(user.getUUID())) 
            lockedList.add(user.getUUID());
        
        String code = user.Keycard;
        if (getConfig().getBoolean("client-command-checker.sendOnce")) 
        {
            int secondLeft = 60;
            user.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("client-command-checker.topString")));
            run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}}]");
            run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}}]");
            run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}}]");
            run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}},{\"text\":\" " + "Remaining" + ": \",\"color\":\"red\",\"bold\":false},{\"text\":\"" + secondLeft + (secondLeft < 2 ? " second" : " seconds") + "\",\"color\":\"yellow\",\"bold\":true}]");
            run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}}]");
            run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}}]");
            run_server_command("tellraw " + user.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}}]");
            user.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("client-command-checker.bottomString")));
        }
        else 
        {
            new BukkitRunnable()
            {
                final Player localPlayer = user.getPlayer();
                int secondLeft = 60;
                public void run() 
                {
                    this.secondLeft --;
                    if (lockedList.contains(this.localPlayer.getUniqueId()))
                    {
                        if (this.localPlayer.isOnline()) 
                        {
                            this.localPlayer.sendMessage(RebugMessage + ChatColor.translateAlternateColorCodes('&', getConfig().getString("client-command-checker.topString")));
                            run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}}]");
                            run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}}]");
                            run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}}]");
                            run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}},{\"text\":\" " + "Remaining" + ": \",\"color\":\"red\",\"bold\":false},{\"text\":\"" + this.secondLeft + (this.secondLeft < 2 ? " second" : " seconds") + "\",\"color\":\"yellow\",\"bold\":true}]");
                            run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}}]");
                            run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}}]");
                            run_server_command("tellraw " + this.localPlayer.getName() + " [\"\",{\"text\":\"" + RebugMessage + "\",\"color\":\"green\",\"bold\":true},{\"text\":\"████████████\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + code + "\"}}]");
                            this.localPlayer.sendMessage(RebugMessage + ChatColor.translateAlternateColorCodes('&', getConfig().getString("client-command-checker.bottomString")));
                        } 
                        else 
                        {
                            this.cancel();
                        	lockedList.remove(user.getUUID());
                        }
                    }
                    else 
                    {
                        this.cancel();
                    }
                }
            }.runTaskTimer(this, 0L, 20L);
        }
        new BukkitRunnable ()
        {
            final Player localPlayer = user.getPlayer();
            public void run() 
            {
            	Player player = Bukkit.getPlayer(this.localPlayer.getUniqueId());
            	if (player == null || !player.isOnline())
            	{
            		cancel();
            		lockedList.remove(player != null ? player.getUniqueId() : localPlayer.getUniqueId());
            	}
            	if (!lockedList.contains(player != null ? player.getUniqueId() : localPlayer.getUniqueId()))
            	{
            		cancel();
            		return;
            	}
            	if (!lockedList.contains(player != null ? player.getUniqueId() : localPlayer.getUniqueId()))
            	{
            		cancel();
            		return;
            	}
            	
                if (lockedList.contains(player != null ? player.getUniqueId() : localPlayer.getUniqueId())) 
                {
                    this.localPlayer.sendMessage(RebugMessage + "You Failed the Client Command Checker!");
                    this.localPlayer.sendMessage(RebugMessage + "To Disable this check use /settings");
                    cancel();
                    lockedList.remove(player != null ? player.getUniqueId() : localPlayer.getUniqueId());
                }
            }
        }.runTaskLater(this, 20 * 60);
    }
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onQuit (PlayerQuitEvent e) 
	{
		Player player = e.getPlayer();
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (p != player)
			{
				User user = getUser(p);
				if (user.CommandTarget != null && user.CommandTarget == player)
				{
					user.getPlayer().closeInventory();
					user.sendMessage("CommandTarget <" + player.getName() + "> Left so closing the Inventory!");
					user.CommandTarget = null;
				}
			}
		}
		
		BPlayerBoard board = Netherboard.instance().getBoard(player);
		if (board != null)
		{
			board.clear();
			board.delete();
		}
		User user = Rebug.getUser(player);
		if (user != null)
		{
			user.Builder = false;
			savePlayer(player);
			Rebug.USERS.remove(user.getPlayer().getUniqueId(), user);
		}
		if (PacketDebuggerPlayers.containsKey(player.getUniqueId()) && !hasAdminPerms(player) && (!player.hasPermission("me.killstorm103.rebug.user.packet_debugger.save") || !player.hasPermission("me.killstorm103.rebug.user.packet_debugger.use")))
			
			PacketDebuggerPlayers.remove(player.getUniqueId(), PacketDebuggerPlayers.get(player.getUniqueId()));
			
		e.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "] " + e.getPlayer().getName());
	}
	@Override
	public void onEnable ()
	{
		INSTANCE = this;
		
		PacketDebuggerPlayers.clear();
		isAlertsEnabled.clear();
		isAllowedToDebugPackets.clear();
		ClientBranded.clear();
		lockedList.clear();
		ClientRegistered.clear ();
		initFolder();
		getConfig().options().copyDefaults(true);
		for (int i = 0; i < list_configs.size(); i ++)
			createCustomConfig(list_configs.get(i));
		
		getServer().getConsoleSender().sendMessage (ChatColor.YELLOW + "Enabling Rebug's commands");
		// ShortCuts
		getCommand("client").setExecutor(new ShortCutBasic());
		getCommand("exploits").setExecutor(new ShortCutBasic());
		getCommand("crashers").setExecutor(new ShortCutBasic());
		getCommand("items").setExecutor(new ShortCutBasic());
		getCommand("enchant").setExecutor(new ShortCutBasic());
		getCommand("settings").setExecutor(new ShortCutBasic());
		getCommand("heal").setExecutor(new ShortCutBasic());
		getCommand("feed").setExecutor(new ShortCutBasic());
		getCommand("enchantment").setExecutor(new ShortCutBasic());
		getCommand("getuuid").setExecutor(new ShortCutBasic());
		getCommand("healandfeed").setExecutor(new ShortCutBasic());
		getCommand("nobreak").setExecutor(new ShortCutBasic());
		getCommand("ac").setExecutor(new ShortCutBasic());
		getCommand("ac").setTabCompleter(new ShortCutBasic());
		getCommand("health").setExecutor(new ShortCutBasic());
		getCommand("credits").setExecutor(new ShortCutBasic());
		getCommand("player").setExecutor(new ShortCutBasic());
		getCommand("player").setTabCompleter(new ShortCutBasic());
		getCommand("invsee").setExecutor(new ShortCutBasic());
		getCommand("potions").setExecutor(new ShortCutBasic());
		getCommand("potions").setTabCompleter(new ShortCutBasic());
		getCommand("potion").setExecutor(new ShortCutBasic());
		getCommand("potion").setTabCompleter(new ShortCutBasic());
		getCommand("packetdebugger").setExecutor(new ShortCutBasic());
		getCommand("packetdebugger").setTabCompleter(new ShortCutBasic());
		getCommand("vclip").setExecutor(new ShortCutBasic());
		getCommand("fly").setExecutor(new ShortCutBasic());
		
		if (commands.isEmpty())
		{
			cmd.clear();
			commands.clear();
			commands.add(new GetIP());
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
			commands.add(new NoBreak());
			commands.add(new FeedCMD());
			commands.add(new Credits());
			commands.add(new Discord());
			commands.add(new HealAndFeedCMD());
			commands.add(new PlayerInfoCMD());
			commands.add(new Enchant());
			commands.add(new ConsoleMessage());
			commands.add(new InvSeeCMD());
			commands.add(new PotionCommand());
			commands.add(new PacketDebugger());
			commands.add(new Reload());
			commands.add(new VClip());
			commands.add(new Help());
			commands.add(new DebugRebugCMD());
			commands.add(new SlashFly());
			commands.add(new SetUserAntiCheat());
			
			for (me.killstorm103.Rebug.Main.Command cmds : commands)
				cmd.add(cmds.getName());
		}
		
		getServer().getConsoleSender().sendMessage (ChatColor.YELLOW + "Enabling Rebug's Events/Listeners");
		PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new EventBlockHandling(), this);
        pm.registerEvents(this, this);
        pm.registerEvents(new EventMenus(), this);
        pm.registerEvents(new EventWeather(),  this);
        pm.registerEvents(new EventHandlePlayerSpawn(), this);
        pm.registerEvents(new EventPlayer(),  this);
        
        pm.registerEvents(new EventCommandPreProcess(),  this);
        
        ResetScaffoldTask = ResetScaffoldTask == null ? getServer().getScheduler().runTaskTimer(this, ResetScaffoldTestArea.getMainTask(), 0, 10000) : ResetScaffoldTask; // 6000
        EverySecondUpdaterTask = EverySecondUpdaterTask == null ? getServer().getScheduler().runTaskTimer(this, OneSecondUpdater_Task.getMainTask(), 0, 20) : EverySecondUpdaterTask;
        
        
        getServer().getConsoleSender().sendMessage (ChatColor.YELLOW + "Enabling Packet Events");
        PacketEvents.IDENTIFIER = "REBUG - Test Server Plugin - killstorm103";
        PacketEvents.getAPI().getEventManager().registerListener(new EventTestPacket(), PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().getEventManager().registerListener(new EventPackets(), PacketListenerPriority.HIGHEST);
        PacketEvents.getAPI().init();
		
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
        	PlaceholderapiHook.registerHook();
        
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
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onLoad () 
	{
		anticheats.clear();
		PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false).checkForUpdates(true).reEncodeByDefault(false).bStats(false);
        PacketEvents.getAPI().load();
	}

	
	public ArrayList<me.killstorm103.Rebug.Main.Command> getCommands ()
	{
		return commands;
	}
	public me.killstorm103.Rebug.Main.Command getCommandByName (String name)
	{
		Iterator<me.killstorm103.Rebug.Main.Command> iter = commands.iterator();
		while (iter.hasNext())
		{
			me.killstorm103.Rebug.Main.Command mod = iter.next();
			if (mod.getName().equalsIgnoreCase(name))
				return mod;
		}
		return null;
	}
	public me.killstorm103.Rebug.Main.Command getCommandBySubName (String name)
	{
		Iterator<me.killstorm103.Rebug.Main.Command> iter = commands.iterator();
		while (iter.hasNext())
		{
			me.killstorm103.Rebug.Main.Command mod = iter.next();
			if (mod.SubAliases() != null && mod.SubAliases().length > 0)
			{
				for (int o = 0; o < mod.SubAliases().length; o ++)
				{
					if (mod.SubAliases()[o].equalsIgnoreCase(name))
						return mod;
				}
			}
		}
		return null;
	}
	private final List<String> cmd = new ArrayList<>();
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		if (alias.equalsIgnoreCase("rebug"))
		{
			if (args.length == 1)
				return cmd;
			
			if (args.length > 1)
			{
				me.killstorm103.Rebug.Main.Command c = getCommandByName(args[0]);
				if (c != null && c.HasCustomTabComplete(sender, command, args, alias))
					return c.onTabComplete(sender, command, args, alias);
			}
		}
		
		return super.onTabComplete(sender, command, alias, args);
	}
	public static boolean hasAdminPerms (Player player)
	{
		if (player == null) return false;
		
 		return player.isOp() || player.hasPermission("me.killstorm103.rebug.server_owner") || player.hasPermission("me.killstorm103.rebug.server_admin");
	}
	public static boolean hasAdminPerms (User user)
	{
		if (user == null) return false;
		
		return hasAdminPerms(user.getPlayer());
	}
	@Override
	public boolean onCommand (CommandSender sender, Command cmd, String command, String[] args)
	{
		Debug(sender, "args.length= " + args.length + " command= " + command);
		
		if (command.toLowerCase().equals(PluginName().toLowerCase()))
		{
			if (args.length == 0)
			{
				Bukkit.dispatchCommand(sender, "rebug help");
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
			me.killstorm103.Rebug.Main.Command commands = getCommandByName(args[0]);
			if (commands != null)
			{
				try
				{
					if (user == null)
					{
						commands.onCommand(sender, command, args);
						return true;
					}
					if ((user.hasPermission(commands.getPermission()) || user.hasPermission(AllCommands_Permission) || hasAdminPerms(user)))
					{
						if (commands.hasCommandCoolDown() && !hasAdminPerms(user) && !user.hasPermission("me.killstorm103.rebug.command_bypass_delay"))
						{
							if (commands.CoolDown.containsKey(user.getPlayer().getUniqueId())) 
							{
								if (commands.CoolDown.get(user.getPlayer().getUniqueId()) > System.currentTimeMillis())
								{
									long time = (commands.CoolDown.get(user.getPlayer().getUniqueId()) - System.currentTimeMillis()) / 1000;
									user.sendMessage("Command is on CoolDown for " + time + " second" + (time > 1 ? "s" : "") + "!");
									return true;
								}
								else
								{
									commands.onCommand(sender, command, args);
									commands.CoolDown.put(user.getPlayer().getUniqueId(), System.currentTimeMillis() + (5 * 1000));
									return true;
								}
							}
							else
							{
								commands.onCommand(sender, command, args);
								commands.CoolDown.put(user.getPlayer().getUniqueId(), System.currentTimeMillis() + (5 * 1000));
								return true;
							}
						}
						else
						{
							commands.onCommand(sender, command, args);
							return true;
						}
					}
					else
					{
						user.sendMessage("You don't have Permission to use this command!");
						return true;
					}
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				sender.sendMessage(RebugMessage + "Unknown Command!");
				return true;
			}
		}
		
		return true;
	}
	private void UpdateUserPerms(User user, String itemName) 
	{
		if (user == null) return;
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getINSTANCE().getConfig().getString("user-update-perms-command").replace("%player%", user.getName()).replace("%user%", user.getName()).replace("%ac%", itemName).replace("%anticheat%", itemName));
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
			boolean dev = true;
			if (dev)
			{
				if (Sudo != null)
					Sudo.sendMessage(RebugMessage + "Sorry but support for Multi AC's is not made yet!");
				else
					user.sendMessage("Sorry but support for Multi AC's is not made yet!");
				
				return;
			}
			if (!getLoadedAntiCheatsFile().getBoolean("allowed-multiple-anticheats"))
			{
				if (Sudo == null)
				{
					user.sendMessage("Sorry but Multiple AntiCheats is Disabled!");
					return;
				}
				else
					Sudo.sendMessage(RebugMessage + ChatColor.YELLOW + "Warning " + ChatColor.DARK_GRAY + "You making " + user.getName() + " Select Multiple AntiCheats while it's Disabled in the Loaded AntiCheats.yml config!");
			}
			int maxACs = getLoadedAntiCheatsFile().getInt("max-anticheats");
			if (getLoadedAntiCheatsFile().getBoolean("limiter-enabled") && maxACs > 1 && ItemName.length - 2 > maxACs)
			{
				if (Sudo != null)
					Sudo.sendMessage(RebugMessage + "Sorry but your trying to Choose more AntiCheats than allowed!");
				else
					user.sendMessage("Sorry but your trying to Choose more AntiCheats than allowed!");
				
				return;
			}
			
			String ID_Name = "", Kickables = "";
			for (int i = 2; i < ItemName.length; i ++)
			{
				if (PT.isStringNull(ItemName[i])) 
				{
					if (Sudo != null)
						Sudo.sendMessage(RebugMessage + "Failed to change user's AntiCheat, the given anticheat name string [" + i + "] was null!");
					else
						user.sendMessage("Failed to change AntiCheats due to the given anticheat name string [" + i + "] was null!");
					
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
				String AntiCheatName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', getLoadedAntiCheatsFile().getString("loaded-anticheats." + ItemName[i].toLowerCase() + ".display-name")));
				
				if (!getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + AntiCheatName.toLowerCase()+ ".enabled"))
				{
					if (Sudo != null)
						Sudo.sendMessage(RebugMessage + ChatColor.YELLOW + "Warning " + ChatColor.DARK_GRAY + "You are making " + user.getName() + "'s Select " + AntiCheatName + " which is a Disabled AntiCheat");
					
					else
					{
						user.sendMessage("AntiCheat: " + AntiCheatName + " is Disabled!");
						if (user.AutoCloseAntiCheatMenu)
							user.getPlayer().closeInventory();
						
						return;
					}
				}
				if (Sudo == null && getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + AntiCheatName.toLowerCase() + ".permission-to-use") &&
				!user.hasPermission("me.killstorm103.rebug.user.select_anticheat_" + AntiCheatName.toLowerCase()))
				{
					user.sendMessage("Sorry but permission-to-use is Enabled for this AntiCheat!");
					user.sendMessage("You need the permission:");
					user.sendMessage("me.killstorm103.rebug.user.select_anticheat_" + AntiCheatName.toLowerCase());
					if (user.AutoCloseAntiCheatMenu)
						user.getPlayer().closeInventory();
					
					return;
				}
				
				if (!getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + AntiCheatName.toLowerCase() + ".is-multi-enabled"))
				{
					if (Sudo != null)
						Sudo.sendMessage(RebugMessage + "Sorry but multi AntiCheat is not Enabled for " + AntiCheatName + "!");
					else
						user.sendMessage("Sorry but multi AntiCheat is not Enabled for " + AntiCheatName + "!");
					
					return;
				}
				
				String m_id = AntiCheatName.toLowerCase();
				if (ID_Name.toLowerCase().contains(m_id))
				{
					if (Sudo == null)
						user.sendMessage("Failed to Add Muti AC " + AntiCheatName + " Due to ID already containing this multi-id Number!");
					else
						Sudo.sendMessage(RebugMessage + "Failed to Add Muti AC " + AntiCheatName + " Due to ID already containing this multi-id Number!");
					
					return;
				}
				if (getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + AntiCheatName.toLowerCase() + ".requires-reconnect"))
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
			user.sendMessage("This is in dev!");
			user.AntiCheat = ID_Name;
	        for (String section : getLoadedAntiCheatsFile().getConfigurationSection("loaded-anticheats.multiple-anticheats").getKeys(true))
	        {
	        //	if (getLoadedAntiCheatsFile().getConfigurationSection("loaded-anticheats.multiple-anticheats." + section).getInt(arg0))
	        	user.sendMessage("key= " + section);
	        	
	        }
			user.NumberIDs = ID_Name;
			UpdateUserPerms (user, user.NumberIDs);
			user.SelectedAntiCheats = ItemName.length - 2;
			if (Sudo != null)
			{
				Sudo.sendMessage(RebugMessage + "Successfully Changed " + user.getName() + "'s AntiCheats to: " + ID_Name);
				user.sendMessage((Sudo instanceof Player ? ((Player) Sudo).getName() : "a Owner or Admin") + " Manually Set Your AntiCheats to: " + ID_Name + "!");
			}
			else
				user.sendMessage("You selected: " + ID_Name);

			if (!PT.isStringNull(Kickables))
			{
				String[] Split = StringUtils.split(Kickables);
				if (Split.length > 1)
				{
					Kickables = "";
					for (int i = 0; i < Split.length; i ++)
					{
						Kickables += ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', getLoadedAntiCheatsFile().getString("loaded-anticheats." + Split[i].toLowerCase() + ".display-name")))
						+ (i < Split.length - 1 ? " & " : "");
					}
				}
				else
					Kickables = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', getLoadedAntiCheatsFile().getString("loaded-anticheats." + Split[0].toLowerCase() + ".display-name")));
				
				PT.KickPlayer(user.getPlayer(), RebugMessage + "Reconnect required in order to make sure " + Kickables + ChatColor.DARK_GRAY + " work properly!");
				return;
			}
			
			UpdateScoreBoard(user, ChatColor.DARK_RED + "AC " + ChatColor.AQUA + "Multi", 10);
			PT.PlaySound(user.getPlayer(), Sound.ANVIL_USE, 1, 1);
			return;
		}
		
		
		
		
		if (PT.isStringNull(ItemName[0])) 
		{
			if (Sudo != null)
				Sudo.sendMessage(RebugMessage + "Failed to change user's AntiCheat, the given anticheat name string was null!");
				
			
			user.sendMessage("Failed to change AntiCheat due to the given AntiCheat name string was null!");
			return;
		}
		
		if (user.AntiCheat.equalsIgnoreCase(ItemName[0]))
		{
			if (Sudo != null)
				Sudo.sendMessage(RebugMessage + "Unable to change " + user.getName() + "'s AntiCheat due to " + user.getName() + " already having this AntiCheat Selected!");
			
			PT.PlaySound(user.getPlayer(), Sound.ANVIL_USE, 1, 1);
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
			if (!hasAdminPerms(user) && !user.hasPermission("me.killstorm103.rebug.user.select_vanilla") && Sudo == null)
			{
				user.sendMessage("You don't have permission to use Vanilla!");
				if (user.AutoCloseAntiCheatMenu)
					user.getPlayer().closeInventory();
				
				return;
			}
			if (!getLoadedAntiCheatsFile().getBoolean("is-vanilla-enabled"))
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
					Sudo.sendMessage(RebugMessage + ChatColor.YELLOW + "Warning " + ChatColor.DARK_GRAY + "You are making " + user.getName() + "'s Select " + ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', 
					Rebug.getINSTANCE().getLoadedAntiCheatsFile().getString("loaded-anticheats." + ItemName[0].toLowerCase() + ".display-name")))
				    + " which is a Disabled AntiCheat");
				
				else
				{
					user.sendMessage("AntiCheat: " + ItemName[0] + " is Disabled!");
					if (user.AutoCloseAntiCheatMenu)
						user.getPlayer().closeInventory();
					
					return;
				}
			}
			if (Sudo == null && getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + ItemName[0].toLowerCase() + ".permission-to-use") &&
			!user.hasPermission("me.killstorm103.rebug.user.select_anticheat_" + ItemName[0].toLowerCase()))
			{
				user.sendMessage("Sorry but permission-to-use is Enabled for this AntiCheat!");
				user.sendMessage("You need the permission:");
				user.sendMessage("me.killstorm103.rebug.user.select_anticheat_" + ItemName[0].toLowerCase());
				if (user.AutoCloseAntiCheatMenu)
					user.getPlayer().closeInventory();
				
				return;
			}
		}
		
		UpdateUserPerms (user, ItemName[0]);
		user.NumberIDs = "";
		if (user.AutoCloseAntiCheatMenu)
			user.getPlayer().closeInventory();
		
		if (getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + ItemName[0].toLowerCase() + ".requires-reconnect"))
		{
			user.SelectedAntiCheats = 1;
			user.AntiCheat = ItemName[0];
			if (Sudo != null)
				Sudo.sendMessage(RebugMessage + "Successfully Changed " + user.getName() + "'s AntiCheat to: " + ChatColor.stripColor(user.getColoredAntiCheat()));
			
			PT.KickPlayer(user.getPlayer(), RebugMessage + "Reconnect required in order to make sure " + user.getColoredAntiCheat() + ChatColor.DARK_GRAY + " works properly!");
			return;
		}
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable()
		{
			
			@Override
			public void run()
			{
				user.AntiCheat = ItemName[0];
				user.SelectedAntiCheats = user.AntiCheat.equalsIgnoreCase("Vanilla") ? 0 : 1;
				String NewAC = user.getColoredAntiCheat(), striped = ChatColor.stripColor(user.AntiCheat).toLowerCase();
		   		if (getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + striped + ".has-short-name"))
		   			NewAC = NewAC.replace(NewAC, ChatColor.translateAlternateColorCodes('&', getLoadedAntiCheatsFile().getString("loaded-anticheats." + striped + ".short-name")) + ChatColor.RESET);
		   		
		   		UpdateScoreBoard(user, ChatColor.DARK_RED + "AC " + NewAC, 10);
		   		
				PT.PlaySound(user.getPlayer(), Sound.ANVIL_USE, 1, 1);
				String AC = ChatColor.stripColor(NewAC);
				if (Sudo != null)
				{
					Sudo.sendMessage(RebugMessage + "Successfully Changed " + user.getName() + "'s AntiCheat to: " + ChatColor.stripColor(user.getColoredAntiCheat()));
					user.sendMessage((Sudo instanceof Player ? ((Player) Sudo).getName() : "a Owner or Admin") + " Manually Set Your AntiCheat to: " + ChatColor.stripColor(user.getColoredAntiCheat()) + "!");
				}
				else
				{
					if (item != null)
						user.sendMessage("You selected: " + AC + (AC.equalsIgnoreCase("vanilla") ||
								AC.equalsIgnoreCase("nocheatplus") || AC.equalsIgnoreCase("NCP") ? "" : " " + ChatColor.stripColor(PT.SubString(item.getItemMeta().getLore().get(3), 10,
								item.getItemMeta().getLore().get(3).length()).replace(" ", ""))));
					else
						user.sendMessage("You selected: " + AC);
				}
			}
		}, 10); // 15
	}
	public int getAccessToCommandsNumber(CommandSender sender) 
	{
		int access = getCommands().size();
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (!hasAdminPerms(player) && !player.hasPermission(AllCommands_Permission))
			{
				for (me.killstorm103.Rebug.Main.Command c : getCommands())
				{
					if (!player.hasPermission(c.getPermission()))
						access --;
				}
			}
			
			return access;
		}
		return access;
	}
}
