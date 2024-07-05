package me.killstorm103.Rebug.PluginHooks;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;
import net.md_5.bungee.api.ChatColor;

public class PlaceholderapiHook extends PlaceholderExpansion
{

	@Override
	public @NotNull String getAuthor () 
	{
		return "killstorm103";
	}

	@Override
	public @NotNull String getIdentifier ()
	{
		return "rebug";
	}

	@Override
	public @NotNull String getVersion () 
	{
		return "1.0";
	}
	@Override
	public @Nullable String onRequest (OfflinePlayer offlineplayer, @NotNull String params) 
	{
		if (offlineplayer != null && offlineplayer.isOnline())
		{
			Player player = offlineplayer.getPlayer();
			User user = Rebug.getUser(player);
			if (user == null) return null;
			
			if (params.equalsIgnoreCase("plugin"))
				return ChatColor.DARK_RED + ChatColor.BOLD.toString() + "REBUG " + ChatColor.RESET + ChatColor.BOLD + "v" + Rebug.PluginVersion();
			
			if (params.equalsIgnoreCase("server_version"))
				return ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Server" + ChatColor.WHITE + ": Spigot " + PT.getServerVersion ();
			
			if (params.equalsIgnoreCase("map_version"))
				return ChatColor.DARK_RED + "Map" + ChatColor.WHITE + ": "+ Config.getMapVersion();
			
			if (params.equalsIgnoreCase("anticheat"))
			{
				String ac = user.getColoredAntiCheat() + ChatColor.RESET, strip = ChatColor.stripColor(ac).toLowerCase();
				if (strip.equalsIgnoreCase("Vanilla"))
					return ac;
				
				if (Rebug.GetMain().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + strip + ".has-short-name"))
					ac = ac.replace(ac, ChatColor.translateAlternateColorCodes('&', Rebug.getINSTANCE().getLoadedAntiCheatsFile().getString("loaded-anticheats." + strip + ".short-name")) + ChatColor.RESET);
				
				
				return ac;
			}
			if (params.equalsIgnoreCase("testserver"))
			{
				return ChatColor.translateAlternateColorCodes('&', ChatColor.DARK_GRAY + "| " + Config.ScoreboardTitle() + " ") + ChatColor.DARK_GRAY + "§cTest §2Server " + ChatColor.DARK_GRAY + "|";
			}
			if (params.equalsIgnoreCase("client_version") || params.equalsIgnoreCase("client"))
			{
				return user.getVersion_Short();
			}
		}
		
		return null;
	}
	public static void registerHook ()
	{
		new PlaceholderapiHook().register();
	}
}
