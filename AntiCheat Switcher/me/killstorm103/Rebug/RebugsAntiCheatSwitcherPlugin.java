package me.killstorm103.Rebug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

import lombok.Getter;
import me.killstorm103.Rebug.Command.Command.SenderType;
import me.killstorm103.Rebug.Command.Commands.*;
import me.killstorm103.Rebug.Command.Handler.*;
import me.killstorm103.Rebug.Events.*;
import me.killstorm103.Rebug.Hooks.PlaceholderapiHook;
import me.killstorm103.Rebug.SoftWares.VelocitySupport;
import me.killstorm103.Rebug.Tasks.*;
import me.killstorm103.Rebug.Utils.FileUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.QuickConfig;
import me.killstorm103.Rebug.Utils.Scheduler;
import me.killstorm103.Rebug.Utils.ServerVersionUtil;
import me.killstorm103.Rebug.Utils.User;
import me.killstorm103.Rebug.Utils.Menu.Old.ItemsAndMenusUtils;

@com.velocitypowered.api.plugin.Plugin(id= "RebugEssential", name = "RebugEssential", version = "1.1", authors = "killstorm103", url = "github.com/killstorm103/Rebug")
@SuppressWarnings("deprecation")
public class RebugsAntiCheatSwitcherPlugin extends JavaPlugin 
{
	
	public final HashMap<UUID, User> USERS = new HashMap<>();
	public final ArrayList<UUID> isAlertsEnabledOnce = new ArrayList<>();
	public static final Map<Plugin, String> Plugin_AntiCheats = new HashMap<>();
	public static final Map<String, Boolean> Manually_Added_AntiCheats = new HashMap<>();
	public static final Map<String, String> AntiCheats = new HashMap<>();
	private final static List<String> list_configs;
	
	@Getter
	private static final Map<String, Boolean> SettingsBooleans = new HashMap<>();
	@Getter
	private static final Map<String, String> SettingsActions = new HashMap<>();
	@Getter
	private static final Map<String, Integer> SettingsIntegers = new HashMap<>();
	@Getter
	private static final Map<String, Double> SettingsDoubles = new HashMap<>();
	@Getter
	private static final Map<String, String> SettingsList = new HashMap<>();
	
	static 
	{
		list_configs = new ArrayList<>();
		list_configs.add("config");
		list_configs.add("loaded anticheats");
		list_configs.add("player settings");
		list_configs.add("server");
		list_configs.add("menus");
	}
	@Getter
	private static RebugsAntiCheatSwitcherPlugin INSTANCE;

	@Getter
	private static final String RebugMessage = ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + "REBUG " + ChatColor.DARK_GRAY + ">> ", PluginName = "Rebug", 
    Author = "killstorm103", Edition = "Essential";

	@Getter
	private static String PluginVersion;
	@Getter
	private static boolean isVelocity = false;
	
	public RebugsAntiCheatSwitcherPlugin (Object proxyServer)
	{
		isVelocity = true;
        new VelocitySupport((com.velocitypowered.api.proxy.ProxyServer) proxyServer);
    }
	@Subscribe
	public void onProxyInitializeEvent (ProxyInitializeEvent e) {}
	public RebugsAntiCheatSwitcherPlugin () {}

