package me.killstorm103.Rebug.Shared.Server.ConsoleFilter;

import java.util.logging.Level;

import com.github.retrooper.packetevents.manager.server.ServerVersion;

import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Server.ConsoleFilter.Engines.*;
import me.killstorm103.Rebug.Shared.Utils.PT;

public class ConsoleFilter 
{
	private Engine engine;
	public static final ConsoleFilter INSTANCE = new ConsoleFilter();
	public Engine getEngine ()
	{
		if (engine == null)
			engine = PT.isServerOlderThan(ServerVersion.V_1_13) ? new OldEngine () : new NewEngine();
		
		return engine;
	}
	
	public void HideConsoleMessages () 
	{
		if (!Rebug.getINSTANCE().getConfig().getBoolean("ConsoleFilter.enabled")) return;
		
		if (Rebug.getINSTANCE().getConfig().getStringList("ConsoleFilter.blocked").isEmpty())
		{
			Rebug.getINSTANCE().Log(java.util.logging.Level.WARNING, "ConsoleFilter is Enabled but ConsoleFilter.blocked is Empty!");
			return;
		}
		Rebug.getINSTANCE().Log(java.util.logging.Level.INFO, "ConsoleFilter - Adding Filter");
		getEngine().HideConsoleMessages();
		Rebug.getINSTANCE().Log(Level.INFO, "Hidden Messages: " + Rebug.getINSTANCE().getConfig().getStringList("ConsoleFilter.blocked").size());
    }
}
