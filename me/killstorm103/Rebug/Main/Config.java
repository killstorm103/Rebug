package me.killstorm103.Rebug.Main;

public class Config
{
	public static final boolean getAllowedToOverRideClientBrand() 
	{
		return Rebug.getGetMain().getConfig().getBoolean("allow-overriding-client-brand");
	}
	public static final String getClientInfoSetting ()
    {
    	return Rebug.getGetMain().getConfig().getString("client-info-grab-fail");
    }
	public static final String getCantOverrideClientBrand() 
	{
		return Rebug.getGetMain().getConfig().getString("antiexploit-override-client-brand");
	}
	public static final boolean TellClientBrandOnJoin() {
		return Rebug.getGetMain().getConfig().getBoolean("tell-client-info-on-join");
	}
	public static final boolean TellClientRegisters()
	{
		return Rebug.getGetMain().getConfig().getBoolean("tell-client-registers");
	}
	public static final boolean AntiCancelClientBrandPacket() 
	{
		return Rebug.getGetMain().getConfig().getBoolean("anti-cancel-client-brand-packet");
	}
	public static final int AntiCancelClientBrandCounter()
	{
		return  Rebug.getGetMain().getConfig().getInt("anti-cancel-client-brand-counter");
	}
	public static final boolean ShouldCancelWeatherChanges() {
		return Rebug.getGetMain().getConfig().getBoolean("cancel-weather-changes");
	}
	public static final boolean ShouldForceGameMode() {
		return Rebug.getGetMain().getConfig().getBoolean("force-gamemode-on-join");
	}
}
