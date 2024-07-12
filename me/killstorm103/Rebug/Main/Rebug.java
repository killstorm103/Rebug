package me.killstorm103.Rebug.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
import me.killstorm103.Rebug.Commands.ResetUserMenus;
import me.killstorm103.Rebug.Commands.SetHealthCMD;
import me.killstorm103.Rebug.Commands.SpawnCMD;
import me.killstorm103.Rebug.Commands.Test;
import me.killstorm103.Rebug.Commands.Unblock;
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
import net.md_5.bungee.api.ChatColor;

public class Rebug extends JavaPlugin implements Listener
{
	public static boolean debug = false, KickOnReloadConfig = false, debugOpOnly = true, PrivatePerPlayerAlerts = true;
	public static final String AllCommands_Permission = "me.killstorm103.rebug.commands.*";
	private static String Version = "0.0";
	private static Rebug getMain, INSTANCE;
	private final ArrayList<me.killstorm103.Rebug.Main.Command> commands = new ArrayList<me.killstorm103.Rebug.Main.Command>();
	public static final HashMap<UUID, User> USERS = new HashMap<>();
	public static final String RebugMessage = ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + "REBUG " + ChatColor.DARK_GRAY + ">> ";
	
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
    public static void Debug (Player player, String msg)
    {
    	if (!debug || PT.isStringNull(msg)) return;
    	
    	if (player != null && (hasAdminPerms(player) || !debugOpOnly))
    		player.sendMessage(RebugMessage + msg); 
    	
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
	public static final double PluginVersion ()
	{
		if (Version.contains("0.0"))
			getINSTANCE().getServer().getConsoleSender().sendMessage("Please restart the server, there was a error when you enabled/loaded Rebug");
		
		
		return Double.parseDouble(Version);
	}
	public boolean DevMode ()
	{
		return true;
	}
	public BukkitTask RestScaffoldTask, EverySecondUpdaterTask;

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
	public void removeConfigs(Player p)
    {
        File playerFile = getPlayerFile(p);
        if (playerFile.exists() && Config.ShouldDeletePlayerConfigAfterLoading())
            playerFile.delete();
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
        
        user.FallDamage = playerConfig.getBoolean("Fall Damage");
        user.Exterranl_Damage = playerConfig.getBoolean("Exterranl Damage");
        user.Damage_Resistance = playerConfig.getBoolean("Damage Resistance");
        user.Fire_Resistance = playerConfig.getBoolean("Fire Resistance");
        user.Hunger = playerConfig.getBoolean("Hunger");
        user.PotionEffects = playerConfig.getBoolean("Potion Effects");
        user.AutoRefillBlocks = playerConfig.getBoolean("Auto Refill Blocks");
        user.Infinite_Blocks = playerConfig.getBoolean("Infinite Blocks");
        user.HideOnlinePlayers = playerConfig.getBoolean("HideOnlinePlayers");
        user.ProximityPlayerHider = playerConfig.getBoolean("Proximity Player Hider");
        user.AutoCloseAntiCheatMenu = playerConfig.getBoolean("Auto Close AntiCheat Menu");
        user.AllowDirectMessages = playerConfig.getBoolean("Allow Direct Messages");
        user.AllowMentions = playerConfig.getBoolean("Allow Mentions");
    	user.AntiCheat = playerConfig.getString("AntiCheat");
    	user.ShowFlags = playerConfig.getBoolean("Show Flags");
    	user.ShowPunishes = playerConfig.getBoolean("Show Punishes");
    	user.AntiCheatKick = playerConfig.getBoolean("AC Kick");
    	user.Vanilla1_8FlyCheck = playerConfig.getBoolean("Vanilla Fly 1_8_Plus");
    	user.Vanilla1_9FlyCheck = playerConfig.getBoolean("Vanilla Fly 1_9Plus");
    	user.NotifyFlyingKick1_8 = playerConfig.getBoolean("Notify Vanilla Fly Kick 1_8");
    	user.NotifyFlyingKick1_9 = playerConfig.getBoolean("Notify Vanilla Fly Kick 1_9");
    	user.potionlevel = playerConfig.getInt("Potion Setting Level");
    	user.potion_effect_seconds = playerConfig.getInt("Potion Setting Timer");
    	int max_timer = getConfig().getInt("potion-settings-max-timer"), max_level = getConfig().getInt("potion-settings-max-level");
    	user.potion_effect_seconds = user.potion_effect_seconds < 1 ? 1 : user.potion_effect_seconds > max_timer ? max_timer : user.potion_effect_seconds;
    	user.potionlevel = user.potionlevel < 1 ? 1 : user.potionlevel > max_level ? max_level : user.potionlevel;
    	user.Yapper_Message_Count = playerConfig.getInt("Yapper");
    	
    	removeConfigs(p);
    }
    public void savePlayer (Player p) 
    {
    	YamlConfiguration playerConfig = new YamlConfiguration();
        User user = getUser(p);
        if (user == null) return;
        
        playerConfig.set("Player Name", p.getName());
		playerConfig.set("Fall Damage", user.FallDamage);
		playerConfig.set("Exterranl Damage", user.Exterranl_Damage);
		playerConfig.set("Damage Resistance", user.Damage_Resistance);
		playerConfig.set("Fire Resistance", user.Fire_Resistance);
		playerConfig.set("Hunger", user.Hunger);
		playerConfig.set("Potion Effects", user.PotionEffects);
		playerConfig.set("Auto Refill Blocks", user.AutoRefillBlocks);
		playerConfig.set("Infinite Blocks", user.Infinite_Blocks);
		playerConfig.set("HideOnlinePlayers", user.HideOnlinePlayers);
		playerConfig.set("Proximity Player Hider", user.ProximityPlayerHider);
		playerConfig.set("Auto Close AntiCheat Menu", user.AutoCloseAntiCheatMenu);
		playerConfig.set("Allow Direct Messages", user.AllowDirectMessages);
		playerConfig.set("Allow Mentions", user.AllowMentions);
		String AntiCheat = ChatColor.translateAlternateColorCodes('&', user.AntiCheat);
		playerConfig.set("AntiCheat", ChatColor.stripColor(AntiCheat));
		playerConfig.set("Show Flags", user.ShowFlags);
		playerConfig.set("Show Punishes", user.ShowPunishes);
		playerConfig.set("AC Kick", user.AntiCheatKick);
		playerConfig.set("Vanilla Fly 1_8_Plus", user.Vanilla1_8FlyCheck);
		playerConfig.set("Vanilla Fly 1_9Plus", user.Vanilla1_9FlyCheck);
		playerConfig.set("Notify Vanilla Fly Kick 1_8", user.NotifyFlyingKick1_8);
		playerConfig.set("Notify Vanilla Fly Kick 1_9", user.NotifyFlyingKick1_9);
		playerConfig.set("Potion Setting Level", user.potionlevel);
		playerConfig.set("Potion Setting Timer", user.potion_effect_seconds);
		playerConfig.set("Yapper", user.Yapper_Message_Count);
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
    public void Reload_Configs (User user)
    {
    	anticheatConfig = YamlConfiguration.loadConfiguration(LoadedAntiCheatsConfigFile);
    	ItemsConfig = YamlConfiguration.loadConfiguration(LoadedItemsConfigFile);
    	DefaultPlayerSettingsConfig = YamlConfiguration.loadConfiguration(DefaultPlayerSettingsFile);
    	anticheats.clear();
    	ItemsAndMenusUtils.INSTANCE.lore.clear();
    	ItemsAndMenusUtils.INSTANCE.AntiCheatMenu = ItemsAndMenusUtils.INSTANCE.ItemPickerMenu = null;
    	try
		{
    		getConfig().load(getConfigFile());
    		getConfig().save(getConfigFile());
    		user.getPlayer().sendMessage(RebugMessage + "Successfully Reloaded Config!");
    		Bukkit.getConsoleSender().sendMessage(RebugMessage + "Successfully Reloaded Config!");
		}
		catch (Exception e) 
		{
			user.getPlayer().sendMessage(RebugMessage + "Failed to Reload Config!");
    		Bukkit.getConsoleSender().sendMessage(RebugMessage + "Failed to Reload Config!");
			e.printStackTrace();
		}
    }
    private void createCustomConfig (String con) 
    {
    	if (con == null || con.length() < 1) return;
    	
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
   		board.set(color + "TB ", 4);
   		board.set(color + "Blocking " + ChatColor.RED + ChatColor.BOLD.toString() + "X", 3);
   		board.set(color + "Sprinting " + ChatColor.RED + ChatColor.BOLD.toString() + "X", 2);
   		board.set(color + "Sneaking " + ChatColor.RED + ChatColor.BOLD.toString() + "X", 1);
   		board.set(color + "OnGround " + ChatColor.RED + ChatColor.BOLD.toString() + "X", 0);
   		user.ScoreBoard = board;
   		user.getPlayer().setScoreboard(user.ScoreBoard.getScoreboard());
   	}
   	public void UpdateScoreBoard (User user, int line, String text)
   	{
   		if (!Config.RebugScoreBoard() || user == null || line < 0) return;
   		
   		BPlayerBoard board = Netherboard.instance().getBoard(user.getPlayer());
   		if (board == null) return;
	   		
   		board.set(text, line);
   	}
   	private final ArrayList<UUID> isAlertsEnabled = new ArrayList<>();
   	
   	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin (PlayerJoinEvent e) 
	{
   		Player player = e.getPlayer();
		Rebug.USERS.put(player.getUniqueId(), new User(player));
		User user = Rebug.getUser(player);
		user.setJoinTime(user.getPlayer().getUniqueId(), System.currentTimeMillis());
		restorePlayer(user.getPlayer());
		UpdateUserPerms(user, user.AntiCheat);
		addToScoreBoard(user.getPlayer());
		if (user.Fire_Resistance)
			user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
		else
			user.getPlayer().removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
		
		if (user.Damage_Resistance)
			user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3, true, false));
		else
			user.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		
		if (Config.ShouldForceGameMode() && !user.getPlayer().isOp() && !user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") && !user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin"))
			user.getPlayer().setGameMode(GameMode.SURVIVAL);
		
