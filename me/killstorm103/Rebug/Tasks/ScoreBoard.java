package me.killstorm103.Rebug.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class ScoreBoard implements Runnable
{
	private static final ScoreBoard board = new ScoreBoard();
	
	public ScoreBoard () {}

	public static ScoreBoard getBoard() 
	{
		return board;
	}
	private void CreateScoreBoard (Player p)
	{
		if (!Rebug.getGetMain().getConfig().getBoolean("rebug-scoreboard"))
			return;
			
		User user = Rebug.getUser(p);
		Player player = user.getPlayer();
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective("Rebug", "Rebugged");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		String name = ChatColor.DARK_GRAY + "| " + Rebug.getGetMain().getConfig().getString("scoreboard-title") + " " + ChatColor.DARK_GRAY + "|";
		name = ChatColor.translateAlternateColorCodes('&', name);
		objective.setDisplayName(name);
		objective.getScore(ChatColor.DARK_GRAY + "| §cTest §2Server " + ChatColor.DARK_GRAY + "|").setScore(10);
		objective.getScore("").setScore(9);;
        objective.getScore(ChatColor.DARK_RED + Rebug.PluginName() + ChatColor.GRAY + " v" + Rebug.PluginVersion()).setScore(8);
        objective.getScore(ChatColor.DARK_RED + "Server " + ChatColor.GRAY + PT.getServerVersion ()).setScore(7);
        objective.getScore(ChatColor.DARK_RED + "Map " + ChatColor.GRAY + Rebug.getGetMain().getConfig().getString("map-version")).setScore(6); // user.getBrand() != null && user.getBrand().length() <= 16 ? 2 : 3
		
        // TO Be Updated:
        
        /*
        Team teams = scoreboard.registerNewTeam("rebug team#1");
        teams.addEntry(ChatColor.RED.toString());
        teams.setPrefix("Client ");
        teams.setSuffix("" + PT.getPlayerVersion(user.getProtocol()));
        objective.getScore(ChatColor.RED.toString()).setScore(6);
        
        teams = null;
        teams = scoreboard.registerNewTeam("rebug team#2");
        teams.addEntry(ChatColor.DARK_RED.toString());
        teams.setPrefix("Protocol ");
        teams.setSuffix("" + PT.getPlayerVersion(user.getPlayer()));
        objective.getScore(ChatColor.DARK_RED.toString()).setScore(5);
        if (user.getBrand().length() <= 16)
        {
        	teams = null;
            teams = scoreboard.registerNewTeam("rebug team#3");
            teams.addEntry(ChatColor.GRAY.toString());
            teams.setPrefix("");
            teams.setSuffix(user.getBrand());
            objective.getScore(ChatColor.GRAY.toString()).setScore(4);
            objective.getScore("").setScore(3);
        }
        */
        
		player.setScoreboard(scoreboard);
	}
	private void UpdateScoreBoard (Player p)
	{
		if (!Rebug.getGetMain().getConfig().getBoolean("rebug-scoreboard"))
			return;
			
		
		User user = Rebug.getUser(p);
		Player player = user.getPlayer();
		Scoreboard scoreboard = player.getScoreboard();
		/*
		Team team = scoreboard.getTeam("rebug team#1");
		team.setSuffix(PT.getPlayerVersion(user.getProtocol()));
		
		team = null;
		team = scoreboard.getTeam("rebug team#2");
		team.setSuffix("" + PT.getPlayerVersion(user.getPlayer()));
		
		team = null;
		if (scoreboard.getTeam("rebug team#3") != null)
		{
			team = scoreboard.getTeam("rebug team#3");
			team.setSuffix(user.getBrand());
		}
		*/
	}

	@Override
	public void run () 
	{
		//if (!Rebug.getGetMain().getConfig().getBoolean("rebug-scoreboard"))
	//		return;
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (player.getScoreboard() != null && player.getScoreboard().getObjective("Rebug") != null)
				UpdateScoreBoard(player);
			else
				CreateScoreBoard(player);
		}
	}
	
}
