package me.killstorm103.Rebug.PluginHooks;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.killstorm103.Rebug.Main.Rebug;
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
			
			if (params.equalsIgnoreCase("anticheat"))
			{
				String ac = user.AntiCheat + ChatColor.RESET;
				ac = ac.replace("NoCheatPlus", "NCP");
				return ac;
			}
			if (params.equalsIgnoreCase("client_version") || params.equalsIgnoreCase("client"))
				return user.getVersion_();
		}
		
		return null;
	}
	public static void registerHook ()
	{
		new PlaceholderapiHook().register();
	}
}
