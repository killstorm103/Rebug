package me.killstorm103.Rebug.Utils.API;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Utils.FileUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.ReflectionUtils;

public abstract class ApiProvider 
{
	public abstract void onHandle (Player player, String AntiCheat) throws Exception;
	
	@SuppressWarnings("deprecation")
	public static void onApi (Player player, String AntiCheat, int AmountOfAntiCheats, int Normal_Is_Minus_1)
	{
		try
		{
			if (AmountOfAntiCheats < 1)
				return;
			
			if (player == null || PT.isStringNull(AntiCheat)) 
			{
				RebugPlugin.getINSTANCE().Log(Level.SEVERE, "isPlayerNull: " + (player == null) + " isAntiCheatNull: " + (PT.isStringNull(AntiCheat)));
				return;
			}
			if (Normal_Is_Minus_1 == -1 && !FileUtils.getLoadedAntiCheatsFile().getBoolean ("loaded-anticheats." + AntiCheat.toLowerCase() + ".api.enabled.normal", false) || Normal_Is_Minus_1 == 1 && !FileUtils.getLoadedAntiCheatsFile().getBoolean ("loaded-anticheats." + AntiCheat.toLowerCase() + ".api.enabled.on-join", false) || Normal_Is_Minus_1 == 2 && !FileUtils.getLoadedAntiCheatsFile().getBoolean ("loaded-anticheats." + AntiCheat.toLowerCase() + ".api.enabled.on-leave", false))
				return;
			
			if (FileUtils.getLoadedAntiCheatsFile().getBoolean ("loaded-anticheats." + AntiCheat.toLowerCase() + ".api.check.for-plugin", true))
			{
				Plugin AntiCheatPlugin = Bukkit.getPluginManager().getPlugin(AntiCheat);
				if (AntiCheatPlugin == null || !AntiCheatPlugin.isEnabled()) 
				{
					RebugPlugin.getINSTANCE().Log(Level.WARNING, "AntiCheat: " + AntiCheat + " was not found or was Disabled when trying to invoke it's Api!");
					return;
				}
			}
			Class<?> clazz = ReflectionUtils.getClass("me.killstorm103.Rebug.Utils.API.AntiCheats." + AntiCheat);
			if (clazz != null)
			{
				try
				{
					ReflectionUtils.getMethod(clazz, new String[] {"onHandle"}, true, new Class<?>[] {Player.class, String.class}).invoke(clazz.newInstance(), new Object[] {player, AntiCheat});
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				RebugPlugin.getINSTANCE().Log(Level.SEVERE, "This AntiCheat (" + AntiCheat + ") does not have a Api or it's Api isn't supported!");
				RebugPlugin.getINSTANCE().Log(Level.INFO, "You should disable it's Api in the Loaded AntiCheats.yml config!");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
