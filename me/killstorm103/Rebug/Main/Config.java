package me.killstorm103.Rebug.Main;

import java.util.List;


public class Config
{
	public static final boolean AllowSubCommands ()
	{
		return Rebug.GetMain().getConfig().getBoolean("allow-sub-commands");
	}
	public static final List<String> getOnJoinCommands ()
	{
		return Rebug.getINSTANCE().getConfig().getStringList("on-join-commands");
	}
	public static final int getAntiCheatItemID (String AC)
	{
		int ID =- Integer.MAX_VALUE;
		try
		{
			ID = Rebug.GetMain().getLoadedAntiCheatsFile().getInt("loaded-anticheats." + AC + ".item");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ID;
	}
	public static final List<String> getItems_Menu ()
	{
		return Rebug.GetMain().getLoadedItemsFile().getStringList("items-menu.items.list");
	}
	public static final int getItemMenuSize ()
	{
		return Rebug.GetMain().getLoadedItemsFile().getInt("Inventory-size");
	}
	public static final boolean hasData (String ac)
	{
		return Rebug.GetMain().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + ac + ".has-data");
	}
	public static final int getInventory_size ()
	{
		return Rebug.GetMain().getLoadedAntiCheatsFile().getInt("loaded-anticheats.Inventory-size");
	}
	public static final int getItemData (String ac)
	{
		int data = 0;
		try
		{
			data = Rebug.GetMain().getLoadedAntiCheatsFile().getInt("loaded-anticheats." + ac + ".data");
		}
		catch (Exception e) {}
		
		return data;
	}
	public static final boolean hasFixedDescription (String ac)
	{
		return Rebug.GetMain().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + ac + ".fix-description");
	}
	public static final int LoadDescriptionFix (String ac)
	{
		int got = 0;
		try
		{
			got = Rebug.GetMain().getLoadedAntiCheatsFile().getInt("loaded-anticheats." + ac + ".offset-description");
		}
		catch (Exception e) {}
		
		return got;
	}
	public static final List<String> getLoadedAntiCheats ()
	{
		return Rebug.GetMain().getLoadedAntiCheatsFile().getStringList("loaded-anticheats.ac-names");
	}
	public static final boolean RebugScoreBoard ()
	{
		return Rebug.GetMain().getConfig().getBoolean("rebug-scoreboard");
	}
	public static final String ScoreboardTitle ()
	{
		return Rebug.GetMain().getConfig().getString("scoreboard-title");
	}
	public static final boolean getAllowedToOverRideClientBrand() 
	{
		return Rebug.GetMain().getConfig().getBoolean("allow-overriding-client-brand");
	}
	public static final List<String> getServerRules ()
	{
		return Rebug.GetMain().getConfig().getStringList("server-rules");
	}
	public static final String getClientInfoSetting ()
    {
    	return Rebug.GetMain().getConfig().getString("client-info-grab-fail");
    }
	public static final String getMapVersion ()
    {
    	return Rebug.GetMain().getConfig().getString("map-version");
    }
	public static final String getCantOverrideClientBrand() 
	{
		return Rebug.GetMain().getConfig().getString("antiexploit-override-client-brand");
	}
	public static final boolean TellClientBrandOnJoin() {
		return Rebug.GetMain().getConfig().getBoolean("tell-client-info-on-join");
	}
	public static final boolean TellClientRegisters()
	{
		return Rebug.GetMain().getConfig().getBoolean("tell-client-registers");
	}
	public static final boolean AntiCancelClientBrandPacket() 
	{
		return Rebug.GetMain().getConfig().getBoolean("anti-cancel-client-brand-packet");
	}
	public static final int AntiCancelClientBrandCounter()
	{
		return  Rebug.GetMain().getConfig().getInt("anti-cancel-client-brand-counter");
	}
	public static final boolean ShouldCancelWeatherChanges() {
		return Rebug.GetMain().getConfig().getBoolean("cancel-weather-changes");
	}
	public static final boolean ShouldForceGameMode() {
		return Rebug.GetMain().getConfig().getBoolean("force-gamemode-on-join");
	}
	public static final String DiscordInviteLink() 
	{
		return Rebug.GetMain().getConfig().getString("discord-link");
	}
	public static final boolean ShouldDeletePlayerConfigAfterLoading() 
	{
		return Rebug.GetMain().getConfig().getBoolean("delete-player-config-after-loading");
	}
	public static final boolean getItemMenuDeleteItem()
	{
		return Rebug.GetMain().getLoadedItemsFile().getBoolean("delete-item");
	}
	public static final boolean getItemMenuDebugItem()
	{
		return Rebug.GetMain().getLoadedItemsFile().getBoolean("debug-item");
	}
}