	@Override
	public void onEnable()
	{
		final long time = System.currentTimeMillis();
		INSTANCE = this;
		PluginVersion = getDescription().getVersion();
		
		FileUtils.initFolder();
		getConfig().options().copyDefaults(true).copyHeader(true); 
		for (int i = 0; i < list_configs.size(); i++)
			FileUtils.createCustomConfig(list_configs.get(i));
		
		Log(Level.WARNING, "If you get a error about getCommand(String) is null or anything else about a Command!");
		Log(Level.WARNING, "it's probably due to a Shortcut CMD not being set in the plugin.yml if");
		Log(Level.WARNING, "not try Editing the Config (look for: command.settings)");
		cmd.clear();
		commands.clear();
		boolean RegisterUsingPaper = true;
		try
		{
			Class.forName("io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents");
		}
		catch (ClassNotFoundException e) 
		{
			RegisterUsingPaper = false;
		}
		Log(Level.INFO, "Registering Command Main Label!");
		if (getConfig().getBoolean("command.settings.main-command.register-using-paper", false) && RegisterUsingPaper)
			PaperSetUpCMD.setup(true, -1, 0);
		else
			BukkitSetUpCMD.setup(true, -1, 0);
			
		Log(Level.INFO, "Registering Commands!");
		commands.add(new AntiCheatMenuCMD());
		commands.add(new Version());
		commands.add(new GetUUID());
		commands.add(new DamageCMD());
	//	commands.add(new ClientCMD());
		commands.add(new BackCMD());
		commands.add(new SetHealthCMD());
		commands.add(new HealCMD());
		commands.add(new FeedCMD());
		commands.add(new Credits());
		commands.add(new Discord());
		commands.add(new HealAndFeedCMD());
		commands.add(new PlayerInfoCMD());
		//commands.add(new DebugItemCMD());
		commands.add(new Reload());
		commands.add(new VClip());
		commands.add(new Help());
		commands.add(new DebugRebugCMD());
		commands.add(new SlashFly());
		commands.add(new SetUserAntiCheat());
		commands.add(new ShowCommands());
		commands.add(new CheckAC());
		commands.add(new SettingsMenuCommand());
		commands.add(new ClientCommandChecker());
		commands.add(new DamageTick());
		commands.add(new TestApiCMD());
		commands.add(new TestCMD());
		commands.add(new InjectCMD());
		commands.add(new DebugItemCMD());
		
		Log(Level.INFO, "Registering Shortcut/SubAliase Commands (if any)!");
		if (getConfig().getBoolean("command.settings.register-on-new-thread.enabled", false))
		{
			final long sleep = getConfig().getLong("command.settings.thread-sleep", 0);
			if (!getConfig().getBoolean("command.settings.register-on-new-thread.normal", true))
			{
				try
				{
					if (getConfig().getBoolean("command.settings.register-commands-using-paper", false) && RegisterUsingPaper)
						PaperSetUpCMD.setup(false, 1, sleep);
					else
						BukkitSetUpCMD.setup(false, 1, sleep);
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				if (getConfig().getBoolean("command.settings.register-commands-using-paper", false) && RegisterUsingPaper)
					PaperSetUpCMD.setup(false, 2, sleep);
				else
					BukkitSetUpCMD.setup(false, 2, sleep);
			}
		}
		else
		{
			if (getConfig().getBoolean("command.settings.register-commands-using-paper", false) && RegisterUsingPaper)
				PaperSetUpCMD.setup(false, 3, 0);
			else
				BukkitSetUpCMD.setup(false, 3, 0);
		}
		try 
		{
			ServerVersionUtil.setServer_Version(getServer().getClass().getPackage().getName().split("\\.")[3]);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			try
			{
				String version = Bukkit.getServer().getBukkitVersion().split("-")[0], suffix = "";
				if (version.chars().filter(c -> c == '.').count() == 1) 
				{
					suffix = "R1";
					version = 'v' + version.replace('.', '_') + '_' + suffix;
				}
				else
				{
					int minor = Integer.parseInt(version.split("\\.")[2].charAt(0) + "");
					version = 'v' + version.replace('.', '_').replace("_" + minor, "") + '_' + "R" + (minor - 1);
				}
				ServerVersionUtil.setServer_Version(version);
			}
			catch (Exception ee) 
			{
				e.printStackTrace();
				ee.printStackTrace();
			}
		}
		String target = Bukkit.getName();
		Log(Level.INFO, "Server Version: " + ServerVersionUtil.getServer_Version());
		Log(Level.INFO, "Bukkit.getName()= " + target);
		ServerVersionUtil.CreateINSTANCE(target);
			
		
		isAlertsEnabledOnce.clear();
		if (getConfig().getBoolean("user.one-second-updater-use-asynchronously", true))
			Scheduler.runTimerAsynchronously(OneSecondUpdater.getMainTask(), 0, 20);
		else
			Scheduler.runTimer(OneSecondUpdater.getMainTask(), 0, 20);
		
		if (getConfig().getBoolean("user.is-moving-check.asynchronously", true))
			Scheduler.runTimerAsynchronously(isMovingUpdater.getMainTask(), getConfig().getInt("user.is-moving-check.delayTicks", 20), getConfig().getInt("user.is-moving-check.periodTicks", 20));
		else
			Scheduler.runTimer(isMovingUpdater.getMainTask(), getConfig().getInt("user.is-moving-check.delayTicks", 20), getConfig().getInt("user.is-moving-check.periodTicks", 20));

		Log(Level.INFO, "Enabling Packet Events");
		PacketEvents.getAPI().getEventManager().registerListener(new EventPacket(), PacketListenerPriority.HIGHEST);
		PacketEvents.getAPI().init();
		try
		{
			ServerVersionUtil.setVersionName(PacketEvents.getAPI().getServerManager().getVersion().name().substring(2).replace("_", "."));
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		ServerVersionUtil.setJavaVersion(System.getProperty("java.version") + "");
		Log(Level.INFO, ServerVersionUtil.getServer_Version().equalsIgnoreCase("Unknown") ? "Failed to get the Server's version!" : "Detected Server Version: " + ServerVersionUtil.getServer_Version() + " (" + ServerVersionUtil.getVersionName() + ") (Java Version: " + ServerVersionUtil.getJavaVersion() + ")");
		Log(Level.INFO, "Detected " + ServerVersionUtil.getSoftWareType() + " as the Server SoftWare Type!");
		if (FileUtils.getServerConfig().get("server.softwares.types." + ServerVersionUtil.getSoftWareType()) != null && ServerVersionUtil.getDetectedSoftWares() == 1)
			Log(Level.INFO, "Detected SoftWare Version: " + ServerVersionUtil.getVersionOfSoftWare());
		else
		{
			Log(Level.WARNING, "Detected " + ServerVersionUtil.getDetectedSoftWares() + " Server SoftWare Types or SoftWare Type is Spoofed or ");
			Log(Level.WARNING, ServerVersionUtil.getSoftWareType() + " isn't set in Server.yml");
			Log(Level.INFO, "So didn't try checking SoftWare Version");
		}
		try
		{
			ServerVersionUtil.setNetworkId(PacketEvents.getAPI().getServerManager().getVersion().getProtocolVersion());
			Log(Level.INFO, "NetworkID: " + ServerVersionUtil.getNetworkId());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		ServerVersionUtil.setServerNewerThan1_8(PT.isServerNewerOrEquals(ServerVersion.V_1_9));
		ServerVersionUtil.setServerNewerOrEquals1_19(PT.isServerNewerOrEquals(ServerVersion.V_1_19));
		ServerVersionUtil.setChatTypeGameInfoSupported(PT.isServerOlderThan(ServerVersion.V_1_19));
		PluginManager pm = Bukkit.getPluginManager();
		Plugin placeholder = pm.getPlugin("PlaceholderAPI");
		Log(Level.INFO, "trying to Hook PlaceholderAPI!");
		if (placeholder != null && placeholder.isEnabled())
			PlaceholderapiHook.registerHook();
		else
			Log(Level.WARNING, "The Plugin PlaceholderAPI wasn't Found or was Disabled!, it is recommended you download and install it!");
		
		Log(Level.INFO, "Enabling Events/Listeners");
		pm.registerEvents(new EventJoinAndLeave(), this);
		pm.registerEvents(new EventCommandPreProcess(), this);
		pm.registerEvents(new EventMenu(), this);
		pm.registerEvents(new EventPlayer(), this);
		pm.registerEvents(new EventWorld(), this);
		
		Scheduler.runLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				Log(Level.INFO, "Trying to create Menus!");
				ItemsAndMenusUtils.getINSTANCE().getRebugSettings();
				ItemsAndMenusUtils.getINSTANCE().getAntiCheats();
				ItemsAndMenusUtils.getINSTANCE().getDebugItemMenu();
			}
			
		}, FileUtils.getServerConfig().getLong("server.onEnable.create-menus-delay", 100));
		
		Log(Level.INFO, "Enabled Rebug Essential in " + (System.currentTimeMillis() - time) + "ms");
		if (ServerVersionUtil.getSoftWareType().equalsIgnoreCase("Bukkit") || ServerVersionUtil.getSoftWareType().equalsIgnoreCase("CraftBukkit"))
			Log(Level.WARNING, ServerVersionUtil.getSoftWareType() + " is outdated and not Recommended!, you should use Spigot/Paper or another Fork instead!");
	}

