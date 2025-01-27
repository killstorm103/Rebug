package me.killstorm103.Rebug.Main;

import java.util.List;

import me.killstorm103.Rebug.Utils.FileUtils;

public class Config 
{
	public static final List<String> getLoadedAntiCheats() {
		return FileUtils.getLoadedAntiCheatsFile().getStringList("loaded-anticheats.ac-names");
	}
	public static final int LoadDescriptionFix(String ac) {
		int got = 0;
		try {
			got = FileUtils.getLoadedAntiCheatsFile()
					.getInt("loaded-anticheats." + ac + ".offset-description");
		} catch (Exception e) {
		}

		return got;
	}
	public static final String ScoreboardTitle() {
		return RebugPlugin.getINSTANCE().getConfig().getString("scoreboard.title");
	}
	public static final String TabScoreboardTitle() {
		return RebugPlugin.getINSTANCE().getConfig().getString("tab.title");
	}
	public static final String DiscordInviteLink() {
		return RebugPlugin.getINSTANCE().getConfig().getString("discord.link");
	}
}
