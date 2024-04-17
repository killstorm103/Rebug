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
}
