package me.killstorm103.Rebug.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;

import io.github.retrooper.packetevents.bstats.Metrics;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.killstorm103.Rebug.Commands.BackCMD;
import me.killstorm103.Rebug.Commands.ClientCMD;
import me.killstorm103.Rebug.Commands.ConsoleMessage;
import me.killstorm103.Rebug.Commands.DamageCMD;
import me.killstorm103.Rebug.Commands.DebugRebugCMD;
import me.killstorm103.Rebug.Commands.Discord;
import me.killstorm103.Rebug.Commands.Enchant;
import me.killstorm103.Rebug.Commands.FeedCMD;
import me.killstorm103.Rebug.Commands.GetIP;
import me.killstorm103.Rebug.Commands.GetUUID;
import me.killstorm103.Rebug.Commands.HealCMD;
import me.killstorm103.Rebug.Commands.Help;
import me.killstorm103.Rebug.Commands.Menu;
import me.killstorm103.Rebug.Commands.NoBreak;
import me.killstorm103.Rebug.Commands.Repair;
import me.killstorm103.Rebug.Commands.SpawnCMD;
import me.killstorm103.Rebug.Commands.Test;
import me.killstorm103.Rebug.Commands.Unblock;
import me.killstorm103.Rebug.Commands.Version;
import me.killstorm103.Rebug.Commands.getInfo;
import me.killstorm103.Rebug.Commands.Handler.EventCommandPreProcess;
import me.killstorm103.Rebug.Commands.ShortCuts.ShortCutBasic;
import me.killstorm103.Rebug.Events.EventBlockHandling;
import me.killstorm103.Rebug.Events.EventHandlePlayerSpawn;
import me.killstorm103.Rebug.Events.EventJoinAndLeave;
import me.killstorm103.Rebug.Events.EventMenus;
import me.killstorm103.Rebug.Events.EventPlayer;
import me.killstorm103.Rebug.Events.EventWeather;
import me.killstorm103.Rebug.PacketEvents.EventAntiCancelBrandPacket;
import me.killstorm103.Rebug.PacketEvents.EventCustomPayLoadPacket;
import me.killstorm103.Rebug.PacketEvents.EventTestPacket;
import me.killstorm103.Rebug.Tasks.*;
import me.killstorm103.Rebug.Utils.User;
import net.md_5.bungee.api.ChatColor;

public class Rebug extends JavaPlugin
{
	public static boolean debug = false, debugOpOnly = true;
	public final int pluginID = 10787;
	public static final String AllCommands_Permission = "me.killstorm103.rebug.commands.*";
	private static String Version = "0.0";
	private static Rebug getMain;
	private ArrayList<me.killstorm103.Rebug.Main.Command> commands = new ArrayList<me.killstorm103.Rebug.Main.Command>();
	public static final HashMap<UUID, User> USERS = new HashMap<>();
	public static String RebugMessage = ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + "REBUG " + ChatColor.DARK_GRAY + ">> ";
	
	public static User getUser(Player player) 
	{
        for (User user : USERS.values()) 
        {
        	if (user.getPlayer() != player && !user.getPlayer().getUniqueId().equals(player.getUniqueId())) continue;
        	
        	return user;
        }
        return null;
    }
    public static void Debug (Player player, String msg)
    {
    	if (!debugOpOnly || player.hasPermission("me.killstorm103.rebug.server_owner") || player.hasPermission("me.killstorm103.rebug.server_admin") || player.isOp())
    		player.sendMessage(RebugMessage + msg); 
    	
    	Bukkit.getServer().getConsoleSender().sendMessage(RebugMessage + msg);
    }
	public static String StartOfPermission ()
	{
		return "me.killstorm103.rebug.";
	}
	public static String PluginName ()
	{
		return "Rebug";
	}
	public static String getAuthor ()
	{
		return "killstorm103";
	}
	public static double PluginVersion ()
	{
		if (Version.contains("0.0"))
			getGetMain().getServer().getConsoleSender().sendMessage("Please restart the server, there was a error when you enabled/loaded Rebug");
		
		
		return Double.parseDouble(Version);
	}
	public boolean DevMode ()
	{
		return true;
	}
	private BukkitTask MainTask;
	private File customConfigFile, PlayerDataBaseFile;

	public FileConfiguration getCustomConfig()
	{
		return this.customConfig;
	}
	
    public File getPlayerDataBaseFile() {
		return PlayerDataBaseFile;
	}
    public File getCustomConfigFile() {
		return customConfigFile;
	}

	public FileConfiguration getPlayerDataBase() {
		return PlayerDataBase;
	}

