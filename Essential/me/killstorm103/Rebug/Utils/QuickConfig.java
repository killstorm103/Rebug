package me.killstorm103.Rebug.Utils;

import java.util.List;

import me.killstorm103.Rebug.RebugPlugin;

public class QuickConfig 
{
	public static final List<String> getLoadedAntiCheats() 
	{
		return FileUtils.getLoadedAntiCheatsFile().getStringList("loaded-anticheats.ac-names");
	}
	public static final int LoadDescriptionFix(String ac) {
		return FileUtils.getLoadedAntiCheatsFile().getInt("loaded-anticheats." + ac + ".offset-description", 0);
	}
	public static final String ScoreboardTitle() {
		return RebugPlugin.getINSTANCE().getConfig().getString("scoreboard.title", "&7| &l&7killstorm103''s&r &7|");
	}
	public static final String TabScoreboardTitle() {
		return RebugPlugin.getINSTANCE().getConfig().getString("tab.title", "&8| &l&7killstorm103''s&r &8| &cTest &2Server &8|");
	}
	public static final String DiscordInviteLink() {
		return RebugPlugin.getINSTANCE().getConfig().getString("discord.link", "https://discord.gg/5xWHx3MUUr");
	}
	public static final String MainLabel ()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.settings.main-command.label", "rebug");
	}
}
