package me.killstorm103.Rebug.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;



public class EventJoinAndLeave implements Listener
{
	private Score score = null;
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin (PlayerJoinEvent e) 
	{
		Player player = e.getPlayer();
		
		if (Rebug.getGetMain().getConfig().getBoolean("force-gamemode-on-join") && !e.getPlayer().isOp())
			player.setGameMode(GameMode.SURVIVAL);
		
		String message = Rebug.getGetMain().getConfig().getString("join-message");
		if (message != null && message.length() > 0 && e.getPlayer().isOp())
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%player%", e.getPlayer().getName())));
		
		
		if (Rebug.getGetMain().getConfig().getBoolean("rebug-scoreboard"))
		{
			score = null;
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Rebug.getGetMain(), new Runnable() {
	            public void run() {        
	            	
	                ScoreboardManager manager = Bukkit.getScoreboardManager();
	                final Scoreboard board = manager.getNewScoreboard();
	                final Objective objective = board.registerNewObjective("Rebugged", "Cummy");        
	                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	                String name = ChatColor.DARK_GRAY + "| " + Rebug.getGetMain().getConfig().getString("scoreboard-title") + " " + ChatColor.DARK_GRAY + "|";
	                name = ChatColor.translateAlternateColorCodes('&', name);
	                objective.setDisplayName(name);
	                score = objective.getScore(ChatColor.DARK_GRAY + "| §cTest §2Server " + ChatColor.DARK_GRAY + "|");
	                score.setScore(10);       
	                score = objective.getScore("");
	                score.setScore(9);    
	                score = objective.getScore(ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + Rebug.PluginName() + ChatColor.DARK_GRAY + " |" + ChatColor.GRAY + " v" + Rebug.PluginVersion());
	                score.setScore(8);
	                score = objective.getScore(ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + "Server" + ChatColor.DARK_GRAY + " |" + ChatColor.GRAY + " " + PT.getServerVersion ());
	                score.setScore(7);
	                score = objective.getScore(ChatColor.DARK_GRAY + "| " + ChatColor.DARK_RED + "Client" + ChatColor.DARK_GRAY + " | " + ChatColor.GRAY + PT.getPlayerVersion(PT.getPlayerVersion(player)));
	                score.setScore(6);
	                player.setScoreboard(board);
	            }
	        },0, 20 * 10);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit (PlayerQuitEvent e) {}
}
