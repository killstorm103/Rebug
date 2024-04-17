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
//import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
////import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;

import io.github.retrooper.packetevents.bstats.Metrics;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.killstorm103.Rebug.Commands.ClientCMD;
import me.killstorm103.Rebug.Commands.ConsoleMessage;
import me.killstorm103.Rebug.Commands.GetIP;
import me.killstorm103.Rebug.Commands.Help;
import me.killstorm103.Rebug.Commands.Menu;
import me.killstorm103.Rebug.Commands.Test;
import me.killstorm103.Rebug.Commands.Unblock;
import me.killstorm103.Rebug.Commands.Version;
import me.killstorm103.Rebug.Commands.getInfo;
import me.killstorm103.Rebug.Events.EventBlockHandling;
import me.killstorm103.Rebug.Events.EventHandlePlayerSpawn;
import me.killstorm103.Rebug.Events.EventJoinAndLeave;
import me.killstorm103.Rebug.Events.EventMenus;
import me.killstorm103.Rebug.Events.EventPlayer;
import me.killstorm103.Rebug.Events.EventWeather;
import me.killstorm103.Rebug.PacketEvents.EventCustomPayLoadPacket;
import me.killstorm103.Rebug.Tasks.ScoreBoard;
import me.killstorm103.Rebug.Utils.User;
import net.md_5.bungee.api.ChatColor;

public class Rebug extends JavaPlugin
{
	public final int pluginID = 10787;
	public static final String AllCommands_Permission = "me.killstorm103.rebug.commands.*";
	private static String Version = "0.0";
	private static Rebug getMain;
	private ArrayList<me.killstorm103.Rebug.Main.Command> commands = new ArrayList<me.killstorm103.Rebug.Main.Command>();
	public static final HashMap<UUID, User> USERS = new HashMap<>();
	
	public static User getUser(Player player) 
	{
        for (User user : USERS.values()) 
        {
        	if (user.getPlayer() != player && !user.getPlayer().getUniqueId().equals(player.getUniqueId())) continue;
        	
        	return user;
        }
        return null;
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
	public boolean Debug ()
	{
		return getConfig().getBoolean("debugger");
	}
	private BukkitTask scoreboardTask;
	private File customConfigFile;

	public FileConfiguration getCustomConfig()
	{
		return this.customConfig;
	}
	
    private FileConfiguration customConfig;
	
    private void createCustomConfig() 
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
	@Override
	public void onEnable ()
	{
		getMain = this;
		getServer().getConsoleSender().sendMessage("Rebug: onEnable - Pre");
		if (scoreboardTask != null)
			scoreboardTask.cancel();
		
		if (!USERS.isEmpty())
		{
			for (Player player : Bukkit.getOnlinePlayers())
			{
				if (player != null && player.getScoreboard() != null && player.getScoreboard().getObjective("Rebug") != null)
				{
					player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
				}
				if (player != null)
				{
					player.kickPlayer("Rebug: Rejoin!");
				}
			}
			USERS.clear();
			User.joinTimeMap.clear();
		}
			
		createCustomConfig();
		
		try
		{
			Version = Rebug.getGetMain().getDescription().getVersion();
		}
		catch (Exception e) {e.printStackTrace();}
		
		getServer().getConsoleSender().sendMessage (ChatColor.YELLOW + "Enabling Rebug's commands");
		commands.add(new GetIP());
		commands.add(new Unblock());
		commands.add(new Version());
		commands.add(new getInfo());
		commands.add(new Test());
		commands.add(new Menu());
		commands.add(new ClientCMD());
		commands.add(new ConsoleMessage());
		commands.add(new Help());
		
		getServer().getConsoleSender().sendMessage (ChatColor.YELLOW + "Enabling Rebug's Events/Listeners");
		PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new EventBlockHandling(), this);
        pm.registerEvents(new EventJoinAndLeave(), this);
        pm.registerEvents(new EventMenus(), this);
        pm.registerEvents(new EventWeather(),  this);
        pm.registerEvents(new EventHandlePlayerSpawn(), this);
        pm.registerEvents(new EventPlayer(),  this);
        
     // Messenger messenger = Bukkit.getMessenger();   messenger.registerIncomingPluginChannel(this, "MC|Brand", new EventJoinAndLeave.BrandListener());
        
        scoreboardTask = getServer().getScheduler().runTaskTimer(this, ScoreBoard.getBoard(), 0, 30);
        
        getServer().getConsoleSender().sendMessage (ChatColor.YELLOW + "Enabling Packet Events");
        PacketEvents.IDENTIFIER = "REBUG";
        PacketEvents.getAPI().getEventManager().registerListener(new EventCustomPayLoadPacket(), PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().init();
        Metrics metrics = new Metrics(this, this.pluginID);
        
        getServer().getConsoleSender().sendMessage ("Rebug: onEnable - Post - " + ChatColor.DARK_GREEN + "Enabled Rebug v" + PluginVersion());
	}
	@Override
	public void onDisable ()
	{
		getServer().getConsoleSender().sendMessage("Rebug: onDisable - Pre");
		if (scoreboardTask != null)
			scoreboardTask.cancel();
		
		PacketEvents.getAPI().terminate();
		getServer().getConsoleSender().sendMessage("Rebug: onDisable - Post - " + ChatColor.DARK_RED + "Disabled Rebug");
	}
	
	@Override
	public void onLoad () 
	{
		getServer().getConsoleSender().sendMessage("Rebug: onLoad - Pre");
		PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false).checkForUpdates(true).bStats(true);
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
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		if (args.length > 0 && alias.equalsIgnoreCase("rebug"))
		{
			for (me.killstorm103.Rebug.Main.Command commands : commands)
			{
				if (args[0].equalsIgnoreCase(commands.getName()))
				{
					commands.onTabComplete(sender, command, args);
				}
			}
		}
		
		return super.onTabComplete(sender, command, alias, args);
	}
	@Override
	public boolean onCommand (CommandSender sender, Command cmd, String command, String[] args)
	{
		command.toLowerCase();
		if (command.equals(PluginName().toLowerCase()))
		{
			if (args.length == 0)
			{
				Bukkit.dispatchCommand(sender, "rebug version");
				Bukkit.dispatchCommand(sender, "rebug help");
				return true;
			}
			if (Debug())
				sender.sendMessage("args.length= " + args.length + " command= " + command);
			
			Player player = null;
			if (sender instanceof Player)
				player = (Player) sender;
				
			for (me.killstorm103.Rebug.Main.Command commands : commands)
			{
				if (args[0].equalsIgnoreCase(commands.getName()))
				{
					if (player == null || player.hasPermission(commands.getPermission()) || player.hasPermission(AllCommands_Permission))
					{
						try
						{
							commands.onCommand(sender, command, args);
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
					else
					{
						if (player != null)
							player.sendMessage("You don't have Permission to use this Command!");
					}
					return true;
				}
			}
		}
		
		return true;
	}
}
