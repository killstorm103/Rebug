package me.killstorm103.Rebug.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.killstorm103.Rebug.Main.RebugPlugin;
import me.killstorm103.Rebug.Utils.FileUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

@SuppressWarnings("deprecation")
public class EventJoinAndLeave implements Listener
{
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onJoin (PlayerJoinEvent e)
	{
		String msg = e.getJoinMessage();
		Player player = e.getPlayer();
		RebugPlugin.getINSTANCE().USERS.put(player.getUniqueId(), new User(player));
		User user = RebugPlugin.getUser(player);
		FileUtils.restorePlayer(user.getPlayer());
		PT.UpdateUserPerms(user.getPlayer(), user.getSelectedAntiCheats() > 1 ? user.getNumberIDs() : user.getAntiCheat());
		if (!RebugPlugin.getINSTANCE().isAlertsEnabledOnce.contains(user.getPlayer().getUniqueId())) 
		{
			for (String s : RebugPlugin.getINSTANCE().getConfig().getStringList("on-join-and-quit.join.commands.once"))
			{
				Bukkit.dispatchCommand(user.getPlayer(), s);
			}
			RebugPlugin.getINSTANCE().isAlertsEnabledOnce.add(user.getPlayer().getUniqueId());
		}
		for (String s : RebugPlugin.getINSTANCE().getConfig().getStringList("on-join-and-quit.join.commands.everytime"))
		{
			Bukkit.dispatchCommand(user.getPlayer(), s);
		}
		
		if (user.getSettingsBooleans().getOrDefault("Damage Resistance", false))
			user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 3, true, false));
		
		if (user.getSettingsBooleans().getOrDefault("Fire Resistance", false))
			user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
		
		if (user.getSettingsBooleans().getOrDefault("Night Vision", false))
			user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
		
		if (RebugPlugin.getINSTANCE().getConfig().getBoolean("on-join-and-quit.join.custom-message-enabled"))
			e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', RebugPlugin.getINSTANCE().getConfig().getString("on-join-and-quit.join.message").replace("%msg%", msg).replace("%message%", msg).replace("%player%", user.getName()).replace("%user%", user.getName())));
	
		PT.addScoreBoard(user);
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onLeave (PlayerQuitEvent e)
	{
		String msg = e.getQuitMessage();
		Player player = e.getPlayer();
		FileUtils.savePlayer(player);
		User user = RebugPlugin.getUser(player);
		if (user != null)
		{
			user.Unload();
			RebugPlugin.getINSTANCE().USERS.remove(user.getPlayer().getUniqueId(), user);
		}
		PT.DeleteScoreBoard(player);
		if (RebugPlugin.getINSTANCE().getConfig().getBoolean("on-join-and-quit.quit.custom-message-enabled"))
			e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', RebugPlugin.getINSTANCE().getConfig().getString("on-join-and-quit.quit.message").replace("%msg%", msg).replace("%message%", msg).replace("%player%", player.getName()).replace("%user%", player.getName())));
	}
}
