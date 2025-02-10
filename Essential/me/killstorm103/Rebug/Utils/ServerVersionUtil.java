package me.killstorm103.Rebug.Utils;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;
import me.killstorm103.Rebug.RebugPlugin;

public class ServerVersionUtil 
{
	@Getter
	private static ServerVersionUtil INSTANCE;
	
	public ServerVersionUtil (@NotNull String type, boolean bedrocksupport)
	{
		INSTANCE = this;
		SoftWareType = type;
		isBedRockSupported = bedrocksupport;
	}
	@Getter @Setter
	private static boolean isBedRockSupported, isServerNewerThan1_8 = true, isServerNewerOrEquals1_19 = false, isSpoofedSoftWare = false, isFolia;
	@Getter @Setter
	private static String Server_Version = "", VersionName = "", VersionOfSoftWare = "", SoftWareType = "";
	@Getter @Setter
	private static int networkId, DetectedSoftWares;
	
	private static String setForce (String name)
	{
		if (!FileUtils.getServerConfig().getBoolean("server.softwares.force.set-name", false)) return name;
		
		setDetectedSoftWares(1);
		return FileUtils.getServerConfig().getString("server.softwares.force.name", PT.isStringNull(name) ? "Unknown" : name);
	}
	public static void CreateINSTANCE (String name)
	{
		setFolia(isServerType("Folia"));
		String toString = "";
		final int size = FileUtils.getServerConfig().getStringList("server.softwares.force.scan.types.default").size();
		int detected = 0;
		for (int i = 0; i < size; i ++)
		{
			String ST = FileUtils.getServerConfig().getStringList("server.softwares.force.scan.types.default").get(i);
			if (isServerType(ST))
			{
				detected ++;
				toString += ST + (i == size - 1 ? "" : "/"); 
			}
		}
		if (FileUtils.getServerConfig().getStringList("server.softwares.force.scan.types." + getServer_Version()) != null)
		{
			final int ScanForVersion = FileUtils.getServerConfig().getStringList("server.softwares.force.scan.types." + getServer_Version()).size();
			for (int i = 0; i < ScanForVersion; i ++)
			{
				String ST = FileUtils.getServerConfig().getStringList("server.softwares.force.scan.types." + getServer_Version()).get(i);
				if (isServerType(ST))
				{
					detected ++;
					if (detected > 1)
						toString += "/" + ST + (i == ScanForVersion - 1 ? "" : "/"); 
					else
						toString += ST + (i == ScanForVersion - 1 ? "" : "/"); 
					
				}
			}
		}
		setDetectedSoftWares(detected > 0 ? detected : 1);
		if (toString != null && toString.length() > 0)
		{
			if (FileUtils.getServerConfig().getBoolean("server.softwares.force.scan.alert-only", false))
			{
				RebugPlugin.getINSTANCE().Log(Level.INFO, "While Force Scanning Rebug Detected the SoftWare Type" + (detected > 1 ? "s" : "") + ":");
				RebugPlugin.getINSTANCE().Log(Level.INFO, toString);
				RebugPlugin.getINSTANCE().Log(Level.INFO, "But we're not setting it to that cause you have alert-only enabled in Server.yml!");
			}
			else
				name =  toString;

			// TODO Add a min check if paths found for it is < than ....
		}
		if (FileUtils.getServerConfig().getBoolean("server.softwares.force.set-version", false))
		{
			setSpoofedSoftWare(true);
			setVersionOfSoftWare(FileUtils.getServerConfig().getString("server.softwares.force.version", "Hidden"));
		}
		new ServerVersionUtil(name, false);
		if (!isSpoofedSoftWare() && FileUtils.getServerConfig().get("server.softwares.types." + ServerVersionUtil.getSoftWareType()) != null && ServerVersionUtil.getDetectedSoftWares() == 1)
		{
			try
			{
				boolean foundATSV = FileUtils.getServerConfig().get("server.softwares.types." + ServerVersionUtil.getSoftWareType() + ".grab-version-from-at." + ServerVersionUtil.getServer_Version()) != null;
				String toString2 = "", grab = 
			    FileUtils.getServerConfig().getConfigurationSection("server.softwares.types." + ServerVersionUtil.getSoftWareType()).getString("grab-version-from-at." + (foundATSV ? ServerVersionUtil.getServer_Version() : "default"), "1")
				.replace(", ", " ").replace(":", " ").replace("-", " ").replace("_", " ").replace("|", " "),
				GrabSplit = Bukkit.getVersion();
				for (String s : FileUtils.getServerConfig().getStringList("server.softwares.replace"))
					GrabSplit = GrabSplit.replace(s, " ");
				
				String[] split = PT.SplitString(GrabSplit), Grabfrom = PT.SplitString(grab);
				if (Grabfrom.length == 1)
					toString2 = split[Integer.parseInt(Grabfrom[0])];
				
				else
				{
					for (int i = 0; i < split.length; i ++)
					{
						for (int o = 0; o < Grabfrom.length; o ++)
						{
							int number = Integer.parseInt(Grabfrom[o]);
							if (number == i)
								toString2 += split[i] + (o == Grabfrom.length - 1 ? "" : ":");
						}
					}
				}
				ServerVersionUtil.setVersionOfSoftWare(toString2);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Failed to get the Server Software Version");
				ServerVersionUtil.setVersionOfSoftWare("Unknown");
			}
		}
		setSoftWareType(FileUtils.getServerConfig().getString("server.softwares.types." + name + ".name", name));
		name = setForce(name);
	}
	public static boolean isServerType (@NotNull String type) 
	{
		if (PT.isStringNull(type)) 
		{
			RebugPlugin.getINSTANCE().Log(Level.SEVERE, "type inside isServerType is @NotNull but is Null!");
			return false;
		}
		if (FileUtils.getServerConfig().get("server.softwares.types." + type) == null) return false;
		
		String path = type + ".path." + (FileUtils.getServerConfig().getConfigurationSection("server.softwares.types").get(type + ".path." + getServer_Version()) != null ? getServer_Version() : "default");
		final int size = FileUtils.getServerConfig().getConfigurationSection("server.softwares.types").getStringList(path).size();
		for (int i = 0; i < size; i ++)
		{
			try
			{
				Class.forName(FileUtils.getServerConfig().getConfigurationSection("server.softwares.types").getStringList(path).get(i).replace("/", ".").replace("\"", "."));
				return true;
			}
			catch (ClassNotFoundException e) 
			{
				if (i == size)
				{
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
    }
}
