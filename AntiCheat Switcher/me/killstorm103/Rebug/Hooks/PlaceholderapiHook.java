package me.killstorm103.Rebug.Hooks;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Utils.FileUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.QuickConfig;
import me.killstorm103.Rebug.Utils.ServerVersionUtil;
import me.killstorm103.Rebug.Utils.User;

@SuppressWarnings("deprecation")
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
		return "1.3";
	}
    
	@Override
	public @Nullable String onRequest(OfflinePlayer offlineplayer, @NotNull String params) 
	{
		if (offlineplayer != null && offlineplayer.isOnline()) 
		{
			if (params.equalsIgnoreCase("server_bedrock_support"))
				return ServerVersionUtil.isBedRockSupported() ? "Supported" : "Unsupported";
			
			if (params.equalsIgnoreCase("plugin"))
				return ChatColor.DARK_RED + ChatColor.BOLD.toString() + "REBUG " + ChatColor.RESET + ChatColor.BOLD + "v" + RebugsAntiCheatSwitcherPlugin.getPluginVersion();

			if (params.equalsIgnoreCase("server_version"))
				return ServerVersionUtil.getVersionName() + " (" + ServerVersionUtil.getServer_Version() + ")" + ChatColor.RESET;

			if (params.equalsIgnoreCase("server_software_name"))
				return ServerVersionUtil.getSoftWareType() + "";
				
			if (params.equalsIgnoreCase("server_software_version"))
				return ServerVersionUtil.getVersionOfSoftWare() + "";
			
			if (params.equalsIgnoreCase("server_software_isspoofed"))
				return ServerVersionUtil.isSpoofedSoftWare() + "";
			
			if (params.equalsIgnoreCase("server_software_detectedtypes"))
				return ServerVersionUtil.getDetectedSoftWares() + "";
				
			if (params.equalsIgnoreCase("server_networkid"))
				return ServerVersionUtil.getNetworkId() + "";
				
			if (params.equalsIgnoreCase("tabscoreboardtitle")) 
				return ChatColor.translateAlternateColorCodes('&', QuickConfig.TabScoreboardTitle());

			if (params.equalsIgnoreCase("scoreboardtitle")) 
				return ChatColor.translateAlternateColorCodes('&', QuickConfig.ScoreboardTitle());
			
			Player player = offlineplayer.getPlayer();
			if (player == null) return null;
			
			if (params.equalsIgnoreCase("user_ping"))
				return PT.getPing(player) + "";
			
			User user = RebugsAntiCheatSwitcherPlugin.getUser(player);
			if (user == null) return null;
			
			
			// Booleans
			if (params.equalsIgnoreCase("user_onGround"))
				return PT.ReturnBooleanType("onGround", user.isOnGround());
			
			if (params.equalsIgnoreCase("user_isMoving"))
				return PT.ReturnBooleanType("isMoving", user.isMoving());
			
			if (params.equalsIgnoreCase("user_isblocking"))
				return PT.ReturnBooleanType("isBlocking", user.isBlocking()); // user.isBlocking()
			
			if (params.equalsIgnoreCase("user_bukkit_isblocking"))
				return PT.ReturnBooleanType("isBlocking", user.getPlayer().isBlocking()); // user.isBlocking()
			
			if (params.equalsIgnoreCase("user_isSprinting"))
				return PT.ReturnBooleanType("isSprinting", user.isSprinting());
			
			if (params.equalsIgnoreCase("user_isSneaking"))
				return PT.ReturnBooleanType("isSneaking", user.isSneaking());
			
			
			// Normal
			if (params.equalsIgnoreCase("clientversion") || params.equalsIgnoreCase("client_version"))
				return PT.getVersion(user) + "";
			
			if (params.equalsIgnoreCase("clientprotocol") || params.equalsIgnoreCase("client_protocol"))
				return PT.getProtocolVersion(user) + "";
			
			if (params.equalsIgnoreCase("client") || params.equalsIgnoreCase("clientinfo") || params.equalsIgnoreCase("client_info"))
				return PT.getVersion(user) + " " + ChatColor.DARK_GRAY + "| " + ChatColor.WHITE + PT.getProtocolVersion(user);
			
			
			if (params.equalsIgnoreCase("user_bps_xz"))
				return user.getBPSXZ();
			
			if (params.equalsIgnoreCase("user_bps_y"))
				return user.getBPSY();
			
			if (params.equalsIgnoreCase("user_packets_preSend"))
				return user.getPreSendPacket() + "";
			
			if (params.equalsIgnoreCase("user_packets_preReceive"))
				return user.getPreReceivePacket() + "";
			
			if (params.equalsIgnoreCase("user_packets_sendPacketCounts"))
				return user.getSendPacketCounts() + "";
			
			if (params.equalsIgnoreCase("user_packets_receivePacketCounts"))
				return user.getReceivePacketCounts() + "";
			
			if (params.equalsIgnoreCase("user_clicks_preRight"))
				return user.getPreCPSRight() + "";
			
			if (params.equalsIgnoreCase("user_clicks_preLeft"))
				return user.getPreCPSLeft() + "";
			
			if (params.equalsIgnoreCase("user_clicks_right"))
				return user.getClicksPerSecondRight() + "";
			
			if (params.equalsIgnoreCase("user_clicks_left"))
				return user.getClicksPerSecondLeft() + "";
			
			if (params.equalsIgnoreCase("user_timer_balance"))
				return user.getTimer_balance() + "";
			
			if (params.equalsIgnoreCase("ColoredAC"))
				return user.getColoredAC() + "";
			
			if (params.equalsIgnoreCase("selected_anticheats"))
				return "" + user.getSelectedAntiCheats();
			
			if (params.equalsIgnoreCase("anticheat")) 
			{
				if (user.getSelectedAntiCheats() > 1)
					return ChatColor.AQUA + "Multi" + ChatColor.RESET;

				String ac = user.getColoredAntiCheat() + ChatColor.RESET.toString(),
						strip = ChatColor.stripColor(ac).toLowerCase();
				
				if (strip.equalsIgnoreCase("Vanilla"))
					return ac;

				if (FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + strip + ".has-short-name", false) && !PT.isStringNull(FileUtils.getLoadedAntiCheatsFile().getString("loaded-anticheats." + strip + ".short-name", "")))
					ac = ac.replace(ac, ChatColor.translateAlternateColorCodes('&', FileUtils.getLoadedAntiCheatsFile().getString("loaded-anticheats." + strip + ".short-name", ac)) + ChatColor.RESET);

				return ac;
			}
		}

		return null;
	}

	public static void registerHook() {
		new PlaceholderapiHook().register();
	}
}