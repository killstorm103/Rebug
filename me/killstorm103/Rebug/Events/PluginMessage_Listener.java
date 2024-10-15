package me.killstorm103.Rebug.Events;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import me.killstorm103.Rebug.Main.Rebug;

public class PluginMessage_Listener implements PluginMessageListener, Listener
{
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPluginMSG (PlayerRegisterChannelEvent e)
	{
	}
	@Override
	public void onPluginMessageReceived (String channel, Player player, byte[] data) 
	{
		if (!Rebug.ForgeModUsers.containsKey(player.getUniqueId()))
			Rebug.ForgeModUsers.put(player.getUniqueId(), getModData(data));
    }
	
	public Map<String, String> getModData (byte[] data) 
	{
		Map<String, String> mods = new HashMap<>();
		mods.clear();
		boolean store = true;
		String tempName = null;
		
		for(int i = 2; i < data.length; store = !store) 
		{
			int end = i + data[i] + 1;
			byte[] range = Arrays.copyOfRange(data, i + 1, end);
			String string = new String(range);
			
			if (store) 
				mods.put(tempName, string);
			else
				tempName = string;
			
			i = end;
		}
		return mods;
	}
}
