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

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Utils.FileUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;
import me.killstorm103.Rebug.Utils.API.ApiProvider;

@SuppressWarnings("deprecation")
public class EventJoinAndLeave implements Listener
{
	public static void onHandle (boolean join, Player player)
	{
		User user = null;
		if (join)
		{
			RebugsAntiCheatSwitcherPlugin.getINSTANCE().USERS.put(player.getUniqueId(), new User(player));
			user = RebugsAntiCheatSwitcherPlugin.getUser(player);
			FileUtils.restorePlayer(user.getPlayer());
			PT.UpdateUserPerms(user.getPlayer(), user.getSelectedAntiCheats() > 1 ? user.getNumberIDs() : user.getAntiCheat());
			ApiProvider.onApi(user.getPlayer(), user.getAntiCheat(), user.getSelectedAntiCheats(), 1);
			if (!RebugsAntiCheatSwitcherPlugin.getINSTANCE().isAlertsEnabledOnce.contains(user.getPlayer().getUniqueId())) 
			{
				for (String s : RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getStringList("on-join-and-quit.join.commands.once"))
				{
					Bukkit.dispatchCommand(user.getPlayer(), s);
				}
				RebugsAntiCheatSwitcherPlugin.getINSTANCE().isAlertsEnabledOnce.add(user.getPlayer().getUniqueId());
			}
			for (String s : RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getStringList("on-join-and-quit.join.commands.everytime"))
			{
				Bukkit.dispatchCommand(user.getPlayer(), s);
			}
			
			if (user.getSettingsBooleans().getOrDefault("Damage Resistance", false))
				user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 3, true, false));
			
			if (user.getSettingsBooleans().getOrDefault("Fire Resistance", false))
				user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
			
			if (user.getSettingsBooleans().getOrDefault("Night Vision", false))
				user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
			
			PT.checkPlayer(user, false);
			return;
		}
		FileUtils.savePlayer(player);
		user = RebugsAntiCheatSwitcherPlugin.getUser(player);
		if (user != null)
		{
			ApiProvider.onApi(user.getPlayer(), user.getAntiCheat(), user.getSelectedAntiCheats(), 2);
			user.Unload();
			RebugsAntiCheatSwitcherPlugin.getINSTANCE().USERS.remove(user.getPlayer().getUniqueId(), user);
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onJoin (PlayerJoinEvent e)
	{
		String msg = e.getJoinMessage();
		Player player = e.getPlayer();
		onHandle(true, player);
		if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("on-join-and-quit.join.custom-message-enabled"))
			e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("on-join-and-quit.join.message").replace("%msg%", msg).replace("%message%", msg).replace("%player%", player.getName()).replace("%user%", player.getName())));
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onLeave (PlayerQuitEvent e)
	{
		String msg = e.getQuitMessage();
		Player player = e.getPlayer();
		onHandle(false, player);
		if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("on-join-and-quit.quit.custom-message-enabled"))
			e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("on-join-and-quit.quit.message").replace("%msg%", msg).replace("%message%", msg).replace("%player%", player.getName()).replace("%user%", player.getName())));
	}
}
