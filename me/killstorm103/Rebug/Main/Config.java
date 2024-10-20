package me.killstorm103.Rebug.Main;

import java.util.List;

public class Config {
	public static final int getAntiCheatItemID(String AC) 
	{
		int ID = 2;
		try {
			ID = Rebug.getINSTANCE().getLoadedAntiCheatsFile().getInt("loaded-anticheats." + AC.toLowerCase() + ".item");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ID;
	}

	// Used for 1.13+ cause spigots gay!
	public static final String getAntiCheatItemNameID(String AC)
	{
		String ID = "";
		try {
			ID = Rebug.getINSTANCE().getLoadedAntiCheatsFile().getString("loaded-anticheats." + AC + ".item-name");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ID;
	}

	public static boolean Experimental_Features(String ex) {
		boolean found = false;
		for (String s : Rebug.getINSTANCE().getConfig().getStringList("experimental-features")) {
			if (s.equalsIgnoreCase(ex)) {
				found = true;
				break;
			}
		}
		if (found && Rebug.getINSTANCE().getConfig().getBoolean("experimental-feature-warning"))
			System.err.println(Rebug.RebugMessage + "Experimental Feature: " + ex);

		return found;
	}

	public static final int getItemMenuSize() {
		return Rebug.getINSTANCE().getLoadedItemsFile().getInt("Inventory-size");
	}

	public static final int getItemData(String ac, boolean manual)
	{
		int data = 0;
		try {
			data = manual ? Rebug.getINSTANCE().getLoadedAntiCheatsFile().getInt("manually-added-anticheats." + ac.toLowerCase() + ".data") : Rebug.getINSTANCE().getLoadedAntiCheatsFile().getInt("loaded-anticheats." + ac.toLowerCase() + ".data");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return data;
	}

	public static final int LoadDescriptionFix(String ac) {
		int got = 0;
		try {
			got = Rebug.getINSTANCE().getLoadedAntiCheatsFile()
					.getInt("loaded-anticheats." + ac + ".offset-description");
		} catch (Exception e) {
		}

		return got;
	}

	public static final List<String> getLoadedAntiCheats() {
		return Rebug.getINSTANCE().getLoadedAntiCheatsFile().getStringList("loaded-anticheats.ac-names");
	}

	public static final boolean RebugScoreBoard() {
		return Rebug.getINSTANCE().getConfig().getBoolean("rebug-scoreboard");
	}

	public static final String ScoreboardTitle() {
		return Rebug.getINSTANCE().getConfig().getString("scoreboard-title");
	}

	public static final boolean getAllowedToOverRideClientBrand() {
		return Rebug.getINSTANCE().getConfig().getBoolean("allow-overriding-client-brand");
	}

	public static final List<String> getServerRules() {
		return Rebug.getINSTANCE().getConfig().getStringList("server-rules");
	}

	public static final String getClientInfoSetting() {
		return Rebug.getINSTANCE().getConfig().getString("client-info-grab-fail");
	}

	public static final String getMapVersion() {
		return Rebug.getINSTANCE().getConfig().getString("map-version");
	}

	public static final String getCantOverrideClientBrand() {
		return Rebug.getINSTANCE().getConfig().getString("antiexploit-override-client-brand");
	}

	public static final boolean TellClientBrandOnJoin() {
		return Rebug.getINSTANCE().getConfig().getBoolean("tell-client-info-on-join");
	}

	public static final boolean TellClientRegisters() {
		return Rebug.getINSTANCE().getConfig().getBoolean("tell-client-registers");
	}

	public static final boolean AntiCancelClientBrandPacket() {
		return Rebug.getINSTANCE().getConfig().getBoolean("anti-cancel-client-brand-packet");
	}

	public static final int AntiCancelClientBrandCounter() {
		return Rebug.getINSTANCE().getConfig().getInt("anti-cancel-client-brand-counter");
	}

	public static final boolean ShouldCancelWeatherChanges() {
		return Rebug.getINSTANCE().getConfig().getBoolean("cancel-weather-changes");
	}

	public static final boolean ShouldForceGameMode() {
		return Rebug.getINSTANCE().getConfig().getBoolean("force-gamemode-on-join");
	}

	public static final String DiscordInviteLink() {
		return Rebug.getINSTANCE().getConfig().getString("discord-link");
	}

	public static final boolean ShouldDeletePlayerConfigAfterLoading() {
		return Rebug.getINSTANCE().getConfig().getBoolean("delete-player-config-after-loading");
	}

	public static final boolean getItemMenuDeleteItem() {
		return Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("delete-item");
	}

	public static final boolean getItemMenuDebugItem() {
		return Rebug.getINSTANCE().getLoadedItemsFile().getBoolean("debug-item");
	}
}
