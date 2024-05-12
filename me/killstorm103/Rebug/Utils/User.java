package me.killstorm103.Rebug.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.killstorm103.Rebug.Main.Rebug;

public class User 
{
	private final Player player;
    private static User user;
    private String brand, register;
    private final int protocol;
    private boolean hasLoadedBefore, BrandSet, Eats, Damageable, Vanilla1_8FlyCheck, Vanilla1_9FlyCheck, NotifyFlyingKick, PotionEffects, AutoRefillBlocks, Kickable, Mentions, HideOnlinePlayers, AllowDirectMessages, ShowFlags, FallDamage;
    private org.bukkit.Location location, death_location;
	public int UnReceivedBrand;
    public static final Map<UUID, Long> joinTimeMap = new HashMap<UUID, Long>();
    
    
    public User (Player player)
    {
        User.setUser(this);
        this.player = player;
        this.BrandSet = this.HideOnlinePlayers = this.hasLoadedBefore = false;
        this.brand = this.register = null;
        this.UnReceivedBrand = 0;
        this.protocol = getNumber (this.player);
        this.death_location = null;
        this.Eats = this.Damageable = this.NotifyFlyingKick = this.AllowDirectMessages = this.Kickable = this.FallDamage = this.ShowFlags = this.PotionEffects = this.Vanilla1_8FlyCheck = this.AutoRefillBlocks = this.Mentions = this.Vanilla1_9FlyCheck = true;
        this.location = this.player.getLocation();
        Rebug.getGetMain().LoadPlayerSettings (this.player);
    }
    
    public boolean HasLoadedBefore() {
		return hasLoadedBefore;
	}

	public void setHasLoadedBefore(boolean hasLoadedBefore) {
		this.hasLoadedBefore = hasLoadedBefore;
	}

	public boolean AllowDirectMessages() {
		return AllowDirectMessages;
	}

	public void setAllowDirectMessages(boolean allowDirectMessages) {
		AllowDirectMessages = allowDirectMessages;
	}

	public boolean HideOnlinePlayers() {
		return HideOnlinePlayers;
	}

	public void setHideOnlinePlayers(boolean hideOnlinePlayers) {
		HideOnlinePlayers = hideOnlinePlayers;
	}

	public boolean isKickable() {
		return Kickable;
	}

	public void setKickable(boolean kickable) {
		Kickable = kickable;
	}

	public boolean isShowFlags() {
		return ShowFlags;
	}

	public void setShowFlags(boolean showFlags) {
		ShowFlags = showFlags;
	}

	public boolean Mentions () 
    {
		return Mentions;
	}

	public void setMentions(boolean mentions) {
		Mentions = mentions;
	}

	public boolean isAutoRefillBlocks() {
		return AutoRefillBlocks;
	}

	public void setAutoRefillBlocks(boolean autoRefillBlocks) {
		AutoRefillBlocks = autoRefillBlocks;
	}

	public boolean isPotionEffects() {
		return PotionEffects;
	}

	public void setPotionEffects(boolean potionEffects) {
		PotionEffects = potionEffects;
	}

	public boolean isVanilla1_8FlyCheck() {
		return Vanilla1_8FlyCheck;
	}

	public void setVanilla1_8FlyCheck(boolean vanilla1_8FlyCheck) {
		Vanilla1_8FlyCheck = vanilla1_8FlyCheck;
	}

	public boolean isVanilla1_9FlyCheck() {
		return Vanilla1_9FlyCheck;
	}

	public void setVanilla1_9FlyCheck(boolean vanilla1_12FlyCheck) {
		Vanilla1_9FlyCheck = vanilla1_12FlyCheck;
	}

	public boolean isNotifyFlyingKick() {
		return NotifyFlyingKick;
	}

	public void setNotifyFlyingKick(boolean notifyFlyingKick) {
		NotifyFlyingKick = notifyFlyingKick;
	}
	public boolean isDamageable() {
		return Damageable;
	}

	public void setDamageable(boolean damageable) {
		Damageable = damageable;
	}

	public boolean isEats() {
		return Eats;
	}

	public void setEats(boolean eats) {
		Eats = eats;
	}

	public org.bukkit.Location getDeath_location() {
		return death_location;
	}

	public void setDeath_location(org.bukkit.Location death_location) {
		this.death_location = death_location;
	}

	public org.bukkit.Location getLocation() {
		return location;
	}

	public void setLocation(org.bukkit.Location location) {
		this.location = location;
	}

	public long getJoinTime(UUID uuid) {
        return joinTimeMap.getOrDefault(uuid, 0L);
    }
    public void removeJoinTime (UUID u)
    {
    	joinTimeMap.remove(u, 0);
    }

    public void setJoinTime(UUID uuid, long joinTime) {
        joinTimeMap.put(uuid, joinTime);
    }
    public boolean getIsBrandSet ()
    {
		return BrandSet;
	}

	public String getRegister() {
		return register;
	}
	public void setRegister(String register) {
		this.register = register;
	}
	public void setBrandSet (boolean brandSet)
	{
		BrandSet = brandSet;
	}

	private int getNumber (Player player)
    {
    	int returning = 0;
    	try
    	{
    		returning = PT.getPlayerVersion(player);
    	}
    	catch (Exception e) 
    	{
    		Bukkit.getServer().getScheduler().runTask(Rebug.getGetMain(), new Runnable()
    		{
				@Override
				public void run()
				{
					Bukkit.getServer().getConsoleSender().sendMessage(Rebug.RebugMessage + "Failed to get " + player.getName() + "'s protocol number so returning: 0");
				}
			});
    	}
    	
    	return returning;
    }
	public String getBrand() {
		return brand;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public int getProtocol() {
		return protocol;
	}

	public Player getPlayer ()
    {
        return this.player;
    }
    public String ClientBrand ()
    {
    	return brand;
    }
    public static User getUser () 
    {
        return user;
    }
    public static void setUser(User user) 
    {
        User.user = user;
    }
    public void setFallDamage (boolean take)
    {
    	this.FallDamage = take;
    }
	public boolean FallDamage() {
		return FallDamage;
	}

}
