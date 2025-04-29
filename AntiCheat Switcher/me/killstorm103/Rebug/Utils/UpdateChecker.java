package me.killstorm103.Rebug.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.retrooper.packetevents.protocol.chat.ChatTypes;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

public class UpdateChecker 
{
	// TODO Add it UpdateChecker to Rebug when it's on Spigot
	
	private final long Ticks = 432000L;
	// TODO Change when it's on Spigot
	private final int ResourceID = 1;
	private final String 
	SpigotResourcePage = "https://www.spigotmc.org/resources/" + ResourceID,
	SpigotApiPage = "https://api.spigotmc.org/legacy/update.php?resource=" + ResourceID;
	private boolean updateAvailable;
    private int versionsBehind;
    private String SpigotVersion;
    
    public void init () 
    {
    	Scheduler.runTimerAsynchronously(new Runnable() 
    	{
			@Override
			public void run() 
			{
				RebugsAntiCheatSwitcherPlugin.Log(Level.INFO, "Checking for updates!");
	            String currentVersion = RebugsAntiCheatSwitcherPlugin.getPluginVersion();
	            SpigotVersion = getSpigotVersion();
	            versionsBehind = calculate(SpigotVersion, currentVersion);
	            updateAvailable = versionsBehind > 0;
	            if (updateAvailable)
	            {
	                RebugsAntiCheatSwitcherPlugin.Log(Level.INFO, "You are " + versionsBehind + " versions" + " behind! Get " + RebugsAntiCheatSwitcherPlugin.getPluginName() + RebugsAntiCheatSwitcherPlugin.getEdition() + "(v" + SpigotVersion + ") at:");
	                RebugsAntiCheatSwitcherPlugin.Log(Level.INFO, SpigotResourcePage);
	                for (Player player : Bukkit.getOnlinePlayers()) 
	                    notifyPlayer(player);
	            } 
	            else 
	            	RebugsAntiCheatSwitcherPlugin.Log(Level.INFO, "No new version found!");
			}
		}, 1L, Ticks);
    }
    
    private int getVersion (String version) 
    {
        return Integer.parseInt(version.replace(".", ""));
    }

    private int calculate (String current, String old) 
    {
        return getVersion(current) - getVersion(old);
    }
	@SuppressWarnings("deprecation")
	private String getSpigotVersion () 
    {
        try
        {
            URLConnection connection = (new URL(SpigotApiPage)).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            
            label30: 
            {
                String var4;
                try 
                {
                    String version = reader.readLine();
                    if (version.length() > 7)
                        break label30;
                    
                    var4 = version;
                }
                catch (Throwable var6) 
                {
                    try 
                    {
                        reader.close();
                    } 
                    catch (Throwable var5)
                    {
                        var6.addSuppressed(var5);
                    }

                    throw var6;
                }
                reader.close();
                return var4;
            }

            reader.close();
        } 
        catch (Exception e) {}

        return "0";
    }
    public void notifyPlayer(Player player) 
    {
    	if (!updateAvailable || !PT.hasAdminPerms(player)) return;

    	PT.sendChat(player, "", ChatTypes.CHAT, Component.text("You are " + this.versionsBehind + " versions" + " behind! Click here to get " + RebugsAntiCheatSwitcherPlugin.getPluginName() + RebugsAntiCheatSwitcherPlugin.getEdition() + "(v" + SpigotVersion + ")!").clickEvent(ClickEvent.openUrl(SpigotResourcePage)).hoverEvent(HoverEvent.showText(Component.text("Click here to go to the download page!"))));
    }
}