	@Override
	public void onLoad() {
		PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
		PacketEvents.getAPI().getSettings().reEncodeByDefault(false).checkForUpdates(true).bStats(false);
		PacketEvents.getAPI().load();
	}

	@Override
	public void onDisable() 
	{
		Bukkit.getOnlinePlayers().forEach(FileUtils::savePlayer);
		Scheduler.cancelTasks(this);
		PacketEvents.getAPI().terminate();
		SettingsActions.clear();
		SettingsBooleans.clear();
		SettingsDoubles.clear();
		SettingsIntegers.clear();
		SettingsList.clear();
	}
	public static void Log(@NotNull Level JavaUtilLog, @NotNull String message)
	{
		getINSTANCE().getLogger().log(JavaUtilLog, message);
	}
	public static void Debug(@NotNull Object object, @NotNull String msg) 
	{
		if (!getSettingsBooleans().getOrDefault("Debug", false))
			return;

		Log(Level.INFO, msg);
		Player player = null;
		if (object instanceof Player) 
			player = (Player) object;
		
		if (object instanceof User) 
			player = ((User) object).getPlayer();
		
		if (object instanceof CommandSender && player == null)
		{
			Bukkit.getConsoleSender().sendMessage(getRebugMessage() + msg);
			return;
		}
		if (player != null && (!getSettingsBooleans().getOrDefault("Debug To Ops Only", true) || PT.hasAdminPerms(player)))
			player.sendMessage(getRebugMessage() + msg);
	}
	public boolean isCommandAllowedShortCut (@NotNull String name)
	{
		if (PT.isStringNull(name)) return false;
		
		me.killstorm103.Rebug.Command.Command cmd = getCommandBySubName(name);
		if (cmd != null && !PT.isStringNull(cmd.SubAliases()) && getConfig().getBoolean("command.commands." + cmd.getName().toLowerCase() + ".shortcut-enabled", false))
			return true;
		
		return false;
	}
	public static User getUser (@NotNull Player player) 
	{
		if (player == null)
		{
			if (getSettingsBooleans().getOrDefault("Debug", true))
				Log(Level.SEVERE, "getUser's player is @NotNull but is Null!");
			
			return null;
		}
		for (User user : getINSTANCE().USERS.values()) 
		{
			if (user.getPlayer() != player && !user.getPlayer().getUniqueId().equals(player.getUniqueId()))
				continue;

			return user;
		}
		return null;
	}

