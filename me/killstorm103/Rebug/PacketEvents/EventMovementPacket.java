package me.killstorm103.Rebug.PacketEvents;

import org.bukkit.entity.Player;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class EventMovementPacket implements PacketListener
{
	@Override
	public void onPacketReceive (PacketReceiveEvent e) 
	{
		Player player = (Player) e.getPlayer();
		if (e.getPacketType() == null || player == null)
			return;
		
		User user = Rebug.getUser(player);
		if (user == null)
		{
			player.kickPlayer("Rebug's \"User\" was somehow null, when reading movement packets");
			return;
		}
	}
}
