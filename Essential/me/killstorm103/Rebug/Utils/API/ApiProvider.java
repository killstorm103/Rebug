package me.killstorm103.Rebug.Utils.API;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import ac.grim.grimac.api.GrimAbstractAPI;
import me.killstorm103.Rebug.Main.RebugPlugin;
import me.killstorm103.Rebug.Utils.PT;

public class ApiProvider 
{
	public static final ApiProvider API_PROVIDER = new ApiProvider();
	
	private static  GrimAbstractAPI GrimApi;
	public static GrimAbstractAPI getGrimAPI ()
	{
		RegisteredServiceProvider<GrimAbstractAPI> provider = Bukkit.getServicesManager().getRegistration(GrimAbstractAPI.class);
		if (provider != null)
		    GrimApi = provider.getProvider();
		
		return GrimApi;
	}
	public static void onApi (Player player, String AntiCheat)
	{
		if (player == null || PT.isStringNull(AntiCheat)) return;
		
		Plugin AntiCheatPlugin = Bukkit.getPluginManager().getPlugin(AntiCheat);
		if (AntiCheatPlugin == null || !AntiCheatPlugin.isEnabled()) 
		{
			RebugPlugin.getINSTANCE().Log(Level.WARNING, "AntiCheat: " + AntiCheat + " was not found or was Disabled when trying to get it's Api!");
			return;
		}
		switch (AntiCheat.toLowerCase())
		{
		case "grimac":
			if (!getGrimAPI().getAlertManager().hasVerboseEnabled(player))
			{
				getGrimAPI().getAlertManager().toggleVerbose(player);
				player.sendMessage(RebugPlugin.getRebugMessage() + "Enabled Verbose Alerts for " + AntiCheat);
			}
			break;
			
		default:
			RebugPlugin.getINSTANCE().Log(Level.WARNING, "This AntiCheat (" + AntiCheat + ") does not have a Api!");
			break;
		}
	}
}