	// TODO Recode this and make it less messy!
	public void Reload_Configs(CommandSender sender)
	{
		if (getSettingsBooleans().getOrDefault("Kick On Reload Config", false)) 
		{
			for (Player players : Bukkit.getOnlinePlayers())
				PT.KickPlayer(players, ChatColor.DARK_RED + "Rejoin reloading Rebug's Config!");
		}
		boolean noerror;
		try {
			getConfig().load(FileUtils.getConfigFile());
			getConfig().save(FileUtils.getConfigFile());
			FileUtils.LoadedAntiCheatsFile = YamlConfiguration.loadConfiguration(FileUtils.LoadedAntiCheatsConfigFile);
			FileUtils.PlayerSettingsConfig = YamlConfiguration.loadConfiguration(FileUtils.PlayerSettingsFile);
			FileUtils.MenusConfig = YamlConfiguration.loadConfiguration(FileUtils.MenusConfigFile);
			FileUtils.ServerConfig = YamlConfiguration.loadConfiguration(FileUtils.ServerFile);
			ItemsAndMenusUtils.setAntiCheatMenu(null);
			ItemsAndMenusUtils.setItemsMenu(null);
			ItemsAndMenusUtils.setRebugSettingsMenu(null);
			ItemsAndMenusUtils.setDebugItemMenu(null);
			ItemsAndMenusUtils.getINSTANCE().getRebugSettings();
			ItemsAndMenusUtils.getINSTANCE().getAntiCheats();
			ItemsAndMenusUtils.getINSTANCE().getDebugItemMenu();
			for (Player p : Bukkit.getOnlinePlayers()) 
			{
				User used = getUser(p);
				if (used != null) 
				{
					used.getPlayer().closeInventory ();
					used.setSettingsMenu(null);
				}
			}
			if (sender instanceof Player)
				sender.sendMessage(RebugMessage + "Successfully Reloaded Config!");

			Log(Level.INFO, "Successfully Reloaded Config!");
			noerror = true;
		} catch (Exception e) {
			noerror = false;
			if (sender instanceof Player)
				sender.sendMessage(RebugMessage + "Failed to Reload Config!");

			Log(Level.SEVERE, "Failed to Reload Config!");
			e.printStackTrace();
		}
		if (noerror) {
			if (FileUtils.getLoadedAntiCheatsFile().getBoolean("Disabled-AntiCheat.disabled")) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.closeInventory();
					User used = getUser(p);
					if (used != null) {
						String def = FileUtils.getLoadedAntiCheatsFile().getString("default-anticheat.anticheat", "Vanilla");
						if (used.getSelectedAntiCheats() < 2) {
							if (!used.getAntiCheat().equalsIgnoreCase("Vanilla")
									&& !used.getAntiCheat().equalsIgnoreCase(def)) {
								if (!FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats."
												+ used.getAntiCheat().toLowerCase() + ".enabled", false)) {
									if (FileUtils.getLoadedAntiCheatsFile()
											.getBoolean("Disabled-AntiCheat.kick-on-Invalid-anticheat", false))
										PT.KickPlayer(used.getPlayer(), RebugMessage + used.getAntiCheat()
												+ " was Disabled! - Invalid AntiCheat upon Config Reload");
									else {
										used.sendMessage(used.getAntiCheat()
												+ " was Disabled! - Invalid AntiCheat upon Config Reload");
										used.setAntiCheat(PT.isStringNull(def) ? "Vanilla" : def);
										used.setNumberIDs("");
										used.setSelectedAntiCheats(0);
										String NewAC = used.getColoredAntiCheat(),
												striped = ChatColor.stripColor(used.getAntiCheat()).toLowerCase();
										if (FileUtils.getLoadedAntiCheatsFile()
												.getBoolean("loaded-anticheats." + striped + ".has-short-name"))
											NewAC = NewAC.replace(NewAC,
													ChatColor.translateAlternateColorCodes('&',
															FileUtils.getLoadedAntiCheatsFile().getString(
																	"loaded-anticheats." + striped + ".short-name"))
															+ ChatColor.RESET);

										PT.UpdateUserPerms(used.getPlayer(), used.getAntiCheat());
									}
								}
							}
						} else {
							String[] check = PT.SplitString(used.getAntiCheat());
							for (int i = 0; i < check.length; i++) {
								if (PT.isStringNull(check[i])) {
									used.setNumberIDs("");
									used.setAntiCheat(PT.isStringNull(def) ? "Vanilla" : def);
									used.setSelectedAntiCheats(0);
									used.sendMessage("a AC String was Null somehow so chaged you to: "
											+ (PT.isStringNull(def) ? "Vanilla" : def));
									break;
								}
								if (check[i].equalsIgnoreCase("Vanilla")) {
									used.setNumberIDs("");
									used.setAntiCheat(PT.isStringNull(def) ? "Vanilla" : def);
									used.setSelectedAntiCheats(0);
									used.sendMessage(
											"Your AntiCheat was somehow Mulit while being Vanilla! so fixed that!");
									break;
								}
								if (!FileUtils.getLoadedAntiCheatsFile().getBoolean(
												"loaded-anticheats." + check[i].toLowerCase() + ".enabled", false)) {
									if (FileUtils.getLoadedAntiCheatsFile()
											.getBoolean("Disabled-AntiCheat.kick-on-Invalid-anticheat", false))
										PT.KickPlayer(used.getPlayer(), RebugMessage + check[i]
												+ " was Disabled! - Invalid AntiCheat upon Config Reload");
									else {
										used.sendMessage(
												check[i] + " was Disabled! - Invalid AntiCheat upon Config Reload");
										used.setAntiCheat(PT.isStringNull(def) ? "Vanilla" : def);
										used.setNumberIDs("");
										used.setSelectedAntiCheats(0);
										String NewAC = used.getColoredAntiCheat(),
												striped = ChatColor.stripColor(used.getAntiCheat()).toLowerCase();
										if (FileUtils.getLoadedAntiCheatsFile()
												.getBoolean("loaded-anticheats." + striped + ".has-short-name", false))
											NewAC = NewAC.replace(NewAC,
													ChatColor.translateAlternateColorCodes('&',
															FileUtils.getLoadedAntiCheatsFile().getString(
																	"loaded-anticheats." + striped + ".short-name"))
															+ ChatColor.RESET);

										PT.UpdateUserPerms(used.getPlayer(), used.getAntiCheat());
									}
								}
							}
						}
					}
				}
				FileUtils.getLoadedAntiCheatsFile().set("Disabled-AntiCheat.disabled", false);
				try {
					FileUtils.getLoadedAntiCheatsFile().save(FileUtils.LoadedAntiCheatsConfigFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private final ArrayList<me.killstorm103.Rebug.Command.Command> commands = new ArrayList<me.killstorm103.Rebug.Command.Command>();
	public ArrayList<me.killstorm103.Rebug.Command.Command> getCommands() {
		return commands;
	}
	
	public int getAccessToCommandsNumber (CommandSender sender)
	{
		int access = getCommands().size();
		for (me.killstorm103.Rebug.Command.Command c : getCommands())
		{
			if (sender instanceof Player)
			{
				Player player = (Player) sender;
				if (c.getType() == SenderType.Console || !player.hasPermission(c.getPermission()) && !PT.hasPermission(player, getConfig().getString("command.settings.permission-for-all-commands", "me.killstorm103.rebug.commands.*")))
					access --;
			}
			else
			{
				if (c.getType() == SenderType.Player)
					access --;
			}
		}
		return access;
	}

	public me.killstorm103.Rebug.Command.Command getCommandByName(String name) {
		Iterator<me.killstorm103.Rebug.Command.Command> iter = commands.iterator();
		while (iter.hasNext()) {
			me.killstorm103.Rebug.Command.Command mod = iter.next();
			if (mod.getName().equalsIgnoreCase(name))
				return mod;
		}
		return null;
	}

	public me.killstorm103.Rebug.Command.Command getCommandBySubName (String name)
	{
		Iterator<me.killstorm103.Rebug.Command.Command> iter = commands.iterator();
		while (iter.hasNext()) 
		{
			me.killstorm103.Rebug.Command.Command mod = iter.next();
			if (!PT.isStringNull(mod.SubAliases()))
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
		if (cmd.isEmpty())
		{
			for (me.killstorm103.Rebug.Command.Command c : getCommands())
			{
				cmd.add(c.getName());
			}
		}
		
		return cmd;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) 
	{
		if (alias.equalsIgnoreCase(QuickConfig.MainLabel())) 
		{
			if (args.length == 1)
				return StringUtil.copyPartialMatches(args[args.length - 1], getCMDList(), new ArrayList<>());
			
			if (args.length > 1)
			{
				me.killstorm103.Rebug.Command.Command c = getCommandByName(args[0]);
				if (c != null && c.HasCustomTabComplete(sender, command, args, alias))
					return c.onTabComplete(sender, command, args, alias);
			}
			return super.onTabComplete(sender, command, alias, args);
		}
		return super.onTabComplete(sender, command, alias, args);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		Debug(sender, "args.length= " + args.length + " command= " + command);
		Debug(sender, "label: " + label + (args.length > 0 ? " args[0]: " + args[0].replace("/", "") : "") + (args.length > 1 ? " " + args[1] : ""));
		try
		{
			me.killstorm103.Rebug.Command.Command commands = null;
			if (label.equalsIgnoreCase(QuickConfig.MainLabel())) 
			{
				if (args.length == 0) 
				{
					Bukkit.dispatchCommand(sender, QuickConfig.MainLabel() + " help");
					return true;
				}
				commands = getCommandByName(args[0]);
				commands = commands == null ? getCommandBySubName(args[0].replace("/", "")) : commands;
				if (commands == null)
				{
					sender.sendMessage(RebugMessage + args[0] + " is a Unknown Command try /" + QuickConfig.MainLabel() + " help!");
					return true;
				}
			}
			else
				commands = commands == null ? getCommandBySubName(args.length > 0 ? args[0].replace("/", "") : label) : commands;
			
			if (commands != null) 
			{
				try {
					SenderType type = commands.getType();
					if (type.equals(SenderType.Player) && !(sender instanceof Player))
					{
						Log(Level.INFO, "Only Players can use this Command!");
						return true;
					}
					if (type.equals(SenderType.Console) && sender instanceof Player)
					{
						sender.sendMessage(RebugMessage + "Only Console can use this Command!");
						return true;
					}
					User user = null;
					if (sender instanceof Player) 
					{
						user = getUser((Player) sender);
						if (user == null) 
						{
							if (commands.BypassUserNullCheck())
							{
								Player player = (Player) sender;
								if (PT.hasPermission(player, commands.getPermission(), PT.AllCommandsPermission()))
								{
									if (getConfig().getBoolean("command.commands." + commands.getName().toLowerCase() + ".has-cooldown", false) && !PT.hasPermission(player, getConfig().getString("command.settings.cooldown-bypass-permission", "me.killstorm103.rebug.command_bypass_delay")))
									{
										int cooldown = getConfig().getInt("command.commands." + commands.getName().toLowerCase() + ".cooldown", 5);
										cooldown = cooldown < 1 ? 1 : cooldown;
										if (commands.CoolDown.containsKey(player.getUniqueId()))
										{
											if (commands.CoolDown.get(player.getUniqueId()) > System.currentTimeMillis()) 
											{
												long time = (commands.CoolDown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
												player.sendMessage(getRebugMessage() + "Command is on CoolDown for " + time + " second" + (time > 1 ? "s" : "") + "!");
												return true;
											}
											else
											{
												commands.onCommand(sender, label, args);
												commands.CoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (cooldown * 1000));
												return true;
											}
										}
										else 
										{
											commands.onCommand(sender, label, args);
											commands.CoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (cooldown * 1000));
											return true;
										}
									}
									else
									{
										commands.onCommand(sender, label, args);
										return true;
									}
								} 
								else
								{
									player.sendMessage(getRebugMessage() + "You don't have Permission to use this command!");
									return true;
								}
							}
							sender.sendMessage(getRebugMessage() + "\"User\" was somehow null when executing command!");
							return true;
						}
					}
					if (user == null) 
					{
						commands.onCommand(sender, label, args);
						return true;
					}
					if (PT.hasPermission(user.getPlayer(), commands.getPermission(), PT.AllCommandsPermission()))
					{
						if (getConfig().getBoolean("command.commands." + commands.getName().toLowerCase() + ".has-cooldown", false) && !PT.hasPermission(user.getPlayer(), getConfig().getString("command.settings.cooldown-bypass-permission", "me.killstorm103.rebug.command_bypass_delay")))
						{
							int cooldown = getConfig().getInt("command.commands." + commands.getName().toLowerCase() + ".cooldown", 5);
							cooldown = cooldown < 1 ? 1 : cooldown;
							if (commands.CoolDown.containsKey(user.getUUID()))
							{
								if (commands.CoolDown.get(user.getUUID()) > System.currentTimeMillis()) 
								{
									long time = (commands.CoolDown.get(user.getUUID()) - System.currentTimeMillis()) / 1000;
									user.sendMessage("Command is on CoolDown for " + time + " second" + (time > 1 ? "s" : "") + "!");
									return true;
								}
								else
								{
									commands.onCommand(sender, label, args);
									commands.CoolDown.put(user.getUUID(), System.currentTimeMillis() + (cooldown * 1000));
									return true;
								}
							}
							else 
							{
								commands.onCommand(sender, label, args);
								commands.CoolDown.put(user.getUUID(), System.currentTimeMillis() + (cooldown * 1000));
								return true;
							}
						}
						else
						{
							commands.onCommand(sender, label, args);
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
					commands.Log(sender, "a error occurred!");
					e.printStackTrace();
				}
			} 
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return super.onCommand(sender, command, label, args);
	}
}
