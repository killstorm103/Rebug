package me.killstorm103.Rebug.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class EventPlayer implements Listener, PacketListener
{
	@SuppressWarnings("deprecation")
	@Override
	public void onPacketSend (PacketSendEvent e)
	{
		if (e.getPacketType() == PacketType.Play.Server.ENTITY_EFFECT)
		{
			Player player = Bukkit.getServer().getPlayer(e.getUser().getUUID());
			User user = Rebug.getUser(player);
			if (user != null && !user.isPotionEffects())
			{
				for (PotionEffect effect : user.getPlayer().getActivePotionEffects())
				{
					if (effect != null)
						user.getPlayer().removePotionEffect(PotionEffectType.getById(effect.getType().getId()));
					
				}
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onPlayerDeath (PlayerDeathEvent e)
	{
		User user = Rebug.getUser(e.getEntity());
		if (user != null)
		{
			user.setDeath_location(user.getPlayer().getLocation());
			user.getPlayer().sendMessage(Rebug.RebugMessage + "Use /back to teleport to where you died!");
		}
	}
	@EventHandler
	public void onHunger (FoodLevelChangeEvent e)
	{
		User user = Rebug.getUser((Player) e.getEntity());
		if (user != null && e.getFoodLevel() < user.getPlayer().getFoodLevel() && !user.isEats())
			e.setCancelled(true);
	}
	@EventHandler
	public void onDamage (EntityDamageEvent e)
	{
		if (e.getEntity() instanceof Player)
		{
			User user = Rebug.getUser((Player) e.getEntity());
			if (user != null)
			{
				if (Rebug.debug)
					Rebug.Debug(user.getPlayer(), "Player= " + user.getPlayer().getName() + " Cause= " + e.getCause().name());
				
				if (e.getCause() == DamageCause.FALL && !user.FallDamage() || e.getCause() != DamageCause.FALL && !user.isDamageable())
					e.setCancelled(true);
			}
		}
	}
}