		final String message = getConfig().getString("join-message");
		if (message != null && message.length() > 0 && user.getPlayer().isOp())
			user.getPlayer().sendMessage(RebugMessage + ChatColor.translateAlternateColorCodes('&', message.replace("%player%", user.getPlayer().getName())));
		
		Location loc = user.getLocation();
        if (!user.getPlayer().hasPlayedBefore() || loc.getBlockX() >= 18 && loc.getBlockY() >= 57 && loc.getBlockZ() >= 306 && loc.getBlockX() <= 61 && loc.getBlockY() <= 88 && loc.getBlockZ() <= 330)
        {
        	Bukkit.getScheduler().runTask(this, new Runnable()
        	{
				@Override
				public void run() 
				{
					user.getPlayer().teleport(new Location(Bukkit.getServer().getWorld("world"), 25, 58, 318, -91.19964F, -2.700141F));
				}
			});
        }
        
   		e.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + user.getPlayer().getName());
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
	} 
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onQuit (PlayerQuitEvent e) 
	{
		Player player = e.getPlayer();
		savePlayer(player);
		User user = Rebug.getUser(player);
		if (user != null)
		{
			user.ScoreBoard.delete();
			user.joinTimeMap.clear();
			user.BlockPlaced.clear();
			Rebug.USERS.remove(user.getPlayer().getUniqueId(), user);
		}
		e.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "] " + e.getPlayer().getName());
	}
	/*
	public static GrimAbstractAPI getGrimAC ()
	{
		RegisteredServiceProvider<GrimAbstractAPI> provider = Bukkit.getServicesManager().getRegistration(GrimAbstractAPI.class);
		if (provider != null)
		{
			GrimAbstractAPI api = provider.getProvider();
			return api;
		}
		
		return null;
	}
	*/
	@Override
	public void onEnable ()
	{
		getMain = this;
		isAlertsEnabled.clear();
		initFolder();
		for (int i = 0; i < list_configs.size(); i ++)
			createCustomConfig(list_configs.get(i));
		
		try
		{
			Version = getDescription().getVersion();
		}
		catch (Exception e) {e.printStackTrace();}
		
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
		getCommand("health").setExecutor(new ShortCutBasic());
		getCommand("credits").setExecutor(new ShortCutBasic());
		getCommand("player").setExecutor(new ShortCutBasic());
		getCommand("invsee").setExecutor(new ShortCutBasic());
		getCommand("potions").setExecutor(new ShortCutBasic());
		getCommand("potion").setExecutor(new ShortCutBasic());
		getCommand("packetdebugger").setExecutor(new ShortCutBasic());
		
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
			commands.add(new ResetUserMenus());
			commands.add(new PlayerInfoCMD());
			commands.add(new Enchant());
			commands.add(new ConsoleMessage());
			commands.add(new InvSeeCMD());
			commands.add(new PotionCommand());
			commands.add(new PacketDebugger());
			commands.add(new Reload());
			commands.add(new Help());
			
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
        
        RestScaffoldTask = RestScaffoldTask == null ? getServer().getScheduler().runTaskTimer(this, ResetScaffoldTestArea.getMainTask(), 0, 10000) : RestScaffoldTask; // 6000
        EverySecondUpdaterTask = EverySecondUpdaterTask == null ? getServer().getScheduler().runTaskTimer(this, OneSecondUpdater_Task.getMainTask(), 0, 20) : EverySecondUpdaterTask;
        
        
        getServer().getConsoleSender().sendMessage (ChatColor.YELLOW + "Enabling Packet Events");
        PacketEvents.IDENTIFIER = "REBUG - Test Server Plugin - killstorm103";
        PacketEvents.getAPI().getEventManager().registerListener(new EventTestPacket(), PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().getEventManager().registerListener(new EventPackets(), PacketListenerPriority.HIGHEST);
        PacketEvents.getAPI().init();
		
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
        	PlaceholderapiHook.registerHook();
        
        INSTANCE = this;
	}
	
	@Override
	public void onDisable ()
	{
		Bukkit.getOnlinePlayers().forEach(this::savePlayer);
		if (RestScaffoldTask != null)
			RestScaffoldTask.cancel();
		
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
			{
				return mod;
			}
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
			for (me.killstorm103.Rebug.Main.Command c : commands)
			{
				if (args.length == 1)
				{
					if (!args[0].isEmpty()) // TODO: Make a search
					{
						
					}
					return cmd;
				}
				
				if (args.length > 1 && args[0].equalsIgnoreCase(c.getName()) && c.HasCustomTabComplete())
				{
					return c.onTabComplete(sender, command, args);
				}
			}
		}
		
		return super.onTabComplete(sender, command, alias, args);
	}
	public static boolean hasAdminPerms (Player player)
	{
		if (player == null) return false;
		
 		return player.hasPermission("me.killstorm103.rebug.server_owner") || player.hasPermission("me.killstorm103.rebug.server_admin") || player.isOp();
	}
	@Override
	public boolean onCommand (CommandSender sender, Command cmd, String command, String[] args)
	{
		if (debug)
			Rebug.Debug((Player) sender, "args.length= " + args.length + " command= " + command);
		
		if (command.toLowerCase().equals(PluginName().toLowerCase()))
		{
			if (args.length == 0)
			{
				Bukkit.dispatchCommand(sender, "rebug version");
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
			try
			{
				for (me.killstorm103.Rebug.Main.Command commands : commands)
				{
					if (args[0].equalsIgnoreCase(commands.getName()))
					{
						if (user == null || user.getPlayer() != null && (user.getPlayer().hasPermission(commands.getPermission()) || user.getPlayer().hasPermission(AllCommands_Permission) || user.getPlayer().isOp() || user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") || user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin")))
						{
							if (user == null)
								commands.onCommand(sender, command, args);
							
							else
							{
								if (!user.getPlayer().isOp() && !user.getPlayer().hasPermission("me.killstorm103.rebug.command_bypass_delay") && !user.getPlayer().hasPermission("me.killstorm103.rebug.server_owner") && !user.getPlayer().hasPermission("me.killstorm103.rebug.server_admin"))
								{
									if (commands.CoolDown.containsKey(user.getPlayer().getUniqueId())) 
									{
										if (commands.hasCommandCoolDown() && commands.CoolDown.get(user.getPlayer().getUniqueId()) > System.currentTimeMillis())
										{
											long time = (commands.CoolDown.get(user.getPlayer().getUniqueId()) - System.currentTimeMillis()) / 1000;
											user.getPlayer().sendMessage(RebugMessage + "Command is on CoolDown for " + time + " second(s)!");
											return true;
										}
										else
										{
											if (commands.hasCommandCoolDown())
												commands.CoolDown.put(user.getPlayer().getUniqueId(), System.currentTimeMillis() + (5 * 1000));
											
											commands.onCommand(sender, command, args);
											return true;
										}
									}
									else
									{
										if (commands.hasCommandCoolDown())
											commands.CoolDown.put(user.getPlayer().getUniqueId(), System.currentTimeMillis() + (5 * 1000));
										
										commands.onCommand(sender, command, args);
										return true;
									}
								}
								else
								{
									commands.onCommand(sender, command, args);
									return true;
								}
							}
						}
						else
						{
							if (user.getPlayer() != null)
								user.getPlayer().sendMessage(RebugMessage + "You don't have Permission to use this Command!");
						}
						return true;
					}
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return true;
	}
	public static void UpdateUserPerms(User user, String itemName) 
	{
		if (user == null) return;
		
		String command = getINSTANCE().getConfig().getString("user-update-perms-command").replace("%user%", user.getPlayer().getName()).replace("%anticheat%", itemName);
		for (int i = 0; i < 3; i ++) // looping is a test for fixing a bug
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
	}
	public static Rebug getINSTANCE () 
	{ 
		if (INSTANCE == null) return getMain;
		
		return INSTANCE;
	}
	public void UpdateAntiCheat (User user, String ItemName, ItemStack item)
	{
		if (user == null) return;
		
		if (PT.isStringNull(ItemName))
			return;
		
		if (!ItemName.equalsIgnoreCase("vanilla") && getLoadedAntiCheatsFile().get("loaded-anticheats." + ItemName.toLowerCase()) == null)
		{
			user.sendMessage("AntiCheat " + "\"" + ItemName + "\" was not found!");
			return;
		}
		
		if (ItemName.equalsIgnoreCase("Vanilla") && !hasAdminPerms(user.getPlayer()) && !user.hasPermission("me.killstorm103.rebug.user.select_vanilla"))
		{
			user.sendMessage("You don't have permission to use Vanilla!");
			if (user.getPlayer().getOpenInventory() != null && user.AutoCloseAntiCheatMenu)
				user.getPlayer().closeInventory();
			
			return;
		}
		
		UpdateUserPerms (user, ItemName);
		if (user.getPlayer().getOpenInventory() != null && user.AutoCloseAntiCheatMenu)
			user.getPlayer().closeInventory();
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable()
		{
			
			@Override
			public void run()
			{
				user.AntiCheat = ItemName;
				String NewAC = user.getColoredAntiCheat(), striped = ChatColor.stripColor(user.AntiCheat).toLowerCase();
		   		if (getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + striped + ".has-short-name"))
				{
		   			NewAC = NewAC.replace(NewAC, ChatColor.translateAlternateColorCodes('&', getLoadedAntiCheatsFile().getString("loaded-anticheats." + striped + ".short-name")) + ChatColor.RESET);
				}
		   		if (user.ScoreBoard != null)
		   			user.ScoreBoard.set(ChatColor.DARK_RED + "AC " + NewAC, 10);
		   		
				PT.PlaySound(user.getPlayer(), Sound.ANVIL_USE, 1, 1);
				String AC = ChatColor.stripColor(NewAC);
				if (item != null)
					user.sendMessage("You selected: " + AC + (AC.equalsIgnoreCase("vanilla") ||
							AC.equalsIgnoreCase("nocheatplus") || AC.equalsIgnoreCase("NCP") ? "" : " " + ChatColor.stripColor(PT.SubString(item.getItemMeta().getLore().get(3), 10,
							item.getItemMeta().getLore().get(3).length()).replace(" ", ""))));
				else
					user.sendMessage("You selected: " + AC);
			}
		}, 10); // 15
	}
}