	private FileConfiguration customConfig, PlayerDataBase;
	public void LoadPlayerSettings (Player player)
	{
	}
	public void SavePlayerSettings (Player player)
	{
	}
    private void createCustomConfig (String con) 
    {
    	if (con == null || con.length() < 1) return;
    	
    	if (con.equalsIgnoreCase("config"))
    	{
    		customConfigFile = new File(getDataFolder(), "config.yml");
            if (!customConfigFile.exists())
            {
                customConfigFile.getParentFile().mkdirs();
                saveResource("config.yml", false);
             }

            customConfig = new YamlConfiguration();
            YamlConfiguration.loadConfiguration(customConfigFile);
    	}
    	if (con.equalsIgnoreCase("player base"))
    	{
    		PlayerDataBaseFile = new File(getDataFolder(), "players.yml");
            if (!PlayerDataBaseFile.exists())
            {
                PlayerDataBaseFile.getParentFile().mkdirs();
                saveResource("players.yml", false);
             }

            PlayerDataBase = new YamlConfiguration();
            YamlConfiguration.loadConfiguration(PlayerDataBaseFile);
    	}
    }
	@Override
	public void onEnable ()
	{
		getMain = this;
		getServer().getConsoleSender().sendMessage("Rebug: onEnable - Pre");
		if (MainTask != null)
			MainTask.cancel();
		
		createCustomConfig("config");
		createCustomConfig("player base");
		
		try
		{
			Version = Rebug.getGetMain().getDescription().getVersion();
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
		getCommand("getuuid").setExecutor(new ShortCutBasic());
		
		
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
		commands.add(new HealCMD());
		commands.add(new Repair());
		commands.add(new NoBreak());
		commands.add(new FeedCMD());
		commands.add(new Discord());
		commands.add(new Enchant());
		commands.add(new DebugRebugCMD());
		commands.add(new ConsoleMessage());
		commands.add(new Help());
		for (me.killstorm103.Rebug.Main.Command cmds : commands)
			cmd.add(cmds.getName());
		
		getServer().getConsoleSender().sendMessage (ChatColor.YELLOW + "Enabling Rebug's Events/Listeners");
		PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new EventBlockHandling(), this);
        pm.registerEvents(new EventJoinAndLeave(), this);
        pm.registerEvents(new EventMenus(), this);
        pm.registerEvents(new EventWeather(),  this);
        pm.registerEvents(new EventHandlePlayerSpawn(), this);
        pm.registerEvents(new EventPlayer(),  this);
        
        pm.registerEvents(new EventCommandPreProcess(),  this);
        
        
        MainTask = getServer().getScheduler().runTaskTimer(this, MainTaskClass.getMainTask(), 0, 30);
        
        getServer().getConsoleSender().sendMessage (ChatColor.YELLOW + "Enabling Packet Events");
        PacketEvents.IDENTIFIER = "REBUG";
        PacketEvents.getAPI().getEventManager().registerListener(new EventCustomPayLoadPacket(), PacketListenerPriority.MONITOR);
        PacketEvents.getAPI().getEventManager().registerListener(new EventTestPacket(), PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().getEventManager().registerListener(new EventAntiCancelBrandPacket(), PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().getEventManager().registerListener(new EventPlayer(), PacketListenerPriority.NORMAL);
       // PacketEvents.getAPI().getEventManager().registerListener(new EventVanillaFlyChecksPacket(), PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().init();
        
        
        @SuppressWarnings("unused")
		Metrics metrics = new Metrics(this, this.pluginID);
        
        getServer().getConsoleSender().sendMessage ("Rebug: onEnable - Post - " + ChatColor.DARK_GREEN + "Enabled Rebug v" + PluginVersion());
	}
	@Override
	public void onDisable ()
	{
		getServer().getConsoleSender().sendMessage("Rebug: onDisable - Pre");
		if (MainTask != null)
			MainTask.cancel();
		
		PacketEvents.getAPI().terminate();
		getServer().getConsoleSender().sendMessage("Rebug: onDisable - Post - " + ChatColor.DARK_RED + "Disabled Rebug");
	}
	
	@Override
	public void onLoad () 
	{
		getServer().getConsoleSender().sendMessage("Rebug: onLoad - Pre");
		PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false).checkForUpdates(true).reEncodeByDefault(false).bStats(false);
        PacketEvents.getAPI().load();
        getServer().getConsoleSender().sendMessage("Rebug: onLoad - Post");
	}

	
	public ArrayList<me.killstorm103.Rebug.Main.Command> getCommands ()
	{
		return commands;
	}
	public static Rebug getGetMain ()
	{
		return getMain;
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
										if (commands.CoolDown.get(user.getPlayer().getUniqueId()) > System.currentTimeMillis())
										{
											long time = (commands.CoolDown.get(user.getPlayer().getUniqueId()) - System.currentTimeMillis()) / 1000;
											user.getPlayer().sendMessage(RebugMessage + "Command is on CoolDown for " + time + " second(s)!");
											return true;
										}
										else
										{
											commands.CoolDown.put(user.getPlayer().getUniqueId(), System.currentTimeMillis() + (5 * 1000));
											commands.onCommand(sender, command, args);
											return true;
										}
									}
									else
									{
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
}
