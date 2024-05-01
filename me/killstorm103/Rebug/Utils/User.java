package me.killstorm103.Rebug.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import me.killstorm103.Rebug.Main.Rebug;

public class User 
{
	private final Player player;
    private static User user;
    private String brand, register;
    private final int protocol;
    private boolean BrandSet;
    private org.bukkit.Location location, death_location;
	public int UnReceivedBrand;
    public static final Map<UUID, Long> joinTimeMap = new HashMap<UUID, Long>();
    
    
    public User (Player player)
    {
        User.setUser(this);
        this.player = player;
        this.BrandSet = false;
        this.brand = this.register = null;
        this.UnReceivedBrand = 0;
        this.protocol = getNumber (this.player);
        this.death_location = null;
        this.location = this.player.getLocation();
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
    		Rebug.getGetMain().getServer().getConsoleSender().sendMessage("Rebug: Failed to get " + player.getName() + "'s protocol number so returning: 0");
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
}
