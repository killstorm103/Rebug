package me.killstorm103.Rebug.Shared.PluginHooks.Apis;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import ac.grim.grimac.api.GrimAbstractAPI;

public class GrimAC 
{
	private static  GrimAbstractAPI GrimApi;
	public static GrimAbstractAPI getGrimAPI ()
	{
		RegisteredServiceProvider<GrimAbstractAPI> provider = Bukkit.getServicesManager().getRegistration(GrimAbstractAPI.class);
		if (provider != null)
		    GrimApi = provider.getProvider();
		
		return GrimApi;
	}
}
