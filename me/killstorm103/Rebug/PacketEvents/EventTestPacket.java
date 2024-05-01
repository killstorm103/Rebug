package me.killstorm103.Rebug.PacketEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientResourcePackStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;

public class EventTestPacket implements PacketListener
{
	@SuppressWarnings("unused")
	@Override
	public void onPacketReceive(PacketReceiveEvent e) 
	{
		if (e.getPacketType() == PacketType.Play.Client.RESOURCE_PACK_STATUS)
		{
			WrapperPlayClientResourcePackStatus rs = new WrapperPlayClientResourcePackStatus(e);
			Player player = Bukkit.getServer().getPlayer(e.getUser().getUUID());
			player.sendMessage("Hash= " + rs.getHash() + " Result= " + rs.getResult());
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT)
		{
			//WrapperPlayClientPlayerBlockPlacement blockPlacement = new WrapperPlayClientPlayerBlockPlacement(e);
		}
		if (e.getPacketType() == PacketType.Play.Client.UPDATE_SIGN)
		{
			WrapperPlayClientUpdateSign packet = new WrapperPlayClientUpdateSign(e);
		}
	}
	
}
