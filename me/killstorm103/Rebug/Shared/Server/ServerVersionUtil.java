package me.killstorm103.Rebug.Shared.Server;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;

public class ServerVersionUtil 
{
	private static ServerVersionUtil INSTANCE;
	public static ServerVersionUtil getINSTANCE ()
	{
		return INSTANCE;
	}
	public ServerVersionUtil (@NotNull SoftWare type)
	{
		INSTANCE = this;
		Type = type;
		Type.version = Server_Version;
	}
	public static String Server_Version, major, minor;
	public static SoftWare Type;
	public static boolean isRunningSpigot (boolean INSTANCE)
	{
		boolean isSpigot = ServerVersionUtil.isServerType(SoftWare.Paper) || ServerVersionUtil.isServerType(SoftWare.Spigot) || ServerVersionUtil.isServerType(SoftWare.PandaSpigot);
		if (INSTANCE)
			new ServerVersionUtil(isSpigot ? ServerVersionUtil.isServerType(SoftWare.Paper) ? SoftWare.Paper : ServerVersionUtil.isServerType(SoftWare.PandaSpigot) ? SoftWare.PandaSpigot : SoftWare.Spigot : SoftWare.Bukkit);
		
		return isSpigot;
	}
	
	
	public static boolean isBukkit ()
	{
		int isBukkit = SoftWare.values().length;
		for (SoftWare types : SoftWare.values())
		{
			if (types != SoftWare.Bukkit && !isServerType(types))
				isBukkit --;
		}
		return isBukkit == 1;
	}
	
	public static boolean isServerType(@NotNull SoftWare type) 
	{
		String path = null;
		switch (type.name.toLowerCase())
		{
		case "spigot":
			path = "org.spigotmc.SpigotConfig";
			break;
			
		case "folia":
			path = "io.papermc.paper.threadedregions.RegionizedServer";
			break;
			
		case "paper":
			try
			{
				Class.forName("com.destroystokyo.paper.ParticleBuilder");
				return true;
			}
			catch (Throwable e)
			{
				path = "io.papermc.paperclip.Paperclip";
			}
			break;
			
		case "pandaspigot":
			path = "com.hpfxd.pandaspigot.paperclip.Paperclip";
			break;
			
		default:
			return false;
		}
        try
        {
            Class.forName(path);
            return true;
        }
        catch (Throwable e) 
        {
            return false;
        }
    }
	public enum SoftWare 
	{
		Paper ("Paper", ""), PandaSpigot ("PandaSpigot", ""), Spigot ("Spigot", ""), Bukkit ("Bukkit", ""), 
		Velocity ("Velocity", ""), Waterfall ("Waterfall", ""), BungeeCord ("BungeeCord", ""),
		Folia ("Folia", ""), Fabric ("Fabric", ""),
		Sponge ("Sponge", "");
		@Getter
		private final String name;
		@Getter
		private String version;
		SoftWare (String name, String version)
		{
			this.name = name;
			this.version = version;
		}
	}
}
