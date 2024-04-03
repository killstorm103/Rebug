package me.killstorm103.Rebug.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitTask;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.killstorm103.Rebug.Commands.*;
import me.killstorm103.Rebug.Events.*;
import me.killstorm103.Rebug.PacketEvents.*;
import me.killstorm103.Rebug.Tasks.ScoreBoard;
import me.killstorm103.Rebug.Utils.*;
import net.md_5.bungee.api.ChatColor;

public class Rebug extends JavaPlugin
{
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
	
	@Override
	public void onEnable ()
	{
		getMain = this;
		saveDefaultConfig();
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
		commands.add(new Help());
		
		getServer().getConsoleSender().sendMessage (ChatColor.YELLOW + "Enabling Rebug's Events/Listeners");
		PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new EventBlockHandling(), this);
        pm.registerEvents(new EventJoinAndLeave(), this);
        pm.registerEvents(new EventMenus(), this);
        pm.registerEvents(new EventWeather(),  this);
        pm.registerEvents(new EventHandlePlayerSpawn(), this);
        pm.registerEvents(new EventPlayer(),  this);
        
        Messenger messenger = Bukkit.getMessenger();
        messenger.registerIncomingPluginChannel(this, "MC|Brand", new EventJoinAndLeave.BrandListener());
        
        scoreboardTask = getServer().getScheduler().runTaskTimer(this, ScoreBoard.getBoard(), 0, 25);
        
        getServer().getConsoleSender().sendMessage (ChatColor.YELLOW + "Enabling Packet Events");
        PacketEvents.IDENTIFIER = "REBUG";
        PacketEvents.getAPI().getEventManager().registerListener(new TestPacket(), PacketListenerPriority.NORMAL);
        
        PacketEvents.getAPI().init();
        getServer().getConsoleSender().sendMessage (ChatColor.DARK_GREEN + "Enabled Rebug v" + PluginVersion());
	}
	@Override
	public void onDisable ()
	{
		if (scoreboardTask != null)
			scoreboardTask.cancel();
		
		PacketEvents.getAPI().terminate();
		getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Disabled Rebug");
	}
	
	@Override
	public void onLoad () 
	{
		PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false).checkForUpdates(true).bStats(true);
        PacketEvents.getAPI().load();
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
				sender.sendMessage("args.length= " + args.length);
			
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
							commands.onCommand(sender, args);
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
