package me.killstorm103.Rebug.Shared.PluginHooks;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.killstorm103.Rebug.Shared.Main.Config;
import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Server.ServerVersionUtil;
import me.killstorm103.Rebug.Shared.Utils.PT;
import me.killstorm103.Rebug.Shared.Utils.User;

public class PlaceholderapiHook extends PlaceholderExpansion 
{
	
	@Override
	public @NotNull String getAuthor() {
		return "killstorm103";
	}

	@Override
	public @NotNull String getIdentifier() {
		return "rebug";
	}

	@Override
	public @NotNull String getVersion() {
		return "1.1";
	}

	@Override
	public @Nullable String onRequest(OfflinePlayer offlineplayer, @NotNull String params) 
	{
		if (offlineplayer != null && offlineplayer.isOnline()) 
		{
			if (params.equalsIgnoreCase("plugin"))
				return ChatColor.DARK_RED + ChatColor.BOLD.toString() + "REBUG " + ChatColor.RESET + ChatColor.BOLD
						+ "v" + Rebug.PluginVersion();

			if (params.equalsIgnoreCase("server_version"))
			{
				if (ServerVersionUtil.Type == null)
					return ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Server" + ChatColor.WHITE + ": " + PT.getServerVersion() + ChatColor.RESET;
				else
					return ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Server" + ChatColor.WHITE + ": " + ServerVersionUtil.Type.name() + " " + PT.getServerVersion() + ChatColor.RESET;
			}

			if (params.equalsIgnoreCase("map_version"))
				return ChatColor.DARK_RED + "Map" + ChatColor.WHITE + ": " + Config.getMapVersion();

			if (params.equalsIgnoreCase("tabscoreboardtitle")) 
				return ChatColor.translateAlternateColorCodes('&', Config.TabScoreboardTitle());

			if (params.equalsIgnoreCase("scoreboardtitle")) 
				return ChatColor.translateAlternateColorCodes('&', Config.ScoreboardTitle());
			
			Player player = offlineplayer.getPlayer();
			if (player == null) return null;
			
			if (params.equalsIgnoreCase("user_ping"))
				return PT.getPing(player) + "";
			
			User user = Rebug.getUser(player);
			if (user == null) return null;
			
			if (params.equalsIgnoreCase("user_onGround"))
				return user.onGround + "";
			
			if (params.equalsIgnoreCase("user_isblocking"))
				return user.isBlocking + "";
			
			if (params.equalsIgnoreCase("user_bps_xz"))
				return user.BPSXZ;
			
			if (params.equalsIgnoreCase("user_bps_y"))
				return user.BPSY;
			
			if (params.equalsIgnoreCase("user_isSprinting"))
				return user.isSprinting + "";
			
			if (params.equalsIgnoreCase("user_packets_preSend"))
				return user.preSend + "";
			
			if (params.equalsIgnoreCase("user_packets_preReceive"))
				return user.preReceive + "";
			
			if (params.equalsIgnoreCase("user_packets_sendPacketCounts"))
				return user.sendPacketCounts + "";
			
			if (params.equalsIgnoreCase("user_packets_receivePacketCounts"))
				return user.receivePacketCounts + "";
			
			if (params.equalsIgnoreCase("user_clicks_preRight"))
				return user.preCPSRight + "";
			
			if (params.equalsIgnoreCase("user_clicks_preLeft"))
				return user.preCPSLeft + "";
			
			if (params.equalsIgnoreCase("user_clicks_right"))
				return user.ClicksPerSecondRight + "";
			
			if (params.equalsIgnoreCase("user_clicks_left"))
				return user.ClicksPerSecondLeft + "";
			
			if (params.equalsIgnoreCase("user_timer_balance"))
				return user.timer_balance + "";
			
			if (params.equalsIgnoreCase("client_version") || params.equalsIgnoreCase("client")) 
				return user.getVersion_Short();
			
			if (params.equalsIgnoreCase("ColoredAC"))
				return user.ColoredAC + "";
			
			if (params.equalsIgnoreCase("selected_anticheats"))
				return "" + user.SelectedAntiCheats;
			
			if (params.equalsIgnoreCase("anticheat")) 
			{
				if (user.SelectedAntiCheats > 1)
					return ChatColor.AQUA + "Multi" + ChatColor.RESET;

				String ac = user.getColoredAntiCheat() + ChatColor.RESET.toString(),
						strip = ChatColor.stripColor(ac).toLowerCase();
				
				if (strip.equalsIgnoreCase("Vanilla"))
					return ac;

				if (Rebug.getINSTANCE().getLoadedAntiCheatsFile().get("loaded-anticheats." + strip + ".has-short-name") != null && Rebug.getINSTANCE().getLoadedAntiCheatsFile().get("loaded-anticheats." + strip + ".short-name") != null && Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + strip + ".has-short-name"))
					ac = ac.replace(ac, ChatColor.translateAlternateColorCodes('&', Rebug.getINSTANCE().getLoadedAntiCheatsFile().getString("loaded-anticheats." + strip + ".short-name")) + ChatColor.RESET);

				return ac;
			}
		}

		return null;
	}

	public static void registerHook() {
		new PlaceholderapiHook().register();
	}
}
