package me.killstorm103.Rebug.PacketEvents;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;

import me.killstorm103.Rebug.Main.Rebug;
import net.minecraft.server.v1_8_R3.Blocks;

public class EventBlockPacket implements PacketListener
{
	@Override
	public void onPacketReceive(PacketReceiveEvent e) 
	{
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT)
		{
			WrapperPlayClientPlayerBlockPlacement packet = new WrapperPlayClientPlayerBlockPlacement(e);
			Player player = Bukkit.getPlayer(e.getUser().getUUID());
			Location loc = new Location(player.getWorld(), packet.getBlockPosition().getX(), packet.getBlockPosition().getY(), packet.getBlockPosition().getZ());
			World world = player.getWorld();
			
			if (Rebug.debug)
				Rebug.Debug(player, loc.toString());
			
			if (loc.getWorld().getName().equalsIgnoreCase("world"))
			{
				if (loc.getBlock() == Blocks.STONE_BUTTON)
				{
				 	final Location fuse = new Location(world, 310, 58, 225);
					Entity tnt = loc.getBlock().getLocation().getWorld().spawn(fuse, TNTPrimed.class);
					((TNTPrimed) tnt).setFuseTicks(40);
				}
				
			}
		}
	}
}
