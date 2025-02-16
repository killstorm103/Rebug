package me.killstorm103.Rebug.Utils.API.AntiCheats;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import ac.grim.grimac.api.GrimAbstractAPI;
import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Utils.FileUtils;
import me.killstorm103.Rebug.Utils.API.ApiProvider;

@SuppressWarnings("deprecation")
public class GrimAC extends ApiProvider
{
	GrimAbstractAPI GrimApi;
	GrimAbstractAPI getGrimAPI ()
	{
		GrimAbstractAPI GrimApi = null;
		RegisteredServiceProvider<GrimAbstractAPI> provider = Bukkit.getServicesManager().getRegistration(GrimAbstractAPI.class);
		if (provider != null)
		    GrimApi = provider.getProvider();
		
		return GrimApi;
	}
	
	@Override
	public void onHandle(Player player, String AntiCheat) throws Exception 
	{
		if (GrimApi == null)
			GrimApi = getGrimAPI();
		
		if (GrimApi == null)
		{
			RebugPlugin.getINSTANCE().Log(Level.SEVERE, AntiCheat + "'s Api was Null!");
			return;
		}
		if (FileUtils.getLoadedAntiCheatsFile().getBoolean ("loaded-anticheats." + AntiCheat.toLowerCase() + ".api.check.for-disabled-alerts", true) && GrimApi.getAlertManager().hasVerboseEnabled(player))
			return;
			
		GrimApi.getAlertManager().toggleVerbose(player);
		player.sendMessage(RebugPlugin.getRebugMessage() + (GrimApi.getAlertManager().hasVerboseEnabled(player) ? ChatColor.GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled") + ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + " Verbose Alerts for " + AntiCheat);
	}
}
