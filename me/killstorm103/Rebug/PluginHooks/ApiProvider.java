package me.killstorm103.Rebug.PluginHooks;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import ac.grim.grimac.api.GrimAbstractAPI;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PTNormal;

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
		if (player == null || PTNormal.isStringNull(AntiCheat)) return;
		
		switch (AntiCheat.toLowerCase())
		{
		case "grimac":
			if (!getGrimAPI().getAlertManager().hasVerboseEnabled(player))
			{
				getGrimAPI().getAlertManager().toggleVerbose(player);
				player.sendMessage(Rebug.RebugMessage + "Enabled Verbose Alerts for " + AntiCheat);
			}
			break;

		default:
			Rebug.getINSTANCE().Log(Level.WARNING, "This AntiCheat (" + AntiCheat + ") does not have a Api!");
			break;
		}
	}
}
