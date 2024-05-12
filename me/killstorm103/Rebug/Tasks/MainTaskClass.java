package me.killstorm103.Rebug.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class MainTaskClass implements Runnable
{
	private static final MainTaskClass task = new MainTaskClass();
	
	public MainTaskClass () {}

	public static MainTaskClass getMainTask() 
	{
		return task;
	}
	private void CreateScoreBoard (Player p)
	{
		if (!Config.RebugScoreBoard())
			return;
		
		User user = Rebug.getUser(p);
		if (user != null && user.getPlayer() != null)
		{
			Player player = user.getPlayer();
			Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			Objective objective = scoreboard.registerNewObjective("Rebug", "Rebugged");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			String name = ChatColor.DARK_GRAY + "| " + Rebug.getGetMain().getConfig().getString("scoreboard-title") + " " + ChatColor.DARK_GRAY + "|";
			name = ChatColor.translateAlternateColorCodes('&', name);
			objective.setDisplayName(name);
			objective.getScore(ChatColor.DARK_GRAY + "| §cTest §2Server " + ChatColor.DARK_GRAY + "|").setScore(10);
			objective.getScore("").setScore(9);
	        objective.getScore(ChatColor.DARK_RED + Rebug.PluginName() + ChatColor.GRAY + " v" + Rebug.PluginVersion()).setScore(8);
	        objective.getScore(ChatColor.DARK_RED + "Server " + ChatColor.GRAY + PT.getServerVersion ()).setScore(7);
	        objective.getScore(ChatColor.DARK_RED + "Map " + ChatColor.GRAY + Rebug.getGetMain().getConfig().getString("map-version")).setScore(5);
	        
	        
	        
	        Team teams = scoreboard.registerNewTeam("rebug team#1");
	        teams.addEntry(ChatColor.RED.toString());
	        teams.setPrefix(ChatColor.DARK_RED + "Ping ");
	        teams.setSuffix("" + PT.getPing(player) + "ms");
	        objective.getScore(ChatColor.RED.toString()).setScore(4);
	        
	        teams = scoreboard.registerNewTeam("rebug team#2");
	        teams.addEntry(ChatColor.BLUE.toString());
	        teams.setPrefix(ChatColor.DARK_RED + "Memory ");
	        teams.setSuffix("" + PT.getUsedMemory());
	        objective.getScore(ChatColor.BLUE.toString()).setScore(6);
	        
	        player.setScoreboard(scoreboard);
		}
	}
	private void UpdateScoreBoard (Player p)
	{
		if (!Config.RebugScoreBoard())
			return;
		
		User user = Rebug.getUser(p);
		
		if (user != null && user.getPlayer() != null)
		{
			Player player = user.getPlayer();
			Scoreboard scoreboard = player.getScoreboard();
			Team team = scoreboard.getTeam("rebug team#1");
			team.setSuffix("" + PT.getPing(player) + "ms");
			
			team = scoreboard.getTeam("rebug team#2");
			team.setSuffix(PT.getUsedMemory() + "");
		}
	}
	@Override
	public void run () 
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (player.getScoreboard() != null && player.getScoreboard().getObjective("Rebug") != null)
				UpdateScoreBoard(player);
			else
				CreateScoreBoard(player);
			
			User user = Rebug.getUser(player);
			if (user != null)
			{
				for (Player target : Bukkit.getOnlinePlayers())
				{
					if (user.getPlayer() != target)
					{
						if (user.HideOnlinePlayers())
						{
							if (user.getPlayer().canSee(target))
								user.getPlayer().hidePlayer(target);
						}
						else
						{
							if (!user.getPlayer().canSee(target))
								user.getPlayer().showPlayer(target);
						}
					}
				}
			}
		}
	}
}
